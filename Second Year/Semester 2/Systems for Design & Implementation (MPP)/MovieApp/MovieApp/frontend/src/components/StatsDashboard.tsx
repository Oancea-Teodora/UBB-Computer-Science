import React, { useState, useEffect } from 'react';
import {
    Box, Paper, Typography, Grid, Card, CardContent,
    CircularProgress, Alert, Divider, Button
} from '@mui/material';
import {
    BarChart, Bar, PieChart, Pie, Cell, LineChart, Line, Area, AreaChart,
    XAxis, YAxis, Tooltip, Legend, ResponsiveContainer, CartesianGrid
} from 'recharts';
import { apiFetch, API_URL } from '../utils/api';

// Colors for charts
const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884d8', '#82ca9d', '#FF6B6B', '#4ECDC4'];

const StatsDashboard: React.FC = () => {
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [stats, setStats] = useState<any>(null);
    const [cachedResponse, setCachedResponse] = useState(false);
    const [executionTime, setExecutionTime] = useState(0);
    const [directorStats, setDirectorStats] = useState<any[]>([]);
    const [yearlyStats, setYearlyStats] = useState<any[]>([]);
    const [genreStats, setGenreStats] = useState<any[]>([]);
    const [userActivity, setUserActivity] = useState<any[]>([]);

    // New movie-specific state variables
    const [recentMovies, setRecentMovies] = useState<any[]>([]);
    const [topTitleMovies, setTopTitleMovies] = useState<any[]>([]);
    const [titleLengthStats, setTitleLengthStats] = useState<any[]>([]);

    // Fetch all stats data
    const fetchAllStats = async () => {
        setLoading(true);
        setError(null);

        try {
            // Fetch dashboard stats for admins
            const dashboardPromise = apiFetch('/stats/dashboard')
                .then(response => {
                    setStats(response);
                    setCachedResponse(response.fromCache || false);
                    setExecutionTime(response.executionTime || 0);
                    return response;
                })
                .catch(err => {
                    console.error('Dashboard stats error:', err);
                    return null;
                });

            // Fetch directors stats
            const directorsPromise = apiFetch('/stats/directors/movies')
                .then(response => {
                    setDirectorStats(response.result || []);
                    return response;
                })
                .catch(err => {
                    console.error('Directors stats error:', err);
                    return null;
                });

            // Fetch yearly stats
            const yearlyPromise = apiFetch('/stats/movies/yearly')
                .then(response => {
                    setYearlyStats(response.result || []);
                    return response;
                })
                .catch(err => {
                    console.error('Yearly stats error:', err);
                    return null;
                });

            // Fetch genre stats (NEW)
            const genrePromise = apiFetch('/stats/movies/genres')
                .then(response => {
                    setGenreStats(response.result || []);
                    return response;
                })
                .catch(err => {
                    console.error('Genre stats error:', err);
                    return null;
                });

            // Fetch user activity stats (NEW)
            const userPromise = apiFetch('/stats/users/activity')
                .then(response => {
                    setUserActivity(response.result || []);
                    return response;
                })
                .catch(err => {
                    console.error('User activity stats error:', err);
                    return null;
                });

            // Fetch recent movies stats (NEW)
            const recentMoviesPromise = apiFetch('/stats/movies/recent')
                .then(response => {
                    setRecentMovies(response.result || []);
                    return response;
                })
                .catch(err => {
                    console.error('Recent movies stats error:', err);
                    return null;
                });

            // Fetch top title movies stats (NEW)
            const topTitlesPromise = apiFetch('/stats/movies/top-titles')
                .then(response => {
                    setTopTitleMovies(response.result || []);
                    return response;
                })
                .catch(err => {
                    console.error('Top titles stats error:', err);
                    return null;
                });

            // Fetch title length distribution stats (NEW)
            const titleLengthsPromise = apiFetch('/stats/movies/title-lengths')
                .then(response => {
                    setTitleLengthStats(response.result || []);
                    return response;
                })
                .catch(err => {
                    console.error('Title lengths stats error:', err);
                    return null;
                });

            // Wait for all fetches to complete
            await Promise.all([dashboardPromise, directorsPromise, yearlyPromise, genrePromise, userPromise, recentMoviesPromise, topTitlesPromise, titleLengthsPromise]);

        } catch (err: any) {
            setError(err.message || 'Failed to load dashboard statistics');
            console.error('Error loading stats:', err);
        } finally {
            setLoading(false);
        }
    };

    const clearStatsCache = async () => {
        try {
            await apiFetch('/stats/clear-cache', { method: 'POST' });
            // Refetch data after clearing cache
            fetchAllStats();
        } catch (err: any) {
            setError('Failed to clear cache: ' + err.message);
        }
    };

    useEffect(() => {
        fetchAllStats();
    }, []);

    if (loading && !stats && !directorStats.length && !yearlyStats.length && !recentMovies.length && !topTitleMovies.length && !titleLengthStats.length) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
                <Box textAlign="center">
                    <CircularProgress size={60} />
                    <Typography variant="h6" sx={{ mt: 2, color: 'text.secondary' }}>
                        Loading Statistics Dashboard...
                    </Typography>
                </Box>
            </Box>
        );
    }

    if (error) {
        return (
            <Alert severity="error" sx={{ m: 2 }}>
                {error}
            </Alert>
        );
    }

    return (
        <Box sx={{ p: 3, backgroundColor: '#f8f9fa', minHeight: '100vh' }}>
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={4}>
                <Typography variant="h4" fontWeight="bold" color="primary">
                    📊 Analytics Dashboard
                </Typography>
                <Box>
                    <Button
                        variant="outlined"
                        color="primary"
                        onClick={fetchAllStats}
                        sx={{ mr: 1 }}
                    >
                        🔄 Refresh
                    </Button>
                    <Button
                        variant="outlined"
                        color="secondary"
                        onClick={clearStatsCache}
                    >
                        🗑️ Clear Cache
                    </Button>
                </Box>
            </Box>

            {cachedResponse ? (
                <Alert severity="info" sx={{ mb: 3 }}>
                    ⚡ Data loaded from cache (instant retrieval) - Optimization in action!
                </Alert>
            ) : (
                <Alert severity="success" sx={{ mb: 3 }}>
                    🆕 Fresh data loaded - Query execution time: {executionTime}ms
                </Alert>
            )}

            {/* Summary Cards */}
            {stats && (
                <Grid container spacing={3} mb={4}>
                    <Grid item xs={12} md={4}>
                        <Card variant="outlined" sx={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', color: 'white' }}>
                            <CardContent>
                                <Typography color="inherit" gutterBottom variant="h6">
                                    🎬 Total Movies
                                </Typography>
                                <Typography variant="h3" fontWeight="bold">
                                    {stats.counts?.movies || 0}
                                </Typography>
                            </CardContent>
                        </Card>
                    </Grid>
                    <Grid item xs={12} md={4}>
                        <Card variant="outlined" sx={{ background: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)', color: 'white' }}>
                            <CardContent>
                                <Typography color="inherit" gutterBottom variant="h6">
                                    🎭 Total Directors
                                </Typography>
                                <Typography variant="h3" fontWeight="bold">
                                    {stats.counts?.directors || 0}
                                </Typography>
                            </CardContent>
                        </Card>
                    </Grid>
                    <Grid item xs={12} md={4}>
                        <Card variant="outlined" sx={{ background: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)', color: 'white' }}>
                            <CardContent>
                                <Typography color="inherit" gutterBottom variant="h6">
                                    👥 Total Users
                                </Typography>
                                <Typography variant="h3" fontWeight="bold">
                                    {stats.counts?.users || 0}
                                </Typography>
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            )}

            {/* Main Charts Grid */}
            <Grid container spacing={3}>
                {/* Chart 1: Directors Stats with Enhanced Design */}
                {directorStats.length > 0 && (
                    <Grid item xs={12} lg={6}>
                        <Paper sx={{ p: 3, borderRadius: 3, boxShadow: 3 }}>
                            <Typography variant="h6" gutterBottom fontWeight="bold" color="primary">
                                🎭 Top Directors by Movie Count
                            </Typography>
                            <Box height={350}>
                                <ResponsiveContainer width="100%" height="100%">
                                    <BarChart
                                        data={directorStats}
                                        margin={{ top: 20, right: 30, left: 20, bottom: 60 }}
                                    >
                                        <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                                        <XAxis
                                            dataKey="directorName"
                                            angle={-45}
                                            textAnchor="end"
                                            height={80}
                                            fontSize={12}
                                        />
                                        <YAxis />
                                        <Tooltip
                                            contentStyle={{
                                                backgroundColor: '#fff',
                                                border: 'none',
                                                borderRadius: '8px',
                                                boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
                                            }}
                                        />
                                        <Legend />
                                        <Bar
                                            dataKey="movieCount"
                                            fill="url(#colorDirectors)"
                                            name="Number of Movies"
                                            radius={[4, 4, 0, 0]}
                                        />
                                        <defs>
                                            <linearGradient id="colorDirectors" x1="0" y1="0" x2="0" y2="1">
                                                <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8} />
                                                <stop offset="95%" stopColor="#8884d8" stopOpacity={0.6} />
                                            </linearGradient>
                                        </defs>
                                    </BarChart>
                                </ResponsiveContainer>
                            </Box>
                        </Paper>
                    </Grid>
                )}

                {/* Chart 2: Genre Distribution Pie Chart (NEW) */}
                {genreStats.length > 0 && (
                    <Grid item xs={12} lg={6}>
                        <Paper sx={{ p: 3, borderRadius: 3, boxShadow: 3 }}>
                            <Typography variant="h6" gutterBottom fontWeight="bold" color="primary">
                                🎪 Movies by Genre Distribution
                            </Typography>
                            <Box height={350}>
                                <ResponsiveContainer width="100%" height="100%">
                                    <PieChart>
                                        <Pie
                                            data={genreStats}
                                            cx="50%"
                                            cy="50%"
                                            labelLine={false}
                                            label={({ genre, percent }) => `${genre} (${(percent * 100).toFixed(1)}%)`}
                                            outerRadius={100}
                                            fill="#8884d8"
                                            dataKey="count"
                                            nameKey="genre"
                                        >
                                            {genreStats.map((_, index) => (
                                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                            ))}
                                        </Pie>
                                        <Tooltip
                                            contentStyle={{
                                                backgroundColor: '#fff',
                                                border: 'none',
                                                borderRadius: '8px',
                                                boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
                                            }}
                                        />
                                        <Legend />
                                    </PieChart>
                                </ResponsiveContainer>
                            </Box>
                        </Paper>
                    </Grid>
                )}

                {/* Chart 3: Movies by Year Line Chart (Enhanced) */}
                {yearlyStats.length > 0 && (
                    <Grid item xs={12}>
                        <Paper sx={{ p: 3, borderRadius: 3, boxShadow: 3 }}>
                            <Typography variant="h6" gutterBottom fontWeight="bold" color="primary">
                                📅 Movies Released by Year Trend
                            </Typography>
                            <Box height={400}>
                                <ResponsiveContainer width="100%" height="100%">
                                    <AreaChart
                                        data={yearlyStats.sort((a, b) => parseInt(a.year) - parseInt(b.year))}
                                        margin={{ top: 20, right: 30, left: 20, bottom: 20 }}
                                    >
                                        <defs>
                                            <linearGradient id="colorYear" x1="0" y1="0" x2="0" y2="1">
                                                <stop offset="5%" stopColor="#82ca9d" stopOpacity={0.8} />
                                                <stop offset="95%" stopColor="#82ca9d" stopOpacity={0.1} />
                                            </linearGradient>
                                        </defs>
                                        <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                                        <XAxis dataKey="year" />
                                        <YAxis />
                                        <Tooltip
                                            contentStyle={{
                                                backgroundColor: '#fff',
                                                border: 'none',
                                                borderRadius: '8px',
                                                boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
                                            }}
                                        />
                                        <Legend />
                                        <Area
                                            type="monotone"
                                            dataKey="count"
                                            stroke="#82ca9d"
                                            fillOpacity={1}
                                            fill="url(#colorYear)"
                                            strokeWidth={3}
                                            name="Number of Movies"
                                        />
                                    </AreaChart>
                                </ResponsiveContainer>
                            </Box>
                        </Paper>
                    </Grid>
                )}

                {/* Chart 4: User Activity Dashboard (NEW) */}
                {userActivity.length > 0 && (
                    <Grid item xs={12}>
                        <Paper sx={{ p: 3, borderRadius: 3, boxShadow: 3 }}>
                            <Typography variant="h6" gutterBottom fontWeight="bold" color="primary">
                                👥 User Activity Overview (Most Active Users)
                            </Typography>
                            <Box height={400}>
                                <ResponsiveContainer width="100%" height="100%">
                                    <BarChart
                                        data={userActivity}
                                        margin={{ top: 20, right: 30, left: 20, bottom: 80 }}
                                    >
                                        <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                                        <XAxis
                                            dataKey="email"
                                            angle={-45}
                                            textAnchor="end"
                                            height={100}
                                            fontSize={11}
                                        />
                                        <YAxis />
                                        <Tooltip
                                            contentStyle={{
                                                backgroundColor: '#fff',
                                                border: 'none',
                                                borderRadius: '8px',
                                                boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
                                            }}
                                        />
                                        <Legend />
                                        <Bar
                                            dataKey="movieCount"
                                            fill="#FF6B6B"
                                            name="Movies Added"
                                            radius={[2, 2, 0, 0]}
                                        />
                                        <Bar
                                            dataKey="directorCount"
                                            fill="#4ECDC4"
                                            name="Directors Added"
                                            radius={[2, 2, 0, 0]}
                                        />
                                    </BarChart>
                                </ResponsiveContainer>
                            </Box>
                        </Paper>
                    </Grid>
                )}

                {/* Decades Pie Chart (Enhanced) */}
                {stats && stats.moviesByDecade && stats.moviesByDecade.length > 0 && (
                    <Grid item xs={12} lg={6}>
                        <Paper sx={{ p: 3, borderRadius: 3, boxShadow: 3 }}>
                            <Typography variant="h6" gutterBottom fontWeight="bold" color="primary">
                                🕰️ Movies by Decade
                            </Typography>
                            <Box height={350}>
                                <ResponsiveContainer width="100%" height="100%">
                                    <PieChart>
                                        <Pie
                                            data={stats.moviesByDecade}
                                            cx="50%"
                                            cy="50%"
                                            labelLine={true}
                                            label={({ label, percent }) => `${label} (${(percent * 100).toFixed(1)}%)`}
                                            outerRadius={100}
                                            fill="#8884d8"
                                            dataKey="count"
                                            nameKey="label"
                                        >
                                            {stats.moviesByDecade.map((_: any, index: number) => (
                                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                            ))}
                                        </Pie>
                                        <Tooltip
                                            contentStyle={{
                                                backgroundColor: '#fff',
                                                border: 'none',
                                                borderRadius: '8px',
                                                boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
                                            }}
                                        />
                                        <Legend />
                                    </PieChart>
                                </ResponsiveContainer>
                            </Box>
                        </Paper>
                    </Grid>
                )}

                {/* Chart: Recent Movies (NEW) */}
                {recentMovies.length > 0 && (
                    <Grid item xs={12} lg={6}>
                        <Paper sx={{ p: 3, borderRadius: 3, boxShadow: 3 }}>
                            <Typography variant="h6" gutterBottom fontWeight="bold" color="primary">
                                🎬 Recently Added Movies
                            </Typography>
                            <Box height={350}>
                                <ResponsiveContainer width="100%" height="100%">
                                    <BarChart
                                        data={recentMovies}
                                        margin={{ top: 20, right: 30, left: 20, bottom: 80 }}
                                    >
                                        <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                                        <XAxis
                                            dataKey="title"
                                            angle={-45}
                                            textAnchor="end"
                                            height={100}
                                            fontSize={10}
                                        />
                                        <YAxis hide />
                                        <Tooltip
                                            contentStyle={{
                                                backgroundColor: '#fff',
                                                border: 'none',
                                                borderRadius: '8px',
                                                boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
                                            }}
                                            formatter={(value, name, props) => [
                                                `Director: ${props.payload.directorName}`,
                                                `Release Date: ${props.payload.date}`
                                            ]}
                                        />
                                        <Bar
                                            dataKey="id"
                                            fill="#FF6B6B"
                                            name="Movie ID"
                                            radius={[4, 4, 0, 0]}
                                        />
                                    </BarChart>
                                </ResponsiveContainer>
                            </Box>
                        </Paper>
                    </Grid>
                )}

                {/* Chart: Top Movies by Title Length (NEW) */}
                {topTitleMovies.length > 0 && (
                    <Grid item xs={12} lg={6}>
                        <Paper sx={{ p: 3, borderRadius: 3, boxShadow: 3 }}>
                            <Typography variant="h6" gutterBottom fontWeight="bold" color="primary">
                                📏 Movies with Longest Titles
                            </Typography>
                            <Box height={350}>
                                <ResponsiveContainer width="100%" height="100%">
                                    <BarChart
                                        data={topTitleMovies}
                                        margin={{ top: 20, right: 30, left: 20, bottom: 80 }}
                                    >
                                        <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                                        <XAxis
                                            dataKey="title"
                                            angle={-45}
                                            textAnchor="end"
                                            height={100}
                                            fontSize={10}
                                        />
                                        <YAxis />
                                        <Tooltip
                                            contentStyle={{
                                                backgroundColor: '#fff',
                                                border: 'none',
                                                borderRadius: '8px',
                                                boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
                                            }}
                                            formatter={(value, name, props) => [
                                                [`${value} characters`, "Title Length"],
                                                [`Director: ${props.payload.directorName}`, ""],
                                                [`Release: ${props.payload.releaseDate}`, ""]
                                            ]}
                                        />
                                        <Bar
                                            dataKey="titleLength"
                                            fill="#4ECDC4"
                                            name="Title Length"
                                            radius={[4, 4, 0, 0]}
                                        />
                                    </BarChart>
                                </ResponsiveContainer>
                            </Box>
                        </Paper>
                    </Grid>
                )}

                {/* Chart: Title Length Distribution (NEW) */}
                {titleLengthStats.length > 0 && (
                    <Grid item xs={12} lg={6}>
                        <Paper sx={{ p: 3, borderRadius: 3, boxShadow: 3 }}>
                            <Typography variant="h6" gutterBottom fontWeight="bold" color="primary">
                                📊 Movie Title Length Distribution
                            </Typography>
                            <Box height={350}>
                                <ResponsiveContainer width="100%" height="100%">
                                    <PieChart>
                                        <Pie
                                            data={titleLengthStats}
                                            cx="50%"
                                            cy="50%"
                                            labelLine={false}
                                            label={({ category, percent }) => `${category} (${(percent * 100).toFixed(1)}%)`}
                                            outerRadius={100}
                                            fill="#8884d8"
                                            dataKey="count"
                                            nameKey="category"
                                        >
                                            {titleLengthStats.map((_, index) => (
                                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                            ))}
                                        </Pie>
                                        <Tooltip
                                            contentStyle={{
                                                backgroundColor: '#fff',
                                                border: 'none',
                                                borderRadius: '8px',
                                                boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
                                            }}
                                        />
                                        <Legend />
                                    </PieChart>
                                </ResponsiveContainer>
                            </Box>
                        </Paper>
                    </Grid>
                )}
            </Grid>

            {/* Performance Info */}
            <Box mt={4} p={3} bgcolor="linear-gradient(135deg, #667eea 0%, #764ba2 100%)" borderRadius={2} color="white">
                <Typography variant="h6" fontWeight="bold" gutterBottom>
                    ⚡ Performance Optimizations
                </Typography>
                <Typography variant="body1">
                    📈 Database indices, intelligent query caching, and optimized SQL queries ensure lightning-fast data retrieval.
                    Cache hits provide instant responses while fresh queries are optimized for minimal execution time.
                </Typography>
            </Box>
        </Box>
    );
};

export default StatsDashboard; 