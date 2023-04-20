import {useState} from 'react';
import styles from './HomePage.module.scss';
import FileInput from '../components/FileInput';
import recognizeText from '../api/recognizeText';


export default function HomePage() {
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
        <div>
            <h1 className={styles.heading}>Handwritten Text Recognizer</h1>

            <p className={styles.description}>
                Using this free service you can recognize a text written by hand.
                The service uses machine learning techniques to convert an image of handwritten text into the digital form.
                This may be useful if you have large amounts of such text that and need to copy it into digital documents.
                This service lets you read the text more easily if you're struggling to recognize the penmanship of your text.
            </p>

            <div className={styles.fileInputContainer}>
                <FileInput onChange={setAddedFile} />
            </div>

            <button className={styles.recognizeBtn} type="button" onClick={onUploadClick} disabled={addedFile === null}>Recognize Text</button>
            <div className={styles.errorMessage}>{errorMessage}</div>

            <p className={styles.recognizedText}>{recognizedText}</p>
        </div>
    )
}