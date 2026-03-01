import "reflect-metadata";
import { DataSource } from "typeorm";
import { Director } from "./entity/Director";
import { Movie } from "./entity/Movie";
import { User } from "./entity/User";
import { Log } from "./entity/Log";
import { MonitoredUser } from "./entity/MonitoredUser";
import * as path from "path";

// Check if we have a PostgreSQL DATABASE_URL (Render provides this)
const databaseUrl = process.env.DATABASE_URL;

let dataSourceConfig: any;

if (databaseUrl && databaseUrl.includes('postgresql')) {
    // Production PostgreSQL configuration for Render
    dataSourceConfig = {
        type: "postgres",
        url: databaseUrl,
        synchronize: true, // In production, you might want to set this to false and use migrations
        logging: process.env.NODE_ENV !== "production",
        entities: [Movie, Director, User, Log, MonitoredUser],
        ssl: process.env.NODE_ENV === 'production' ? { rejectUnauthorized: false } : false,
        maxQueryExecutionTime: 1000,
    };
} else {
    // Development SQLite configuration
    const dbPath = process.env.DB_PATH || path.join(process.cwd(), "db.sqlite");
    dataSourceConfig = {
        type: "sqlite",
        database: dbPath,
        synchronize: true,
        logging: process.env.NODE_ENV !== "production",
        entities: [Movie, Director, User, Log, MonitoredUser],
        maxQueryExecutionTime: 1000,
        extra: {
            pragma: [
                "journal_mode=WAL",
                "synchronous=NORMAL",
                "cache_size=30000",
                "foreign_keys=ON",
                "temp_store=MEMORY",
                "mmap_size=300000000",
                "busy_timeout=5000",
                "page_size=4096",
                "count_changes=OFF",
                "wal_autocheckpoint=100"
            ]
        }
    };
}

export const AppDataSource = new DataSource(dataSourceConfig);