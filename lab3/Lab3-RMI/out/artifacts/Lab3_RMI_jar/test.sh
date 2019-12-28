function run(){
    java -jar Lab3-RMI.jar localhost 1099 $@
}

run register x x
run register y y
run add x x y "2019-12-01_01:00" "2019-12-01_03:00" MeetingTitle
run query x x "2019-12-00_01:00" "2019-12-02_01:00"
run delete x x 1
run clear x x
