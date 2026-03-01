import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    OneToMany,
} from "typeorm";
import { IsNotEmpty, Length } from "class-validator";
import { Movie } from "./Movie";

@Entity()
export class Director {
    @PrimaryGeneratedColumn()
    id!: number;

    @Column({ unique: true })
    @IsNotEmpty()
    @Length(1, 100)
    name!: string;

    @OneToMany(() => Movie, (m) => m.director)
    movies!: Movie[];
}
