import styles from './LoadingCircle.module.scss'

export default function LoadingCircle({className}) {
    return (
        <span className={className}>
            <svg className={styles.loadingAnim} version="1.1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 90 90" fill="currentColor">
                <circle cx="45" cy="6.65217" r="6.65217" />
                <circle cx="64.1739" cy="11.7391" r="6.65217" />
                <circle cx="78.2608" cy="25.8259" r="6.65217" />
                <circle cx="83.3479" cy="45" r="6.65217" />
                <circle opacity="0.9" cx="78.2608" cy="64.1737" r="6.65217" />
                <circle opacity="0.8" cx="64.1739" cy="78.2608" r="6.65217" />
                <circle opacity="0.7" cx="45" cy="83.3477" r="6.65217" />
                <circle opacity="0.6" cx="25.826" cy="78.2608" r="6.65217" />
                <circle opacity="0.5" cx="11.7391" cy="64.1737" r="6.65217" />
                <circle opacity="0.4" cx="6.65217" cy="45" r="6.65217" />
                <circle opacity="0.3" cx="11.7391" cy="25.8259" r="6.65217" />
                <circle opacity="0.2" cx="25.826" cy="11.7391" r="6.65217" />
            </svg>
        </span>
    )
}