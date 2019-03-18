package thbaymet.github.io.spark

import org.apache.spark.sql.SparkSession

/**
  * All classes in this project should use this trait to perform its spark
  * connection with the cluster.
  */
trait SparkConnection {

  /**
    * Spark session which will be used in extended classes
    * as an entry point to spark cluster
    *
    * @note Do not provide any parameters as they should be given from
    *       an external properties file.
    */
  implicit val spark: SparkSession = SparkSession.builder().getOrCreate()

}
