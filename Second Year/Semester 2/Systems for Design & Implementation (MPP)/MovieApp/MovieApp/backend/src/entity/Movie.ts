import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    ManyToOne,
    JoinColumn,
    Index,
    CreateDateColumn,
    UpdateDateColumn
} from "typeorm";
import { IsNotEmpty, Length } from "class-validator";
import { Director } from "./Director";
import { User } from "./User";

@Entity()
@Index(["title", "director"])
@Index("idx_movie_date_title", ["date", "title"])
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
    @Index('idx_movie_date')
    date!: string;

    @Column()
    @IsNotEmpty()
    poster!: string;

    @Column({ nullable: true })
    genre?: string;

    @ManyToOne(() => Director, director => director.movies, { lazy: true })
    @JoinColumn()
    @Index('idx_movie_director')
    director!: Director;

    @ManyToOne(() => User, user => user.movies, { lazy: true })
    @JoinColumn()
    @Index('idx_movie_user')
    user!: User;

    @CreateDateColumn()
    createdAt!: Date;

    @UpdateDateColumn()
    updatedAt!: Date;
}
