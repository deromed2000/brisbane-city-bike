# Brisbane-City-Bike

## About
Brisbane-City-Bike is a Big Data platform project based on Spark/Scala. It processes JSON data
files in order to provide City Bike data clustering.

## Code
It is a common maven based spark/scala project. Short description of the project root folders:

- **[data](data)** contains input and output files to test the job on a local machine
- **[jobs](jobs)** contains KSH scripts to handle job executions on the valid/production environments
- **[parm](parm)** contains properties file to define job submit parameters
- **[src](src)** contains source code of the spark job

## Test input
The input JSON files are in the [data/in/](data/in/) folder, outside of the project source folder.
On the real application mode, input JSON data files should be on the HDFS /user/${USER}/data/in/
folder.

## Test model
For the sake of this test, model binary files are in [data/model/](data/model/) folder.
You should not keep them on the git repository.
On the real cluster mode, model will be saved on the HDFS /user/${USER}/data/model folder.

## Test output data
Output data is stored under [data/out/](data/out/) folder.
On the real cluster mode, output data will be saved on the HDFS /user/${USER}/data/out/ folder.

## How to run the project on local, main App class in an IDE

### Spark parameters/options
Only spark master option is required to provide in order to run the
job in an IDE.

Simply provide spark parameters as VM options
Ex:
```ruby
-Dspark.master=local -Dspark.app.name="Brisbane-City-Bike"
```

### Program arguments

Provide job specific parameters as "Program arguments" on your IDE
Ex:
```ruby
data/in/* data/model data/out/
```

Description of job arguments

* data/in/* Path to the input file folder
* data/model Path to store model
* data/out/ Path to save output data


## How to test the job on the DEV cluster
Please use job scripts under [jobs](jobs) folder to run your spark job on a dev cluser.

- Use [Model Trainer](src/main/scala/thbaymet/github/io/app/ModelTrainer.scala) class to train a model
- You can use [App](src/main/scala/thbaymet/github/io/app/App.scala) class to process data with already
prepared model


### How to build the JAR file
As this is a maven project, run maven package/install commands to build the JAR
You should use any production deployment methods (Jenkins, Nexus, Docker) to deploy the delivery on
validation/production.


## Go on validation and then on production
- Ask to create (on your Quality Center Management) two AutoSys jobs to handle file copy and data processing
jobs defined under [jobs](jobs) folder.
- Deploy job files (ksh), parm files, and spark jar on your environment (your edge node to where spark jobs
 are submitted).
- Ask to activate AutoSys scheduler.


## To do
* JSON files should be copied to HDFS (ex: ${INPUT_FOLDER}) before launching processing
* Data read from each file may be stored in a staging partitioned (by import date, etc.) table
* Any JSON file processed successfully should be marked as "correctly processed" file
* Any JSON file not processed succesfully should be marked as "corrupted" file
* Processed file should be moved to an out folder (ex: /user/${USER}/data/out/processed &
/user/${USER}/data/out/rejected)

* Any Spark, Hadoop, etc. dependencies should be set "provided" scope to prevent large jar file
* Create a common project as a library in order to keep common functions/implements and use it as a dependency in this project.
* Create a package object for each package and provide scala documentation
* Implement maximum spark/scala unit tests possible to prevent most of possible issues about file reading,
corrupted json files, more data normalizing.
* Code should be adapted to verify if there is already a model on the given path, create one if there is not
* A schedular option must be chosen (Oozie, Autosys) in order to handle pipeline orchestration on
validation/production environments
