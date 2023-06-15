import {useState} from 'react';
import styles from './HomePage.module.scss';
import FileInput from '../components/FileInput';
import recognizeText from '../api/recognizeText';
import LoadingCircle from '../assets/icons/LoadingCircle';
import pdfButton from '../assets/icons/pdfButton.png';
import docxButton from '../assets/icons/docxButton.png';
import txtButton from '../assets/icons/txtButton.png';
import FileDownloadBtn from '../components/FileDownloadBtn';
import CommonButton from '../components/CommonButton';
import ImageCropPopup from '../components/ImageCropPopup';


export default function HomePage() {
    const [addedFile, setAddedFile] = useState(null);
    const [imageArea, setImageArea] = useState(null);

    const [isLoading, setIsLoading] = useState(false);
    const [recognizedText, setRecognizedText] = useState(null);
    const [errorMessage, setErrorMessage] = useState(null);

    const [pdfFileId, setPdfFileId] = useState(null);
    const [docxFileId, setDocxFileId] = useState(null);
    const [txtFileId, setTxtFileId] = useState(null);

    const onUploadClick = () => {
        if (!addedFile) {
            return;
        }

        setIsLoading(true);
        setErrorMessage(null);
        setRecognizedText(null);
        setPdfFileId(null);
        setDocxFileId(null);
        setTxtFileId(null);

        recognizeText(addedFile, imageArea).then(result => {
            setRecognizedText(result["text"]);
            setIsLoading(false);
            setPdfFileId(result["pdfId"]);
            setDocxFileId(result["docxId"]);
            setTxtFileId(result["txtId"]);

        }).catch(reason => {
            console.error("Unable to upload file to server.", reason);
            setIsLoading(false);
            setErrorMessage("Error occurred: " + reason["message"]);
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
                <FileInput onFileAdded={(file, area) => (setAddedFile(file), setImageArea(area))} />
            </div>
            <div className={styles.sizeLimitText}>
                Max size: 10 MB
            </div>

            <CommonButton
                className={styles.recognizeBtn}
                onClick={onUploadClick}
                disabled={addedFile === null || isLoading}
            >Recognize Text</CommonButton>
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