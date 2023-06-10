export default function downloadFile(fileId) {
    return window.open("/api/download-file/" + fileId, "_blank");
}