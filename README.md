# ssd8-experiments
My ssd8-networking&distribution course experiments in nwpu.  



## Purpose
为了借鉴和交流.  

存一下`SSD8`的实验课作业, 毕竟花了不少时间.  

如果有**STAR**就更好了(笑).  



## Note
1. **Don't copy the code directly**, the purpose of this repo is above.

2. please star me! I will be happy ^\_^  

   

## Structure
```
├── exam1
│   ├── Exam1-HttpProxy
│   ├── HttpProxy.jar
│   ├── README.md
│   └── README.pdf
├── exam2
│   ├── Client.jar
│   ├── Exam2-RmiDistributedMessageSystem
│   ├── README.md
│   ├── README.pdf
│   └── RmiServer.jar
├── lab1
│   ├── Lab1-TCP_UDP_Socket
│   ├── README.md
│   └── README.pdf
├── lab2
│   ├── HttpServer.jar
│   ├── Lab2-HttpServer&HttpClient
│   ├── MultiThreadHttpServer.jar
│   ├── README.md
│   └── README.pdf
├── lab3
│   ├── Lab3-RMI
│   ├── README.md
│   ├── README.pdf
│   ├── RmiClient.jar
│   └── test.sh
├── LICENSE
├── README.md
├── 参考资料
│   ├── hrping.v504.rar
│   ├── JavaCodeConventions.pdf
│   ├── javathread.ppt
│   ├── JDK_API_1_6_zh_CN.CHM
│   ├── RawCap.exe
│   ├── rexec.pdf
│   ├── rfc1945_HTTP1.0.txt
│   ├── rfc2068_HTTP1.1.txt
│   ├── rfc4511_LDAP.txt
│   ├── rfc5531_RPCv2.txt
│   ├── rfc7530_NFSv4.txt
│   ├── sun推荐的Java编码规范.doc
│   ├── webservice
│   └── 分布式系统原理与范型.pdf
├── 实验内容
│   ├── Client.java
│   ├── Exam 1.doc
│   ├── Exam 2.doc
│   ├── Exam 2.rar
│   ├── Exercise 1.doc
│   ├── Exercise 2
│   ├── Exercise 2.rar
│   ├── Exercise 3.doc
│   └── Exercise 4.doc
├── 理论课件
│   ├── 01_概述.ppt
│   ├── 02_名字服务.ppt
│   ├── 03_分布式进程.ppt
│   ├── 04_分布式系统通信.ppt
│   ├── 05_分布式系统同步.ppt
│   ├── 06_多副本一致性与DSM.ppt
│   ├── 07_分布式文件系统.ppt
│   ├── 11_Socket编程.ppt
│   ├── 12_面向对象的分布计算.ppt
│   ├── 13_面向服务的分布计算.ppt
│   ├── 14_公用服务式-志愿者参与分布计算.ppt
│   └── src&tools
└── 网络与分布计算实验课进度安排.doc

16 directories, 52 files
```



## Key point

### lab1

1. used **thread pool** to implement multi-thread operations.
2. used **strategy design pattern** to reduce maintenance difficulty, and clear the code.
3. used **regular expression** to handle command parsing.
4. used **TCP** for handle the commands, **UDP** for transferring files.
5. considered **robustness**.

### lab2

1. used **thread pool** to implement multi-thread operations.
2. used **strategy design pattern** to reduce maintenance difficulty, and clear the code.
3. used **builder design pattern** to keep `HttpRequest` and `HttpResponse` constant, and provide flexible build process.
4. defined **HTTP status** name and corresponding code.

### lab3

1. used **strategy design pattern** to reduce maintenance difficulty, and clear the code.

2. used **stream** operation to provide faster develop process.

### lab4

`TODO`

### exam1

1. used **thread pool** to implement multi-thread operations.
2. used **strategy design pattern** to reduce maintenance difficulty, and clear the code.
3. used **builder design pattern** to keep `HttpRequest` and `HttpResponse` constant, and provide flexible build process.
4. defined **HTTP status** name and corresponding code.

### exam2

1. used **strategy design pattern** to reduce maintenance difficulty, and clear the code.

2. use **Response** generic class to provide error informations.



## LICENSE

```
MIT License  

Copyright (c) 2019 Chunxu Zhang  
```
