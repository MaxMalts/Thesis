export default function recognizeText(file) {
    let formData = new FormData();
    formData.append("file", file);

    return fetch("/api/recognize-text", {
        method: "POST",
        credentials: "include",
        cache: "no-cache",
        body: formData
    }).then(response => {
        if (response.ok && response.headers.get("Content-Type") === "application/json") {
            return response.json();
        } else {
            return Promise.reject(response);
        }
    }).then(json => json["text"]);
}