.PHONY: all

TARGETS = $(patsubst %.java,%,$(wildcard *.java))

all:
	@- $(foreach T,$(TARGETS), \
		bash ./test.sh $T; \
	)
