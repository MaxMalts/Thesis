import sys
from extract_lines import extract_lines


def main(argc, argv):
    if (argc < 2):
        raise ValueError("No file name passed to the program")
    
    sys.stderr.write(str(argc) + '\n')
    sys.stderr.write(' '.join(argv) + '\n')
    
    lines = extract_lines(argv[1])
    sys.stdout.buffer.write(str(len(lines)).encode("utf-8"))
    
    res = "Hello World from python".encode("utf-8")
    sys.stdout.buffer.write(res)

if __name__ == '__main__':
    main(len(sys.argv), sys.argv)