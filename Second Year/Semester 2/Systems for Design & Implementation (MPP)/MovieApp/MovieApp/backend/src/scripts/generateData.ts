import { faker } from '@faker-js/faker';
import { AppDataSource } from '../data-source';
import { Director } from '../entity/Director';
import { Movie } from '../entity/Movie';

const NUM_DIRECTORS = 100000; // We'll create 1000 directors
const MOVIES_PER_DIRECTOR = 2; // Each director will have 100 movies

async function generateData() {
    try {
        console.log('Initializing database connection...');
        await AppDataSource.initialize();
        console.log('Database connection initialized');

        const directorRepo = AppDataSource.getRepository(Director);
        const movieRepo = AppDataSource.getRepository(Movie);

        // Generate directors
        console.log('Generating directors...');
        const directors: Director[] = [];
        for (let i = 0; i < NUM_DIRECTORS; i++) {
            const director = directorRepo.create({
                name: faker.person.fullName()
            });
            directors.push(director);
        }
        await directorRepo.save(directors);
        console.log(`Created ${NUM_DIRECTORS} directors`);

        // Generate movies for each director
        console.log('Generating movies...');
        const batchSize = 1000;
        let totalMovies = 0;

        for (const director of directors) {
            const movies: Movie[] = [];
            for (let i = 0; i < MOVIES_PER_DIRECTOR; i++) {
                const movie = movieRepo.create({
                    title: faker.music.songName(),
                    date: faker.date.past().toISOString().split('T')[0],
                    poster: faker.image.url(),
                    director: director
                });
                movies.push(movie);
                totalMovies++;

                // Save in batches to avoid memory issues
                if (movies.length >= batchSize) {
                    await movieRepo.save(movies);
                    console.log(`Saved ${totalMovies} movies so far...`);
                    movies.length = 0;
                }
            }

            // Save any remaining movies
            if (movies.length > 0) {
                await movieRepo.save(movies);
                console.log(`Saved ${totalMovies} movies so far...`);
            }
        }

        console.log('Data generation complete!');
        console.log(`Created ${NUM_DIRECTORS} directors and ${totalMovies} movies`);

    } catch (error) {
        console.error('Error generating data:', error);
    } finally {
        await AppDataSource.destroy();
    }
}

generateData(); 