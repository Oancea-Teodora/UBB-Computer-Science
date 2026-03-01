import React from "react";
import Card from "@mui/material/Card";
import CardMedia from "@mui/material/CardMedia";
import CardContent from "@mui/material/CardContent";
import CardActions from "@mui/material/CardActions";
import Typography from "@mui/material/Typography";
import IconButton from "@mui/material/IconButton";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";

interface MovieCardProps {
    title: string;
    director: string;
    date: string;
    poster: string;
    onEdit: () => void;
    onDelete: () => void;

    isEarliest?: boolean;
    isLatest?: boolean;
}

const MovieCard: React.FC<MovieCardProps> = ({
    title,
    director,
    date,
    poster,
    onEdit,
    onDelete,
    isEarliest,
    isLatest,
}) => {
    return (
        <Card sx={{
            width: 220, borderRadius: 2, boxShadow: 1, overflow: "hidden", border: isEarliest
                ? "2px solid green"
                : isLatest
                    ? "2px solid blue"
                    : "1px solid transparent",
        }}>
            <CardMedia component="img" image={poster} alt={title} sx={{ height: 330 }} />
            <CardContent>
                <Typography variant="subtitle1" component="div" fontWeight="bold">
                    {title}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    {director}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    {date}
                </Typography>
            </CardContent>

            {/* Edit and Delete Icons */}
            <CardActions>
                <IconButton onClick={onEdit} color="primary">
                    <EditIcon />
                </IconButton>
                <IconButton onClick={onDelete} color="error">
                    <DeleteIcon />
                </IconButton>
            </CardActions>
        </Card>
    );
};

export default MovieCard;
