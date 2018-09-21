package com.github.dapeng.util

import com.github.dapeng.repository.deploy.ServiceRepository
import com.github.dapeng.socket.entity.YamlServiceVo
import org.springframework.beans.factory.annotation.Autowired

import scala.io.Source
import scala.util.matching.Regex
import collection.JavaConverters._

/**
  * @author with struy.
  *         Create by 2018/9/21 13:47
  *         email :yq1724555319@gmail.com
  */

class BuildServerUtil {

  @Autowired
  def setServiceRep(serviceRep: ServiceRepository): Unit = {
    BuildServerUtil.serviceRep = serviceRep
  }
}

object BuildServerUtil {

  var serviceRep: ServiceRepository = _
  val NonOperationPattern: Regex = """(.*/(.*?)\.git)@@(.*)""".r
  val OperationPattern: Regex = """(.*/(.*?)\.git)@@(.*);(.*)""".r

  def getLabel(label: String) = {
    val pos = label.indexOf('=')
    label.substring(0, pos) -> label.substring(pos + 1)
  }

  def getLabelDetail(labelValue: String) = {

    labelValue match {
      case OperationPattern(gitUrl, projectName, branch, operation) => (gitUrl, projectName, branch, operation)
      case NonOperationPattern(gitUrl, projectName, branch) => (gitUrl, projectName, branch, "")
      case _ => ("", "", "", "")
    }
  }


  def getBuildServices(serviceYmlFile: String, services: java.util.ArrayList[YamlServiceVo]): List[YamlServiceVo] = {
    val content = Source.fromString(serviceYmlFile).getLines().toList
    services.add(getSourceService(serviceYmlFile))
    val dependencies = content.filter(_.trim.startsWith("project.build-depends"))
    if (dependencies.isEmpty) {
      val ymlService = getSourceService(serviceYmlFile)
      if (!services.contains(ymlService)) {
        services.add(ymlService)
      }
      services.asScala.toList
    } else {
      dependencies.flatMap(d => {
        val (_, _, serviceName, _) = getLabelDetail(d)
        val dependencyService = serviceRep.findByName(serviceName)
        if (null == dependencyService) {
          services.asScala.toList
        } else {
          val labels = dependencyService.get(0).getComposeLabels
          val ymlService = getSourceService(labels)
          if (services.contains(ymlService)) {
            services.asScala.toList
          } else {
            services.add(ymlService)
            getBuildServices(labels, services)
          }
        }
      })
    }
  }

  def getSortedBuildServices(serviceYmlFile: String, services: java.util.ArrayList[YamlServiceVo]): java.util.List[YamlServiceVo] = {
    getBuildServices(serviceYmlFile, services).distinct.reverse.filterNot(_.getServiceName.isEmpty).asJava
  }


  def getSourceService(serviceYmlFile: String): YamlServiceVo = {
    val content = Source.fromString(serviceYmlFile).getLines().toList
    val source = content.filter(_.trim.startsWith("project.source"))(0)

    val (gitURL, gitName, serviceName, buildOperation) = getLabelDetail(source)
    val serviceVo = new YamlServiceVo()
    serviceVo.setGitURL(gitURL)
    serviceVo.setGitName(gitName)
    serviceVo.setServiceName(serviceName)
    serviceVo.setBuildOperation(buildOperation)
    serviceVo
  }
}
