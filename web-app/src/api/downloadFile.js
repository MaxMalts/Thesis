export default function downloadFile(fileId) {
    if (!(fileId instanceof String) && !(typeof fileId === "string")) {
        throw new Error("Wrong fileId type");
    }

    return window.open("/api/download-file/" + fileId, "_blank");
}