# Lab2 - HttpServer & HttpClient

| ID         | Author       | Date            |
| ---------- | ------------ | --------------- |
| 2017303024 | Zhang Chunxu | Sat 16 Nov 2019 |



## Key point

1. used **thread pool** to implement multi-thread operations.
2. used **strategy design pattern** to reduce maintenance difficulty, and clear the code.
3. used **builder design pattern** to keep HttpRequest and HttpResponse constant, and provide flexible build process.
4. defined **HTTP status** name and corresponding code.
5. considered **robustness**.



## How to run

### Deploy

```bash
mkdir server && cd server
cp ../HttpServer.jar ../MultiThreadHttpServer.jar .
vim 404.html 403.html # please write your own error pages.
vim index.html # write the HTML page, which you want to visit.
cd ..
```



### Run

```bash
# run single thread HTTP server (open default 80 port need permission on linux):
sudo java -jar HttpServer.jar ./server/
# or specify port instead of default 80 port
java -jar HttpServer.jar ./server/ 8080
# or run multiple threads HTTP server:
sudo java -jar MultiThreadHttpServer.jar ./server/ 
```



## Demo screen shot

1. get a not existed page:

![](/home/tanglizi/Pictures/Screenshot from 2019-12-07 13-04-26.png)



2. get `index.html`

![](/home/tanglizi/Pictures/Screenshot from 2019-12-07 13-04-46.png)



3. get `file.txt`

![Screenshot from 2019-12-07 13-04-56](/home/tanglizi/Pictures/Screenshot from 2019-12-07 13-04-56.png)

