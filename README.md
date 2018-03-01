# Zero-Copy Test tools

This is a tool for testing zero copy using Java.

This tool is using Java NIO and using transferTo function for zero-copy.

required jdk 1.8+

#### 1. Build or Download

You can compile this tool yourself.

```
mvn clean comiple package
```

Alternatively, you can [download](https://github.com/Reve1988/zero-copy/raw/master/bin/zerocopy.jar) and use the compiled binary file.

#### 2. How to run

##### Run server

- All parentheses are mandatory

```
java -jar zerocopy.jar server [port]
```

##### Run client

- All parentheses are mandatory

```
java -jar zerocopy.jar [serverInfo(host:port)] [testFilePath] [zerocopy(true|false)]
```

#### 3. Example

##### Run server
```
$ ls
test.txt	zerocopy.jar
$ java -jar zerocopy.jar server 9000 &
[1] 2564
$ jps
2579 Jps
2564 jar
```

##### Run client
```
$ java -jar zerocopy.jar client localhost:9000 ./test.txt true
Client Usage : java -jar [jarfile] host:port filePath zerocopy
13:07:20.134 [Thread-0] INFO kr.revelope.zerocopy.Receiver - Accepted : java.nio.channels.SocketChannel[connected local=/127.0.0.1:9000 remote=/127.0.0.1:50045]
13:07:20.143 [main] INFO kr.revelope.zerocopy.Sender - Transferred 5bytes.
13:07:20.146 [main] INFO kr.revelope.zerocopy.Sender - Time taken in 3ms.
```

##### Stop server
```
$ kill 2564
[1]+  Exit 143                java -jar zerocopy.jar server 9000
$ jps
2582 Jps
```

#### 4. Article

- English : https://www.ibm.com/developerworks/linux/library/j-zerocopy/
- Korean : https://kgw1988.blog.me
