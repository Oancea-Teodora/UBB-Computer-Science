import React, { useState, useEffect } from "react";
import { Modal, Box, TextField, Button, Typography, MenuItem, CircularProgress, Alert } from "@mui/material";
import { Movie } from "../types/movie";
import { Director } from "../types/director";
import { useAuth } from "../contexts/AuthContext";
import { apiFetch } from "../utils/api";

interface EditMovieModalProps {
    movie: Movie;
    onSave: (movie: Movie) => void;
    onClose: () => void;
    cachedDirectors?: Director[];
}

const EditMovieModal: React.FC<EditMovieModalProps> = ({ movie, onSave, onClose, cachedDirectors = [] }) => {
    const [editedMovie, setEditedMovie] = useState<Movie>(movie);
    const [directors, setDirectors] = useState<Director[]>(cachedDirectors);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [errors, setErrors] = useState<{ [key: string]: string }>({});
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);
    const { user } = useAuth();

    useEffect(() => {
        // Only fetch directors if we don't have cached ones
        if (cachedDirectors.length > 0) {
            setDirectors(cachedDirectors);
            return;
        }

        // If no cached directors, fetch from server
        const fetchDirectors = async () => {
            setIsLoading(true);
            try {
                // The server already filters by user unless admin
                const data = await apiFetch(
                    `/directors?limit=100&noMovies=true&countOnly=true`
                );

                if (data.directors) {
                    setDirectors(data.directors);
                } else {
                    console.error("No directors array in response:", data);
                    setDirectors([]);
                }
            } catch (err) {
                console.error("Failed to fetch directors:", err);
                setDirectors([]);
            } finally {
                setIsLoading(false);
            }
        };

        fetchDirectors();
    }, [cachedDirectors, user]);

    const validateForm = (): boolean => {
        const newErrors: { [key: string]: string } = {};

        if (!editedMovie.title?.trim()) {
            newErrors.title = "Title is required";
        }

        if (!editedMovie.director?.id || editedMovie.director.id === 0) {
            newErrors.director = "Please select a director";
        }

        if (!editedMovie.date?.trim()) {
            newErrors.date = "Date is required";
        }

        if (!editedMovie.poster?.trim()) {
            newErrors.poster = "Poster URL is required";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;

        // Clear error for this field when user starts typing
        if (errors[name]) {
            setErrors(prev => ({ ...prev, [name]: '' }));
        }

        if (name === "directorId") {
            const selectedDirector = directors.find((d) => d.id === Number(value));
            if (selectedDirector) {
                setEditedMovie({
                    ...editedMovie,
                    director: {
                        id: selectedDirector.id!,
                        name: selectedDirector.name
                    },
                });
                // Clear director error
                if (errors.director) {
                    setErrors(prev => ({ ...prev, director: '' }));
                }
            }
        } else {
            setEditedMovie({ ...editedMovie, [name]: value });
        }
    };

    const handleSubmit = () => {
        if (!validateForm()) {
            return;
        }

        setIsSubmitting(true);
        onSave(editedMovie);
        // Note: setIsSubmitting(false) will be handled by the parent component closing the modal
    };

    const handleClose = () => {
        if (isSubmitting) {
            return; // Prevent closing while submitting
        }
        onClose();
    };

    return (
        <Modal open={true} onClose={handleClose}>
            <Box sx={{
                position: "absolute",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)",
                width: 400,
                bgcolor: "background.paper",
                boxShadow: 24,
                p: 4,
                borderRadius: 2
            }}>
                <Typography variant="h6" gutterBottom>
                    {movie.id ? "Edit Movie" : "Add Movie"}
                </Typography>

                {directors.length === 0 && !isLoading && (
                    <Alert severity="warning" sx={{ mb: 2 }}>
                        No directors available. Please create a director first.
                    </Alert>
                )}

                <TextField
                    label="Title"
                    name="title"
                    fullWidth
                    margin="normal"
                    value={editedMovie.title}
                    onChange={handleChange}
                    error={!!errors.title}
                    helperText={errors.title}
                    disabled={isSubmitting}
                />

                <TextField
                    select
                    label="Director"
                    name="directorId"
                    fullWidth
                    margin="normal"
                    value={editedMovie.director?.id || ""}
                    onChange={handleChange}
                    disabled={isLoading || isSubmitting}
                    error={!!errors.director}
                    helperText={errors.director}
                >
                    {isLoading ? (
                        <MenuItem disabled>
                            <CircularProgress size={20} sx={{ mr: 1 }} />
                            Loading directors...
                        </MenuItem>
                    ) : directors.length === 0 ? (
                        <MenuItem disabled>No directors available</MenuItem>
                    ) : (
                        directors.map((director) => (
                            <MenuItem key={director.id} value={director.id}>
                                {director.name}
                            </MenuItem>
                        ))
                    )}
                </TextField>

                <TextField
                    label="Date"
                    name="date"
                    fullWidth
                    margin="normal"
                    value={editedMovie.date}
                    onChange={handleChange}
                    error={!!errors.date}
                    helperText={errors.date}
                    disabled={isSubmitting}
                    placeholder="e.g., 15 July 2023 or 2023-07-15"
                />

                <TextField
                    label="Poster URL"
                    name="poster"
                    fullWidth
                    margin="normal"
                    value={editedMovie.poster}
                    onChange={handleChange}
                    error={!!errors.poster}
                    helperText={errors.poster}
                    disabled={isSubmitting}
                    placeholder="https://example.com/poster.jpg"
                />

                <Box mt={2} sx={{ display: "flex", justifyContent: "space-between" }}>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleSubmit}
                        disabled={isSubmitting || isLoading || directors.length === 0}
                    >
                        {isSubmitting ? (
                            <>
                                <CircularProgress size={20} sx={{ mr: 1 }} />
                                Saving...
                            </>
                        ) : (
                            "Save"
                        )}
                    </Button>
                    <Button
                        variant="outlined"
                        color="error"
                        onClick={handleClose}
                        disabled={isSubmitting}
                    >
                        Cancel
                    </Button>
                </Box>
            </Box>
        </Modal>
    );
};

export default EditMovieModal;
