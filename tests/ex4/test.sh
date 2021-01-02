java -jar ../../mjavac.jar parse marshal ./$1.java ./result.xml 2> ./result.err

if [ -f "./$1.err" ]; then
	diff -rup ./result.err ./$1.err || printf "\e[31merror in test $1\e[0m\n"
else
	python3 ../../tools/compare_asts.py ./$1.java.xml ./result.xml || printf "\e[31merror in test $1\e[0m\n"
fi
