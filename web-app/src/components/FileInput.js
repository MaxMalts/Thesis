import {useState} from 'react';
import styles from './FileInput.module.scss';
import DropFileIcon from '../assets/icons/DropFileIcon';
import PlusIcon from '../assets/icons/PlusIcon';
import ImageCropPopup from './ImageCropPopup';

export default function FileInput({onFileAdded}) {
    const [isDragging, setIsDragging] = useState(false);

    const [fileName, setFileName] = useState(null);
    const [file, setFile] = useState(null);

    const [imageSource, setImageSource] = useState(null);
    const [imageArea, setImageArea] = useState(null);
    const [showCropPopup, setShowCropPopup] = useState(false);


    const onFileChange = event => {
        if (event.target.files) {
            setFileName(event.target.files[0].name);
            setFile(event.target.files[0]);
            setImageSource(URL.createObjectURL(event.target.files[0]));
            setImageArea(null);
            setShowCropPopup(true);
        }
    };

    const onCrop = event => {
        event.preventDefault();
        setShowCropPopup(true);
        return false;
    }

    const onCropConfirm = area => {
        setImageArea(area);
        setShowCropPopup(false);
        onFileAdded(file, area);
    };

    const onCropCancel = () => {
        setFileName(null);
        setFile(null);
        setImageSource(null);
        setImageArea(null);
        setShowCropPopup(false);
    };

    return (
        <div className={styles.container}>
            <label className={styles.dropArea}>
                <input
                    className={styles.fileInput} type="file" onChange={onFileChange}
                    accept="image/*"
                    capture="environment"
                    onDragOver={() => setIsDragging(true)} onDragLeave={() => setIsDragging(false)}
                    onDrop={() => setIsDragging(false)}
                />

                <span>Select or drop your file</span>
                <PlusIcon className={styles.addFileIcon} />
                {fileName &&
                    <span className={styles.fileName}>
                        File selected: <b>{fileName}</b>
                        <button
                            className={styles.cropBtn}
                            onClick={onCrop}
                        >Crop</button>
                    </span>
                }

                {isDragging && (
                    <span className={styles.draggingOverlay}>
                        <DropFileIcon className={styles.dropFileIcon} />
                    </span>
                )}
            </label>

            {showCropPopup &&
                <ImageCropPopup
                    imgSrc={imageSource}
                    initialArea={imageArea}
                    onConfirm={onCropConfirm}
                    onCancel={onCropCancel}
                />
            }
        </div>
    );
}