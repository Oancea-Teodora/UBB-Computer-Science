import "reflect-metadata";
import { DataSource } from "typeorm";
import { MovieSchema } from "./models/movie";
import { Director } from "./entity/Director";
import { Movie } from "./entity/Movie";

export const AppDataSource = new DataSource({
    type: "sqlite",
    database: "db.sqlite",
    synchronize: true,
    logging: true,
    entities: [
        Movie, Director
    ],
    extra: {
        pragma: ["journal_mode=WAL"]
    }
});