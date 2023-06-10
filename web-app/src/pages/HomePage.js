import {useState} from 'react';
import styles from './HomePage.module.scss';
import FileInput from '../components/FileInput';
import recognizeText from '../api/recognizeText';
import LoadingCircle from '../assets/icons/LoadingCircle';
import pdfButton from '../assets/icons/pdfButton.png';
import docxButton from '../assets/icons/docxButton.png';
import txtButton from '../assets/icons/txtButton.png';
import FileDownloadBtn from '../components/FileDownloadBtn';


export default function HomePage() {
    const [addedFile, setAddedFile] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [recognizedText, setRecognizedText] = useState(null);
    const [errorMessage, setErrorMessage] = useState(null);

    const [pdfFileId, setPdfFileId] = useState(null);
    const [docxFileId, setDocxFileId] = useState(null);
    const [txtFileId, setTxtFileId] = useState(null);

    const onUploadClick = event => {
        if (!addedFile) {
            return;
        }

        setIsLoading(true);
        setErrorMessage(null);
        setRecognizedText(null);
        setPdfFileId(null);
        setDocxFileId(null);
        setTxtFileId(null);

        recognizeText(addedFile).then(result => {
            setRecognizedText(result["text"]);
            setIsLoading(false);
            setPdfFileId(result["pdfId"]);
            setDocxFileId(result["docxId"]);
            setTxtFileId(result["txtId"]);

        }).catch(reason => {
            console.error("Unable to upload file to server.", reason);
            setErrorMessage("Unable to upload file, please try again.");
            setIsLoading(false);
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

            <button className={styles.recognizeBtn} type="button" onClick={onUploadClick} disabled={addedFile === null || isLoading}>Recognize Text</button>
            {isLoading &&
                <LoadingCircle className={styles.loadingCircle} />
            }
            <div className={styles.errorMessage}>{errorMessage}</div>

            <div className={styles.filesDownloadContainer}>
                <FileDownloadBtn title="Download PDF" icon={pdfButton} fileId={pdfFileId} />
                <FileDownloadBtn title="Download DOCX" icon={docxButton} fileId={docxFileId} />
                <FileDownloadBtn title="Download TXT" icon={txtButton} fileId={txtFileId} />
            </div>

            <p className={styles.recognizedText}>{recognizedText}</p>
        </div>
    );
}