package thbaymet.github.io.data

import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Handles JSON data read with the minimum data loose by
  * providing a known data schema
  */
object Reader {

  /**
    * @param path path to the json data file folder
    * @param spark spark session should be provided implicitly
    */
  def apply(path: String)(implicit spark: SparkSession): DataFrame = {

    /**
      * City bike JSON data fields
      */
    val id = StructField("id", LongType)
    val name = StructField("name", StringType)
    val address = StructField("address", StringType)
    val latitude = StructField("latitude", DoubleType)
    val longitude = StructField("longitude", DoubleType)
    val coordinate = StructField("coordinates", (new StructType).add(latitude).add(longitude))
    val position = StructField("position", StringType)

    /**
      * City bike JSON data schema
      */
    val fields = Seq(id, name, address, latitude, longitude, coordinate, position)
    val schema = StructType(fields)

    /**
      * Read json files from the given path
      */
    spark.read.schema(schema).option("multiLine", value = true).json(path)
  }
}
