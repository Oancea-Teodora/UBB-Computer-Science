import { Router, Response, Request, NextFunction } from "express";
import { AppDataSource } from "../data-source";
import { User } from "../entity/User";
import { auth, AuthRequest } from "../middleware/auth";

const router = Router();
const repo = () => AppDataSource.getRepository(User);

// Define a middleware function for checking admin role
function checkAdmin(req: AuthRequest, res: Response, next: NextFunction) {
    if (!req.user || req.user.role !== 'admin') {
        console.log(`Admin access denied for user: ${req.user?.email || 'unknown'}`);
        res.status(403).json({ error: "Access denied. Admin privileges required" });
        return;
    }
    console.log(`Admin access granted for user: ${req.user.email}`);
    next();
}

// GET /users - Get all users (admin only)
router.get("/", [auth, checkAdmin], async (req: AuthRequest, res: Response) => {
    try {
        console.log("GET /users request received from admin");

        const page = parseInt((req.query.page as string) || "1", 10);
        const limit = parseInt((req.query.limit as string) || "10", 10);
        const nameFilter = (req.query.name as string) || "";

        const qb = repo()
            .createQueryBuilder("u")
            .select(["u.id", "u.email", "u.name", "u.role"]);

        if (nameFilter.trim()) {
            qb.where("u.name LIKE :n OR u.email LIKE :e",
                { n: `%${nameFilter}%`, e: `%${nameFilter}%` });
        }

        const [users, totalItems] = await qb
            .orderBy("u.name", "ASC")
            .skip((page - 1) * limit)
            .take(limit)
            .getManyAndCount();

        const totalPages = Math.ceil(totalItems / limit);

        console.log(`Found ${users.length} users`);

        res.json({
            users,
            page,
            totalItems,
            totalPages
        });

    } catch (err) {
        console.error("Error fetching users:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// GET /users/:id - Get specific user by id (admin only)
router.get("/:id", [auth, checkAdmin], async (req: AuthRequest, res: Response) => {
    try {
        const id = Number(req.params.id);
        const user = await repo().findOne({
            where: { id },
            select: ["id", "email", "name", "role"] // Don't include password hash
        });

        if (!user) {
            res.status(404).json({ error: "User not found" });
            return;
        }

        res.json(user);
    } catch (err) {
        console.error("Error fetching user:", err);
        res.status(500).json({ error: "Server error" });
    }
});

export default router; 