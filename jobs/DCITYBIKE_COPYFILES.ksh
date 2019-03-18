#!/bin/ksh
#====================================================================
# Application ... : City-Bike
# Job name ...... : DCITYBIKE_COPYFILES
# File .......... : DCITYBIKE_COPYFILES.ksh
# Description ... : Copy JSON data files from FTP folder to the HDFS
# Authors ....... : @thbaymet
# Language ...... : Shell
#====================================================================

export CB_JOB=0

# Kerberos Key Check

function COPY_CITY_BIKE_DATA_FILES
{

local_folder=ftp
local_temp_folder=${local_folder}/liv
hdfs_in_folder=/user/${USER}/data/in
[[ ! -d ${local_temp_folder} ]] && mkdir ${local_temp_folder}
mv ${local_folder}/*.json ${local_temp_folder}/
hdfs dfs -copyFromLocal ${local_temp_folder}/*.json ${hdfs_in_folder}

if [[ $? -eq 0 ]]; then
    echo "City Bike files are correctly copied to the HDFS"
    # rm -f ${local_temp_folder}/*.json
    CB_JOB=0
else
    echo "Some errors are occurred during City Bike files copy to the HDFS"
    CB_JOB=1
fi

}


# Main

COPY_CITY_BIKE_DATA_FILES

# End
