FROM anapsix/alpine-java
COPY out/Server.jar /home/Server.jar
CMD ["java","-Dredir=172.17.02","-jar","/home/Server.jar"]
#   Kill container
#docker rm -f srv1
#   Delete image
#sudo docker rmi srv
#   Rebuild
#sudo docker build -t srv .
#   run
#sudo docker run -d --name srv1 srv
#   logs
#sudo docker logs srv1
