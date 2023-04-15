import styles from './App.module.scss';
import Header from './components/Header';
import HomePage from './pages/HomePage';

function App() {
  return (
    <div className={styles.app}>
        <Header/>
        <HomePage/>
    </div>
  );
}

export default App;