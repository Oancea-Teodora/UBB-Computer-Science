// src/seed.ts
import "reflect-metadata";
import { AppDataSource } from "./data-source";
import { Director } from "./entity/Director";
import { Movie } from "./entity/Movie";
import { User } from "./entity/User";
import * as bcrypt from "bcrypt";

interface RawMovie {
    title: string;
    directorName: string;
    date: string;
    poster: string;
}


const rawMovies: RawMovie[] = [
    { title: "Interstellar", directorName: "Christopher Nolan", date: "7 November 2014", poster: "/interstellar.jpg" },
    { title: "Inception", directorName: "Christopher Nolan", date: "16 July 2010", poster: "https://…TP8n4Rf1Ehjkr…" },
    { title: "The Dark Knight", directorName: "Christopher Nolan", date: "18 July 2008", poster: "https://…MTMxNTMwOD…" },
    { title: "The Matrix", directorName: "Lana & Lilly Wachowski", date: "31 March 1999", poster: "https://…The_Matrix_Poster.jpg" },
    { title: "Avengers: Endgame", directorName: "Anthony & Joe Russo", date: "26 April 2019", poster: "https://…Endgame_poster.jpg" },
    { title: "Parasite", directorName: "Bong Joon-ho", date: "30 May 2019", poster: "https://…Parasite_(2019_film).png" },
    { title: "Joker", directorName: "Todd Phillips", date: "4 October 2019", poster: "https://…Joker_(2019_film)_poster.jpg" },
    { title: "Fight Club", directorName: "David Fincher", date: "15 October 1999", poster: "https://…Fight_Club_poster.jpg" },
    { title: "Pulp Fiction", directorName: "Quentin Tarantino", date: "14 October 1994", poster: "https://…Pulp_Fiction_(1994)_poster.jpg" },
    { title: "Gladiator", directorName: "Ridley Scott", date: "5 May 2000", poster: "https://…Gladiator.jpg" },
    { title: "The Social Network", directorName: "David Fincher", date: "1 October 2010", poster: "https://…The_Social_Network_poster.png" },
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

async function seed() {
    const dataSource = await AppDataSource.initialize();
    const userRepo = dataSource.getRepository(User);
    const directorRepo = dataSource.getRepository(Director);
    const movieRepo = dataSource.getRepository(Movie);

    // Create admin user if it doesn't exist
    let adminUser = await userRepo.findOne({ where: { email: "admin@gmail.com" } });
    if (!adminUser) {
        console.log("Creating admin user...");
        adminUser = userRepo.create({
            email: "admin@gmail.com",
            password: "admin",
            name: "Administrator",
            role: "admin"
        });
        adminUser = await userRepo.save(adminUser);
        console.log("Admin user created successfully");
    }

    // build a map of Director entities
    const directorMap = new Map<string, Director>();
    for (const { directorName } of rawMovies) {
        if (!directorMap.has(directorName)) {
            let d = await directorRepo.findOne({ where: { name: directorName } });
            if (!d) {
                d = directorRepo.create({
                    name: directorName,
                    user: adminUser // Associate with admin user
                });
                d = await directorRepo.save(d);
            }
            directorMap.set(directorName, d);
        }
    }

    // insert movies
    for (const { title, directorName, date, poster } of rawMovies) {
        const existing = await movieRepo.findOne({ where: { title } });
        if (existing) continue;

        const movie = movieRepo.create({
            title,
            date,
            poster,
            director: directorMap.get(directorName)!,  // safe because we seeded above
            user: adminUser // Associate with admin user
        });
        await movieRepo.save(movie);
        console.log("Inserted movie:", title);
    }

    console.log("✅ Seed complete");
    await dataSource.destroy();
}

seed().catch(err => {
    console.error("❌ Seed failure:", err);
    process.exit(1);
});