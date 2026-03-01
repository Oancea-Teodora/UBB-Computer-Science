import { Router, Request, Response } from "express";
import { AppDataSource } from "../data-source";
import { Director } from "../entity/Director";
import { Movie } from "../entity/Movie";
import { auth, AuthRequest } from "../middleware/auth";
import { CacheService } from "../services/CacheService";
import { User } from "../entity/User";

const router = Router();
const cacheService = CacheService.getInstance();

// GET /stats/directors/movies - Optimized with caching
router.get("/directors/movies", auth, async (req: AuthRequest, res: Response) => {
    try {
        // Build cache key based on user role
        const userId = req.user?.id;
        const isAdmin = req.user?.role === 'admin';
        const cacheKey = `directors_movies_${isAdmin ? 'admin' : userId}`;

        const response = await cacheService.getOrSet(
            cacheKey,
            async () => {
                // Optimized query with better indexing
                const queryBuilder = AppDataSource
                    .createQueryBuilder()
                    .select("d.name", "directorName")
                    .addSelect("COUNT(m.id)", "movieCount")
                    .addSelect("AVG(CAST(strftime('%Y', m.date) AS INTEGER))", "avgYear")
                    .from(Director, "d")
                    .leftJoin("d.movies", "m", "m.directorId = d.id") // Explicit join
                    .leftJoin("d.user", "u"); // Join with user

                // Filter by user unless admin
                if (!isAdmin && userId) {
                    queryBuilder.where("u.id = :userId", { userId });
                }

                return await queryBuilder
                    .groupBy("d.id")
                    .orderBy("movieCount", "DESC")
                    .limit(10)
                    .getRawMany();
            }
        );

        res.json({
            executionTime: response.executionTime,
            fromCache: response.fromCache,
            result: response.data
        });
    } catch (err) {
        console.error("Error in directors/movies stats:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// GET /stats/movies/yearly - Optimized with caching and query improvements
router.get("/movies/yearly", auth, async (req: AuthRequest, res: Response) => {
    try {
        // Build cache key based on user role
        const userId = req.user?.id;
        const isAdmin = req.user?.role === 'admin';
        const cacheKey = `movies_yearly_${isAdmin ? 'admin' : userId}`;

        const response = await cacheService.getOrSet(
            cacheKey,
            async () => {
                // More efficient approach using subquery for complex filtering
                const queryBuilder = AppDataSource
                    .createQueryBuilder()
                    .select("year", "year")
                    .addSelect("COUNT(*)", "count")
                    .from(subQuery => {
                        const subQb = subQuery
                            .select("strftime('%Y', m.date)", "year")
                            .from(Movie, "m");

                        // Filter by user unless admin
                        if (!isAdmin && userId) {
                            subQb.innerJoin("m.user", "u")
                                .where("u.id = :userId", { userId });
                        }

                        return subQb;
                    }, "year_data");

                return await queryBuilder
                    .groupBy("year")
                    .orderBy("year", "DESC")
                    .getRawMany();
            }
        );

        res.json({
            executionTime: response.executionTime,
            fromCache: response.fromCache,
            result: response.data
        });
    } catch (err) {
        console.error("Error in movies/yearly stats:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// New endpoint for dashboard statistics with optimized query
router.get("/dashboard", auth, async (req: AuthRequest, res: Response) => {
    try {
        // Only available to admin users
        if (req.user?.role !== 'admin') {
            res.status(403).json({ error: "Access denied" });
            return;
        }

        const cacheKey = 'dashboard_stats';

        const response = await cacheService.getOrSet(
            cacheKey,
            async () => {
                // Use transaction to ensure data consistency across multiple queries
                return await AppDataSource.transaction(async transactionalManager => {
                    // Get total counts
                    const [totalMovies, totalDirectors, totalUsers] = await Promise.all([
                        transactionalManager.getRepository(Movie).count(),
                        transactionalManager.getRepository(Director).count(),
                        transactionalManager.getRepository(User).count()
                    ]);

                    // Get top directors by movie count
                    const topDirectors = await transactionalManager
                        .createQueryBuilder()
                        .select("d.name", "name")
                        .addSelect("COUNT(m.id)", "movieCount")
                        .from(Director, "d")
                        .leftJoin("d.movies", "m")
                        .groupBy("d.id")
                        .orderBy("movieCount", "DESC")
                        .limit(5)
                        .getRawMany();

                    // Get movie distribution by decades - using materialized approach
                    const moviesByDecade = await transactionalManager
                        .query(`
                            WITH decades AS (
                                SELECT 
                                    (CAST(strftime('%Y', date) AS INTEGER) / 10) * 10 AS decade,
                                    COUNT(*) AS count
                                FROM movie
                                GROUP BY decade
                            )
                            SELECT 
                                decade || 's' AS label,
                                count
                            FROM decades
                            ORDER BY decade DESC
                        `);

                    return {
                        counts: { movies: totalMovies, directors: totalDirectors, users: totalUsers },
                        topDirectors,
                        moviesByDecade
                    };
                });
            },
            10 * 60 * 1000 // 10 minute cache for dashboard
        );

        res.json({
            executionTime: response.executionTime,
            fromCache: response.fromCache,
            ...response.data
        });
    } catch (err) {
        console.error("Error in dashboard stats:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// GET /stats/movies/genres - Movie distribution by genre (new endpoint)
router.get("/movies/genres", auth, async (req: AuthRequest, res: Response) => {
    try {
        const userId = req.user?.id;
        const isAdmin = req.user?.role === 'admin';
        const cacheKey = `movies_genres_${isAdmin ? 'admin' : userId}`;

        const response = await cacheService.getOrSet(
            cacheKey,
            async () => {
                const queryBuilder = AppDataSource
                    .createQueryBuilder()
                    .select("m.genre", "genre")
                    .addSelect("COUNT(*)", "count")
                    .from(Movie, "m");

                // Filter by user unless admin
                if (!isAdmin && userId) {
                    queryBuilder.innerJoin("m.user", "u")
                        .where("u.id = :userId", { userId });
                }

                return await queryBuilder
                    .where("m.genre IS NOT NULL")
                    .andWhere("m.genre != ''")
                    .groupBy("m.genre")
                    .orderBy("count", "DESC")
                    .limit(8)
                    .getRawMany();
            }
        );

        res.json({
            executionTime: response.executionTime,
            fromCache: response.fromCache,
            result: response.data
        });
    } catch (err) {
        console.error("Error in movies/genres stats:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// GET /stats/users/activity - User activity statistics (admin only)
router.get("/users/activity", auth, async (req: AuthRequest, res: Response) => {
    try {
        if (req.user?.role !== 'admin') {
            res.status(403).json({ error: "Access denied" });
            return;
        }

        const cacheKey = 'users_activity_admin';

        const response = await cacheService.getOrSet(
            cacheKey,
            async () => {
                return await AppDataSource.transaction(async transactionalManager => {
                    // Get user activity stats
                    const userActivity = await transactionalManager
                        .createQueryBuilder()
                        .select("u.email", "email")
                        .addSelect("COUNT(DISTINCT m.id)", "movieCount")
                        .addSelect("COUNT(DISTINCT d.id)", "directorCount")
                        .addSelect("u.created_at", "joinDate")
                        .from(User, "u")
                        .leftJoin("u.movies", "m")
                        .leftJoin("u.directors", "d")
                        .where("u.role != 'admin'")
                        .groupBy("u.id")
                        .orderBy("movieCount", "DESC")
                        .limit(10)
                        .getRawMany();

                    return userActivity;
                });
            },
            5 * 60 * 1000 // 5 minute cache
        );

        res.json({
            executionTime: response.executionTime,
            fromCache: response.fromCache,
            result: response.data
        });
    } catch (err) {
        console.error("Error in users/activity stats:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// GET /stats/movies/recent - Most recently added movies
router.get("/movies/recent", auth, async (req: AuthRequest, res: Response) => {
    try {
        const userId = req.user?.id;
        const isAdmin = req.user?.role === 'admin';
        const cacheKey = `movies_recent_${isAdmin ? 'admin' : userId}`;

        const response = await cacheService.getOrSet(
            cacheKey,
            async () => {
                const queryBuilder = AppDataSource
                    .createQueryBuilder()
                    .select(["m.id", "m.title", "m.date", "m.createdAt", "d.name as directorName"])
                    .from(Movie, "m")
                    .leftJoin("m.director", "d");

                // Filter by user unless admin
                if (!isAdmin && userId) {
                    queryBuilder.innerJoin("m.user", "u")
                        .where("u.id = :userId", { userId });
                }

                return await queryBuilder
                    .orderBy("m.createdAt", "DESC")
                    .limit(10)
                    .getRawMany();
            }
        );

        res.json({
            executionTime: response.executionTime,
            fromCache: response.fromCache,
            result: response.data
        });
    } catch (err) {
        console.error("Error in movies/recent stats:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// GET /stats/movies/top-titles - Movies with longest titles
router.get("/movies/top-titles", auth, async (req: AuthRequest, res: Response) => {
    try {
        const userId = req.user?.id;
        const isAdmin = req.user?.role === 'admin';
        const cacheKey = `movies_top_titles_${isAdmin ? 'admin' : userId}`;

        const response = await cacheService.getOrSet(
            cacheKey,
            async () => {
                const queryBuilder = AppDataSource
                    .createQueryBuilder()
                    .select("m.title", "title")
                    .addSelect("LENGTH(m.title)", "titleLength")
                    .addSelect("d.name", "directorName")
                    .addSelect("m.date", "releaseDate")
                    .from(Movie, "m")
                    .leftJoin("m.director", "d");

                // Filter by user unless admin
                if (!isAdmin && userId) {
                    queryBuilder.innerJoin("m.user", "u")
                        .where("u.id = :userId", { userId });
                }

                return await queryBuilder
                    .orderBy("titleLength", "DESC")
                    .limit(10)
                    .getRawMany();
            }
        );

        res.json({
            executionTime: response.executionTime,
            fromCache: response.fromCache,
            result: response.data
        });
    } catch (err) {
        console.error("Error in movies/top-titles stats:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// GET /stats/movies/title-lengths - Distribution of movie title lengths
router.get("/movies/title-lengths", auth, async (req: AuthRequest, res: Response) => {
    try {
        const userId = req.user?.id;
        const isAdmin = req.user?.role === 'admin';
        const cacheKey = `movies_title_lengths_${isAdmin ? 'admin' : userId}`;

        const response = await cacheService.getOrSet(
            cacheKey,
            async () => {
                const queryBuilder = AppDataSource
                    .createQueryBuilder()
                    .select("CASE " +
                        "WHEN LENGTH(m.title) <= 10 THEN 'Short (≤10)' " +
                        "WHEN LENGTH(m.title) <= 20 THEN 'Medium (11-20)' " +
                        "WHEN LENGTH(m.title) <= 30 THEN 'Long (21-30)' " +
                        "ELSE 'Very Long (30+)' END", "category")
                    .addSelect("COUNT(*)", "count")
                    .from(Movie, "m");

                // Filter by user unless admin
                if (!isAdmin && userId) {
                    queryBuilder.innerJoin("m.user", "u")
                        .where("u.id = :userId", { userId });
                }

                return await queryBuilder
                    .groupBy("category")
                    .orderBy("count", "DESC")
                    .getRawMany();
            }
        );

        res.json({
            executionTime: response.executionTime,
            fromCache: response.fromCache,
            result: response.data
        });
    } catch (err) {
        console.error("Error in movies/title-lengths stats:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// Clear cache endpoint (admin only)
router.post("/clear-cache", auth, async (req: AuthRequest, res: Response) => {
    try {
        if (req.user?.role !== 'admin') {
            res.status(403).json({ error: "Access denied" });
            return;
        }

        cacheService.clearAll();
        res.json({ message: "Statistics cache cleared successfully" });
    } catch (err) {
        console.error("Error clearing stats cache:", err);
        res.status(500).json({ error: "Server error" });
    }
});

// Basic stats endpoint for testing (no authentication required)
router.get("/public", async (req: Request, res: Response) => {
    try {
        res.json({
            message: "Statistics service is running",
            status: "operational",
            note: "Please log in to access detailed statistics"
        });
    } catch (err) {
        console.error("Error in public stats:", err);
        res.status(500).json({ error: "Server error" });
    }
});

export default router; 