import sys
import cv2
from extract_lines import extract_lines
from lines_recognizer import lines_recognizer


def main(argc, argv):
    if (argc != 6):
        raise ValueError("Wrong number of arguments passed to program")
    
    filePath = argv[1]
    x1, y1, x2, y2 = map(int, argv[2:])
    
    img = cv2.imread(filePath, cv2.IMREAD_GRAYSCALE)
    lines_images = extract_lines(img[y1:y2, x1:x2])
    
    recognizer = lines_recognizer()
    result = recognizer.recognize_lines(lines_images, are_binarized=True)
    result = ' '.join(result)
    
    sys.stdout.buffer.write(result.encode("utf-8"))

if __name__ == '__main__':
    main(len(sys.argv), sys.argv)