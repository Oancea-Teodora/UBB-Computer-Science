import React, { useEffect, useState } from "react";
import {
    Box,
    Button,
    TextField,
    Grid,
    Card,
    CardContent,
    Typography,
    IconButton,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Pagination,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import { Director } from "../types/director";
import useOnlineStatus from "../hooks/useOnlineStatus";
import { queueOperation, processQueue, OfflineOperation } from "../utils/offlineQueue";

const DirectorList = () => {
    const [directors, setDirectors] = useState<Director[]>([]);
    const [searchQuery, setSearchQuery] = useState<string>("");
    const [selectedDirector, setSelectedDirector] = useState<Director | null>(null);
    const [isEditModalOpen, setEditModalOpen] = useState<boolean>(false);
    const [isAddModalOpen, setAddModalOpen] = useState<boolean>(false);
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [totalPages, setTotalPages] = useState<number>(1);
    const itemsPerPage = 10;

    const isOnline = useOnlineStatus();
    const [serverDown, setServerDown] = useState<boolean>(false);

    const fetchDirectors = async () => {
        try {
            const res = await fetch(
                `/directors?page=${currentPage}&limit=${itemsPerPage}&name=${searchQuery}`
            );
            if (!res.ok) {
                setServerDown(true);
                throw new Error("Server error");
            }
            const data = await res.json();
            setDirectors(data.directors || data);
            setTotalPages(data.totalPages || 1);
            setServerDown(false);
        } catch (err) {
            console.error("Failed to fetch directors:", err);
            setServerDown(true);
        }
    };

    useEffect(() => {
        fetchDirectors();
    }, [currentPage, searchQuery]);

    useEffect(() => {
        if (isOnline && !serverDown) {
            processQueue().then(() => {
                fetchDirectors();
            });
        }
    }, [isOnline, serverDown]);

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSearchQuery(event.target.value);
        setCurrentPage(1);
    };

    const handleOpenAddDirector = () => {
        setSelectedDirector({ name: "" });
        setAddModalOpen(true);
    };

    const handleAddDirector = (newDirector: Director) => {
        if (!isOnline || serverDown) {
            const op: OfflineOperation = { type: "add", payload: newDirector };
            queueOperation(op);
            setDirectors((prev) => [newDirector, ...prev]);
            setAddModalOpen(false);
            return;
        }

        fetch("/directors", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newDirector),
        })
            .then(async (res) => {
                if (!res.ok) {
                    const errorData = await res.json();
                    console.error("Backend validation failed:", errorData);
                    return;
                }
                await res.json();
                fetchDirectors();
                setAddModalOpen(false);
            })
            .catch((err) => console.error("Add error:", err));
    };

    const handleDelete = (id: number) => {
        if (!isOnline || serverDown) {
            const op: OfflineOperation = { type: "delete", payload: { id } };
            queueOperation(op);
            setDirectors((prev) => prev.filter((director) => director.id !== id));
            return;
        }

        fetch(`/directors/${id}`, { method: "DELETE" })
            .then(() => {
                fetchDirectors();
            })
            .catch((err) => console.error("Failed to delete director:", err));
    };

    const handleEdit = (director: Director) => {
        setSelectedDirector(director);
        setEditModalOpen(true);
    };

    const handleUpdateDirector = (updatedDirector: Director) => {
        if (!isOnline || serverDown) {
            const op: OfflineOperation = { type: "update", payload: updatedDirector };
            queueOperation(op);
            setDirectors((prev) =>
                prev.map((d) => (d.id === updatedDirector.id ? updatedDirector : d))
            );
            setEditModalOpen(false);
            return;
        }

        fetch(`/directors/${updatedDirector.id}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedDirector),
        })
            .then((res) => res.json())
            .then(() => {
                fetchDirectors();
                setEditModalOpen(false);
            })
            .catch((err) => console.error("Failed to update director:", err));
    };

    const handlePageChange = (event: React.ChangeEvent<unknown>, page: number) => {
        setCurrentPage(page);
    };

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

    const renderDirectorModal = () => {
        const isOpen = isEditModalOpen || isAddModalOpen;
        const title = isEditModalOpen ? "Edit Director" : "Add Director";
        const director = selectedDirector || { name: "" };
        const onSave = isEditModalOpen ? handleUpdateDirector : handleAddDirector;
        const onClose = () => {
            setEditModalOpen(false);
            setAddModalOpen(false);
        };

        return (
            <Dialog open={isOpen} onClose={onClose}>
                <DialogTitle>{title}</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Name"
                        fullWidth
                        value={director.name}
                        onChange={(e) => setSelectedDirector({ ...director, name: e.target.value })}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose}>Cancel</Button>
                    <Button onClick={() => onSave(director)}>Save</Button>
                </DialogActions>
            </Dialog>
        );
    };

    return (
        <>
            {renderStatusBanner()}

            <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                <Button
                    variant="contained"
                    sx={{ backgroundColor: "black", color: "white", "&:hover": { backgroundColor: "#333" } }}
                    onClick={handleOpenAddDirector}
                >
                    Add Director
                </Button>
                <TextField
                    label="Search by Name"
                    variant="outlined"
                    value={searchQuery}
                    onChange={handleSearchChange}
                />
            </Box>

            <Grid container spacing={2}>
                {directors.length > 0 ? (
                    directors.map((director) => (
                        <Grid item key={director.id}>
                            <Card sx={{ width: 220, borderRadius: 2, boxShadow: 1 }}>
                                <CardContent>
                                    <Typography variant="h6" component="div">
                                        {director.name}
                                    </Typography>
                                    {director.movies && (
                                        <Typography variant="body2" color="text.secondary">
                                            Movies: {director.movies.length}
                                        </Typography>
                                    )}
                                </CardContent>
                                <Box sx={{ display: "flex", justifyContent: "flex-end", p: 1 }}>
                                    <IconButton onClick={() => handleEdit(director)} color="primary">
                                        <EditIcon />
                                    </IconButton>
                                    <IconButton
                                        onClick={() => director.id && handleDelete(director.id)}
                                        color="error"
                                    >
                                        <DeleteIcon />
                                    </IconButton>
                                </Box>
                            </Card>
                        </Grid>
                    ))
                ) : (
                    <Typography>No directors found.</Typography>
                )}
            </Grid>

            <Box sx={{ display: "flex", justifyContent: "center", mt: 2 }}>
                <Pagination
                    count={totalPages}
                    page={currentPage}
                    onChange={handlePageChange}
                    color="primary"
                />
            </Box>

            {renderDirectorModal()}
        </>
    );
};

export default DirectorList; 