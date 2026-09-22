# Simple Testing Server

A simple test server in Java 25 that simply shows visit count and system information.

Build:
```sh
./gradlew bootJar
```
or on Windows:
```sh
gradlew.bat bootJar
```

Run on port 80 (might require root previleges):
```sh
java -jar build/libs/test-server-1.1.jar --server.port=80
```
or run on the default port 8080:
```sh
java -jar build/libs/test-server-1.1.jar
```