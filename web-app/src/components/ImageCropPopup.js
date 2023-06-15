import {useRef, useState} from 'react';
import styles from './ImageCropPopup.module.scss';
import CommonButton from './CommonButton';
import ReactCrop from 'react-image-crop';
import 'react-image-crop/src/ReactCrop.scss'
import pdfButton from '../assets/icons/pdfButton.png';
import fitImageInContainer from '../assets/helpers/fitImageInParent';

export default function ImageCropPopup({imgSrc, initialArea, onConfirm}) {
    const [crop, setCrop] = useState({
        unit: "px",
        x: initialArea.x1,
        y: initialArea.y1,
        width: initialArea.x2 - initialArea.x1,
        height: initialArea.y2 - initialArea.y1
    });
    const imgRef = useRef();
    const containerRef = useRef();

    return (
        <div className={styles.overlay}>
            <div className={styles.popupContainer}>
                <div className={styles.heading}>Crop the text</div>

                <div className={styles.imageContainer} ref={containerRef}>
                    <div className={styles.imageContainerInner}>
                        <ReactCrop crop={crop} onChange={crop => setCrop(crop)}>
                            <img
                                className={styles.image}
                                src={pdfButton}
                                alt=""
                                ref={imgRef}
                                onLoad={() => {
                                    new ResizeObserver(() => fitImageInContainer(imgRef.current, containerRef.current)).observe(containerRef.current)
                                }}
                            />
                        </ReactCrop>
                    </div>
                </div>


                <CommonButton
                    className={styles.confirmBtn}
                    onClick={() => onConfirm({
                        x1: crop.x,
                        y1: crop.y,
                        x2: crop.x + crop.width,
                        y2: crop.y + crop.height
                    })}
                >
                    Confirm
                </CommonButton>
            </div>
        </div>
    )
}