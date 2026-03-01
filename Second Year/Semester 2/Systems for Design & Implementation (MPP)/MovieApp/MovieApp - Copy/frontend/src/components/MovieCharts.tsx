// src/components/MovieCharts.tsx
import React from "react";
import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer,
    PieChart,
    Pie,
    Cell,
    Legend,
} from "recharts";
import { Movie } from "../types/movie";

type MovieChartsProps = {
    movies: Movie[];
};

const getDirectorCounts = (movies: Movie[]) => {
    const counts: Record<string, number> = {};
    movies.forEach((movie) => {
        counts[movie.director] = (counts[movie.director] || 0) + 1;
    });
    return Object.entries(counts).map(([director, count]) => ({
        director,
        count,
    }));
};

const getMoviesPerYear = (movies: Movie[]) => {
    const counts: Record<string, number> = {};
    movies.forEach((movie) => {
        const year = new Date(movie.date).getFullYear().toString();
        counts[year] = (counts[year] || 0) + 1;
    });
    return Object.entries(counts)
        .map(([year, count]) => ({ year, count }))
        .sort((a, b) => parseInt(a.year) - parseInt(b.year));
};

const getMoviesPerDecade = (movies: Movie[]) => {
    const counts: Record<string, number> = {};
    movies.forEach((movie) => {
        const year = new Date(movie.date).getFullYear();
        const decade = `${Math.floor(year / 10) * 10}s`;
        counts[decade] = (counts[decade] || 0) + 1;
    });
    return Object.entries(counts).map(([name, value]) => ({ name, value }));
};

const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042", "#8884d8", "#82ca9d", "#d24dff"];

const MovieCharts: React.FC<MovieChartsProps> = ({ movies }) => {
    const directorData = getDirectorCounts(movies);
    const yearData = getMoviesPerYear(movies);
    const decadeData = getMoviesPerDecade(movies);

    return (
        <div style={{ marginTop: 50 }}>


            {/* Chart 2: Movies per Year */}
            <h3 style={{ textAlign: "center", marginTop: 40 }}>📅 Movies Released per Year</h3>
            <div style={{ width: "100%", height: 300 }}>
                <ResponsiveContainer width="100%" height="100%">
                    <BarChart data={yearData}>
                        <XAxis dataKey="year" />
                        <YAxis />
                        <Tooltip />
                        <Bar dataKey="count" fill="#82ca9d" />
                    </BarChart>
                </ResponsiveContainer>
            </div>

            {/* Chart 3: Movies by Decade */}
            <h3 style={{ textAlign: "center", marginTop: 40 }}>📊 Movies by Decade</h3>
            <div style={{ width: "100%", height: 300 }}>
                <ResponsiveContainer width="100%" height="100%">
                    <PieChart>
                        <Pie
                            data={decadeData}
                            dataKey="value"
                            nameKey="name"
                            cx="50%"
                            cy="50%"
                            outerRadius={100}
                            label
                        >
                            {decadeData.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                            ))}
                        </Pie>
                        <Tooltip />
                        <Legend />
                    </PieChart>
                </ResponsiveContainer>
            </div>


            {/* Chart 1: Movies by Director */}
            <h3 style={{ textAlign: "center" }}>🎬 Movies by Director</h3>
            <div style={{ width: "100%", height: 300 }}>
                <ResponsiveContainer width="100%" height="100%">
                    <BarChart data={directorData}>
                        <XAxis dataKey="director" />
                        <YAxis />
                        <Tooltip />
                        <Bar dataKey="count" fill="#1976d2" />
                    </BarChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default MovieCharts;
