JAVAC=/usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<
    
CLASSES = \
        MeanFilterSerial.class \
        MedianFilterSerial.class \
        MedianFilterParallel.class \
        MeanFilterParallel.class 

CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)



default: $(CLASS_FILES)


clean:
	rm $(BINDIR)/*.class
	   
docs:
	javadoc -d doc/ src/*.java      	
