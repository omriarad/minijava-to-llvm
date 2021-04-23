#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

for src in $(ls tests/pool/*.java.xml | cut -d. -f1); do
	java -jar ./mjavac.jar --marshal $src.java $DIR/result.xml
	python3 tools/compare_asts.py $src.java.xml $DIR/result.xml || printf "\e[31merror in test $1\e[0m\n"
done

for src in $(ls tests/pool/*.err | cut -d. -f1); do
	java -jar ./mjavac.jar --marshal $src.java $DIR/result.xml 2> $DIR/result.err
	diff -rup $DIR/result.err $src.err || printf "\e[31merror in test $1\e[0m\n"
done
