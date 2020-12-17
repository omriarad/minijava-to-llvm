.PHONY: all

TARGETS = $(patsubst %.java.xml,%,$(wildcard *.java.xml))

all:
	@- $(foreach T,$(TARGETS), \
		java -jar ../../mjavac.jar unmarshal semantic ./$T.java.xml ./result > /dev/null; \
		diff -rup ./result ./$T.res || echo "\e[31merror in test $T\e[0m" ; \
	)
