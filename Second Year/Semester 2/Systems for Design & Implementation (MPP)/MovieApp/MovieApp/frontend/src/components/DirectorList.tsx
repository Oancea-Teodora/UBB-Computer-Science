import React, { useEffect, useState, useRef, useCallback } from "react";
import {
    Box,
    Button,
    TextField,
    IconButton,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TableContainer,
    Table,
    TableHead,
    TableBody,
    TableRow,
    TableCell,
    Paper,
    Alert,
    CircularProgress,
    Typography,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import { Director } from "../types/director";
import useOnlineStatus from "../hooks/useOnlineStatus";
import { queueOperation, processQueue, OfflineOperation } from "../utils/offlineQueue";
import { useAuth } from "../contexts/AuthContext";
import { apiFetch, API_URL } from "../utils/api";

const DirectorList = () => {
    const [directors, setDirectors] = useState<Director[]>([]);
    const [searchQuery, setSearchQuery] = useState<string>("");
    const [selectedDirector, setSelectedDirector] = useState<Director | null>(null);
    const [modalOpen, setModalOpen] = useState<boolean>(false);

    // Infinite scrolling states
    const [loading, setLoading] = useState<boolean>(false);
    const [hasMore, setHasMore] = useState<boolean>(true);
    const [page, setPage] = useState<number>(1);
    const [totalItems, setTotalItems] = useState<number>(0);
    const itemsPerPage = 20; // Increased batch size for better UX

    // Intersection observer ref
    const observerRef = useRef<IntersectionObserver | null>(null);
    const loadingTriggerRef = useRef<HTMLDivElement | null>(null);

    const isOnline = useOnlineStatus();
    const [serverDown, setServerDown] = useState<boolean>(false);
    const { isAuthenticated } = useAuth();
    const [authError, setAuthError] = useState<boolean>(false);

    const fetchDirectors = useCallback(async (pageNum: number = 1, reset: boolean = false) => {
        if (!isAuthenticated) {
            setAuthError(true);
            return;
        }

        if (loading && !reset) return; // Prevent duplicate requests

        setLoading(true);

        try {
            const data = await apiFetch(
                `/directors?page=${pageNum}&limit=${itemsPerPage}&name=${searchQuery}&sortBy=name&order=ASC&countOnly=true`
            );

            if (data.unauthorized) {
                setAuthError(true);
                return;
            }

            const newDirectors = data.directors || [];
            const total = data.totalItems || 0;

            if (reset || pageNum === 1) {
                // Reset the list (new search or initial load)
                setDirectors(newDirectors);
                setPage(1);
            } else {
                // Append to existing list (infinite scroll)
                setDirectors(prev => [...prev, ...newDirectors]);
            }

            setTotalItems(total);
            setHasMore(newDirectors.length === itemsPerPage && (reset ? newDirectors.length : directors.length + newDirectors.length) < total);
            setServerDown(false);
        } catch (error) {
            console.error("Error fetching directors:", error);
            if (reset || pageNum === 1) {
                setDirectors([]);
            }
            setServerDown(true);
        } finally {
            setLoading(false);
        }
    }, [isAuthenticated, searchQuery, itemsPerPage, loading, directors.length]);

    // Initial load and search changes
    useEffect(() => {
        if (isAuthenticated) {
            setPage(1);
            fetchDirectors(1, true);
        }
    }, [searchQuery, isAuthenticated]);

    // Intersection observer for infinite scrolling
    useEffect(() => {
        if (!hasMore || loading) return;

        if (observerRef.current) {
            observerRef.current.disconnect();
        }

        observerRef.current = new IntersectionObserver(
            (entries) => {
                const target = entries[0];
                if (target.isIntersecting && hasMore && !loading) {
                    const nextPage = page + 1;
                    setPage(nextPage);
                    fetchDirectors(nextPage, false);
                }
            },
            {
                threshold: 0.1,
                rootMargin: '50px',
            }
        );

        if (loadingTriggerRef.current) {
            observerRef.current.observe(loadingTriggerRef.current);
        }

        return () => {
            if (observerRef.current) {
                observerRef.current.disconnect();
            }
        };
    }, [hasMore, loading, page, fetchDirectors]);

    useEffect(() => {
        if (isOnline && !serverDown && isAuthenticated) {
            processQueue().then(() => {
                fetchDirectors(1, true);
            });
        }
    }, [isOnline, serverDown, isAuthenticated]);

    const handleSave = async () => {
        if (!selectedDirector) return;

        if (!isOnline || serverDown) {
            const op: OfflineOperation = {
                type: selectedDirector.id ? "update" : "add",
                payload: selectedDirector
            };
            queueOperation(op);
            setDirectors(prev => {
                if (selectedDirector.id) {
                    return prev.map(d => d.id === selectedDirector.id ? selectedDirector : d);
                }
                return [selectedDirector, ...prev];
            });
            setModalOpen(false);
            return;
        }

        try {
            const url = selectedDirector.id
                ? `/directors/${selectedDirector.id}`
                : `/directors`;
            const method = selectedDirector.id ? "PATCH" : "POST";

            const data = await apiFetch(url, {
                method,
                body: JSON.stringify(selectedDirector)
            });

            if (data.unauthorized) {
                setAuthError(true);
                return;
            }

            // Refresh the list from the beginning
            await fetchDirectors(1, true);
            setModalOpen(false);
        } catch (error) {
            console.error("Failed to save director:", error);
        }
    };

    const handleDelete = async (id: number) => {
        if (!isOnline || serverDown) {
            const op: OfflineOperation = { type: "delete", payload: { id } };
            queueOperation(op);
            setDirectors(prev => prev.filter(d => d.id !== id));
            return;
        }

        try {
            const data = await apiFetch(`/directors/${id}`, {
                method: "DELETE"
            });

            if (data && data.unauthorized) {
                setAuthError(true);
                return;
            }

            // Refresh the list from the beginning
            await fetchDirectors(1, true);
        } catch (error) {
            console.error("Failed to delete director:", error);
        }
    };

    const StatusBanner = () => {
        if (!isOnline) {
            return (
                <Box sx={{ backgroundColor: "red", color: "white", p: 1, textAlign: "center", mb: 2 }}>
                    Your internet connection is down.
                </Box>
            );
        }
        if (isOnline && serverDown) {
            return (
                <Box sx={{ backgroundColor: "orange", color: "black", p: 1, textAlign: "center", mb: 2 }}>
                    Our server is unreachable. You're in offline mode.
                </Box>
            );
        }
        return null;
    };

    if (authError) {
        return (
            <Alert severity="info" sx={{ mt: 2, mb: 2 }}>
                Please log in to view and manage directors
            </Alert>
        );
    }

    return (
        <Box sx={{ p: 3 }}>
            <StatusBanner />
            <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
                <TextField
                    label="Search directors"
                    variant="outlined"
                    size="small"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    sx={{ width: 300 }}
                />
                <Button
                    variant="contained"
                    color="primary"
                    onClick={() => {
                        setSelectedDirector({ name: "" });
                        setModalOpen(true);
                    }}
                >
                    Add Director
                </Button>
            </Box>

            {/* Progress indicator */}
            <Box sx={{ mb: 2, textAlign: 'center' }}>
                <Typography variant="body2" color="text.secondary">
                    Showing {directors.length} of {totalItems} directors
                    {searchQuery && ` (filtered by "${searchQuery}")`}
                </Typography>
            </Box>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>
                                Name
                                <span style={{ marginLeft: '4px', color: '#666' }}>↑</span>
                            </TableCell>
                            <TableCell>Movies</TableCell>
                            <TableCell align="right">Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {directors.map((director) => (
                            <TableRow key={director.id}>
                                <TableCell>{director.name}</TableCell>
                                <TableCell>
                                    {director.movieCount || director.movies?.length || 0} movies
                                </TableCell>
                                <TableCell align="right">
                                    <IconButton
                                        onClick={() => {
                                            setSelectedDirector(director);
                                            setModalOpen(true);
                                        }}
                                    >
                                        <EditIcon />
                                    </IconButton>
                                    <IconButton
                                        onClick={() => handleDelete(director.id!)}
                                        disabled={!!(director.movieCount || director.movies?.length)}
                                    >
                                        <DeleteIcon />
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}

                        {/* Loading indicator row */}
                        {loading && (
                            <TableRow>
                                <TableCell colSpan={3} sx={{ textAlign: 'center', py: 2 }}>
                                    <CircularProgress size={24} sx={{ mr: 1 }} />
                                    <Typography variant="body2" component="span" color="text.secondary">
                                        Loading more directors...
                                    </Typography>
                                </TableCell>
                            </TableRow>
                        )}

                        {/* No more data indicator */}
                        {!loading && !hasMore && directors.length > 0 && (
                            <TableRow>
                                <TableCell colSpan={3} sx={{ textAlign: 'center', py: 2 }}>
                                    <Typography variant="body2" color="text.secondary">
                                        All directors loaded ({directors.length} total)
                                    </Typography>
                                </TableCell>
                            </TableRow>
                        )}

                        {/* Empty state */}
                        {!loading && directors.length === 0 && (
                            <TableRow>
                                <TableCell colSpan={3} sx={{ textAlign: 'center', py: 4 }}>
                                    <Typography variant="body1" color="text.secondary">
                                        {searchQuery ? `No directors found matching "${searchQuery}"` : 'No directors found'}
                                    </Typography>
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </TableContainer>

            {/* Invisible trigger element for intersection observer */}
            {hasMore && !loading && (
                <div
                    ref={loadingTriggerRef}
                    style={{
                        height: '20px',
                        margin: '10px 0',
                        visibility: 'hidden'
                    }}
                />
            )}

            <Dialog open={modalOpen} onClose={() => setModalOpen(false)}>
                <DialogTitle>
                    {selectedDirector?.id ? "Edit Director" : "Add Director"}
                </DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Name"
                        fullWidth
                        value={selectedDirector?.name || ""}
                        onChange={(e) =>
                            setSelectedDirector({
                                ...selectedDirector!,
                                name: e.target.value,
                            })
                        }
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setModalOpen(false)}>Cancel</Button>
                    <Button onClick={handleSave} color="primary">
                        Save
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default DirectorList; 