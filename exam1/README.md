# Exam 1 - HttpProxy

| ID         | Author       | Date            |
| ---------- | ------------ | --------------- |
| 2017303024 | Zhang Chunxu | Sat 16 Nov 2019 |



## Key point

1. used **thread pool** to implement multi-thread operations.
2. used **strategy design pattern** to reduce maintenance difficulty, and clear the code.
3. used **builder design pattern** to keep `HttpRequest` and `HttpResponse` constant, and provide flexible build process.
4. defined **HTTP status** name and corresponding code.
5. considered **robustness**.



## How to run

### Run

```bash
java -jar HttpProxy.jar
# or you want to run on another port
java -jar HttpProxy.jar 1080
```



# Note
1. Most browser use HTTP/1.1 as default HTTP version.
   If you want this proxy can support HTTP/1.1, you can uncomment the code in `HttpHandler.java` on 94 and 95 lines.



# Demo shootscreen

![Screenshot from 2019-12-07 17-44-18](/home/tanglizi/Pictures/Screenshot from 2019-12-07 17-44-18.png)