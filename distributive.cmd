call mvn clean install -P maximum
cd Evolution
call mvn assembly:single
cd ../
::md dist
copy Evolution\target\evolution-1.3.2-SNAPSHOT-jar-with-dependencies.jar dist\evolution.jar
