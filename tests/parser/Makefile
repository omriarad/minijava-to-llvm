.PHONY: all

TARGETS = $(patsubst %.java.xml,%,$(wildcard ../pool/*.java.xml)) \
		  $(patsubst %.err,%,$(wildcard ../pool/*.err))

all:
	@- $(foreach T,$(TARGETS), \
		bash ./test.sh $T; \
	)
