package thbaymet.github.io.ml

import org.apache.spark.ml.param.{Param, Params}

/**
  * It handles transformers with a single output column
  */
trait HasOutputCol extends Params {
  val outputCol: Param[String] = new Param[String](this, "outputColumn", "the output column to populate")
  def setOutputColumn(column: String): this.type = set(outputCol, column)
  final def outputColumn: String = $(outputCol)
}
