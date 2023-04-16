import {useEffect} from 'react';
import styles from './App.module.scss';
import Header from './components/Header';
import HomePage from './pages/HomePage';
import {updateDarkMode} from './assets/helpers/darkModeHelpers';

function App() {
    useEffect(() => {
        updateDarkMode();
    }, []);

    return (
        <div className={styles.app}>
            <Header />
            <HomePage />
        </div>
    );
}

export default App;