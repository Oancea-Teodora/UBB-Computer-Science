import React from "react";

const FileDownload = ({ filename }) => {
    // Create the download URL
    const downloadUrl = `http://localhost:3001/files/download/${filename}`;

    return (
        <div>
            <a href={downloadUrl} download={filename}>
                Download {filename}
            </a>
        </div>
    );
};

export default FileDownload;
