import sys
from extract_lines import extract_lines
from handwriting_recognizer import handwriting_recognizer


def main(argc, argv):
    if (argc < 2):
        raise ValueError("No file name passed to the program")
    
    sys.stderr.write(str(argc) + '\n')
    sys.stderr.write(' '.join(argv) + '\n')
    
    lines_images = extract_lines(argv[1])
    
    recognizer = handwriting_recognizer()
    result = recognizer.recognize_lines(lines_images, are_binarized=True)
    result = ' '.join(result)
    
    sys.stdout.buffer.write(result.encode("utf-8"))

if __name__ == '__main__':
    main(len(sys.argv), sys.argv)