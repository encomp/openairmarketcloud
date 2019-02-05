#!/usr/bin/env bash
# Script that executes a specific pipeline.

mainPath=/Users/edgarrico/AndroidStudioProjects/OpenAirMarketCloud/etl/build
configPath=$mainPath/pipeline/config
configH2File=$configPath/h2_environment_variables.sql
pipelineConfig=$mainPath/pipeline/config/pipeline.xml
log=$mainPath/pipeline/config/log4j.properties
scriptsPath=$mainPath/pipeline/
inputPath=$mainPath/pipeline/data/input/
outputPath=$mainPath/pipeline/data/output/

# Request the pipeline to execute
echo "Please enter the runner you want to use to execute the pipeline: "
read pipelineRunner
if [[ ($pipelineRunner ==  "extract") || ($pipelineRunner ==  "default") ]]
  then
    # Request the MS-SQL credentials
    echo "Please enter the user name of MS-SQL: "
    read msSqlUser
    echo "Please enter the password: "
    read msSqlPass
    # Request the H2 configuration to use
    echo "Please enter the database name of H2 that you want to use: "
    read h2DataBaseName
    echo "Do you want to run in DEBUG mode [y/n]: "
    read mode
    if test "$mode" = "y"
      then
        echo "Please enter the port number in which the server is running: "
        read h2Port
    fi
    msSqlUrl=jdbc:sqlserver://localhost:1433;database=SPVGanaMasSQL
    h2DatabasePath=$mainPath/pipeline/database/$h2DataBaseName
    # Define the required env variables for H2
    debugh2url=jdbc:h2:tcp://localhost:$h2Port/$h2DataBaseName;MULTI_THREADED=TRUE
    h2url=jdbc:h2:file:$h2DatabasePath;MULTI_THREADED=TRUE
    # Setting up the proper configuration for H2
    if test "$mode" = "y"
      then
        echo "Running in debug mode"
        h2url=$debugh2url
     else
      echo "Not running in debug mode"
     fi
    # Setting up the file that contains the environment variables for H2
    echo "SET @path_main            = '"$mainPath"/pipeline';" > $configH2File
    cat ./h2_environment_variables.template >> $configH2File
    echo "Please enter the pipelines you want to execute followed by a space: "
    read pipelines
    # Define an array for the pipelines
    OIFS="$IFS"
    IFS=' '
    read -a pipelinesArray <<< "${pipelines}"
    IFS="$OIFS"
    for pipelineId in "${pipelinesArray[@]}"
    do
      java -Dlog4j.configuration=file://$log \
        -Dflogger.backend_factory=com.google.common.flogger.backend.log4j.Log4jBackendFactory#getInstance \
        -jar ./../../etl-1.0-jar-with-dependencies.jar \
        --h2Url=$h2url --h2MaxPoolSize=5 --h2FilePath=$configH2File \
        --msSqlUser=$msSqlUser --msSqlPass=$msSqlPass --msSqlMaxPoolSize=5 \
        --pipelineConfig=$pipelineConfig --pipelineRunner=$pipelineRunner --pipelineId=$pipelineId \
        --scriptsPath=$scriptsPath --inputPath=$inputPath --outputPath=$outputPath
    done
    echo "Finish the execution of the pipelines."
fi
exit 1