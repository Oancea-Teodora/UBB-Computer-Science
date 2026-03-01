export interface Director {
    id?: number;
    name: string;
    movies?: {
        id: number;
        title: string;
        date: string;
        poster: string;
    }[];
} 