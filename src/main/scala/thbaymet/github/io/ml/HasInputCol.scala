package thbaymet.github.io.ml

import org.apache.spark.ml.param.{Param, Params}

/**
  * It handles transformers with a single input column
  */
trait HasInputCol extends Params {
  val inputCol: Param[String] = new Param[String](this, "inputColumn", "the input column to check")
  def setInputColumn(column: String): this.type = set(inputCol, column)
  final def inputColumn: String = $(inputCol)
}
