# Makefiles have targets and these targets correspond to commands
# that you would like to run

# target, known as default target, it is the first one so it is 
# automatically called when "make" is typed with no arguments 

all: Plot.java	Cube.java Point.java ThreeDimensionalSpace.java 
	javac TDSmain.java
run:
	java TDSmain
args:
	ARGS = $(
commit:
	git commit -m $
.PHONY: clean

clean:
	rm -f *.class