export interface Director {
    id?: number;
    name: string;
    movieCount?: number;
    movies?: {
        id: number;
        title: string;
        date: string;
        poster: string;
    }[];
    user?: {
        id: number;
        name?: string;
        email?: string;
    };
} 