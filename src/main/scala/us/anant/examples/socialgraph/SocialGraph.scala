package us.anant.examples.socialgraph

//import com.datastax.driver.dse.DseSession
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import com.datastax.bdp.graph.spark.graphframe.{DseGraphFrame, DseGraphTraversal}
import org.apache.tinkerpop.gremlin.structure.Vertex
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.addV
import com.datastax.bdp.graph.spark.graphframe._
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext



object SocialGraph {

  /**
   * V
   */
  val EmployeeLabel = "employeeVertex"

  /**
   * E
   */
  val Follows = "follows"
  val Friend = "friend"

  /**
   * Properties
   */
  val EmpId = "empId"
  val Name = "name"
  val Age = "age"

  val graphName = "social_graph_example"
//  val graphOptions = new GraphOptions().setGraphName(graphName)


  def readData(g: DseGraphFrame): Unit = {

      val youngEmployees = g.V().has(EmployeeLabel, Age, 25).valueMap().toList()

      println("Found employees age 25:")
      println(youngEmployees.toString)

      // use graph frame syntax
      // https://docs.datastax.com/en/dse/6.7/dse-dev/datastax_enterprise/graph/graphAnalytics/dseGraphFrameOverview.html#UsingDseGraphFrame
      
      println("Found people (a) who are related (e) to someone (b) who follows (e2) someone else (c):")
      val followers = g.find("(a)-[e]->(b); (b)-[e2]->(c)").filter(s"e2.`~label` = '$Follows'").select(s"a.$Name", "e.`~label`", s"b.$Name", "e2.`~label`", s"c.$Name").distinct.show

  }

  /**
   * NOTE this compiles, but might not work as written, due to runtime error: java.lang.UnsupportedOperationException: Row to Vertex conversion is not supported: Use .df().collect() instead of the iterator
   *
   */ 
  def addEmployeeVertex(g: DseGraphFrame, e: Employee): Vertex = {
    // for graph frame, can execute limited traversals from the DseGraphTraversal returned by g.V()
    val dgt : DseGraphTraversal[Vertex] = g.V()

    dgt.addV(EmployeeLabel)
      .property(EmpId, e.empId)
      .property(Name, e.name)
      .property(Age, e.age)
      .next()
  }

  /**
    * To do addV and addE from a DseGraphFrame, need to first call g.V() or g.E() to get a DseGraphTraversal. 
    * See https://docs.datastax.com/en/dse-graphframe-api/6.7/#com.datastax.bdp.graph.spark.graphframe.DseGraphFrame
    **/
  def addEmployeesRelation(g: DseGraphFrame,
                           e1: Employee,
                           e2: Employee,
                           relation: String,
                           eMap: Map[Employee, Vertex]): Unit = {
    val a = "a"
    val b = "b"

    val e1v = eMap(e1)
    val e2v = eMap(e2)

    // for graph frame, can execute limited traversals from the DseGraphTraversal returned by g.V()
    val dgt : DseGraphTraversal[Vertex] = g.V()

    dgt.V().V(e1v.id()).as(a).V(e2v.id()).as(b).addE(relation).from(a).to(b).iterate()
  }

  def generateData(): (Seq[Employee], Seq[Relation]) = {
    val alice = Employee(1, "Alice", 29)
    val bob = Employee(2, "Bob", 26)
    val charlie = Employee(3, "Charlie", 25)
    val dylan = Employee(4, "Dylan", 31)
    val eli = Employee(5, "Eli", 33)

    val employees = Seq(alice, bob, charlie, dylan, eli)
    val relations = Seq(
      Relation(alice, bob, Friend),
      Relation(charlie, bob, Follows),
      Relation(charlie, alice, Follows),
      Relation(dylan, eli, Friend),
      Relation(dylan, eli, Follows),
      Relation(eli, dylan, Follows),
      Relation(charlie, eli, Follows),
      Relation(charlie, dylan, Follows)
    )

    (employees, relations)
  }

  // TODO
  def updateData(g: DseGraphFrame): Unit = {
  }

  def deleteData(g: DseGraphFrame): Unit = {
    // dropping young people

    g.V().has(EmployeeLabel, Age, 25).drop().iterate()
  }

  /**
   * Execute using DSE graph frames
   *
   *
   */
  def executeGraphFrameCommands (spark : SparkSession) : Unit = {
    try {
      println("Starting: executeGraphFrameCommands")
      

      println("instantiating graph frame obj")
      val dgf : DseGraphFrame = spark.dseGraph(SocialGraph.graphName)


      // TODO writing new vertices doesn't seem to be a common graph frames use case, so while there's probably a way to do it, skipping for now. 
      // println("writing using graph frames")
      // SocialGraph.writeData(dgf)
      println("reading using graph frames")
      SocialGraph.readData(dgf)

      // TODO 
      // SocialGraph.updateData(dgf)

      println("deleting using graph frames")
      SocialGraph.deleteData(dgf)
      println("done executing operations using graph frames")
    } catch {
      case e: Throwable => e.printStackTrace 
      throw e

    } finally {
      println("Done: executeGraphFrameCommands")
    }
  }
}
