# Lab1 - TCP & UDP

| ID         | Author       | Date            |
| ---------- | ------------ | --------------- |
| 2017303024 | Zhang Chunxu | Sat 16 Nov 2019 |



## Key point

1. used **thread pool** to implement multi-thread operations.
2. used **strategy design pattern** to reduce maintenance difficulty, and clear the code.
3. used **regular expression** to handle command parsing.
4. used **TCP** for handle the commands, **UDP** for transferring files.
5. considered **robustness**.



## How to run

### Deploy

```bash
cd Lab1-TCP_UDP_Socket/src/main/java
javac org/tanglizi/dist/FileServer.java org/tanglizi/dist/FileClient.java
```



### Run

```bash
# run FileServer:
java org.tanglizi.dist.FileServer /home/
# run FileClient:
java org.tanglizi.dist.FileClient
```

And then your terminal will be like this:

![](/home/tanglizi/Pictures/Screenshot from 2019-11-16 15-10-57.png)



## Demo screen shot

Left side is client, and right side is server.

- Single client
![](/home/tanglizi/Pictures/Screenshot from 2019-11-16 15-16-04.png)

- Two client

  ![](/home/tanglizi/Pictures/Screenshot from 2019-11-16 15-20-35.png)

- Robustness test (pay attention to the error message)

  ![](/home/tanglizi/Pictures/Screenshot from 2019-11-16 15-23-04.png)