BIN_PATH := ./bin/
SRC_PATH := ./src/

JAVAC := javac -encoding utf8 -d $(BIN_PATH) -classpath $(SRC_PATH)
JAVA := java -cp $(BIN_PATH)

build:
	$(JAVAC) $(SRC_PATH)BaseReceiver.java 
	$(JAVAC) $(SRC_PATH)BaseSender.java 
	$(JAVAC) $(SRC_PATH)CLI.java 
	$(JAVAC) $(SRC_PATH)ModChat.java 
	$(JAVAC) $(SRC_PATH)ModFile.java 
	$(JAVAC) $(SRC_PATH)ModFind.java 
	$(JAVAC) $(SRC_PATH)ScannerThread.java 
	$(JAVAC) $(SRC_PATH)UserInfo.java 
	$(JAVAC) $(SRC_PATH)Utility.java

run:
	@$(JAVA) CLI

clean:
	@rm ./bin/*.class
	@rm ./src/*.class