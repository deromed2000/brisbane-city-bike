package thbaymet.github.io.data

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Applies a feature vector transformation to merge the given multiple columns
  * into the single output column
  */
object Assembler {

  def apply(dataFrame: DataFrame, inputColumns: Array[String], outputColumn: String)
           (implicit spark: SparkSession): DataFrame = {

    require(inputColumns.forall(dataFrame.schema.fieldNames.contains),
      "Given dataset does not contain all input columns to assemble !")

    val vectorAssembler = new VectorAssembler()
      .setInputCols(inputColumns)
      .setOutputCol(outputColumn)

    vectorAssembler.transform(dataFrame)
  }
}
