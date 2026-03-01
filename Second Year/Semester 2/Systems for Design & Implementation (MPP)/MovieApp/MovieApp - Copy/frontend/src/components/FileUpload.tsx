import React, { useState } from "react";

const FileUpload = () => {
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const [uploadResponse, setUploadResponse] = useState<any>(null);
    const [downloadFileName, setDownloadFileName] = useState<string>("");

    // Handle file selection.
    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            setSelectedFile(e.target.files[0]);
        }
    };

    // Handle the file upload.
    const handleUpload = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!selectedFile) {
            alert("Please select a file first!");
            return;
        }
        const formData = new FormData();
        formData.append("file", selectedFile);

        try {
            const res = await fetch("http://localhost:3001/files/upload", {
                method: "POST",
                body: formData,
            });
            if (!res.ok) {
                throw new Error("Upload failed");
            }
            const data = await res.json();
            setUploadResponse(data);
            alert("File uploaded successfully!");
        } catch (err) {
            console.error("Upload error:", err);
            alert("File upload failed!");
        }
    };

    // Handle file download by navigating to the download URL.
    const handleDownload = () => {
        if (!downloadFileName) {
            alert("Please enter a file name to download.");
            return;
        }
        window.location.href = `http://localhost:3001/files/download/${downloadFileName}`;
    };

    return (
        <div style={{ margin: "20px", padding: "10px", border: "1px solid gray" }}>
            <h2>File Upload</h2>
            <form onSubmit={handleUpload}>
                <input type="file" onChange={handleFileChange} />
                <button type="submit">Upload File</button>
            </form>
            {uploadResponse && uploadResponse.file && (
                <div>
                    <p>
                        Uploaded File: <strong>{uploadResponse.file.filename}</strong>
                    </p>
                </div>
            )}

            <div style={{ marginTop: "20px" }}>
                <h3>Download File</h3>
                <input
                    type="text"
                    placeholder="Enter filename"
                    value={downloadFileName}
                    onChange={(e) => setDownloadFileName(e.target.value)}
                />
                <button onClick={handleDownload}>Download</button>
            </div>
        </div>
    );
};

export default FileUpload;
