all: class 
class :
	javac ET4HValidator.java
jar : 
	jar cvfm ET4HValidator.jar  manifest.txt ET4HValidator*.class

