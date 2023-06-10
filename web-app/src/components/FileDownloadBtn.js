import styles from './FileDownloadBtn.module.scss';
import downloadFile from '../api/downloadFile';
import {useEffect, useState} from 'react';


export default function FileDownloadBtn({title, icon, fileId}) {
    const [isDisabled, setIsDisabled] = useState(fileId === null);

    useEffect(() => {
        setIsDisabled(fileId === null);
    }, [fileId]);

    return (
        <button
            className={styles.button}
            title={title}
            onClick={() => isDisabled ? null : downloadFile(fileId)}
            disabled={isDisabled}
        >
            <img className={styles.image} src={icon} alt={title} />
        </button>
    );
}