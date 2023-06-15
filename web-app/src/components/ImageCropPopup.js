import {useEffect, useRef, useState} from 'react';
import styles from './ImageCropPopup.module.scss';
import CommonButton from './CommonButton';
import ReactCrop from 'react-image-crop';
import 'react-image-crop/src/ReactCrop.scss'
import CrossIcon from '../assets/icons/CrossIcon';
import fitImageInContainer from '../assets/helpers/fitImageInContainer';

export default function ImageCropPopup({imgSrc, initialArea, onConfirm, onCancel}) {
    const [crop, setCrop] = useState(initialArea == null ? null : {
        unit: "%",
        x: initialArea.x1,
        y: initialArea.y1,
        width: initialArea.x2 - initialArea.x1,
        height: initialArea.y2 - initialArea.y1
    });
    const imgRef = useRef();
    const containerRef = useRef();

    const imgResizeObserver = new ResizeObserver(() => fitImageInContainer(imgRef.current, containerRef.current));

    useEffect(() => {
        return () => {
            imgResizeObserver.disconnect();
        }
    }, []);

    const percentCropToArea = percentCrop => {
        let width = imgRef.current.naturalWidth;
        let height = imgRef.current.naturalHeight;
        return {
            x1: Math.floor(percentCrop.x * width * 0.01),
            y1: Math.floor(percentCrop.y * height * 0.01),
            x2: Math.floor((percentCrop.x + percentCrop.width) * width * 0.01),
            y2: Math.floor((percentCrop.y + percentCrop.height) * height * 0.01)
        };
    };

    return (
        <>
            <div className={styles.overlay} onClick={() => onCancel ? onCancel() : null}></div>
            <div className={styles.popupContainer}>
                <div className={styles.heading}>Crop the text</div>

                <div className={styles.imageContainer} ref={containerRef}>
                    <div className={styles.imageContainerInner}>
                        <ReactCrop className={styles.reactCrop} crop={crop} onChange={(_, percentCrop) => setCrop(percentCrop)}>
                            <img
                                className={styles.image}
                                src={imgSrc}
                                alt=""
                                ref={imgRef}
                                onLoad={() => imgResizeObserver.observe(containerRef.current)}
                            />
                        </ReactCrop>
                    </div>
                </div>

                <div className={styles.confirmBtnContainer}>
                    <CommonButton
                        className={styles.confirmBtn}
                        onClick={() => onConfirm ? onConfirm(percentCropToArea(crop)) : null}
                        disabled={crop === null || crop === undefined || crop.width === 0 || crop.height === 0}
                    >Confirm</CommonButton>
                </div>

                <button className={styles.closeBtn} onClick={() => onCancel ? onCancel() : null}>
                    <CrossIcon />
                </button>
            </div>
        </>
    )
}