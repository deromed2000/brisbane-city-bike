#!/bin/ksh
#====================================================================
# Application ... : City-Bike
# Job name ...... : DCITYBIKE_PROCESS
# File .......... : DCITYBIKE_PROCESS.ksh
# Description ... : Run spark job to process City Bike json files
# Authors ....... : @thbaymet
# Language ...... : Shell
#====================================================================

export CB_JOB=0

# Kerberos Key Check


# Job parameters
export SPARK_APP_NAME=Brisbane-City-Bike
export SPARK_CONF_PROPERTIES_FILE=parm/brisbane-city-bike.spark.properties
export SPARK_MASTER=yarn
export SPARK_DEPLOY_MODE=cluster
export JAR_FILE=bin/brisbane-city-bike.jar
export INPUT_FOLDER=/user/${USER}/data/in/*
export MODEL_FOLDER=/user/${USER}/data/model/
export OUTPUT_FOLDER=/user/${USER}/data/out/



function PROCESS_MODEL_TRAINER
{

date

spark-submit \
    --class thbaymet.github.io.app.ModelTrainer \
    --properties-file=${SPARK_CONF_PROPERTIES_FILE} \
    --conf spark.master=${SPARK_MASTER} \
    --conf spark.deployMode=${SPARK_DEPLOY_MODE} \
    --conf spark.app.name=${SPARK_APP_NAME} \
    --verbose \
    ${JAR_FILE} ${INPUT_FOLDER} ${MODEL_FOLDER} ${OUTPUT_FOLDER} \
    2>&1

date

if [[ $? -eq 0 ]]; then
    echo "City Bike model trainer job successfully finished"
    CB_JOB=0
else
    echo "Some errors are occurred during City Bike model trainer job execution"
    CB_JOB=1
fi

}

function PROCESS_DATA_PROCESSING
{

date

spark-submit \
    --class thbaymet.github.io.app.App \
    --properties-file=${SPARK_CONF_PROPERTIES_FILE} \
    --conf spark.master=${SPARK_MASTER} \
    --conf spark.deployMode=${SPARK_DEPLOY_MODE} \
    --conf spark.app.name=${SPARK_APP_NAME} \
    --verbose \
    ${JAR_FILE} ${INPUT_FOLDER} ${MODEL_FOLDER} ${OUTPUT_FOLDER} \
    2>&1

date

if [[ $? -eq 0 ]]; then
    echo "City Bike data processing job successfully finished"
    CB_JOB=0
else
    echo "Some errors are occurred during City Bike data processing job execution"
    CB_JOB=1
fi

}


# Main

# Things to do before the job submit

# Model training is one shot running
# PROCESS_MODEL_TRAINER

# Data processing will run as a daily job
PROCESS_DATA_PROCESSING

# End
