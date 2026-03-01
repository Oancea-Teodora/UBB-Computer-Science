import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    OneToMany,
    Index,
    ManyToOne,
    JoinColumn,
} from "typeorm";
import { IsNotEmpty, Length } from "class-validator";
import { Movie } from "./Movie";
import { User } from "./User";

@Entity()
export class Director {
    @PrimaryGeneratedColumn()
    id!: number;

    @Column({ unique: true })
    @IsNotEmpty()
    @Length(1, 100)
    @Index()
    name!: string;

    @OneToMany(() => Movie, (m) => m.director)
    movies!: Movie[];

    @ManyToOne(() => User, user => user.directors)
    @JoinColumn()
    @Index()
    user!: User;
}
