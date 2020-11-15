.PHONY: all

TARGETS = $(patsubst %.sh,%,$(wildcard *.sh))

all:
	@- $(foreach T,$(TARGETS), \
		./$T.sh ; \
		diff -rup ./result$T.xml ./result.xml || echo "\e[31merror in test $T\e[0m" ; \
	)
	@- cd tmp; bash -c renaming_commands.sh
	@- git diff
