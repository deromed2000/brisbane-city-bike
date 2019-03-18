package thbaymet.github.io.transformer

import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions.col
import org.scalatest.FunSuite

class PopulateEmptyValuesTest extends FunSuite with DataFrameSuiteBase {

  Logger.getRootLogger.setLevel(Level.WARN)

  import sqlContext.implicits._

  test("test PopulateEmptyValues") {

    val schema = Seq("coordinates", "latitude")

    val inputData = Seq(
      ("12.34", null),
      ("12.34", "13.24"),
      ("null", "13.24")
    )

    val expectedData = Seq(
      ("12.34", "12.34"),
      ("12.34", "13.24"),
      ("null", "13.24")
    )

    val inputDf = sc.parallelize(inputData).toDF(schema:_*).orderBy(schema.map(col):_*)
    val expectedDf = sc.parallelize(expectedData).toDF(schema:_*).orderBy(schema.map(col):_*)

    val actualDf = new PopulateEmptyValues()
      .setInputColumn("coordinates")
      .setOutputColumn("latitude")
      .transform(inputDf)

    inputDf.show()
    expectedDf.show()
    actualDf.show()

    assertDataFrameEquals(actualDf, expectedDf)

  }
}
