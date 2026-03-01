// src/routes/directors.ts
import { Router, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { Director } from "../entity/Director";
import { validate } from "class-validator";
import { body, validationResult } from "express-validator";

const router = Router();
const repo = () => AppDataSource.getRepository(Director);

// POST /directors
router.post(
    "/",
    [
        body("name")
            .notEmpty()
            .withMessage("Name is required")
            .isLength({ min: 1, max: 100 })
            .withMessage("Name must be between 1 and 100 characters"),
    ],
    async (req: Request, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const director = repo().create(req.body);
            const validationErrors = await validate(director);
            if (validationErrors.length > 0) {
                res.status(400).json({ errors: validationErrors });
                return;
            }

            const saved = await repo().save(director);
            res.status(201).json(saved);
            return;
        } catch (err: any) {
            console.error(err);
            if (err.code === "SQLITE_CONSTRAINT_UNIQUE") {
                res.status(400).json({ error: "Director with this name already exists" });
                return;
            }
            res.status(500).json({ error: "Server error" });
            return;
        }
    }
);

// GET /directors
router.get(
    "/",
    async (req: Request, res: Response): Promise<void> => {
        try {
            const nameFilter = (req.query.name as string) || "";
            const sortBy = (req.query.sortBy as string) || "name";
            const order = ((req.query.order as string) || "ASC")
                .toUpperCase() as "ASC" | "DESC";

            const qb = repo()
                .createQueryBuilder("d")
                .leftJoinAndSelect("d.movies", "m")
                .orderBy(`d.${sortBy}`, order);

            if (nameFilter.trim()) {
                qb.where("d.name ILIKE :n", { n: `%${nameFilter}%` });
            }

            const list = await qb.getMany();
            res.json(list);
            return;
        } catch (err) {
            console.error(err);
            res.status(500).json({ error: "Server error" });
            return;
        }
    }
);

// GET /directors/:id
router.get(
    "/:id",
    async (req: Request, res: Response): Promise<void> => {
        try {
            const id = Number(req.params.id);
            const d = await repo().findOne({
                where: { id },
                relations: ["movies"]
            });
            if (!d) {
                res.status(404).json({ error: "Director not found" });
                return;
            }
            res.json(d);
            return;
        } catch (err) {
            console.error(err);
            res.status(500).json({ error: "Server error" });
            return;
        }
    }
);

// PATCH /directors/:id
router.patch(
    "/:id",
    [
        body("name")
            .optional()
            .notEmpty()
            .withMessage("Name cannot be empty")
            .isLength({ min: 1, max: 100 })
            .withMessage("Name must be between 1 and 100 characters"),
    ],
    async (req: Request, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const id = Number(req.params.id);
            const director = await repo().findOne({
                where: { id },
                relations: ["movies"]
            });

            if (!director) {
                res.status(404).json({ error: "Director not found" });
                return;
            }

            const updatedDirector = repo().merge(director, req.body);
            const validationErrors = await validate(updatedDirector);
            if (validationErrors.length > 0) {
                res.status(400).json({ errors: validationErrors });
                return;
            }

            const saved = await repo().save(updatedDirector);
            res.json(saved);
            return;
        } catch (err: any) {
            console.error(err);
            if (err.code === "SQLITE_CONSTRAINT_UNIQUE") {
                res.status(400).json({ error: "Director with this name already exists" });
                return;
            }
            res.status(500).json({ error: "Server error" });
            return;
        }
    }
);

// DELETE /directors/:id
router.delete(
    "/:id",
    async (req: Request, res: Response): Promise<void> => {
        try {
            const id = Number(req.params.id);
            const director = await repo().findOne({
                where: { id },
                relations: ["movies"]
            });

            if (!director) {
                res.status(404).json({ error: "Director not found" });
                return;
            }

            // Check if director has movies
            if (director.movies && director.movies.length > 0) {
                res.status(400).json({
                    error: "Cannot delete director with associated movies",
                    movieCount: director.movies.length
                });
                return;
            }

            await repo().delete(id);
            res.sendStatus(204);
            return;
        } catch (err) {
            console.error(err);
            res.status(500).json({ error: "Server error" });
            return;
        }
    }
);

export default router;
