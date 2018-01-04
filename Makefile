all:
	javac -d class src/*.java

clean:
	rm -f class/*.class
