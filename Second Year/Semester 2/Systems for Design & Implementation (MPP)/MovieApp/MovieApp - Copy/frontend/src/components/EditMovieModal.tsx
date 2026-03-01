import React, { useState, useEffect } from "react";
import { Modal, Box, TextField, Button, Typography, MenuItem } from "@mui/material";
import { Movie } from "../types/movie";
import { Director } from "../types/director";

interface EditMovieModalProps {
    movie: Movie;
    onSave: (movie: Movie) => void;
    onClose: () => void;
}

const EditMovieModal: React.FC<EditMovieModalProps> = ({ movie, onSave, onClose }) => {
    const [editedMovie, setEditedMovie] = useState<Movie>(movie);
    const [directors, setDirectors] = useState<Director[]>([]);

    useEffect(() => {
        // Fetch directors when the modal opens
        fetch("/directors")
            .then((res) => res.json())
            .then((data) => setDirectors(data))
            .catch((err) => console.error("Failed to fetch directors:", err));
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
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
            }
        } else {
            setEditedMovie({ ...editedMovie, [name]: value });
        }
    };

    return (
        <Modal open={true} onClose={onClose}>
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
                <TextField
                    label="Title"
                    name="title"
                    fullWidth
                    margin="normal"
                    value={editedMovie.title}
                    onChange={handleChange}
                />
                <TextField
                    select
                    label="Director"
                    name="directorId"
                    fullWidth
                    margin="normal"
                    value={editedMovie.director?.id || ""}
                    onChange={handleChange}
                >
                    {directors.map((director) => (
                        <MenuItem key={director.id} value={director.id}>
                            {director.name}
                        </MenuItem>
                    ))}
                </TextField>
                <TextField
                    label="Date"
                    name="date"
                    fullWidth
                    margin="normal"
                    value={editedMovie.date}
                    onChange={handleChange}
                />
                <TextField
                    label="Poster URL"
                    name="poster"
                    fullWidth
                    margin="normal"
                    value={editedMovie.poster}
                    onChange={handleChange}
                />
                <Box mt={2} sx={{ display: "flex", justifyContent: "space-between" }}>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={() => onSave(editedMovie)}
                    >
                        Save
                    </Button>
                    <Button variant="outlined" color="error" onClick={onClose}>
                        Cancel
                    </Button>
                </Box>
            </Box>
        </Modal>
    );
};

export default EditMovieModal;
