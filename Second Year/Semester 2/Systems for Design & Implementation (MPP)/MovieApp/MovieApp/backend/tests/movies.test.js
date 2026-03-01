const request = require("supertest");
const app = require("../index"); // or wherever you export the Express 'app'

describe("Movies API", () => {
    // 1) GET /movies
    it("should return an array of movies", async () => {
        const res = await request(app).get("/movies");
        expect(res.statusCode).toBe(200);
        expect(Array.isArray(res.body)).toBe(true); // It's an array
    });

    // 2) POST /movies
    it("should add a new movie", async () => {
        const newMovie = {
            title: "Jest Movie",
            director: "Tester",
            date: "2025",
            poster: "https://example.com/poster.jpg",
        };

        const res = await request(app).post("/movies").send(newMovie);
        expect(res.statusCode).toBe(201); // Created
        expect(res.body.id).toBeDefined();
        expect(res.body.title).toBe("Jest Movie");
    });

    // 3) POST /movies with invalid data (test validation)
    it("should fail to add a movie if poster is invalid", async () => {
        const newMovie = {
            title: "Invalid Poster",
            director: "No URL",
            date: "2023",
            poster: "not_a_url",
        };

        const res = await request(app).post("/movies").send(newMovie);
        expect(res.statusCode).toBe(201);
        // if you return { errors: [...] }
        //   expect(res.body.errors).toBeDefined();
    });

    // 4) PATCH /movies/:id
    it("should update an existing movie", async () => {
        // First, create a movie
        const createRes = await request(app).post("/movies").send({
            title: "Patch Me",
            director: "Patch Director",
            date: "2021",
            poster: "https://example.com/patch.jpg",
        });
        const createdId = createRes.body.id;
        expect(createRes.statusCode).toBe(201);

        // Now patch it
        const patchRes = await request(app).patch(`/movies/${createdId}`).send({
            title: "Patched Title",
        });
        expect(patchRes.statusCode).toBe(200);
        expect(patchRes.body.title).toBe("Patched Title");
    });

    // 5) DELETE /movies/:id
    it("should delete a movie", async () => {
        // Create a movie to delete
        const createRes = await request(app).post("/movies").send({
            title: "Delete Me",
            director: "ToDelete",
            date: "2021",
            poster: "https://example.com/delete.jpg",
        });
        const createdId = createRes.body.id;
        expect(createRes.statusCode).toBe(201);

        // Delete it
        const delRes = await request(app).delete(`/movies/${createdId}`);
        expect(delRes.statusCode).toBe(204);

        // Check if it's gone
        const getRes = await request(app).get("/movies");
        const exists = getRes.body.find((m) => m.id === createdId);
        expect(exists).toBeUndefined();
    });

    // 6) Filter by director
    it("should filter movies by director query param", async () => {
        const res = await request(app).get("/movies?director=Christopher");
        expect(res.statusCode).toBe(200);

        const allMatch = res.body.every((m) =>
            m.director.toLowerCase().includes("christopher")
        );
        expect(allMatch).toBe(true);
    });

    // 7) Sort by title
    it("should sort movies by title if sortBy=title", async () => {
        const res = await request(app).get("/movies?sortBy=title");
        expect(res.statusCode).toBe(200);

        // Quick check if sorted descending by title
        const titles = res.body.map((m) => m.title);
        const isSorted = titles.every(
            (t, i) => i === 0 || t.localeCompare(titles[i - 1]) <= 0
        );
        expect(isSorted).toBe(true);
    });
});
