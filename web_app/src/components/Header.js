import MoonIcon from '../assets/icons/MoonIcon.js';
import styles from './Header.module.scss';
import {isDarkMode, updateDarkMode} from '../assets/helpers/darkModeHelpers';


export default function Header() {
    const onThemeSwitcherClick = () => {
        updateDarkMode(!isDarkMode());
    }

    return (
        <header className={styles.header}>
            <div className={styles.logo}>
                <a className={styles.logoLink} href='/'>QuickHCR</a>
            </div>

            <button className={styles.themeSwitcher} title="Dark theme" onClick={onThemeSwitcherClick}>
                <MoonIcon className={styles.themeSwitcherIcon} />
            </button>
        </header>
    );
}