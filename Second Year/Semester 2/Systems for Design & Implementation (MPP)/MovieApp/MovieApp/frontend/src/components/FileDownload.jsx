import React from "react";
import { API_URL } from "../utils/api";

const FileDownload = ({ filename }) => {
    // Create the download URL
    const downloadUrl = `${API_URL}/files/download/${filename}`;

    return (
        <div>
            <a href={downloadUrl} download={filename}>
                Download {filename}
            </a>
        </div>
    );
};

export default FileDownload;
