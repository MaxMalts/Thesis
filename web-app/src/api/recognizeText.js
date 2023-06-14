export default function recognizeText(file, area) {
    if (!(file instanceof File)) {
        throw new Error("Wrong file type");
    }
    if ((!(area.x1 instanceof Number) && !(typeof area.x1 === "number"))
        || (!(area.y1 instanceof Number) && !(typeof area.y1 === "number"))
        || (!(area.x2 instanceof Number) && !(typeof area.x2 === "number"))
        || (!(area.y2 instanceof Number) && !(typeof area.y2 === "number"))) {
        throw new Error("Wrong area object format");
    }

    let formData = new FormData();
    formData.append("file", file);
    formData.append("area", new Blob([JSON.stringify(area)], {type: "application/json"}));

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