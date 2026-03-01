import { Movie } from "../entity/Movie";

export interface InMemoryMovie {
    id: number;
    title: string;
    director: string;
    date: string;
    poster: string;
}

let movies: InMemoryMovie[] = [
    {
        id: 1,
        title: "Interstellar",
        director: "Christopher Nolan",
        date: "7 November 2014",
        poster: "/interstellar.jpg",
    },
    {
        id: 2,
        title: "Inception",
        director: "Christopher Nolan",
        date: "16 July 2010",
        poster: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQTP8n4Rf1EhjkrONXTXzgJVmIfFdVmxzaR0w&s",
    },
    {
        id: 3,
        title: "The Dark Knight",
        director: "Christopher Nolan",
        date: "18 July 2008",
        poster: "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_FMjpg_UX1000_.jpg",
    },
    {
        id: 4,
        title: "The Matrix",
        director: "Lana & Lilly Wachowski",
        date: "31 March 1999",
        poster: "https://upload.wikimedia.org/wikipedia/en/c/c1/The_Matrix_Poster.jpg",
    },
    {
        id: 5,
        title: "Avengers: Endgame",
        director: "Anthony & Joe Russo",
        date: "26 April 2019",
        poster: "https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg",
    },
    {
        id: 6,
        title: "Parasite",
        director: "Bong Joon-ho",
        date: "30 May 2019",
        poster: "https://upload.wikimedia.org/wikipedia/en/5/53/Parasite_%282019_film%29.png",
    },
    {
        id: 7,
        title: "Joker",
        director: "Todd Phillips",
        date: "4 October 2019",
        poster: "https://upload.wikimedia.org/wikipedia/en/e/e1/Joker_%282019_film%29_poster.jpg",
    },
    {
        id: 8,
        title: "Fight Club",
        director: "David Fincher",
        date: "15 October 1999",
        poster: "https://upload.wikimedia.org/wikipedia/en/f/fc/Fight_Club_poster.jpg",
    },
    {
        id: 9,
        title: "Pulp Fiction",
        director: "Quentin Tarantino",
        date: "14 October 1994",
        poster: "https://upload.wikimedia.org/wikipedia/en/3/3b/Pulp_Fiction_%281994%29_poster.jpg",
    },
    {
        id: 10,
        title: "Gladiator",
        director: "Ridley Scott",
        date: "5 May 2000",
        poster: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSH-rAnpCrP0jqKWBWf12oa1_P2lADNinE3Ug&s",
    },
    {
        id: 11,
        title: "The Social Network",
        director: "David Fincher",
        date: "1 October 2010",
        poster: "https://upload.wikimedia.org/wikipedia/en/8/8c/The_Social_Network_film_poster.png",
    },
    {
        id: 12,
        title: "The Prestige",
        director: "Christopher Nolan",
        date: "20 October 2006",
        poster: "https://upload.wikimedia.org/wikipedia/en/d/d2/Prestige_poster.jpg",
    },
    {
        id: 13,
        title: "Memento",
        director: "Christopher Nolan",
        date: "11 October 2000",
        poster: "https://upload.wikimedia.org/wikipedia/en/c/c7/Memento_poster.jpg",
    },
    {
        id: 14,
        title: "The Green Mile",
        director: "Frank Darabont",
        date: "10 December 1999",
        poster: "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcRR51ttp6RM2rMzTH-ZQGkAfZsmSfPvDqza2uEuJDirIR1LfLOothPLa3GCD8d4JUq8ifJ2ciT1ZELt9DEOEM6_Ra49Jay9dagJwK1QXA",
    },
    {
        id: 15,
        title: "Titanic",
        director: "James Cameron",
        date: "19 December 1997",
        poster: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS9VdVd80d_GJa8nMpaW8IerDJ6_x_7gJaR7G_nELrx4GnfFt9U4pLpDIDINZxzd_SV30U&usqp=CAU",
    },
    {
        id: 16,
        title: "The Wolf of Wall Street",
        director: "Martin Scorsese",
        date: "25 December 2013",
        poster: "https://m.media-amazon.com/images/M/MV5BMjIxMjgxNTk0MF5BMl5BanBnXkFtZTgwNjIyOTg2MDE@._V1_FMjpg_UX1000_.jpg",
    },
    {
        id: 17,
        title: "Jurassic Park",
        director: "Steven Spielberg",
        date: "11 June 1993",
        poster: "https://upload.wikimedia.org/wikipedia/en/e/e7/Jurassic_Park_poster.jpg",
    },
    {
        id: 18,
        title: "Braveheart",
        director: "Mel Gibson",
        date: "24 May 1995",
        poster: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTza6xK8dx7XdbF9kWmFt--qURNE4rSeKmxSg&s",
    },
    {
        id: 19,
        title: "Saving Private Ryan",
        director: "Steven Spielberg",
        date: "24 July 1998",
        poster: "https://upload.wikimedia.org/wikipedia/en/a/ac/Saving_Private_Ryan_poster.jpg",
    },
    {
        id: 20,
        title: "Forrest Gump",
        director: "Robert Zemeckis",
        date: "6 July 1994",
        poster: "https://upload.wikimedia.org/wikipedia/en/6/67/Forrest_Gump_poster.jpg",
    },

];

export function getMovies(): InMemoryMovie[] {
    return movies;
}

export function setMovies(newMovies: InMemoryMovie[]): void {
    movies = newMovies;
}