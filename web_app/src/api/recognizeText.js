export default function recognizeText(file) {
    return fetch("/api/recognizeText", {
        method: "POST",
        credentials: "include",
        cache: "no-cache",
        headers: {
            "Content-Length": file.size.toString(),
            "Content-Type": file.type
        },
        body: file
    }).then(response => {
        if (response.ok && response.headers.get("Content-Type") === "text/plain") {
            return response.json();
        } else {
            return Promise.reject(response);
        }
    }).then(json => json["text"]);
}