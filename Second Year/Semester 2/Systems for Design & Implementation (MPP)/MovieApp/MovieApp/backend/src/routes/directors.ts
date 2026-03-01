// src/routes/directors.ts
import { Router, Request, Response, NextFunction } from "express";
import { AppDataSource } from "../data-source";
import { Director } from "../entity/Director";
import { validate } from "class-validator";
import { body, validationResult } from "express-validator";
import { auth, AuthRequest } from "../middleware/auth";

const router = Router();
const repo = () => AppDataSource.getRepository(Director);

// GET /directors/public - Allows accessing basic director data without authentication
router.get("/public", async (req: Request, res: Response): Promise<void> => {
    try {
        console.log("GET /directors/public request received");
        res.json({
            directors: [],
            page: 1,
            totalItems: 0,
            totalPages: 0,
            message: "Please log in to view your directors or register to create your collection"
        });
    } catch (err) {
        console.error("Error fetching public directors:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// Middleware to check admin role
const requireAdmin = async (req: AuthRequest, res: Response, next: NextFunction): Promise<void> => {
    if (!req.user || req.user.role !== 'admin') {
        res.status(403).json({ error: "Access denied. Admin privileges required" });
        return;
    }
    next();
};

// Helper function for POST /directors
const createDirectorHandler = async (req: AuthRequest, res: Response): Promise<void> => {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
        res.status(400).json({ errors: errors.array() });
        return;
    }

    try {
        const directorName = req.body.name;
        console.log(`Creating director "${directorName}" for user ${req.user?.id || 'unknown'}`);

        const director = repo().create({
            ...req.body,
            user: req.user // Associate director with the current user
        });

        const validationErrors = await validate(director);
        if (validationErrors.length > 0) {
            res.status(400).json({ errors: validationErrors });
            return;
        }

        const savedResult = await repo().save(director);
        console.log(`Director "${directorName}" created for user ${req.user?.id || 'unknown'}`);
        res.status(201).json(savedResult);
    } catch (err: any) {
        console.error("Error creating director:", err.message);
        if (err.code === "SQLITE_CONSTRAINT_UNIQUE") {
            res.status(400).json({ error: "Director with this name already exists" });
            return;
        }
        res.status(500).json({ error: "Server error" });
    }
};

// POST /directors
router.post(
    "/",
    auth,
    [
        body("name")
            .notEmpty()
            .withMessage("Name is required")
            .isLength({ min: 1, max: 100 })
            .withMessage("Name must be between 1 and 100 characters"),
    ],
    createDirectorHandler
);

// Helper function for GET /directors
const getDirectorsHandler = async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        console.log("GET /directors request received");
        const page = parseInt((req.query.page as string) || "1", 10);
        const limit = parseInt((req.query.limit as string) || "10", 10);
        const nameFilter = (req.query.name as string) || "";
        const sortBy = (req.query.sortBy as string) || "name";
        const order = ((req.query.order as string) || "ASC")
            .toUpperCase() as "ASC" | "DESC";
        const noMovies = req.query.noMovies === "true";
        const countOnly = req.query.countOnly === "true"; // Just get counts, no movie details

        // Select only the fields we need
        const qb = repo()
            .createQueryBuilder("d")
            .select(["d.id", "d.name"]);

        // Only join movies if we need them
        if (!noMovies) {
            if (countOnly) {
                // Just load the count of movies, not the actual movie data
                qb.loadRelationCountAndMap("d.movieCount", "d.movies");
            } else {
                qb.leftJoinAndSelect("d.movies", "m");
            }
        }

        // Filter by user unless admin
        if (req.user && req.user.role !== 'admin') {
            qb.innerJoin("d.user", "u")
                .andWhere("u.id = :userId", { userId: req.user.id });
        }

        qb.orderBy(`d.${sortBy}`, order);

        if (nameFilter.trim()) {
            qb.where("d.name LIKE :n", { n: `%${nameFilter}%` });
        }

        const [directors, totalItems] = await qb
            .skip((page - 1) * limit)
            .take(limit)
            .getManyAndCount();

        const totalPages = Math.ceil(totalItems / limit);

        console.log(`Found ${directors.length} directors for user ${req.user?.id || 'unknown'}`);

        res.json({
            directors,
            page,
            totalItems,
            totalPages
        });
    } catch (err: any) {
        console.error("Error fetching directors:", err.message);
        res.status(500).json({ error: "Server error" });
    }
};

// GET /directors
router.get("/", auth, getDirectorsHandler);

// Helper function for GET all directors
const getAllDirectorsHandler = async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        const directors = await repo().find();
        res.json({ directors });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Server error" });
    }
};

// GET all directors (admin only)
router.get("/all", auth, requireAdmin, getAllDirectorsHandler);

// Helper function for GET /directors/:id
const getDirectorByIdHandler = async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        const id = Number(req.params.id);
        const director = await repo().findOne({
            where: { id },
            relations: ["movies", "user"]
        });

        if (!director) {
            res.status(404).json({ error: "Director not found" });
            return;
        }

        // Check if the director belongs to the user or user is admin
        if (req.user && req.user.role !== 'admin' && director.user && director.user.id !== req.user.id) {
            res.status(403).json({ error: "Access denied" });
            return;
        }

        res.json(director);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Server error" });
    }
};

// GET /directors/:id
router.get("/:id", auth, getDirectorByIdHandler);

// Helper function for PATCH /directors/:id
const updateDirectorHandler = async (req: AuthRequest, res: Response): Promise<void> => {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
        res.status(400).json({ errors: errors.array() });
        return;
    }

    try {
        const id = Number(req.params.id);
        const director = await repo().findOne({
            where: { id },
            relations: ["movies", "user"]
        });

        if (!director) {
            res.status(404).json({ error: "Director not found" });
            return;
        }

        // Check if the director belongs to the user or user is admin
        if (req.user && req.user.role !== 'admin' && director.user && director.user.id !== req.user.id) {
            res.status(403).json({ error: "You can only update your own directors" });
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
    } catch (err: any) {
        console.error(err);
        if (err.code === "SQLITE_CONSTRAINT_UNIQUE") {
            res.status(400).json({ error: "Director with this name already exists" });
            return;
        }
        res.status(500).json({ error: "Server error" });
    }
};

// PATCH /directors/:id
router.patch(
    "/:id",
    auth,
    [
        body("name")
            .optional()
            .notEmpty()
            .withMessage("Name cannot be empty")
            .isLength({ min: 1, max: 100 })
            .withMessage("Name must be between 1 and 100 characters"),
    ],
    updateDirectorHandler
);

// Helper function for DELETE /directors/:id
const deleteDirectorHandler = async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        const id = Number(req.params.id);
        const director = await repo().findOne({
            where: { id },
            relations: ["movies", "user"]
        });

        if (!director) {
            res.status(404).json({ error: "Director not found" });
            return;
        }

        // Check if the director belongs to the user or user is admin
        if (req.user && req.user.role !== 'admin' && director.user && director.user.id !== req.user.id) {
            res.status(403).json({ error: "You can only delete your own directors" });
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
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Server error" });
    }
};

// DELETE /directors/:id
router.delete("/:id", auth, deleteDirectorHandler);

export default router;
