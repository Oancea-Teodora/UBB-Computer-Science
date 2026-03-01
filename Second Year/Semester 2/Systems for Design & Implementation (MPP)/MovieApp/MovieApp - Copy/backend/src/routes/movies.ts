// src/routes/movies.ts
import { Router, Request, Response } from "express";
import { body, validationResult } from "express-validator";
import { AppDataSource } from "../data-source";
import { Movie } from "../entity/Movie";
import { Director } from "../entity/Director";

const router = Router();
const movieRepo = () => AppDataSource.getRepository(Movie);
const directorRepo = () => AppDataSource.getRepository(Director);

// GET /movies?page=1&limit=10&director=Name
router.get("/", async (req: Request, res: Response): Promise<void> => {
    try {
        const page = parseInt((req.query.page as string) || "1", 10);
        const limit = parseInt((req.query.limit as string) || "10", 10);
        const director = (req.query.director as string) || "";

        const qb = movieRepo()
            .createQueryBuilder("m")
            .leftJoinAndSelect("m.director", "d");

        if (director.trim()) {
            qb.where("d.name ILIKE :name", { name: `%${director}%` });
        }

        qb.orderBy("m.title", "ASC")
            .skip((page - 1) * limit)
            .take(limit);

        const [movies, totalItems] = await qb.getManyAndCount();
        const totalPages = Math.ceil(totalItems / limit);

        res.json({ movies, page, totalItems, totalPages });
        return;
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Server error" });
        return;
    }
});

// POST /movies
router.post(
    "/",
    [
        body("title").notEmpty().withMessage("Title is required"),
        body("directorId").isInt().withMessage("directorId must be an integer"),
        body("date").notEmpty().withMessage("Date is required"),
        body("poster").notEmpty().withMessage("Poster is required"),
    ],
    async (req: Request, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const { title, date, poster, directorId } = req.body;
            const dir = await directorRepo().findOne({ where: { id: directorId } });
            if (!dir) {
                res.status(400).json({ error: "Invalid directorId" });
                return;
            }

            const movie = movieRepo().create({ title, date, poster, director: dir });
            const saved = await movieRepo().save(movie);
            res.status(201).json(saved);
            return;
        } catch (err) {
            console.error(err);
            res.status(500).json({ error: "Server error" });
            return;
        }
    }
);

// PATCH /movies/:id
router.patch(
    "/:id",
    [
        body("title").optional().notEmpty().withMessage("Title cannot be empty"),
        body("directorId").optional().isInt().withMessage("directorId must be integer"),
        body("date").optional().notEmpty().withMessage("Date cannot be empty"),
        body("poster").optional().notEmpty().withMessage("Poster cannot be empty"),
    ],
    async (req: Request, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        const id = Number(req.params.id);
        try {
            const repo = movieRepo();
            let movie = await repo.findOne({
                where: { id },
                relations: ["director"],
            });
            if (!movie) {
                res.status(404).json({ error: "Movie not found" });
                return;
            }

            if (req.body.directorId) {
                const dir = await directorRepo().findOne({
                    where: { id: req.body.directorId },
                });
                if (!dir) {
                    res.status(400).json({ error: "Invalid directorId" });
                    return;
                }
                movie.director = dir;
            }

            if (req.body.title) movie.title = req.body.title;
            if (req.body.date) movie.date = req.body.date;
            if (req.body.poster) movie.poster = req.body.poster;

            const updated = await repo.save(movie);
            res.json(updated);
            return;
        } catch (err) {
            console.error(err);
            res.status(500).json({ error: "Server error" });
            return;
        }
    }
);

// DELETE /movies/:id
router.delete("/:id", async (req: Request, res: Response): Promise<void> => {
    try {
        await movieRepo().delete(Number(req.params.id));
        res.sendStatus(204);
        return;
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Server error" });
        return;
    }
});

export default router;
