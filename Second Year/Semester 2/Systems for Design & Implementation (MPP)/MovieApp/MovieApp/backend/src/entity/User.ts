import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    OneToMany,
    CreateDateColumn,
    UpdateDateColumn,
    BeforeInsert,
    BeforeUpdate
} from "typeorm";
import { IsEmail, IsNotEmpty, Length, MinLength } from "class-validator";
import * as bcrypt from "bcrypt";
import { Movie } from "./Movie";
import { Director } from "./Director";

export type UserRole = 'user' | 'admin';

@Entity()
export class User {
    @PrimaryGeneratedColumn()
    id!: number;

    @Column({ unique: true })
    @IsEmail()
    @IsNotEmpty()
    email!: string;

    @Column()
    @IsNotEmpty()
    @MinLength(6)
    password!: string;

    @Column()
    @IsNotEmpty()
    @Length(2, 50)
    name!: string;

    @Column({
        type: 'varchar',
        default: "user"
    })
    role!: UserRole;

    @Column({
        type: 'int',
        default: 0
    })
    tokenVersion!: number;

    // 2FA Fields
    @Column({
        type: 'boolean',
        default: false
    })
    twoFactorEnabled!: boolean;

    @Column({
        type: 'varchar',
        nullable: true
    })
    twoFactorSecret?: string;

    @Column({
        type: 'varchar',
        nullable: true
    })
    twoFactorBackupEmail?: string;

    @Column({
        type: 'simple-array',
        nullable: true
    })
    twoFactorBackupCodes?: string[];

    @OneToMany(() => Movie, movie => movie.user)
    movies!: Movie[];

    @OneToMany(() => Director, director => director.user)
    directors!: Director[];

    @CreateDateColumn()
    createdAt!: Date;

    @UpdateDateColumn()
    updatedAt!: Date;

    @BeforeInsert()
    @BeforeUpdate()
    async hashPassword() {
        // Only hash the password if it's not already hashed
        // Bcrypt hashes always start with $2b$, $2a$, or $2y$
        if (this.password && !this.password.startsWith('$2')) {
            const salt = await bcrypt.genSalt();
            this.password = await bcrypt.hash(this.password, salt);
        }
    }

    async validatePassword(password: string): Promise<boolean> {
        return bcrypt.compare(password, this.password);
    }

    isAdmin(): boolean {
        return this.role === 'admin';
    }
} 