import React, { useEffect, useState } from "react";
import Grid from "@mui/material/Grid";
import MovieCard from "./MovieCard";
import EditMovieModal from "./EditMovieModal";
import { Movie } from "../types/movie";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import { Pagination } from "@mui/material";
import MovieCharts from "./MovieCharts";
import useOnlineStatus from "../hooks/useOnlineStatus";
import { queueOperation, processQueue, OfflineOperation } from "../utils/offlineQueue";
import { io } from "socket.io-client";

const MovieList = () => {
    const [movies, setMovies] = useState<Movie[]>([]);
    const [allMovies, setAllMovies] = useState<Movie[]>([]); // State for chart data.
    const [searchQuery, setSearchQuery] = useState<string>("");
    const [selectedMovie, setSelectedMovie] = useState<Movie | null>(null);
    const [isEditModalOpen, setEditModalOpen] = useState<boolean>(false);
    const [isAddModalOpen, setAddModalOpen] = useState<boolean>(false);

    // Pagination state for the movie list.
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [totalPages, setTotalPages] = useState<number>(1);
    const itemsPerPage = 10; // Must match the backend limit

    // Offline and server status.
    const isOnline = useOnlineStatus();
    const [serverDown, setServerDown] = useState<boolean>(false);

    useEffect(() => {
        // Connect to your backend Socket.IO server.
        const socket = io("http://localhost:3001"); // change URL/port if needed

        // Listen for the newMovie event.
        socket.on("newMovie", (newMovie: Movie) => {
            console.log("Received new movie via socket:", newMovie);
            //  setAllMovies((prev) => [newMovie, ...prev])
            // if (currentPage === 1) {
            //  setMovies((prev) => [newMovie, ...prev]);
            // }
        });

        // Cleanup the connection when the component unmounts.
        return () => {
            socket.disconnect();
        };
    }, [currentPage]);


    // Fetch paginated movies.
    const fetchMovies = async () => {
        try {
            const res = await fetch(
                `/movies?page=${currentPage}&limit=${itemsPerPage}&director=${searchQuery}`
            );
            if (!res.ok) {
                setServerDown(true);
                throw new Error("Server error");
            }
            const data = await res.json();
            setMovies(data.movies);
            setTotalPages(data.totalPages);
            setServerDown(false);
        } catch (err) {
            console.error("Failed to fetch movies:", err);
            setServerDown(true);
        }
    };

    // Fetch all movies (for charts) by setting all=true.
    const fetchAllMovies = async () => {
        try {
            const res = await fetch(
                `/movies?all=true&director=${searchQuery}&sortBy=title`
            );
            if (!res.ok) {
                throw new Error("Server error");
            }
            const data = await res.json();
            setAllMovies(data.movies);
        } catch (err) {
            console.error("Failed to fetch all movies:", err);
        }
    };

    // Initial fetch for both paginated movies and full movie list.
    useEffect(() => {
        fetchMovies();
        fetchAllMovies();
    }, [currentPage, searchQuery]);

    // Optionally, add polling to refresh data every few seconds.
    useEffect(() => {
        const intervalId = setInterval(() => {
            if (isOnline) {
                fetchMovies();
                fetchAllMovies();
            }
        }, 5000);
        return () => clearInterval(intervalId);
    }, [isOnline, currentPage, searchQuery]);

    // Process offline operations when back online and server is up.
    useEffect(() => {
        if (isOnline && !serverDown) {
            processQueue().then(() => {
                fetchMovies();
                fetchAllMovies();
            });
        }
    }, [isOnline, serverDown]);

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSearchQuery(event.target.value);
        setCurrentPage(1);
    };

    const handleOpenAddMovie = () => {
        setSelectedMovie({
            title: "",
            director: { id: 0, name: "" },
            date: "",
            poster: "",
        });
        setAddModalOpen(true);
    };

    const handleAddMovie = (newMovie: Movie) => {
        if (!isOnline || serverDown) {
            const op: OfflineOperation = { type: "add", payload: newMovie };
            queueOperation(op);
            setMovies((prev) => [newMovie, ...prev]);
            setAddModalOpen(false);
            return;
        }
        fetch("/movies", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newMovie),
        })
            .then(async (res) => {
                if (!res.ok) {
                    const errorData = await res.json();
                    console.error("Backend validation failed:", errorData);
                    return;
                }
                await res.json();
                fetchMovies();
                fetchAllMovies();
                setAddModalOpen(false);
            })
            .catch((err) => console.error("Add error:", err));
    };

    const handleDelete = (id: number) => {
        if (!isOnline || serverDown) {
            const op: OfflineOperation = { type: "delete", payload: { id } };
            queueOperation(op);
            setMovies((prev) => prev.filter((movie) => movie.id !== id));
            return;
        }
        fetch(`/movies/${id}`, { method: "DELETE" })
            .then(() => {
                fetchMovies();
                fetchAllMovies();
            })
            .catch((err) => console.error("Failed to delete movie:", err));
    };

    const handleEdit = (movie: Movie) => {
        setSelectedMovie(movie);
        setEditModalOpen(true);
    };

    const handleUpdateMovie = (updatedMovie: Movie) => {
        if (!isOnline || serverDown) {
            const op: OfflineOperation = { type: "update", payload: updatedMovie };
            queueOperation(op);
            setMovies((prev) =>
                prev.map((m) => (m.id === updatedMovie.id ? updatedMovie : m))
            );
            setEditModalOpen(false);
            return;
        }
        fetch(`/movies/${updatedMovie.id}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedMovie),
        })
            .then((res) => res.json())
            .then((savedMovie) => {
                fetchMovies();
                fetchAllMovies();
                setEditModalOpen(false);
            })
            .catch((err) => console.error("Failed to update movie:", err));
    };

    const handlePageChange = (event: React.ChangeEvent<unknown>, page: number) => {
        setCurrentPage(page);
    };

    // Render status banners for offline and server issues.
    const renderStatusBanner = () => {
        if (!isOnline) {
            return (
                <Box sx={{ backgroundColor: "red", color: "white", p: 1, textAlign: "center" }}>
                    Your internet connection is down.
                </Box>
            );
        }
        if (isOnline && serverDown) {
            return (
                <Box sx={{ backgroundColor: "orange", color: "black", p: 1, textAlign: "center" }}>
                    Our server is unreachable. You're in offline mode.
                </Box>
            );
        }
        return null;
    };

    return (
        <>
            {renderStatusBanner()}

            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                <Button
                    variant="contained"
                    sx={{ backgroundColor: "black", color: "white", "&:hover": { backgroundColor: "#333" } }}
                    onClick={handleOpenAddMovie}
                >
                    Add Movie
                </Button>
                <TextField
                    label="Search by Director"
                    variant="outlined"
                    value={searchQuery}
                    onChange={handleSearchChange}
                />
            </Box>

            <Grid container spacing={2}>
                {movies.length > 0 ? (
                    movies.map((movie) => {
                        const getYear = (datestring: string): number => {
                            const parsed = new Date(datestring);
                            if (isNaN(parsed.getTime())) {
                                const yearMatch = datestring.match(/\d{4}$/);
                                return yearMatch ? Number(yearMatch[0]) : 0;
                            }
                            return parsed.getFullYear();
                        };

                        const years = movies.map((m) => getYear(m.date));
                        const minYear = Math.min(...years);
                        const maxYear = Math.max(...years);
                        const year = getYear(movie.date);

                        return (
                            <Grid item key={movie.id}>
                                <MovieCard
                                    title={movie.title}
                                    director={movie.director.name}
                                    date={movie.date}
                                    poster={movie.poster}
                                    isEarliest={year === minYear}
                                    isLatest={year === maxYear}
                                    onEdit={() => handleEdit(movie)}
                                    // onDelete={() => handleDelete(movie.id)}
                                    onDelete={() => movie.id !== undefined && handleDelete(movie.id)}
                                />
                            </Grid>
                        );
                    })
                ) : (
                    <p>No movies found.</p>
                )}
            </Grid>

            <Box display="flex" justifyContent="center" mt={2}>
                <Pagination
                    count={totalPages}
                    page={currentPage}
                    onChange={handlePageChange}
                    color="primary"
                />
            </Box>

            {/* Pass allMovies (the complete sorted list) to MovieCharts */}
            <MovieCharts movies={allMovies} />

            {isEditModalOpen && selectedMovie && (
                <EditMovieModal
                    movie={selectedMovie}
                    onSave={handleUpdateMovie}
                    onClose={() => setEditModalOpen(false)}
                />
            )}

            {isAddModalOpen && selectedMovie && (
                <EditMovieModal
                    movie={selectedMovie}
                    onSave={handleAddMovie}
                    onClose={() => setAddModalOpen(false)}
                />
            )}
        </>
    );
};

export default MovieList;
