import styles from './CommonButton.module.scss';

export default function CommonButton({className, onClick, disabled, children}) {
    return (
        <button className={styles.button + ' ' + className} onClick={onClick} type="button" disabled={disabled}>{children}</button>
    )
}