import { z } from "zod";

export const MovieSchema = z.object({
    id: z.number().optional(),
    title: z.string().min(1, "Title is required"),
    director: z.string().min(1, "Director is required"),
    date: z.string().min(4, "Date is required"),
    poster: z.string().min(1, "Poster must be a valid text"),
});

export type Movie = z.infer<typeof MovieSchema>;
