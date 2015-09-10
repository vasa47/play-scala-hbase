package controllers

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException
import play.api.mvc.{Action, Controller}
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, HBaseConfiguration}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import play.api.Logger
import play.api.libs.json.Json
import java.util.UUID
import scala.collection.JavaConversions._
import scala.sys.process.processInternal.IOException

object Application extends Controller {

  val barsTableName = "Cars"
  val family = Bytes.toBytes("vi") // vehicle information
  val qualifier = Bytes.toBytes("detailsObject")  // json object of vehicle Info
  
  lazy val hbaseConfig = {
    val conf = HBaseConfiguration.create()
    val hbaseAdmin = new HBaseAdmin(conf)
    
    // create a table in HBase if it doesn't exist
    if (!hbaseAdmin.tableExists(barsTableName)) {
      val desc = new HTableDescriptor(barsTableName)
      desc.addFamily(new HColumnDescriptor(family))
      hbaseAdmin.createTable(desc)
      Logger.info("Cars table created")
    }
    
    // return the HBase config
    conf
  }

  def index = Action {
    // return the server-side generated webpage from app/views/index.scala.html
    Ok(views.html.index("Play Framework + Scala + Hbase"))
  }

  // Function to add Cars

  def addBar() = Action(parse.json) { request =>
    // create a new row in the table that contains the JSON sent from the client
    val table = new HTable(hbaseConfig, barsTableName)
    val put = new Put(Bytes.toBytes(UUID.randomUUID().toString))
    put.add(family, qualifier, Bytes.toBytes(request.body.toString()))
    table.put(put)
    table.close()
    Ok
  }

  // function to get View of Cars.
  def getBars = Action {
    // query the table and return a JSON list of the bars in the table
    val table = new HTable(hbaseConfig, barsTableName)
    val scan = new Scan()
    scan.addColumn(family, qualifier)
    val scanner = table.getScanner(scan)
    val results = try {
      scanner.toList.map {result =>
        Json.parse(result.getValue(family, qualifier))
      }
    } finally {
      scanner.close()
      table.close()
    }
    Ok(Json.toJson(results))
  }

 // Function to Modify Car if exist by id.
  def modifyCar() = Action(parse.json) {  request =>
    val table = new HTable(hbaseConfig, barsTableName)
    val rowId = request.getQueryString("carId").toString
    // Just check for the existence of the Car and Report Error if not there
    val carData = new Get(Bytes.toBytes(rowId))
    val carModifydata = table.get(carData)
    try {
      if(!(carModifydata == null || carModifydata.isEmpty())) {

        val put = new Put(Bytes.toBytes(request.getQueryString("carId").toString))
        put.add(family, qualifier, Bytes.toBytes(request.body.toString()))
        table.put(put)
        table.close()

      }else {
        // return error log error
        println("Expection happens")
      }

    }catch {
      case e: NullPointerException => println(e.printStackTrace())
    }

    Ok()
  }

  // function to delete Car by ID
    def deleteCar() = Action(parse.json) { request =>
    val table = new HTable(hbaseConfig, barsTableName)
    val rowId = request.getQueryString("carId").toString
    // Just check for the existence of the Car and Report Error if not there
    val carData = new Get(Bytes.toBytes(rowId))
    val carModifydata = table.get(carData)

    try {
      if(!(carModifydata == null || carModifydata.isEmpty())){
        val deleteCar = new Delete(Bytes.toBytes(rowId))
        table.delete(deleteCar)
        table.close()
      }
    }catch{
      case e: NullPointerException => print(e.printStackTrace())
      case e: IOException => print(e.printStackTrace())
    }
    Ok
    }


  // function to get a car by ID.
  def getOneCar() = Action(parse.json) { request =>

    val table = new HTable(hbaseConfig, barsTableName)
    val rowId = request.getQueryString("carId").toString
    // Just check for the existence of the Car and Report Error if not there
    val carData = new Get(Bytes.toBytes(rowId))
    val carModifydata = table.get(carData)
    if((carModifydata == null || carModifydata.isEmpty())) {
      Ok("Not found")

    }
    val getCarData = new Get(Bytes.toBytes(rowId))
    val carDataList = table.get(getCarData)
    val results = carDataList.getColumn(family, qualifier)
    table.close()


    Ok(Json.toJson(results))
  }
}