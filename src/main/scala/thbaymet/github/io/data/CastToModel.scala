package thbaymet.github.io.data

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import thbaymet.github.io.model.CityBike

/**
  * Handles to cast input json data frame to CityBike dataset
  */
object CastToModel {

  /**
    * @param dataFrame data frame which contains CityBike data
    * @param spark spark session provided implicitly
    * @return given data frame as a CityBike dataset
    */
  def apply(dataFrame: DataFrame)(implicit spark: SparkSession): Dataset[CityBike] = {
    // TODO: This casting process should be done properly with Encoders ?
    import spark.implicits._
    dataFrame.as[CityBike]
  }
}
