package thbaymet.github.io.app

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.clustering.KMeansModel
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode}
import thbaymet.github.io.data._
import thbaymet.github.io.model.CityBike
import thbaymet.github.io.spark.SparkConnection

object ModelTrainer extends scala.App with SparkConnection {

  Logger.getRootLogger.setLevel(Level.WARN)

  val logger: Logger = Logger.getLogger(this.getClass)

  val inputDataPath = args(0)
  val dataModelPath = args(1)
  val dataResultPath = args(2)

  // TODO: Should be provided with job parameters ?
  val nbClusters = 2
  val nbIterators = 20
  val nbSeed = 1L

  val stagingData: DataFrame = Reader(inputDataPath)


  /**
    * Cast data frame to CityBike data set
    */
  val cityBikes: Dataset[CityBike] = CastToModel(stagingData)

  /**
    * Normalize data
    */
  val normalizedData: DataFrame = Normalizer(cityBikes.toDF())

  /**
    * Put any other logic implementation here
    */

  val toAssembleCols = Array("latitude", "longitude")
  val featuresCol = "features"
  val clusterColumns = "id" +: toAssembleCols
  val toAssembleData = normalizedData.selectExpr(clusterColumns :_ *).na.drop

  val assembledData = Assembler(toAssembleData, toAssembleCols, featuresCol)

  /**
    * Train k-means model
    */
  val cityBikeModel: KMeansModel = Modeler(assembledData, nbClusters, nbIterators, nbSeed)

  /**
    * Join predictions with the original data before saving
    */
  cityBikeModel.transform(assembledData).selectExpr(Seq("id", "prediction") :_ *)
    .join(normalizedData, Seq("id"))
    .repartition(1)
    .write.mode(SaveMode.Overwrite)
    .option("header", true)
    .option("delimiter", ";")
    .csv(dataResultPath)

  /**
    * Cluster results
    */
  cityBikeModel.clusterCenters

  /**
    * Save calculated city bike model
    */
  cityBikeModel.write.overwrite.save(dataModelPath)
}
