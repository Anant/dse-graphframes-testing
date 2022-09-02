package us.anant.examples


// recommended by oss scc docs
// "Enable Cassandra-specific functions on the SparkContext, SparkSession, RDD, and DataFrame:"
import org.apache.spark.sql.SparkSession
import org.apache.log4j.BasicConfigurator
import us.anant.examples.socialgraph.SocialGraph
// import graph frames classes

object SocialGraphExample extends App {

  val usingOssSpark : Boolean = args(0) == "oss-spark"
  val dseVersion : String = args(1)

  // setup logging
  BasicConfigurator.configure()

  // execute some GremlinOLTP using OSS spark C* connector
  val spark = SparkSession.builder.appName("SocialGraphExample").getOrCreate()

  try {
    // execute some basic queries using java driver
    println("executing graph frame");
    SocialGraph.executeGraphFrameCommands(spark) 
    println("\n==== SUCCESS ====\n");

  } catch {
    case e: Throwable => e.printStackTrace 

  } finally {

    spark.stop()
    sys.exit(0)
  }
}
