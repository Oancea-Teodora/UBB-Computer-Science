import {
    Entity,
    PrimaryGeneratedColumn,
    Column,
    CreateDateColumn,
    ManyToOne,
    JoinColumn,
    Index
} from "typeorm";
import { User } from "./User";

export type ActionType = 'CREATE' | 'READ' | 'UPDATE' | 'DELETE';
export type EntityType = 'Movie' | 'Director' | 'User' | 'Other';

@Entity()
export class Log {
    @PrimaryGeneratedColumn()
    id!: number;

    @ManyToOne(() => User, { nullable: true })
    @JoinColumn()
    @Index()
    user?: User;

    @Column({ nullable: true })
    userId?: number;

    @Column()
    @Index()
    action!: ActionType;

    @Column()
    @Index()
    entityType!: EntityType;

    @Column({ nullable: true })
    entityId?: number;

    @Column({ type: 'text', nullable: true })
    details?: string;

    @CreateDateColumn()
    @Index()
    timestamp!: Date;

    @Column({ default: false })
    isUnusual!: boolean;
} 