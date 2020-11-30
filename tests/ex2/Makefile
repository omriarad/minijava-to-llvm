.PHONY: all

TARGETS = $(patsubst %.java.xml,%,$(wildcard *.java.xml))

all:
	@- $(foreach T,$(TARGETS), \
		java -jar ../../mjavac.jar unmarshal compile ./$T.java.xml ./out.ll ; \
		diff -rup ./out.ll ./$T.ll || echo "\e[31merror in test $T\e[0m" ; \
	)
