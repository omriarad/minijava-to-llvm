.PHONY: all

TARGETS = $(patsubst %.ll,%,$(wildcard ../pool/*.ll))

all:
	@- $(foreach T,$(TARGETS), \
		java -jar ../../mjavac.jar ./$T.java ./out.ll ; \
		diff -rup ./out.ll ./$T.ll || echo "\e[33mwarning in test $T\e[0m" ; \
		lli out.ll > output.txt ; \
		lli ./$T.ll > expected_output.txt ; \
		diff -rup output.txt expected_output.txt || echo "\e[31merror in test $T\e[0m" ; \
	)
