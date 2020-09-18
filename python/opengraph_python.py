import opengraph
import argparse
import sys

def main(args):
        parser = argparse.ArgumentParser()
        parser.add_argument('url', type=str)
        args = parser.parse_args(args)
        try:
                ograph = opengraph.OpenGraph(url=args.url)
                if "image" in ograph:
                        print(ograph.image)
                else:
                        print("Invalid URL")

        except Exception as e:
                 print("Exception of ", e.__class__)

if __name__ == "__main__":
    main(sys.argv[1:])


