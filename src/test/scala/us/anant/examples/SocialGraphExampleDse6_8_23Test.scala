package us.anant.examples

import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfterAll
//import com.holdenkarau.spark.testing.SharedSparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext
import org.apache.log4j.BasicConfigurator
import us.anant.examples.socialgraph.SocialGraph

import collection.mutable.Stack
import org.scalatest.flatspec.AnyFlatSpec

//class SocialGraphExampleDse6_8_23Test extends AnyFunSuite with SharedSparkContext {
class SocialGraphExampleDse6_8_23Test extends AnyFunSuite with BeforeAndAfterAll {
  val usingOssSpark : Boolean = false
  val dseVersion : String = "6.8.23"

  var spark : SparkSession = _  
  var sc : SparkContext = _
  //var sc : SparkContext = spark.sparkContext
  
  val sparkMaster = sys.env.getOrElse("SPARK_MASTER_TEST", "local[*]")

  override def beforeAll(){
    spark = SparkSession.builder
      .appName("SocialGraphExample")
      .master(sparkMaster)
      .getOrCreate()
    //spark = SparkSession.builder.appName("SocialGraphExample").getOrCreate()
    sc = spark.sparkContext

    // TODO drop any existing graph so start with clean db
  }

  //test("Test Cassandra operations from Java driver and scc") {
  //  println("running some cql and dataframe operations");
  //  testCQL(spark, sc, session, usingOssSpark)
  //}

  test("example test") {
    val list = List(1, 2, 3, 4)
    val rdd = sc.parallelize(list)

    assert(rdd.count === list.length)
  }

  test("Gremlin Spark Jobs ran from DSE GraphFrames") {
    println("executing graph frame");
    SocialGraph.executeGraphFrameCommands(spark) 
  }

  override def afterAll() {
    spark.stop()
  }
}

// class SocialGraphExampleTest extends AnyFunSuite with SharedSparkContext {
//   // https://github.com/holdenk/spark-testing-base/wiki/SharedSparkContext
// 
//   test("test initializing spark context") {
//     val list = List(1, 2, 3, 4)
//     val rdd = sc.parallelize(list)
// 
//     assert(rdd.count === list.length)
//   }
// }
