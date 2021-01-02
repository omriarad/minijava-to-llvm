import sys
from binascii import hexlify
import argparse
from lxml import etree

def remove_line_numers(root):
    for elem in root.xpath("//*[lineNumber]"):
        for child in elem.getchildren():
            if child.tag == "lineNumber":
                elem.remove(child)

def compare_asts(*asts):
    etrees = set()
    for ast in asts:
        with open(ast, "rb") as f:
            ast_data = f.read()
            ast_data = ast_data.decode('utf-8').encode('ascii')
        tree = etree.XML(ast_data)
        remove_line_numers(tree)
        etree.indent(tree, space="")
        etrees.add(etree.tostring(tree))
    return len(etrees) == 1

def get_args():
    parser = argparse.ArgumentParser()
    parser.add_argument('asts', metavar='A', type=str, nargs=2)
    return parser.parse_args()

def main(ast1, ast2):
    if compare_asts(ast1, ast2) is not True:
        sys.exit(1)

if __name__ == '__main__':
    args = get_args()
    main(*args.asts)
