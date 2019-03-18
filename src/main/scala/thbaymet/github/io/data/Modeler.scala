package thbaymet.github.io.data

import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.sql.Dataset

/**
  * Modeler class to train a k-means clustering model.
  */
object Modeler {

  def apply(dataFrame: Dataset[_], clusters: Int, iterators: Int, seed: Long): KMeansModel = {

    /**
      * Train k-means model
      */
    val kMeans = new KMeans().setK(clusters).setMaxIter(iterators).setSeed(seed)

    kMeans.fit(dataFrame)
  }
}
