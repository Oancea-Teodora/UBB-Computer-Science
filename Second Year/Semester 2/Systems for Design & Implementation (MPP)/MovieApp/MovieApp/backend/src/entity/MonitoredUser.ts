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

@Entity()
export class MonitoredUser {
    @PrimaryGeneratedColumn()
    id!: number;

    @ManyToOne(() => User, { nullable: false })
    @JoinColumn()
    @Index()
    user!: User;

    @Column()
    userId!: number;

    @CreateDateColumn()
    monitoredSince!: Date;

    @Column({ type: 'text' })
    reason!: string;

    @Column({ type: 'integer', default: 1 })
    suspiciousActivityCount!: number;

    @Column({ default: true })
    isActive!: boolean;
} 