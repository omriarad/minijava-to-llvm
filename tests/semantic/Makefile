.PHONY: all

TARGETS = $(patsubst %.res,%,$(wildcard ../pool/*.res))

all:
	@- $(foreach T,$(TARGETS), \
		java -jar ../../mjavac.jar --semantic ./$T.java ./result > /dev/null 2>&1; \
		diff -rup ./result ./$T.res || echo "\e[31merror in test $T\e[0m" ; \
	)
