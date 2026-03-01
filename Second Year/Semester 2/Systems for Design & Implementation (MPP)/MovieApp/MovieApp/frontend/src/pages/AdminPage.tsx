import React, { useState } from 'react';
import {
    Box,
    Container,
    Paper,
    Typography,
    Tabs,
    Tab,
    Alert
} from '@mui/material';
import { useAuth } from '../contexts/AuthContext';
import MovieList from '../components/MovieList';
import DirectorList from '../components/DirectorList';
import UsersList from '../components/UsersList';

interface TabPanelProps {
    children?: React.ReactNode;
    index: number;
    value: number;
}

const TabPanel: React.FC<TabPanelProps> = ({ children, value, index }) => {
    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`admin-tabpanel-${index}`}
        >
            {value === index && (
                <Box sx={{ p: 3 }}>
                    {children}
                </Box>
            )}
        </div>
    );
};

const AdminPage: React.FC = () => {
    const [tabValue, setTabValue] = useState(0);
    const { user } = useAuth();
    const isAdmin = user?.role === 'admin';

    if (!isAdmin) {
        return (
            <Container maxWidth="md" sx={{ mt: 8 }}>
                <Alert severity="error">
                    Access denied. Only administrators can access this page.
                </Alert>
            </Container>
        );
    }

    const handleTabChange = (_: React.SyntheticEvent, newValue: number) => {
        setTabValue(newValue);
    };

    return (
        <Container maxWidth="xl" sx={{ mt: 8 }}>
            <Paper elevation={3} sx={{ p: 2 }}>
                <Typography variant="h4" gutterBottom>
                    Admin Dashboard
                </Typography>
                <Typography variant="subtitle1" sx={{ mb: 3 }}>
                    Welcome, {user?.name}! Manage your application data here.
                </Typography>

                <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                    <Tabs value={tabValue} onChange={handleTabChange} aria-label="admin tabs">
                        <Tab label="Users" />
                        <Tab label="Movies" />
                        <Tab label="Directors" />
                    </Tabs>
                </Box>

                <TabPanel value={tabValue} index={0}>
                    <UsersList />
                </TabPanel>

                <TabPanel value={tabValue} index={1}>
                    <MovieList />
                </TabPanel>

                <TabPanel value={tabValue} index={2}>
                    <DirectorList />
                </TabPanel>
            </Paper>
        </Container>
    );
};

export default AdminPage; 