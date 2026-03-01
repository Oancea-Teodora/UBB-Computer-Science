import React, { useState } from 'react';
import {
    Container,
    Tab,
    Tabs,
    Box,
    Typography,
    Paper,
    Snackbar,
    Alert
} from '@mui/material';
import UsersList from '../components/UsersList';
import MovieList from '../components/MovieList';
import DirectorList from '../components/DirectorList';
import MonitoredUsers from '../components/MonitoredUsers';
import ActivityLogs from '../components/ActivityLogs';
import StatsDashboard from '../components/StatsDashboard';
import SecurityIcon from '@mui/icons-material/Security';
import ListAltIcon from '@mui/icons-material/ListAlt';
import PeopleIcon from '@mui/icons-material/People';
import MovieIcon from '@mui/icons-material/Movie';
import TheaterComedyIcon from '@mui/icons-material/TheaterComedy';
import BarChartIcon from '@mui/icons-material/BarChart';

interface TabPanelProps {
    children?: React.ReactNode;
    index: number;
    value: number;
}

function TabPanel(props: TabPanelProps) {
    const { children, value, index, ...other } = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`admin-tabpanel-${index}`}
            aria-labelledby={`admin-tab-${index}`}
            style={{ width: '100%' }}
            {...other}
        >
            {value === index && <Box sx={{ pt: 3, width: '100%' }}>{children}</Box>}
        </div>
    );
}

const AdminDashboard: React.FC = () => {
    const [tabValue, setTabValue] = useState(0);

    const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
        setTabValue(newValue);
    };

    return (
        <Container maxWidth="lg" sx={{ mt: 4 }}>
            <Paper sx={{ p: 2, mb: 3 }}>
                <Typography variant="h4" gutterBottom>
                    Admin Dashboard
                </Typography>
                <Typography color="textSecondary" paragraph>
                    Manage users, content, and system monitoring from this centralized dashboard.
                </Typography>
            </Paper>

            <Box sx={{ width: '100%', mb: 2 }}>
                <Tabs
                    value={tabValue}
                    onChange={handleTabChange}
                    variant="scrollable"
                    scrollButtons="auto"
                    aria-label="admin dashboard tabs"
                >
                    <Tab icon={<PeopleIcon />} label="Users" />
                    <Tab icon={<MovieIcon />} label="Movies" />
                    <Tab icon={<TheaterComedyIcon />} label="Directors" />
                    <Tab icon={<SecurityIcon />} label="Security Monitoring" />
                    <Tab icon={<ListAltIcon />} label="Activity Logs" />
                    <Tab icon={<BarChartIcon />} label="Statistics" />
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
            <TabPanel value={tabValue} index={3}>
                <MonitoredUsers />
            </TabPanel>
            <TabPanel value={tabValue} index={4}>
                <ActivityLogs />
            </TabPanel>
            <TabPanel value={tabValue} index={5}>
                <StatsDashboard />
            </TabPanel>
        </Container>
    );
};

export default AdminDashboard; 