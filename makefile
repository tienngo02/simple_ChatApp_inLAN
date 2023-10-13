JAVAC = javac
JAVAC_FLAGS = -d bin -sourcepath src

# Define the list of source files in the "client" and "server" packages
CLIENT_SOURCES := $(wildcard src/client/*.java)
SERVER_SOURCES := $(wildcard src/server/*.java)

# Define the corresponding compiled files in the "client" and "server" packages
CLIENT_CLASSES := $(patsubst src/client/%.java, bin/client/%.class, $(CLIENT_SOURCES))
SERVER_CLASSES := $(patsubst src/server/%.java, bin/server/%.class, $(SERVER_SOURCES))

# Define the default target to compile all source files
all: client server

# Target to compile the "client" package
client: $(CLIENT_CLASSES)

# Target to compile the "server" package
server: $(SERVER_CLASSES)

# Define the rule to compile Java source files
$(CLIENT_CLASSES): bin/client/%.class : src/client/%.java
	$(JAVAC) $(JAVAC_FLAGS) $<

$(SERVER_CLASSES): bin/server/%.class : src/server/%.java
	$(JAVAC) $(JAVAC_FLAGS) $<

# Target to clean up the compiled classes
.PHONY:
clean:
	rm -rf bin/client/*.class
	rm -rf bin/server/*.class

runclient:
	java -cp bin client.ChatClient

runserver:
	java -cp bin server.ChatServer