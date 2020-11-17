.PHONY: all

TARGETS = $(patsubst %.sh,%,$(wildcard *.sh))

all:
	@- $(foreach T,$(TARGETS), \
		bash ./$T.sh ; \
		diff -rup ./result$T.xml ./result.xml || echo "\e[31merror in test $T\e[0m" ; \
	)
