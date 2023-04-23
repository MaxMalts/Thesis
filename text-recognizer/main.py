import sys

def main(argc, argv):
    res = "Hello World from python".encode("utf-8")
    sys.stdout.buffer.write(res)

if __name__ == '__main__':
    main(sys.argv, len(sys.argv))