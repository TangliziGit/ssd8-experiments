# Lab3 - RMI

| ID         | Author       | Date            |
| ---------- | ------------ | --------------- |
| 2017303024 | Zhang Chunxu | Sat 13 Dec 2019 |



## Key point

2. used **strategy design pattern** to reduce maintenance difficulty, and clear the code.
2. used **stream** operation to provide faster develop process.
3. considered **robustness**.



## How to run

### Deploy

```bash
None
```



### Run

```bash
# first run your java rmiregistery.
java -jar RmiClient.jar <serverName> <portNumber> <command> [options...]

# example:
java -jar RmiClient.jar run add x x y "2019-12-01_01:00" "2019-12-01_03:00" MeetingTitle
```

and you can edit `test.sh` file to test it faster.

```bash
# test.sh sample
function run(){
    java -jar RmiClient.jar localhost 1099 $@
}

run register x x
run register y y
run add x x y "2019-12-01_01:00" "2019-12-01_03:00" MeetingTitle
run add x x y "2019-12-00_01:00" "2019-12-02_03:00" MeetingTitle
run query x x "2019-12-00_01:00" "2019-12-02_01:00"
run delete x x 1
run delete x x 10
run clear x x
```





## Demo screen shot

1. print help information and run `test.sh`:

   ![Screenshot from 2019-12-21 14-50-29](/home/tanglizi/Pictures/Screenshot from 2019-12-21 14-50-29.png)

2. run `test.sh` again:

   ![Screenshot from 2019-12-21 14-51-36](/home/tanglizi/Pictures/Screenshot from 2019-12-21 14-51-36.png)
