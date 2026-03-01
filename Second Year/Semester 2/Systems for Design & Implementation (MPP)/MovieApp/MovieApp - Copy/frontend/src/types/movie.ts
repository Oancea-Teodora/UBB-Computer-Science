export interface Movie {
    id?: number;
    title: string;
    director: { id: number; name: string };
    date: string;
    poster: string;
}
