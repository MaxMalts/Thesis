export default function recognizeText(file) {
    if (!(file instanceof File)) {
        throw new Error("Wrong file type");
    }

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
        } else if (response.status === 422) {
            return response.json().then(result => Promise.reject(result));
        } else {
            return Promise.reject({
                'statusCode': response.status,
                'message': response.status === 413 ? 'file to large' : 'service unavailable'
            });
        }
    });
}