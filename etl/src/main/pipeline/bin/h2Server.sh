#!/usr/bin/env bash
# Script to start or stop a H2 Serve database

echo "Please enter the port number of the H2 database server: "
read h2Port
echo "Please type 'start' to initiate the H2 Server or type 'stop' to shutdown the H2 Server: "
read action

if test "$action" = "start"
then
echo "Starting the H2 Server."
java -Xms512m -Xmx2048m -cp ../../etl-1.0-jar-with-dependencies.jar org.h2.tools.Server -tcp -tcpPort "$h2Port" -baseDir "./../database/" &
else
echo "Stopping the H2 Server."
java -cp ../../etl-1.0-jar-with-dependencies.jar org.h2.tools.Server -tcpShutdown "tcp://localhost:$h2Port"
fi
