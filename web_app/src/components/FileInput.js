import {useState} from 'react';
import styles from './FileInput.module.scss';
import DropFileIcon from '../assets/icons/DropFileIcon';
import PlusIcon from '../assets/icons/PlusIcon';

export default function FileInput({onChange}) {
    const [fileName, setFileName] = useState(null);
    const [isDragging, setIsDragging] = useState(false);

    const handleFileChange = event => {
        if (event.target.files) {
            setFileName(event.target.files[0].name);
            onChange(event.target.files[0]);
        }
    };

    return (
        <div className={styles.container}>
            <label className={styles.dropArea}>
                <input
                    className={styles.fileInput} type="file" onChange={handleFileChange}
                    accept="image/*"
                    capture="environment"
                    onDragOver={() => setIsDragging(true)} onDragLeave={() => setIsDragging(false)}
                    onDrop={() => setIsDragging(false)}
                />

                <span>Select or drop your file</span>
                <PlusIcon className={styles.addFileIcon} />
                {fileName && <span className={styles.fileName}>File selected: <b>{fileName}</b></span>}


                {isDragging && (
                    <span className={styles.draggingOverlay}>
                        <DropFileIcon className={styles.dropFileIcon} />
                    </span>
                )}
            </label>
        </div>
    );
}