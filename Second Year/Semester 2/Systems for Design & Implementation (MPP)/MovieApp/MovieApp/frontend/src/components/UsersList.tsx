import React, { useEffect, useState } from "react";
import {
    Box,
    Button,
    TextField,
    Pagination,
    TableContainer,
    Table,
    TableHead,
    TableBody,
    TableRow,
    TableCell,
    Paper,
    Alert,
    Typography,
    Chip
} from "@mui/material";
import { apiFetch, API_URL } from "../utils/api";
import { useAuth } from "../contexts/AuthContext";

interface User {
    id: number;
    name: string;
    email: string;
    role: string;
}

const UsersList: React.FC = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [searchQuery, setSearchQuery] = useState<string>("");
    const [page, setPage] = useState<number>(1);
    const [totalPages, setTotalPages] = useState<number>(1);
    const [totalItems, setTotalItems] = useState<number>(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const rowsPerPage = 10;

    const { isAuthenticated, user } = useAuth();
    const isAdmin = user?.role === 'admin';

    const fetchUsers = async () => {
        if (!isAuthenticated || !isAdmin) {
            setError("Only administrators can view this page");
            return;
        }

        setLoading(true);
        try {
            const data = await apiFetch(
                `/users?page=${page}&limit=${rowsPerPage}&name=${searchQuery}`
            );

            if (data.unauthorized) {
                setError("You are not authorized to view this data");
                return;
            }

            setUsers(data.users);
            setTotalItems(data.totalItems);
            setTotalPages(data.totalPages);
            setError(null);
        } catch (error: any) {
            console.error("Error fetching users:", error);
            setError(error.message || "Failed to load users");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, [page, searchQuery, isAuthenticated, isAdmin]);

    if (!isAdmin) {
        return (
            <Alert severity="warning" sx={{ mt: 2, mb: 2 }}>
                You don't have permission to access this page
            </Alert>
        );
    }

    return (
        <Box sx={{ p: 3 }}>
            <Typography variant="h5" gutterBottom>
                Users Management
            </Typography>

            <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
                <TextField
                    label="Search users"
                    variant="outlined"
                    size="small"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    sx={{ width: 300 }}
                />
            </Box>

            {error && (
                <Alert severity="error" sx={{ mb: 2 }}>
                    {error}
                </Alert>
            )}

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>ID</TableCell>
                            <TableCell>Name</TableCell>
                            <TableCell>Email</TableCell>
                            <TableCell>Role</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {loading ? (
                            <TableRow>
                                <TableCell colSpan={4} align="center">
                                    Loading...
                                </TableCell>
                            </TableRow>
                        ) : users.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={4} align="center">
                                    No users found
                                </TableCell>
                            </TableRow>
                        ) : (
                            users.map((user) => (
                                <TableRow key={user.id}>
                                    <TableCell>{user.id}</TableCell>
                                    <TableCell>{user.name}</TableCell>
                                    <TableCell>{user.email}</TableCell>
                                    <TableCell>
                                        <Chip
                                            label={user.role}
                                            color={user.role === 'admin' ? "primary" : "default"}
                                            size="small"
                                        />
                                    </TableCell>
                                </TableRow>
                            ))
                        )}
                    </TableBody>
                </Table>
            </TableContainer>

            <Box sx={{ display: "flex", justifyContent: "center", mt: 2 }}>
                <Pagination
                    count={totalPages}
                    page={page}
                    onChange={(_, value) => setPage(value)}
                    color="primary"
                />
            </Box>
        </Box>
    );
};

export default UsersList; 