import { AppDataSource } from "../data-source";
import { ActionType, EntityType, Log } from "../entity/Log";
import { User } from "../entity/User";
import { Repository } from "typeorm";

export class LoggingService {
    private static instance: LoggingService;
    private repo = AppDataSource.getRepository(Log);

    private constructor() { }

    public static getInstance(): LoggingService {
        if (!LoggingService.instance) {
            LoggingService.instance = new LoggingService();
        }
        return LoggingService.instance;
    }

    public getRepository(): Repository<Log> {
        return this.repo;
    }

    public async logAction(
        action: ActionType,
        entityType: EntityType,
        user: User | null,
        entityId?: number,
        details?: string
    ): Promise<Log> {
        const log = new Log();
        log.action = action;
        log.entityType = entityType;

        if (user) {
            log.user = user;
            log.userId = user.id;
        }

        if (entityId) {
            log.entityId = entityId;
        }

        if (details) {
            log.details = details;
        }

        return await this.repo.save(log);
    }

    public async findRecentUserActions(
        userId: number,
        timeWindowMinutes: number = 5,
        actionType?: ActionType
    ): Promise<Log[]> {
        const timeWindow = new Date();
        timeWindow.setMinutes(timeWindow.getMinutes() - timeWindowMinutes);

        const queryBuilder = this.repo
            .createQueryBuilder("log")
            .where("log.userId = :userId", { userId })
            .andWhere("log.timestamp >= :timeWindow", { timeWindow });

        if (actionType) {
            queryBuilder.andWhere("log.action = :actionType", { actionType });
        }

        return queryBuilder
            .orderBy("log.timestamp", "DESC")
            .getMany();
    }

    public async countUserActionsInTimeWindow(
        userId: number,
        timeWindowMinutes: number = 5,
        actionType?: ActionType
    ): Promise<number> {
        const timeWindow = new Date();
        timeWindow.setMinutes(timeWindow.getMinutes() - timeWindowMinutes);

        const queryBuilder = this.repo
            .createQueryBuilder("log")
            .where("log.userId = :userId", { userId })
            .andWhere("log.timestamp >= :timeWindow", { timeWindow });

        if (actionType) {
            queryBuilder.andWhere("log.action = :actionType", { actionType });
        }

        return queryBuilder.getCount();
    }
} 