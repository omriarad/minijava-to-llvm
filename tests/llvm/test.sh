#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

for src in $(ls tests/pool/*.ll | cut -d. -f1); do
	java -jar ./mjavac.jar $src.java $DIR/out.ll
	diff -rup $DIR/out.ll $src.ll || echo "\e[33mwarning in test $T\e[0m"
	lli $DIR/out.ll > $DIR/output.txt
	lli $src.ll > $DIR/expected_output.txt
	diff -rup $DIR/output.txt $DIR/expected_output.txt || echo "\e[31merror in test $T\e[0m"
done
