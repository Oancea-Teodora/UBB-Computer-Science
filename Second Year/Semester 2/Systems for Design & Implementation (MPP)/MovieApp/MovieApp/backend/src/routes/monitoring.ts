import { Router, Response, NextFunction } from "express";
import { auth, AuthRequest } from "../middleware/auth";
import { MonitoringService } from "../services/MonitoringService";
import { LoggingService } from "../services/LoggingService";

const router = Router();

// Middleware to check if user is admin
function checkAdmin(req: AuthRequest, res: Response, next: NextFunction) {
    if (!req.user || req.user.role !== 'admin') {
        console.log(`Admin access denied for user: ${req.user?.email || 'unknown'}`);
        res.status(403).json({ error: "Access denied. Admin privileges required" });
        return;
    }
    console.log(`Admin access granted for user: ${req.user.email}`);
    next();
}

// GET /monitoring/users - Get all monitored users (admin only)
router.get("/users", [auth, checkAdmin], async (req: AuthRequest, res: Response) => {
    try {
        const monitoringService = MonitoringService.getInstance();
        const monitoredUsers = await monitoringService.getMonitoredUsers();

        res.json({
            monitoredUsers,
            count: monitoredUsers.length
        });
    } catch (err) {
        console.error("Error fetching monitored users:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// GET /monitoring/logs - Get recent activity logs (admin only)
router.get("/logs", [auth, checkAdmin], async (req: AuthRequest, res: Response) => {
    try {
        const page = parseInt((req.query.page as string) || "1", 10);
        const limit = parseInt((req.query.limit as string) || "25", 10);
        const userId = req.query.userId ? parseInt(req.query.userId as string, 10) : undefined;

        const logRepo = LoggingService.getInstance().getRepository();

        const queryBuilder = logRepo.createQueryBuilder("log")
            .leftJoinAndSelect("log.user", "user")
            .orderBy("log.timestamp", "DESC")
            .skip((page - 1) * limit)
            .take(limit);

        // Filter by user if specified
        if (userId) {
            queryBuilder.where("log.userId = :userId", { userId });
        }

        const [logs, totalItems] = await queryBuilder.getManyAndCount();
        const totalPages = Math.ceil(totalItems / limit);

        res.json({
            logs,
            page,
            totalItems,
            totalPages
        });
    } catch (err) {
        console.error("Error fetching activity logs:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// POST /monitoring/users/:id/unmonitor - Remove a user from monitoring (admin only)
router.post("/users/:id/unmonitor", [auth, checkAdmin], async (req: AuthRequest, res: Response) => {
    try {
        const id = parseInt(req.params.id, 10);
        const monitoringService = MonitoringService.getInstance();

        const success = await monitoringService.unmonitorUser(id);

        if (success) {
            res.json({ message: "User removed from monitoring" });
        } else {
            res.status(404).json({ error: "Monitored user not found" });
        }
    } catch (err) {
        console.error("Error unmonitoring user:", err);
        res.status(500).json({ error: "Server error" });
    }
});

export default router; 