#!/usr/bin/env bash
# Script to connect to a specific H2 Server using the H2 Console

echo "Please enter the port number in which the server is running: "
read h2Port
echo "Please enter the db name of H2 that you want to use: "
read h2DataBaseName

java -cp ../../etl-1.0-jar-with-dependencies.jar org.h2.tools.Shell -driver "org.h2.Driver" \
     -url "jdbc:h2:tcp://localhost:$h2Port/$h2DataBaseName" \
     -user "sa" -password ""
