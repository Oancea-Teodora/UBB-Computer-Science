import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    ManyToOne,
    Index,
} from "typeorm";
import { IsNotEmpty, Length } from "class-validator";
import { Director } from "./Director";

@Entity()
export class Movie {
    @PrimaryGeneratedColumn()
    id!: number;

    @Column()
    @IsNotEmpty()
    @Length(1, 255)
    @Index()
    title!: string;

    @Column()
    @IsNotEmpty()
    date!: string;

    @Column()
    @IsNotEmpty()
    poster!: string;

    @ManyToOne(() => Director, (d) => d.movies, { eager: true })
    director!: Director;
}
