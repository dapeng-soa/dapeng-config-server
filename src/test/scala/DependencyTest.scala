package scala

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

object DependencyTest {

  val NonOperationPattern = """(.*/(.*?)\.git)@@(.*)""".r
  val OperationPattern = """(.*/(.*?)\.git)@@(.*);(.*)""".r

  def main(args: Array[String]): Unit = {
    val sb = new StringBuilder(64)
    sb.append("project.source=http://pms.today36524.td/central-services/order.git@@orderService;sbt clean compile package docker").append("\r\n")
    sb.append("project.build-depends.3=http://pms.today36524.td/central-services/stock.git@@stockService;sbt clean api/compile api/package api/publishLocal api/publishM2").append("\r\n")
    sb.append("project.depends=mysql,zookeeper,idGenService,memberService").append("\r\n")
    sb.append("project.owner=WPY")

    val result = getBuildServices(sb.toString(), ListBuffer.empty).distinct.reverse
    result.foreach(println(_))
  }


  def getLabel(label: String) = {
    val pos = label.indexOf('=')
    (label.substring(0, pos) -> label.substring(pos + 1))
  }

  def getLabelDetail(labelValue: String) = {

    labelValue match {
      case OperationPattern(gitUrl, projectName, branch, operation) => (gitUrl, projectName, branch, operation)
      case NonOperationPattern(gitUrl, projectName, branch) => (gitUrl, projectName, branch, "")
      case _ => ("", "", "", "")
    }
  }


  def getBuildServices(serviceYmlFile: String, services: mutable.ListBuffer[YamlService]): List[YamlService] = {
    val content = Source.fromString(serviceYmlFile).getLines().toList
    services.append(getSourceService(serviceYmlFile))
    val dependencies = content.filter(_.trim.startsWith("project.build-depends"))
    if (dependencies.isEmpty) {
      val ymlService = getSourceService(serviceYmlFile)
      if (!services.contains(ymlService)) {
        services.append(ymlService)
      }
      services.toList
    } else {
      dependencies.flatMap(d => {
        val (_, _, serviceName, _) = getLabelDetail(d)
        val dependencyService = getServiceYmlByName(serviceName)
        val ymlService = getSourceService(dependencyService)
        if (services.contains(ymlService)) {
          services.toList
        } else {
          services.append(ymlService)
          getBuildServices(dependencyService, services)
        }
      })
    }
  }


  def getSourceService(serviceYmlFile: String) = {
    val content = Source.fromString(serviceYmlFile).getLines().toList
    val source = content.filter(_.trim.startsWith("project.source"))(0)

    val (gitURL, gitName, serviceName, buildOperation) = getLabelDetail(source)
    YamlService(gitURL, gitName, serviceName, buildOperation)
  }


  def getServiceYmlByName(serviceName: String) = {
    serviceName match {
      case "orderService" =>
        val sb = new StringBuilder(64)
        sb.append("project.source=http://pms.today36524.td/central-services/order.git@@orderService;sbt clean compile package docker").append("\r\n")
        sb.append("project.build-depends.3=http://pms.today36524.td/central-services/stock.git@@stockService;sbt clean api/compile api/package api/publishLocal api/publishM2").append("\r\n")
        sb.append("project.depends=mysql,zookeeper,idGenService,memberService").append("\r\n")
        sb.append("project.owner=WPY")
        sb.toString()
      case "stockService" =>
        val sbStock = new StringBuilder(64)
        sbStock.append("project.source=http://pms.today36524.td/central-services/stock.git@@stockService; sbt clean compile package docker").append("\r\n")
        // sbStock.append("project.build-depends.1=http://pms.today36524.td/central-services/goods.git@@goodsService;sbt clean api/compile api/package api/publishLocal api/publishM2").append("\r\n")
        sbStock.append("project.build-depends.2=http://pms.today36524.td/central-services/order.git@@orderService;sbt clean api/compile api/package api/publishLocal api/publishM2").append("\r\n")
        sbStock.append("project.build-depends.3=http://pms.today36524.td/central-services/purchase.git@@purchaseService;sbt clean api/compile api/package api/publishLocal api/publishM2").append("\r\n")
        sbStock.append("project.depends=mysql,zookeeper,idGenService,goodsService,purchaseService,orderService").append("\r\n")
        sbStock.append("project.owner=ZJ")
        sbStock.toString()
      case "purchaseService" =>
        val sbPurchase = new StringBuilder(64)
        sbPurchase.append("project.source=http://pms.today36524.td/central-services/purchase.git@@purchaseService;sbt clean compile package docker").append("\r\n")
        //    sbPurchase.append("project.build-depends.1=http://pms.today36524.td/central-services/supplier.git@@supplierService;sbt clean api/compile api/package api/publishLocal api/publishM2").append("\r\n")
        //    sbPurchase.append("project.build-depends.2=http://pms.today36524.td/central-services/goods.git@@goodsService;sbt clean api/compile api/package api/publishLocal api/publishM2").append("\r\n")
        //    sbPurchase.append("project.build-depends.3=http://pms.today36524.td/central-services/admin.git@@adminService; sbt clean api/compile api/package api/publishLocal api/publishM2").append("\r\n")
        sbPurchase.append("project.depends=mysql,zookeeper,idGenService").append("\r\n")
        sbPurchase.append("project.owner=ZJ")
        sbPurchase.toString()
      case _ => ""
    }

  }


}

case class YamlService(gitURL: String, gitName: String, serviceName: String, buildOperation: String)
