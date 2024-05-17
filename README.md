# JavaCrypt

## Description
This objective of this project is creating a simple cryptography application that allows to use basic cryptographic operations such as file encryption and decryption as well as hash calculation.

## How to build
If you want to build this project yourself, follow these simple steps :
- Clone this repository.
- Run the command ```mvn install``` or use the "mvnw" file located in the root of the project this way : ```./mvnw install```
- The output jar will be in the target/ directory along with the lib/ directory.

Notes:
- The lib/ directory contains the bouncy castle provider that cannot be put in the jar because of signing issues. Without this directory, the application might not work correctly.
- The executable jar is the one with the suffix "shaded" since this project is using maven shade plugin.


## Work in progress features
- Asymmetric cryptography.
- Stream cipher cryptography.
- HMAC based file authentication.
- Digital signature based file authentication.
- Use keystore to save keys.
