# Getting Started

This is a LAN console chat app written in Java.

# How to use

Before starting chat, the GUI will ask for the username. It will also print out the list of current users.
After that, the user can chat in a public chatroom. The user can also use multiple commands to perform certain tasks.

## Supported commands:
- /pm <username>: Toggles private chat with another user in the chatroom. Other people except for the intended recipient cannot see user's messages after this command.

- /all: Returns to the public chatroom. Everyone else can see user's messages.

- /users: Displays a list of current users using the chat app

- /name: Change username. New username must not replicate an existing username. 

# Makefile

To make testing and compiling more convenient, a makefile has been written.

## Compile:
- make clean: Clear everything in `bin` folder
- make all: Compile both client and server programs

## Testing:
- make runserver: Start a test server on localhost
- make runclient: Start a test client on localhost

