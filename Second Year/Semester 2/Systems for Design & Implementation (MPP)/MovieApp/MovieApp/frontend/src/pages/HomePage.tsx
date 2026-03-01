import "../index.css"; // Make sure to import your global CSS
import MovieList from "../components/MovieList";
import DirectorList from "../components/DirectorList";
import FileUpload from "../components/FileUpload";
import AutoGenerateToggle from "../components/AutoGenerateToggle";
import { Box, Tabs, Tab } from "@mui/material";
import { useState } from "react";

const HomePage = () => {
  const [activeTab, setActiveTab] = useState(0);

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setActiveTab(newValue);
  };

  return (
    <div>
      {/* 1) HOMEPAGE SECTION */}
      <section className="homepage" id="homepage">
        <div className="homepage-overlay"></div>
        <div className="homepage-text">
          <h1>Welcome to Movie Manager</h1>
          <p>Manage your movie collection easily.</p>
        </div>
      </section>

      {/* 2) MOVIES SECTION */}
      <section className="movies-section" id="movies-section">
        <h2 className="movies-title">Movies</h2>
        <p className="movies-intro"></p>

        {/* Container that aligns image (left) and text (right) */}
        <div className="movies-container">
          {/* Left Column: Image */}
          <div className="movies-image-container">
            <img
              src="/movies.png"
              alt="Movies Collection"
              className="movies-image"
            />
          </div>

          {/* Right Column: Text */}
          <div className="movies-text">
            <h3 className="movies-subtitle">Here you can browse and manage your movie collection.</h3>
            <p>
              Movies, also known as films, are a type of visual communication that
              uses moving pictures and sound to tell stories or educate viewers. They
              come in various genres, including action, drama, comedy, and science
              fiction.
            </p>
            <p>
              Modern cinema is constantly evolving, with cutting-edge technology and
              special effects creating immersive experiences for audiences. From
              Hollywood to independent productions, there's always something new to
              discover.
            </p>
          </div>
        </div>
      </section>

      {/* 3) CONTENT SECTION */}
      <section className="list" id="list">
        <Box sx={{ width: '100%', mb: 4 }}>
          <Tabs value={activeTab} onChange={handleTabChange} centered>
            <Tab label="Movies" />
            <Tab label="Directors" />
          </Tabs>
        </Box>

        {activeTab === 0 ? (
          <>
            <h2>List Of Movies</h2>
            <AutoGenerateToggle />
            <div className="movie-cards">
              <MovieList />
            </div>
          </>
        ) : (
          <>
            <h2>List Of Directors</h2>
            <div className="director-cards">
              <DirectorList />
            </div>
          </>
        )}
      </section>

      <section className="files" id="files">
        <FileUpload />
      </section>
    </div>
  );
};

export default HomePage;
