#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

for src in $(ls tests/pool/*.res | cut -d. -f1); do
	java -jar ./mjavac.jar --semantic $src.java $DIR/result > /dev/null 2>&1
	diff -rup $DIR/result $src.res || echo "\e[31merror in test $T\e[0m"
done
