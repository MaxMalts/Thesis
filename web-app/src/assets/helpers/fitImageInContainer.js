export default function fitImageInContainer(image, container) {
    let imageRatio = image.naturalWidth / image.naturalHeight;
    let containerStyle = getComputedStyle(container);
    let containerWidth = parseInt(containerStyle.width);
    let containerHeight = parseInt(containerStyle.height);
    let containerRatio = containerWidth / containerHeight;

    if (imageRatio > containerRatio) {
        image.style.width = containerStyle.width;
        image.style.height = (containerWidth / imageRatio) + "px";
    } else {
        image.style.width = (containerHeight * imageRatio) + "px";
        image.style.height = containerStyle.height;
    }
}