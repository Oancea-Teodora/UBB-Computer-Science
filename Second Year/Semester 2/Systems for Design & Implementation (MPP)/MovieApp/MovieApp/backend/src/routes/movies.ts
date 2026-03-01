// src/routes/movies.ts
import { Router, Request, Response, NextFunction } from "express";
import { body, validationResult } from "express-validator";
import { AppDataSource } from "../data-source";
import { Movie } from "../entity/Movie";
import { Director } from "../entity/Director";
import { auth, AuthRequest } from "../middleware/auth";
import { CacheService } from "../services/CacheService";

const router = Router();
const movieRepo = () => AppDataSource.getRepository(Movie);
const directorRepo = () => AppDataSource.getRepository(Director);

// Initialize the cache service
const cacheService = CacheService.getInstance();

// Middleware to check admin role
const requireAdmin = async (req: AuthRequest, res: Response, next: NextFunction): Promise<void> => {
    if (!req.user || req.user.role !== 'admin') {
        res.status(403).json({ error: "Access denied. Admin privileges required" });
        return;
    }
    next();
};

// GET /movies?page=1&limit=10&director=Name
router.get("/", auth, async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        console.log("GET /movies request received");
        const page = parseInt((req.query.page as string) || "1", 10);
        const limit = parseInt((req.query.limit as string) || "10", 10);
        const director = (req.query.director as string) || "";
        const all = req.query.all === "true";

        // Build cache key based on user role and query parameters
        const userId = req.user?.id;
        const isAdmin = req.user?.role === 'admin';
        const cacheKey = `movies_${isAdmin ? 'admin' : userId}_${page}_${limit}_${director}_${all}`;

        // Use cache for improved performance
        const response = await cacheService.getOrSet(
            cacheKey,
            async () => {
                // Select only needed fields for faster queries
                const qb = movieRepo()
                    .createQueryBuilder("m")
                    .select(["m.id", "m.title", "m.date", "m.poster", "d.id", "d.name"])
                    .leftJoin("m.director", "d");

                // Filter by director name if specified
                if (director.trim()) {
                    qb.where("d.name LIKE :name", { name: `%${director}%` });
                }

                // Filter by user unless admin
                if (req.user && req.user.role !== 'admin') {
                    qb.innerJoin("m.user", "u")
                        .andWhere("u.id = :userId", { userId: req.user.id });
                }

                if (!all) {
                    qb.orderBy("m.title", "ASC")
                        .skip((page - 1) * limit)
                        .take(limit);
                }

                const [movies, totalItems] = await qb.getManyAndCount();
                const totalPages = Math.ceil(totalItems / limit);

                console.log(`Found ${movies.length} movies for user ${req.user?.id || 'unknown'}`);

                return { movies, page, totalItems, totalPages };
            },
            // TTL of 5 minutes for regular users, 2 minutes for admin to balance freshness with performance
            isAdmin ? 2 * 60 * 1000 : 5 * 60 * 1000
        );

        res.json(response.data);
    } catch (err) {
        console.error("Error fetching movies:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// GET all movies (admin only)
router.get("/all", auth, requireAdmin, async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        const cacheKey = 'movies_all_admin';

        const response = await cacheService.getOrSet(
            cacheKey,
            async () => {
                // Use a more optimized query with only the needed fields
                const movies = await movieRepo()
                    .createQueryBuilder("m")
                    .select(["m.id", "m.title", "m.date", "m.poster", "d.id", "d.name"])
                    .leftJoin("m.director", "d")
                    .orderBy("m.title", "ASC")
                    .getMany();

                return { movies };
            },
            // 2 minute cache for admin all movies query
            2 * 60 * 1000
        );

        res.json(response.data);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Server error" });
    }
});

// POST /movies
router.post(
    "/",
    auth,
    [
        body("title").notEmpty().withMessage("Title is required"),
        body("directorId").isInt().withMessage("directorId must be an integer"),
        body("date").notEmpty().withMessage("Date is required"),
        body("poster").notEmpty().withMessage("Poster is required"),
    ],
    async (req: AuthRequest, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const { title, date, poster, directorId } = req.body;
            const dir = await directorRepo().findOne({
                where: { id: directorId }
            });
            if (!dir) {
                res.status(400).json({ error: "Invalid directorId" });
                return;
            }

            // Check if director belongs to the user or user is admin
            if (req.user && req.user.role !== 'admin' && dir.user && dir.user.id !== req.user.id) {
                res.status(403).json({ error: "You can only add movies for your own directors" });
                return;
            }

            const movie = movieRepo().create({
                title,
                date,
                poster,
                director: dir,
                user: req.user  // Associate movie with the current user
            });
            const saved = await movieRepo().save(movie);
            console.log(`Movie created: "${title}" by user ${req.user?.id || 'unknown'}`);
            res.status(201).json(saved);
        } catch (err) {
            console.error("Error creating movie:", err);
            res.status(500).json({ error: "Server error" });
        }
    }
);

// PATCH /movies/:id
router.patch(
    "/:id",
    auth,
    [
        body("title").optional().notEmpty().withMessage("Title cannot be empty"),
        body("directorId").optional().isInt().withMessage("directorId must be integer"),
        body("date").optional().notEmpty().withMessage("Date cannot be empty"),
        body("poster").optional().notEmpty().withMessage("Poster cannot be empty"),
    ],
    async (req: AuthRequest, res: Response): Promise<void> => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            res.status(400).json({ errors: errors.array() });
            return;
        }

        try {
            const id = Number(req.params.id);
            const movie = await movieRepo().findOne({
                where: { id },
                relations: ["director", "user"]
            });

            if (!movie) {
                res.status(404).json({ error: "Movie not found" });
                return;
            }

            // Check if movie belongs to the user or user is admin
            if (req.user && req.user.role !== 'admin' && movie.user && movie.user.id !== req.user.id) {
                res.status(403).json({ error: "You can only update your own movies" });
                return;
            }

            // If updating director, check that the new director exists and belongs to user
            if (req.body.directorId) {
                const dir = await directorRepo().findOne({
                    where: { id: req.body.directorId },
                    relations: ["user"]
                });
                if (!dir) {
                    res.status(400).json({ error: "Invalid directorId" });
                    return;
                }

                // Check if director belongs to the user or user is admin
                if (req.user && req.user.role !== 'admin' && dir.user && dir.user.id !== req.user.id) {
                    res.status(403).json({ error: "You can only assign directors that belong to you" });
                    return;
                }

                movie.director = dir;
            }

            // Update other fields
            if (req.body.title) movie.title = req.body.title;
            if (req.body.date) movie.date = req.body.date;
            if (req.body.poster) movie.poster = req.body.poster;

            const saved = await movieRepo().save(movie);
            res.json(saved);
        } catch (err) {
            console.error(err);
            res.status(500).json({ error: "Server error" });
        }
    }
);

// DELETE /movies/:id
router.delete("/:id", auth, async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        const id = Number(req.params.id);
        const movie = await movieRepo().findOne({
            where: { id },
            relations: ["user"]
        });

        if (!movie) {
            res.status(404).json({ error: "Movie not found" });
            return;
        }

        // Check if movie belongs to the user or user is admin
        if (req.user && req.user.role !== 'admin' && movie.user && movie.user.id !== req.user.id) {
            res.status(403).json({ error: "You can only delete your own movies" });
            return;
        }

        await movieRepo().delete(id);
        res.sendStatus(204);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Server error" });
    }
});

// CLEAR CACHE - Admin only endpoint
router.post("/clear-cache", auth, requireAdmin, async (req: AuthRequest, res: Response): Promise<void> => {
    try {
        cacheService.clearAll();
        res.json({ message: "Cache cleared successfully" });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: "Server error" });
    }
});

// GET /movies/public - Allows accessing basic movie data without authentication
router.get("/public", async (req: Request, res: Response): Promise<void> => {
    try {
        console.log("GET /movies/public request received");
        res.json({
            movies: [],
            page: 1,
            totalItems: 0,
            totalPages: 0,
            message: "Please log in to view your movies or register to create your collection"
        });
    } catch (err) {
        console.error("Error fetching public movies:", err);
        res.status(500).json({ error: "Server error" });
    }
});

export default router;
