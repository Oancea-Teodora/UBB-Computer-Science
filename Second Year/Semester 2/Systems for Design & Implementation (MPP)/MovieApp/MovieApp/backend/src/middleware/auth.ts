import { Request, Response, NextFunction } from "express";
import * as jwt from "jsonwebtoken";
import { AppDataSource } from "../data-source";
import { User, UserRole } from "../entity/User";

const JWT_SECRET = process.env.JWT_SECRET || "your-secret-key";
const JWT_REFRESH_SECRET = process.env.JWT_REFRESH_SECRET || "your-refresh-secret-key";

// Token expiration times
const ACCESS_TOKEN_EXPIRY = "15m";  // 15 minutes
const REFRESH_TOKEN_EXPIRY = "7d";   // 7 days

export interface AuthRequest extends Request {
    user?: User;
}

export interface TokenPayload {
    id: number;
    email: string;
    role: UserRole;
    tokenVersion?: number;
}

export const auth = async (req: AuthRequest, res: Response, next: NextFunction): Promise<void> => {
    try {
        const token = req.header("Authorization")?.replace("Bearer ", "");

        if (!token) {
            console.log("Authentication failed: No token provided");
            throw new Error("No token provided");
        }

        try {
            const decoded = jwt.verify(token, JWT_SECRET) as TokenPayload;

            const user = await AppDataSource.getRepository(User).findOne({
                where: { id: decoded.id },
                relations: ["movies", "directors"]
            });

            if (!user) {
                console.log(`Authentication failed: User with ID ${decoded.id} not found`);
                throw new Error("User not found");
            }

            // Additional security: Check if token version matches (for token invalidation)
            if (decoded.tokenVersion && user.tokenVersion !== decoded.tokenVersion) {
                console.log(`Authentication failed: Token version mismatch for user ${user.email}`);
                throw new Error("Token version invalid");
            }

            console.log(`User authenticated: ${user.email} (${user.role})`);
            req.user = user;
            next();
        } catch (jwtError: any) {
            if (jwtError.name === 'TokenExpiredError') {
                console.log("JWT token expired");
                res.status(401).json({
                    error: "Token expired",
                    code: "TOKEN_EXPIRED",
                    message: "Access token has expired. Please refresh your token."
                });
                return;
            }
            console.log("JWT verification error:", jwtError.message);
            throw new Error("Invalid token");
        }
    } catch (error: any) {
        console.error("Authentication error:", error.message);
        res.status(401).json({ error: "Please authenticate" });
    }
};

// Middleware to check if the user has admin role
export const requireAdmin = async (req: AuthRequest, res: Response, next: NextFunction): Promise<void> => {
    if (!req.user || req.user.role !== 'admin') {
        console.log(`Admin access denied for user: ${req.user?.email || 'unknown'}`);
        res.status(403).json({ error: "Access denied. Admin privileges required" });
        return;
    }
    console.log(`Admin access granted for user: ${req.user.email}`);
    next();
};

// Generate access token with short expiry
export const generateAccessToken = (user: User): string => {
    const payload: TokenPayload = {
        id: user.id,
        email: user.email,
        role: user.role,
        tokenVersion: user.tokenVersion || 0
    };
    return jwt.sign(payload, JWT_SECRET, { expiresIn: ACCESS_TOKEN_EXPIRY });
};

// Generate refresh token with longer expiry
export const generateRefreshToken = (user: User): string => {
    const payload: TokenPayload = {
        id: user.id,
        email: user.email,
        role: user.role,
        tokenVersion: user.tokenVersion || 0
    };
    return jwt.sign(payload, JWT_REFRESH_SECRET, { expiresIn: REFRESH_TOKEN_EXPIRY });
};

// Verify refresh token
export const verifyRefreshToken = (token: string): TokenPayload => {
    return jwt.verify(token, JWT_REFRESH_SECRET) as TokenPayload;
};

// Legacy function for backward compatibility
export const generateToken = (user: User): string => {
    return generateAccessToken(user);
}; 