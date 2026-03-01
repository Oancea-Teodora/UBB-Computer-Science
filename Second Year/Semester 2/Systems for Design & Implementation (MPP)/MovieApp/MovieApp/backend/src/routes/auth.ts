import { Router, Request, Response } from "express";
import { body, validationResult } from "express-validator";
import { AppDataSource } from "../data-source";
import { User } from "../entity/User";
import { generateAccessToken, generateRefreshToken, verifyRefreshToken, auth, AuthRequest } from "../middleware/auth";
import { TwoFactorService } from "../services/twoFactorService";

const router = Router();

// POST /auth/register
router.post(
    "/register",
    [
        body("email").isEmail().withMessage("Please enter a valid email"),
        body("password")
            .isLength({ min: 6 })
            .withMessage("Password must be at least 6 characters long"),
        body("name")
            .isLength({ min: 2, max: 50 })
            .withMessage("Name must be between 2 and 50 characters"),
    ],
    async (req: Request, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const { email, password, name } = req.body;

            console.log(`Registering new user: ${email}`);

            // Check if user already exists
            const existingUser = await AppDataSource.getRepository(User).findOneBy({
                email,
            });
            if (existingUser) {
                console.log(`Registration failed: Email ${email} already registered`);
                res.status(400).json({ error: "Email already registered" });
                return;
            }

            // Create new user
            const user = AppDataSource.getRepository(User).create({
                email,
                password,
                name,
                role: "user" // explicitly set role to user
            });

            await AppDataSource.getRepository(User).save(user);
            console.log(`User registered successfully: ${email} (ID: ${user.id})`);

            // Generate tokens
            const accessToken = generateAccessToken(user);
            const refreshToken = generateRefreshToken(user);

            res.status(201).json({
                user: {
                    id: user.id,
                    email: user.email,
                    name: user.name,
                    role: user.role,
                },
                accessToken,
                refreshToken,
                // Legacy support
                token: accessToken,
            });
        } catch (err: any) {
            console.error("Registration error:", err.message);
            res.status(500).json({ error: "Server error" });
        }
    }
);

// POST /auth/login
router.post(
    "/login",
    [
        body("email").isEmail().withMessage("Please enter a valid email"),
        body("password").notEmpty().withMessage("Password is required"),
        body("twoFactorToken").optional().isString(),
        body("backupCode").optional().isString(),
    ],
    async (req: Request, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const { email, password, twoFactorToken, backupCode } = req.body;
            console.log(`Login attempt: ${email}`);

            // Find user
            const user = await AppDataSource.getRepository(User).findOneBy({
                email,
            });
            if (!user) {
                console.log(`Login failed: User ${email} not found`);
                res.status(401).json({ error: "Invalid credentials" });
                return;
            }

            // Validate password
            const isValidPassword = await user.validatePassword(password);
            if (!isValidPassword) {
                console.log(`Login failed: Invalid password for ${email}`);
                res.status(401).json({ error: "Invalid credentials" });
                return;
            }

            // Check if 2FA is enabled
            if (user.twoFactorEnabled && user.twoFactorSecret) {
                // User has 2FA enabled, verify 2FA token or backup code
                let twoFactorValid = false;

                if (twoFactorToken) {
                    // Verify TOTP token
                    twoFactorValid = TwoFactorService.verifyToken(user.twoFactorSecret, twoFactorToken);
                    if (twoFactorValid) {
                        console.log(`2FA token verified for ${email}`);
                    }
                } else if (backupCode) {
                    // Verify backup code
                    twoFactorValid = TwoFactorService.verifyBackupCode(user, backupCode);
                    if (twoFactorValid) {
                        console.log(`Backup code used for ${email}`);
                        // Save updated backup codes and send notification
                        await AppDataSource.getRepository(User).save(user);

                        // Send notification email
                        const emailConfig = TwoFactorService.getEmailTransportConfig();
                        if (emailConfig) {
                            TwoFactorService.sendBackupCodeUsedEmail(
                                user.email,
                                user.twoFactorBackupCodes?.length || 0,
                                emailConfig
                            ).catch(err => console.error("Failed to send backup code notification:", err));
                        }
                    }
                }

                if (!twoFactorValid) {
                    console.log(`Login failed: Invalid 2FA for ${email}`);
                    res.status(401).json({
                        error: "Two-factor authentication required",
                        requires2FA: true,
                        message: "Please provide a valid authenticator code or backup code"
                    });
                    return;
                }
            }

            console.log(`Login successful: ${email} (ID: ${user.id}, Role: ${user.role})`);

            // Generate tokens
            const accessToken = generateAccessToken(user);
            const refreshToken = generateRefreshToken(user);

            res.json({
                user: {
                    id: user.id,
                    email: user.email,
                    name: user.name,
                    role: user.role,
                    twoFactorEnabled: user.twoFactorEnabled,
                },
                accessToken,
                refreshToken,
                // Legacy support
                token: accessToken,
            });
        } catch (err: any) {
            console.error("Login error:", err.message);
            res.status(500).json({ error: "Server error" });
        }
    }
);

// POST /auth/login-page - Simple HTML form for direct login
router.get("/login-page", (_req: Request, res: Response): void => {
    const htmlForm = `
<!DOCTYPE html>
<html>
<head>
    <title>Login to Movie App</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            padding-top: 50px;
        }
        .container {
            width: 90%;
            max-width: 600px;
        }
        form {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            width: 100%;
            margin-bottom: 20px;
        }
        h2 {
            text-align: center;
            margin-bottom: 30px;
            color: #333;
        }
        .input-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            box-sizing: border-box;
        }
        button {
            width: 100%;
            padding: 12px;
            background-color: #1976d2;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }
        button:hover {
            background-color: #1565c0;
        }
        .error {
            color: #d32f2f;
            background-color: #fdecea;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
            display: none;
        }
        .success {
            color: #388e3c;
            background-color: #edf7ed;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
            display: none;
        }
        .debug-panel {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        pre {
            background-color: #f8f8f8;
            padding: 15px;
            border-radius: 4px;
            overflow-x: auto;
            font-size: 14px;
            border: 1px solid #eee;
        }
        .copy-btn {
            background-color: #4CAF50;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <form id="loginForm">
            <h2>Login to Movie App</h2>
            <div id="errorAlert" class="error"></div>
            <div id="successAlert" class="success"></div>
            
            <div class="input-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required placeholder="Enter your email">
            </div>
            
            <div class="input-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required placeholder="Enter your password">
            </div>
            
            <button type="submit">Login</button>
            
            <div style="margin-top: 20px; text-align: center;">
                <p>After successful login, copy the token and user data below.</p>
                <p><a href="/" id="backLink">Return to App</a></p>
            </div>
        </form>

        <div class="debug-panel">
            <h3>Debug Information</h3>
            <p>Default Credentials:</p>
            <ul>
                <li><strong>Admin:</strong> admin@gmail.com / admin</li>
                <li><strong>Regular User:</strong> ion@gmail.com / password</li>
            </ul>

            <h3>Authentication Token</h3>
            <pre id="tokenDisplay">No token yet</pre>
            <button id="copyToken" class="copy-btn">Copy Token</button>

            <h3>User Data</h3>
            <pre id="userData">No user data yet</pre>
            <button id="copyUserData" class="copy-btn">Copy User Data</button>

            <h3>Manual Token Installation</h3>
            <p>Copy this code to your browser console when on the main app page:</p>
            <pre id="consoleCode">// No token generated yet</pre>
            <button id="copyConsoleCode" class="copy-btn">Copy Code</button>
        </div>
    </div>
    
    <script>
        document.getElementById('loginForm').addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const errorEl = document.getElementById('errorAlert');
            const successEl = document.getElementById('successAlert');
            const tokenDisplay = document.getElementById('tokenDisplay');
            const userData = document.getElementById('userData');
            const consoleCode = document.getElementById('consoleCode');
            
            errorEl.style.display = 'none';
            successEl.style.display = 'none';
            
            try {
                const response = await fetch('/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email, password })
                });
                
                const data = await response.json();
                
                if (!response.ok) {
                    throw new Error(data.error || 'Login failed');
                }
                
                // Store auth data in localStorage
                localStorage.setItem('token', data.token);
                localStorage.setItem('user', JSON.stringify(data.user));
                
                // Update displays
                tokenDisplay.textContent = data.token;
                userData.textContent = JSON.stringify(data.user, null, 2);
                consoleCode.textContent = "localStorage.setItem('token', '" + data.token + "');\n" +
                    "localStorage.setItem('user', '" + JSON.stringify(data.user) + "');";
                
                // Show success message
                successEl.innerHTML = 'Login successful! You can now return to the app or copy your token below.';
                successEl.style.display = 'block';
                
                // Update back link to include token in URL for frontend
                document.getElementById('backLink').href = '/?token=' + encodeURIComponent(data.token) + '&user=' + encodeURIComponent(JSON.stringify(data.user));
                
            } catch (error) {
                errorEl.textContent = error.message || 'Login failed';
                errorEl.style.display = 'block';
            }
        });
        
        // Copy button functionality
        document.getElementById('copyToken').addEventListener('click', function() {
            copyToClipboard(document.getElementById('tokenDisplay').textContent);
            this.textContent = 'Copied!';
            setTimeout(() => this.textContent = 'Copy Token', 2000);
        });
        
        document.getElementById('copyUserData').addEventListener('click', function() {
            copyToClipboard(document.getElementById('userData').textContent);
            this.textContent = 'Copied!';
            setTimeout(() => this.textContent = 'Copy User Data', 2000);
        });
        
        document.getElementById('copyConsoleCode').addEventListener('click', function() {
            copyToClipboard(document.getElementById('consoleCode').textContent);
            this.textContent = 'Copied!';
            setTimeout(() => this.textContent = 'Copy Code', 2000);
        });
        
        function copyToClipboard(text) {
            const textarea = document.createElement('textarea');
            textarea.value = text;
            document.body.appendChild(textarea);
            textarea.select();
            document.execCommand('copy');
            document.body.removeChild(textarea);
        }
    </script>
</body>
</html>
    `;
    res.send(htmlForm);
});

// POST /auth/refresh - Refresh access token using refresh token
router.post(
    "/refresh",
    [
        body("refreshToken").notEmpty().withMessage("Refresh token is required"),
    ],
    async (req: Request, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const { refreshToken } = req.body;

            // Verify refresh token
            const decoded = verifyRefreshToken(refreshToken);

            // Find user
            const user = await AppDataSource.getRepository(User).findOneBy({
                id: decoded.id,
            });

            if (!user) {
                console.log(`Refresh failed: User ${decoded.id} not found`);
                res.status(401).json({ error: "Invalid refresh token" });
                return;
            }

            // Check token version
            if (decoded.tokenVersion !== user.tokenVersion) {
                console.log(`Refresh failed: Token version mismatch for user ${user.email}`);
                res.status(401).json({ error: "Invalid refresh token" });
                return;
            }

            // Generate new tokens
            const newAccessToken = generateAccessToken(user);
            const newRefreshToken = generateRefreshToken(user);

            console.log(`Token refreshed for user: ${user.email}`);

            res.json({
                user: {
                    id: user.id,
                    email: user.email,
                    name: user.name,
                    role: user.role,
                },
                accessToken: newAccessToken,
                refreshToken: newRefreshToken,
                // Legacy support
                token: newAccessToken,
            });
        } catch (err: any) {
            console.error("Token refresh error:", err.message);
            res.status(401).json({ error: "Invalid refresh token" });
        }
    }
);

// POST /auth/logout - Invalidate tokens by incrementing token version
router.post("/logout", auth, async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        const user = req.user!;

        // Increment token version to invalidate all existing tokens
        user.tokenVersion = (user.tokenVersion || 0) + 1;
        await AppDataSource.getRepository(User).save(user);

        console.log(`User logged out: ${user.email} (new token version: ${user.tokenVersion})`);

        res.json({ message: "Logged out successfully" });
    } catch (err: any) {
        console.error("Logout error:", err.message);
        res.status(500).json({ error: "Server error" });
    }
});

// GET /auth/profile - Get current user profile
router.get("/profile", auth, async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        const user = req.user!;

        res.json({
            user: {
                id: user.id,
                email: user.email,
                name: user.name,
                role: user.role,
                createdAt: user.createdAt,
                updatedAt: user.updatedAt,
            }
        });
    } catch (err: any) {
        console.error("Profile fetch error:", err.message);
        res.status(500).json({ error: "Server error" });
    }
});

// PUT /auth/profile - Update user profile
router.put(
    "/profile",
    auth,
    [
        body("name")
            .optional()
            .isLength({ min: 2, max: 50 })
            .withMessage("Name must be between 2 and 50 characters"),
        body("email")
            .optional()
            .isEmail()
            .withMessage("Please enter a valid email"),
    ],
    async (req: AuthRequest, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const user = req.user!;
            const { name, email } = req.body;

            // Check if email is already taken by another user
            if (email && email !== user.email) {
                const existingUser = await AppDataSource.getRepository(User).findOneBy({
                    email,
                });
                if (existingUser) {
                    res.status(400).json({ error: "Email already in use" });
                    return;
                }
                user.email = email;
            }

            if (name) {
                user.name = name;
            }

            await AppDataSource.getRepository(User).save(user);
            console.log(`Profile updated for user: ${user.email}`);

            res.json({
                user: {
                    id: user.id,
                    email: user.email,
                    name: user.name,
                    role: user.role,
                    updatedAt: user.updatedAt,
                }
            });
        } catch (err: any) {
            console.error("Profile update error:", err.message);
            res.status(500).json({ error: "Server error" });
        }
    }
);

// POST /auth/change-password - Change user password
router.post(
    "/change-password",
    auth,
    [
        body("currentPassword").notEmpty().withMessage("Current password is required"),
        body("newPassword")
            .isLength({ min: 6 })
            .withMessage("New password must be at least 6 characters long"),
    ],
    async (req: AuthRequest, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const user = req.user!;
            const { currentPassword, newPassword } = req.body;

            // Validate current password
            const isValidPassword = await user.validatePassword(currentPassword);
            if (!isValidPassword) {
                res.status(400).json({ error: "Current password is incorrect" });
                return;
            }

            // Update password
            user.password = newPassword;

            // Increment token version to invalidate all existing tokens
            user.tokenVersion = (user.tokenVersion || 0) + 1;

            await AppDataSource.getRepository(User).save(user);
            console.log(`Password changed for user: ${user.email} (new token version: ${user.tokenVersion})`);

            res.json({ message: "Password changed successfully. Please log in again." });
        } catch (err: any) {
            console.error("Password change error:", err.message);
            res.status(500).json({ error: "Server error" });
        }
    }
);

// ============ TWO-FACTOR AUTHENTICATION ROUTES ============

// POST /auth/2fa/setup - Start 2FA setup process
router.post("/2fa/setup", auth, async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        const user = req.user!;

        if (user.twoFactorEnabled) {
            res.status(400).json({ error: "Two-factor authentication is already enabled" });
            return;
        }

        // Generate secret and QR code
        const { secret, otpauthUrl } = TwoFactorService.generateSecret(user.email);
        const qrCodeDataUrl = await TwoFactorService.generateQRCode(otpauthUrl);

        // Store the secret temporarily (not yet enabled)
        user.twoFactorSecret = secret;
        await AppDataSource.getRepository(User).save(user);

        console.log(`2FA setup initiated for user: ${user.email}`);

        res.json({
            secret,
            qrCode: qrCodeDataUrl,
            manualEntryKey: secret,
            instructions: {
                step1: "Install an authenticator app like Google Authenticator, Authy, or 1Password",
                step2: "Scan the QR code or manually enter the secret key",
                step3: "Enter the 6-digit code from your authenticator app to verify setup"
            }
        });
    } catch (err: any) {
        console.error("2FA setup error:", err.message);
        res.status(500).json({ error: "Server error" });
    }
});

// POST /auth/2fa/verify-setup - Complete 2FA setup
router.post(
    "/2fa/verify-setup",
    auth,
    [
        body("token").isLength({ min: 6, max: 6 }).isNumeric().withMessage("Token must be a 6-digit number"),
    ],
    async (req: AuthRequest, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const user = req.user!;
            const { token } = req.body;

            if (user.twoFactorEnabled) {
                res.status(400).json({ error: "Two-factor authentication is already enabled" });
                return;
            }

            if (!user.twoFactorSecret) {
                res.status(400).json({ error: "No 2FA setup in progress. Please start setup first." });
                return;
            }

            // Verify the token
            const isValid = TwoFactorService.verifyToken(user.twoFactorSecret, token);
            if (!isValid) {
                res.status(400).json({ error: "Invalid verification code. Please try again." });
                return;
            }

            // Generate backup codes
            const backupCodes = TwoFactorService.generateBackupCodes();

            // Enable 2FA
            user.twoFactorEnabled = true;
            user.twoFactorBackupCodes = backupCodes;
            user.twoFactorBackupEmail = user.email;

            // Increment token version to invalidate existing tokens
            user.tokenVersion = (user.tokenVersion || 0) + 1;

            await AppDataSource.getRepository(User).save(user);

            console.log(`2FA enabled for user: ${user.email}`);

            // Send setup email with backup codes
            const emailConfig = TwoFactorService.getEmailTransportConfig();
            if (emailConfig) {
                try {
                    await TwoFactorService.sendSetupEmail(user.email, backupCodes, emailConfig);
                } catch (emailError) {
                    console.error("Failed to send 2FA setup email:", emailError);
                    // Continue with success response even if email fails
                }
            }

            res.json({
                message: "Two-factor authentication has been successfully enabled!",
                backupCodes,
                warning: "Save these backup codes in a secure location. Each code can only be used once.",
                instructions: {
                    usage: "Use backup codes if you lose access to your authenticator device",
                    security: "Store backup codes separately from your device and account credentials",
                    regeneration: "You can generate new backup codes in your account settings"
                }
            });
        } catch (err: any) {
            console.error("2FA verification error:", err.message);
            res.status(500).json({ error: "Server error" });
        }
    }
);

// POST /auth/2fa/disable - Disable 2FA
router.post(
    "/2fa/disable",
    auth,
    [
        body("password").notEmpty().withMessage("Password is required"),
        body("token").optional().isLength({ min: 6, max: 6 }).isNumeric(),
        body("backupCode").optional().isString(),
    ],
    async (req: AuthRequest, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const user = req.user!;
            const { password, token, backupCode } = req.body;

            if (!user.twoFactorEnabled) {
                res.status(400).json({ error: "Two-factor authentication is not enabled" });
                return;
            }

            // Verify password
            const isValidPassword = await user.validatePassword(password);
            if (!isValidPassword) {
                res.status(400).json({ error: "Invalid password" });
                return;
            }

            // Verify 2FA token or backup code
            let twoFactorValid = false;

            if (token && user.twoFactorSecret) {
                twoFactorValid = TwoFactorService.verifyToken(user.twoFactorSecret, token);
            } else if (backupCode) {
                twoFactorValid = TwoFactorService.verifyBackupCode(user, backupCode);
            }

            if (!twoFactorValid) {
                res.status(400).json({ error: "Invalid 2FA code or backup code" });
                return;
            }

            // Disable 2FA
            user.twoFactorEnabled = false;
            user.twoFactorSecret = undefined;
            user.twoFactorBackupCodes = undefined;
            user.twoFactorBackupEmail = undefined;

            // Increment token version to invalidate existing tokens
            user.tokenVersion = (user.tokenVersion || 0) + 1;

            await AppDataSource.getRepository(User).save(user);

            console.log(`2FA disabled for user: ${user.email}`);

            res.json({
                message: "Two-factor authentication has been disabled",
                warning: "Your account is now less secure. Consider re-enabling 2FA for better protection."
            });
        } catch (err: any) {
            console.error("2FA disable error:", err.message);
            res.status(500).json({ error: "Server error" });
        }
    }
);

// GET /auth/2fa/status - Get 2FA status
router.get("/2fa/status", auth, async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        const user = req.user!;

        res.json({
            twoFactorEnabled: user.twoFactorEnabled,
            backupCodesRemaining: user.twoFactorBackupCodes?.length || 0,
            backupEmail: user.twoFactorBackupEmail,
            setupInProgress: !user.twoFactorEnabled && !!user.twoFactorSecret
        });
    } catch (err: any) {
        console.error("2FA status error:", err.message);
        res.status(500).json({ error: "Server error" });
    }
});

// POST /auth/2fa/regenerate-backup-codes - Regenerate backup codes
router.post(
    "/2fa/regenerate-backup-codes",
    auth,
    [
        body("password").notEmpty().withMessage("Password is required"),
        body("token").isLength({ min: 6, max: 6 }).isNumeric().withMessage("Token must be a 6-digit number"),
    ],
    async (req: AuthRequest, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const user = req.user!;
            const { password, token } = req.body;

            if (!user.twoFactorEnabled || !user.twoFactorSecret) {
                res.status(400).json({ error: "Two-factor authentication is not enabled" });
                return;
            }

            // Verify password
            const isValidPassword = await user.validatePassword(password);
            if (!isValidPassword) {
                res.status(400).json({ error: "Invalid password" });
                return;
            }

            // Verify 2FA token
            const isValidToken = TwoFactorService.verifyToken(user.twoFactorSecret, token);
            if (!isValidToken) {
                res.status(400).json({ error: "Invalid 2FA token" });
                return;
            }

            // Generate new backup codes
            const newBackupCodes = TwoFactorService.generateBackupCodes();
            user.twoFactorBackupCodes = newBackupCodes;

            await AppDataSource.getRepository(User).save(user);

            console.log(`Backup codes regenerated for user: ${user.email}`);

            res.json({
                message: "New backup codes have been generated",
                backupCodes: newBackupCodes,
                warning: "Your old backup codes are no longer valid. Save these new codes in a secure location."
            });
        } catch (err: any) {
            console.error("Backup code regeneration error:", err.message);
            res.status(500).json({ error: "Server error" });
        }
    }
);

export default router; 