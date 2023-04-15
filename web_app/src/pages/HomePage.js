import {useState} from 'react';
import styles from './HomePage.module.scss.css';
import FileInput from '../components/FileInput';
import recognizeText from '../api/recognizeText';


export default function() {
    const [addedFile, setAddedFile] = useState(null);
    const [recognizedText, setRecognizedText] = useState(null)
    const [errorMessage, setErrorMessage] = useState(null)

    const onUploadClick = event => {
        if (!addedFile) {
            return;
        }

        recognizeText(addedFile).then(result => {
            setRecognizedText(result);
        }).catch(reason => {
            setErrorMessage("Unable to upload file to server, please try again.")
        });
    }

    return (
        <div className={styles.homePage}>
            <h1 className={styles.heading}>Handwritten Text Recognizer</h1>

            <div>Choose an image containing your handwritten text:</div>
            <FileInput onChange={setAddedFile}/>
            <button type="button" onClick={onUploadClick} disabled={addedFile === null}>Recognize Text</button>
            <div>{errorMessage}</div>

            <p>{recognizedText}</p>
        </div>
    )
}