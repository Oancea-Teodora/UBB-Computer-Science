// src/routes/files.ts
import { Router, Request, Response } from "express";
import multer, { FileFilterCallback } from "multer";
import path from "path";

const router = Router();

// 1. Configure Multer storage engine
const storage = multer.diskStorage({
    destination: (_req: Request, _file: Express.Multer.File, cb: (error: Error | null, destination: string) => void) => {
        cb(null, path.resolve(__dirname, "../uploads")); // absolute path to backend/uploads
    },
    filename: (_req: Request, file: Express.Multer.File, cb: (error: Error | null, filename: string) => void) => {
        // Prefix with timestamp to avoid name collisions
        cb(null, `${Date.now()}-${file.originalname}`);
    },
});

// 2. Create the Multer instance
const upload = multer({
    storage,
    limits: { fileSize: 1024 * 1024 * 1024 }, // up to 1 GB
    fileFilter: (_req: Request, file: Express.Multer.File, cb: FileFilterCallback) => {
        // Optionally filter by mimetype, e.g. only videos:
        // if (!file.mimetype.startsWith("video/")) {
        //   return cb(new Error("Only video files are allowed"));
        // }
        cb(null, true);
    },
});

// 3. POST /files/upload — single-file upload
router.post(
    "/upload",
    upload.single("file"),
    async (req: Request, res: Response): Promise<void> => {
        if (!req.file) {
            res.status(400).json({ error: "No file uploaded" });
            return;
        }

        // Respond with file metadata
        res.status(200).json({
            message: "File uploaded successfully",
            filename: req.file.filename,
            originalName: req.file.originalname,
            size: req.file.size,
        });
        // no return value
    }
);


// 4. GET /files/download/:filename — serve a file for download
router.get("/download/:filename", (req: Request, res: Response) => {
    const { filename } = req.params;
    const filepath = path.resolve(__dirname, "../uploads", filename);
    res.download(filepath, filename, (err) => {
        if (err) {
            console.error("Download error:", err);
            res.status(500).json({ error: "Error downloading file" });
        }
    });
});

export default router;
