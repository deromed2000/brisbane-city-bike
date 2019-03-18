package thbaymet.github.io.data

import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.{DataFrame, SparkSession}
import thbaymet.github.io.transformer.PopulateEmptyValues

/**
  * Handles simple clean, normalize, filter transformations on
  * the given data frame.
  */
object Normalizer {

  /**
    * Applies customer data transformations in order to clean, normalize data
    *
    * @param dataFrame the given data frame
    * @param spark spark session should given implicitly
    * @return the given data frame cleaned, normalized
    */
  def apply(dataFrame: DataFrame)(implicit spark: SparkSession): DataFrame = {

    /**
      * To fill latitude 'null' values with coordinates.latitude values
      */
    val latitudePopulate = new PopulateEmptyValues()
      .setInputColumn("coordinates.latitude")
      .setOutputColumn("latitude")

    /**
      * To fill longitude 'null' values with coordinates.longitude values
      */
    val longitudePopulate = new PopulateEmptyValues()
      .setInputColumn("coordinates.longitude")
      .setOutputColumn("longitude")

    /**
      * Put all transformers in a single array
      */
    val transformers = Array(latitudePopulate, longitudePopulate)

    /**
      * Create and apply spark ml pipeline to transform the given data frame
      */
    val populatedData = new Pipeline().setStages(transformers).fit(dataFrame).transform(dataFrame)

    /**
      * Drop set of useless columns
      * @note As there are only some data with coordinates column
      *       we decided to get rid of this column.
      */
    val columnsToDrop = Seq("coordinates")
    populatedData.drop(columnsToDrop :_ *)
  }
}
