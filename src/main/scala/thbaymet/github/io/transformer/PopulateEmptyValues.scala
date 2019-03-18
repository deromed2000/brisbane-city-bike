package thbaymet.github.io.transformer

import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.functions.{col, when}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset}
import thbaymet.github.io.ml.{HasInputCol, HasOutputCol}

/**
  * This transformer populates a column with the another column values
  * if the former is null, keeps the same value otherwise
  *
  * It requires both columns to be present in the given data set schema
  */
class PopulateEmptyValues(override val uid: String) extends Transformer
  with HasInputCol with HasOutputCol {

  override def transform(dataset: Dataset[_]): DataFrame = {
    dataset.withColumn(outputColumn,
      when(col(outputColumn).isNull, col(inputColumn)).otherwise(col(outputColumn)))
  }

  override def copy(extra: ParamMap): Transformer = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = {
    // TODO: Should verify if the given dataset contains the given input column
    require(
      schema.fieldNames.contains(outputColumn),
      s"The given dataset does not contain '$outputColumn' column!"
    )
    schema
  }

  def this() = this(Identifiable.randomUID("populateEmptyValues"))
}
