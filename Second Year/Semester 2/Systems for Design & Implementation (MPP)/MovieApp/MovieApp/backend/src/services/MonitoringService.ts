import { AppDataSource } from "../data-source";
import { ActionType, Log } from "../entity/Log";
import { MonitoredUser } from "../entity/MonitoredUser";
import { User } from "../entity/User";
import { LoggingService } from "./LoggingService";

interface ActivityThreshold {
    timeWindowMinutes: number;
    maxActions: number;
}

export class MonitoringService {
    private static instance: MonitoringService;
    private logRepo = AppDataSource.getRepository(Log);
    private monitoredUserRepo = AppDataSource.getRepository(MonitoredUser);
    private userRepo = AppDataSource.getRepository(User);
    private loggingService = LoggingService.getInstance();

    // Thresholds for suspicious activity (these can be adjusted)
    private thresholds: Record<ActionType, ActivityThreshold> = {
        'CREATE': { timeWindowMinutes: 5, maxActions: 15 },
        'READ': { timeWindowMinutes: 5, maxActions: 50 },
        'UPDATE': { timeWindowMinutes: 5, maxActions: 15 },
        'DELETE': { timeWindowMinutes: 5, maxActions: 10 },
    };

    private constructor() { }

    public static getInstance(): MonitoringService {
        if (!MonitoringService.instance) {
            MonitoringService.instance = new MonitoringService();
        }
        return MonitoringService.instance;
    }

    public async startMonitoring(): Promise<void> {
        console.log("Starting activity monitoring...");
        this.monitorActivityPeriodically();
    }

    private monitorActivityPeriodically(): void {
        // Check for suspicious activity every 2 minutes
        setInterval(async () => {
            try {
                await this.detectSuspiciousActivity();
            } catch (error) {
                console.error("Error in monitoring service:", error);
            }
        }, 2 * 60 * 1000); // 2 minutes
    }

    private async detectSuspiciousActivity(): Promise<void> {
        console.log("Checking for suspicious activity...");

        // Get all users
        const users = await this.userRepo.find();

        for (const user of users) {
            // Don't monitor admin users
            if (user.role === 'admin') continue;

            // Check each action type against thresholds
            for (const [actionType, threshold] of Object.entries(this.thresholds) as [ActionType, ActivityThreshold][]) {
                const actionCount = await this.loggingService.countUserActionsInTimeWindow(
                    user.id,
                    threshold.timeWindowMinutes,
                    actionType as ActionType
                );

                // If user exceeds threshold, mark them for monitoring
                if (actionCount > threshold.maxActions) {
                    await this.flagUserAsSuspicious(
                        user,
                        `Performed ${actionCount} ${actionType} operations in ${threshold.timeWindowMinutes} minutes (exceeds threshold of ${threshold.maxActions})`
                    );
                }
            }
        }
    }

    private async flagUserAsSuspicious(user: User, reason: string): Promise<void> {
        // Check if user is already being monitored
        const existingMonitoredUser = await this.monitoredUserRepo.findOne({
            where: { userId: user.id, isActive: true },
            relations: ['user']
        });

        if (existingMonitoredUser) {
            // Update existing monitored user entry
            existingMonitoredUser.suspiciousActivityCount += 1;
            await this.monitoredUserRepo.save(existingMonitoredUser);
            console.log(`User ${user.email} suspicious activity count increased to ${existingMonitoredUser.suspiciousActivityCount}`);
        } else {
            // Create new monitored user entry
            const monitoredUser = new MonitoredUser();
            monitoredUser.user = user;
            monitoredUser.userId = user.id;
            monitoredUser.reason = reason;
            await this.monitoredUserRepo.save(monitoredUser);
            console.log(`User ${user.email} added to monitored users list. Reason: ${reason}`);
        }
    }

    public async unmonitorUser(id: number): Promise<boolean> {
        const monitoredUser = await this.monitoredUserRepo.findOneBy({ id });

        if (!monitoredUser) {
            return false;
        }

        monitoredUser.isActive = false;
        await this.monitoredUserRepo.save(monitoredUser);
        return true;
    }

    public async getMonitoredUsers(): Promise<MonitoredUser[]> {
        return this.monitoredUserRepo.find({
            relations: ['user'],
            where: { isActive: true },
            order: { suspiciousActivityCount: 'DESC' }
        });
    }
} 