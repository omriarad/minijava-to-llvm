This is a joint project by Omri Arad, Tal Trotskovsky and Guy Haviv that implements a compiler from an object-oriented language called MiniJava into LLVM assembly/IR as part of the 2020 TAU compilation course.

MiniJava is (almost exactly) a subset of Java, defined in the Appendix of Appel and Palsberg's Modern Compiler Implementation in Java, 2nd edition and described on the MiniJava web site (http://www.cambridge.org/resources/052182060X/).

The provided build settings (in build.xml) are intended for Ubuntu 18.04 and Java 11, It works using Apache Ant.

## Project Structure
```
build.xml
	(directives for compiling the project using 'ant')

build/
	(temp directory created when you build)

examples/
	ast/
		(examples of AST XMLs representing Java programs)

schema/
	ast.xsd
		(XML schema for ASTs)

src/
	project source files

tools/
	(third party JARs for lexing & parsing and XML manipulation + scripts)

tests/
	test pool with expected outcomes and scripts to test the various stages (parsing, semantic checking and LLVM/IR translation)
```

## Run dependencies for your OS

``` bash
sudo apt install openjdk-11-jdk
sudo apt install llvm
sudo apt install ant
# for running the tests
pip3 install lxml
```

## How to Build

Check out source (for example public version):

``` bash
git clone https://github.com/omriarad/minijava-to-llvm
cd minijava-to-llvm
ant
```

## How to Run
``` bash
java -jar mjavac.jar [minijava_src_file.java] [output.ll]
```

## How to Run tests
```bash
ant test # Will run all testing stages
ant parser # Will run parser tests
ant semantic # Will run semantic check tests
ant llvm # Will run LLVM translation tests
```
