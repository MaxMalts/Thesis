export function updateDarkMode(value = null) {
    let isDark = value;
    if (value === null) {
        isDark = isDarkMode();
    }

    if (isDark) {
        localStorage.setItem("darkMode", "true");
        document.documentElement.classList.add("darkMode")
    } else {
        localStorage.setItem("darkMode", "false");
        document.documentElement.classList.remove("darkMode")
    }
}

export function isDarkMode() {
    return localStorage.getItem("darkMode") === "true"
}