import React, { useState, useEffect } from "react";
import {
    Switch,
    FormControlLabel,
    Box,
    CircularProgress,
    Typography,
} from "@mui/material";

const AutoGenerateToggle: React.FC = () => {
    const [enabled, setEnabled] = useState<boolean>(false);
    const [loading, setLoading] = useState<boolean>(true);

    // Fetch current status
    const fetchStatus = async () => {
        try {
            // const res = await fetch("/control/auto-generate");
            const res = await fetch("http://localhost:3001/control/auto-generate");
            const data = await res.json();
            setEnabled(data.autoGenerate);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    // Toggle endpoint
    const toggle = async () => {
        const newEnable = !enabled;
        try {
            //  await fetch("/control/auto-generate", {
            await fetch("http://localhost:3001/control/auto-generate", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ enable: newEnable }),
            });
            setEnabled(newEnable);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchStatus();
    }, []);

    return (
        <Box display="flex" alignItems="center">
            {loading ? (
                <>
                    <CircularProgress size={20} />
                    <Typography variant="body2" sx={{ ml: 1 }}>
                        Loading…
                    </Typography>
                </>
            ) : (
                <FormControlLabel
                    control={
                        <Switch
                            checked={enabled}
                            onChange={toggle}
                            color="primary"
                        />
                    }
                    label={`Auto-Generate ${enabled ? "ON" : "OFF"}`}
                />
            )}
        </Box>
    );
};

export default AutoGenerateToggle;
