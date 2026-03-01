import { AppBar, Toolbar, Box, Button } from "@mui/material";

const Navbar = () => {
    return (
        <AppBar
            position="fixed"
            sx={{
                backgroundColor: "rgba(25, 25, 25, 0.9)",
                backdropFilter: "blur(15px)",
                boxShadow: "none",
            }}
        >
            <Toolbar>

                <Box>
                    <Button component="a" href="#homepage" sx={{ color: "#fff" }}>
                        Home
                    </Button>
                    <Button component="a" href="#movies-section" sx={{ color: "#fff" }}>
                        Movies
                    </Button>
                    <Button component="a" href="#list" sx={{ color: "#fff" }}>
                        List
                    </Button>
                </Box>
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;
