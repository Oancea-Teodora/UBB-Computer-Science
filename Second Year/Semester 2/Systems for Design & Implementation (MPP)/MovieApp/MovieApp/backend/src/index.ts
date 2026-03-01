// src/index.ts
import "reflect-metadata";
import express, { Request, Response } from "express";
import cors from "cors";
import http from "http";
import { Server as SocketIOServer } from "socket.io";
import { AppDataSource } from "./data-source";
import directorsRouter from "./routes/directors";
import moviesRouter from "./routes/movies";
import fileRoutes from "./routes/files";
import statsRouter from "./routes/stats";
import authRouter from "./routes/auth";
import usersRouter from "./routes/users";
import monitoringRouter from "./routes/monitoring";
import { Director } from "./entity/Director";
import { Movie } from "./entity/Movie";
import { User } from "./entity/User";
import { Log } from "./entity/Log";
import { MonitoredUser } from "./entity/MonitoredUser";
import { loggingMiddleware } from "./middleware/logging";
import { MonitoringService } from "./services/MonitoringService";

// Use Render's PORT environment variable or fallback to 3001 (avoiding frontend port 3000)
const PORT = parseInt(process.env.PORT || '3001', 10);
const app = express();
let autoGenerate = true;

// CORS configuration - allow frontend domain
const allowedOrigins = [
  'http://localhost:3000', // Frontend dev server
  'http://localhost:3001', // Backend server
  'http://localhost:5173', // Vite dev server
  'http://localhost:5174', // Alternative Vite port
  process.env.FRONTEND_URL, // Render frontend URL
  /https:\/\/.*\.onrender\.com$/, // Any Render subdomain
].filter(Boolean);

app.use(cors({
  origin: function (origin, callback) {
    // Allow requests with no origin (like mobile apps or curl requests)
    if (!origin) return callback(null, true);

    // Check if origin matches any of our allowed origins (including regex patterns)
    const isAllowed = allowedOrigins.some(allowedOrigin => {
      if (typeof allowedOrigin === 'string') {
        return origin === allowedOrigin;
      } else if (allowedOrigin instanceof RegExp) {
        return allowedOrigin.test(origin);
      }
      return false;
    });

    if (isAllowed || process.env.NODE_ENV === 'development') {
      return callback(null, true);
    } else {
      console.log(`CORS: Blocked origin ${origin}`);
      return callback(new Error('Not allowed by CORS'));
    }
  },
  credentials: true,
  methods: ['GET', 'POST', 'PATCH', 'DELETE', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization']
}));

app.use(express.json());

// Apply logging middleware to track all requests
app.use(loggingMiddleware);

app.use("/directors", directorsRouter);
app.use("/movies", moviesRouter);
app.use("/files", fileRoutes);
app.use("/stats", statsRouter);
app.use("/auth", authRouter);
app.use("/users", usersRouter);
app.use("/monitoring", monitoringRouter);

// Health check endpoint
app.get("/health", (_req, res) => {
  res.status(200).json({ status: "healthy", message: "Server is running" });
});

app.post("/control/auto-generate", (req: Request, res: Response) => {
  autoGenerate = Boolean(req.body.enable);
  res.json({ autoGenerate });
});

app.get("/control/auto-generate", (_req, res) => {
  res.json({ autoGenerate });
});

app.get("/", (_req: Request, res: Response): void => {
  res.send("Server is running!");
});

const server = http.createServer(app);
const io = new SocketIOServer(server, {
  cors: {
    origin: function (origin, callback) {
      // Allow requests with no origin (like mobile apps or curl requests)
      if (!origin) return callback(null, true);

      // Check if origin matches any of our allowed origins (including regex patterns)
      const isAllowed = allowedOrigins.some(allowedOrigin => {
        if (typeof allowedOrigin === 'string') {
          return origin === allowedOrigin;
        } else if (allowedOrigin instanceof RegExp) {
          return allowedOrigin.test(origin);
        }
        return false;
      });

      if (isAllowed || process.env.NODE_ENV === 'development') {
        return callback(null, true);
      } else {
        console.log(`Socket.IO CORS: Blocked origin ${origin}`);
        return callback(new Error('Not allowed by CORS'));
      }
    },
    credentials: true,
    methods: ['GET', 'POST']
  }
});

io.on("connection", sock => {
  console.log("Client connected:", sock.id);
  sock.on("disconnect", () => console.log("Client disconnected"));
});

// Create an admin user
const createAdminUser = async () => {
  const userRepo = AppDataSource.getRepository(User);
  let adminUser = await userRepo.findOne({ where: { email: "admin@gmail.com" } });

  if (!adminUser) {
    console.log("Creating admin user...");
    adminUser = userRepo.create({
      email: "admin@gmail.com",
      password: "admin",
      name: "Administrator",
      role: "admin"
    });
    await userRepo.save(adminUser);
    console.log("Admin user created successfully");
  }

  return adminUser;
};

AppDataSource.initialize()
  .then(async () => {
    console.log("DataSource initialized");

    // Create admin user
    const adminUser = await createAdminUser();
    console.log("Admin user created/retrieved");

    const dirRepo = AppDataSource.getRepository(Director);
    const movRepo = AppDataSource.getRepository(Movie);

    // Seed data
    const seed = [
      { title: "Interstellar", directorName: "Christopher Nolan", date: "7 November 2014", poster: "/interstellar.jpg" },
      { title: "The Prestige", directorName: "Christopher Nolan", date: "20 October 2006", poster: "https://…Prestige_poster.jpg" },
      { title: "Memento", directorName: "Christopher Nolan", date: "11 October 2000", poster: "https://…Memento_poster.jpg" },
      { title: "The Green Mile", directorName: "Frank Darabont", date: "10 December 1999", poster: "https://…Green_Mile.jpg" },
      { title: "Titanic", directorName: "James Cameron", date: "19 December 1997", poster: "https://…Titanic.jpg" },
      { title: "The Wolf of Wall Street", directorName: "Martin Scorsese", date: "25 December 2013", poster: "https://…Wall_Street.jpg" },
      { title: "Jurassic Park", directorName: "Steven Spielberg", date: "11 June 1993", poster: "https://…Jurassic_Park_poster.jpg" },
      { title: "Braveheart", directorName: "Mel Gibson", date: "24 May 1995", poster: "https://…Braveheart.jpg" },
      { title: "Saving Private Ryan", directorName: "Steven Spielberg", date: "24 July 1998", poster: "https://…Saving_Private_Ryan_poster.jpg" },
      { title: "Forrest Gump", directorName: "Robert Zemeckis", date: "6 July 1994", poster: "https://…Forrest_Gump_poster.jpg" },
    ];

    // Check if we need to seed data for admin
    const moviesCount = await movRepo.count();
    if (moviesCount === 0) {
      console.log("Seeding initial data for admin user...");

      // ensure each director is associated with the admin user
      const names = Array.from(new Set(seed.map(m => m.directorName)));
      await Promise.all(names.map(async name => {
        let d = await dirRepo.findOneBy({ name });
        if (!d) {
          d = dirRepo.create({
            name,
            user: adminUser
          });
          await dirRepo.save(d);
        }
      }));

      // save each movie
      for (const m of seed) {
        const director = await dirRepo.findOneBy({ name: m.directorName });
        if (!director) continue;  // should never happen
        await movRepo.save({
          title: m.title,
          date: m.date,
          poster: m.poster,
          director,
          user: adminUser
        });
      }

      console.log("Seeding complete");
    } else {
      console.log(`Database already has ${moviesCount} movies, skipping seed`);
    }

    // Start monitoring service
    try {
      const monitoringService = MonitoringService.getInstance();
      await monitoringService.startMonitoring();
      console.log("User activity monitoring started");
    } catch (error) {
      console.error("Failed to start monitoring service:", error);
    }

    // Start server after successful initialization
    server.listen(PORT, '0.0.0.0', () => {
      console.log(`🚀 Server is running on port ${PORT}`);
      console.log(`- API: http://0.0.0.0:${PORT}`);
      console.log(`- Health check: http://0.0.0.0:${PORT}/health`);
    });
  })
  .catch((err: Error | unknown) => {
    console.error("❌ Error during Data Source initialization:", err);
    process.exit(1);
  });

// optional: emit new auto-generated movies via WebSockets
setInterval(async () => {
  if (!autoGenerate) return;
  try {
    const movRepo = AppDataSource.getRepository(Movie);
    const dirs = await AppDataSource.getRepository(Director).find();
    if (dirs.length === 0) return;
    const randomDir = dirs[Math.floor(Math.random() * dirs.length)];
    const currentCount = await movRepo.count();

    // Find admin user to associate with auto-generated movies
    const userRepo = AppDataSource.getRepository(User);
    const adminUser = await userRepo.findOne({ where: { email: "admin@gmail.com" } });

    if (!adminUser) return;

    const saved = await movRepo.save({
      title: `AutoGenerated ${currentCount}`,
      date: new Date().toISOString().slice(0, 10),
      poster: `https://via.placeholder.com/200x300?text=Movie`,
      director: randomDir,
      user: adminUser
    });

    io.emit("newMovie", saved);
  } catch (error: Error | unknown) {
    console.error("Error in auto-generate interval:", error);
  }
}, 7000);

// Debug endpoint
app.get('/debug/auth', (req: Request, res: Response) => {
  const token = req.header('Authorization')?.replace('Bearer ', '');

  res.json({
    timestamp: new Date().toISOString(),
    hasToken: !!token,
    tokenPreview: token ? `${token.substring(0, 10)}...` : null,
    headers: {
      authorization: req.headers.authorization,
      'content-type': req.headers['content-type'],
      host: req.headers.host,
      origin: req.headers.origin,
      referer: req.headers.referer
    },
    cookies: req.headers.cookie,
    query: req.query
  });
});
