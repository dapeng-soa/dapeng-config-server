package com.github.test

import java.io.{File, FileWriter}

import com.github.dapeng.socket.server.BuildServerShellInvoker
import com.github.dapeng.vo.YamlVo
import com.google.gson.Gson
import org.yaml.snakeyaml.Yaml

import collection.JavaConverters._
import scala.io.Source

object YamlFileTest {

  def main(args: Array[String]): Unit = {

    val voJson =
      """
        {"lastDeployTime":1532576041,"yamlService":{"name":"zookeeper","image":"zookeeper:3.4.11","env":"soa_jmxrmi_enable: false\r\nsoa_core_pool_size: 100\r\nsoa_zookeeper_host: soa_zookeeper:2181\r\nsoa_monitor_enable: false\r\nsoa_transactional_enable: false\r\nslow_service_check_enable: false\r\n","volumes":"","ports":"","extraHosts":"","composeLabels":"soa_zookeeper_host:soa_zookeeper:2181\nsoa_core_pool_size:100\nsoa_monitor_enable:false\nsoa_transactional_enable:false\nsoa_jmxrmi_enable:false\nslow_service_check_enable:false","dockerExtras":""}}
      """

    val yamlVo = new Gson().fromJson(voJson, classOf[YamlVo])

    val yamlService = yamlVo.getYamlService
    val fields = yamlService.getClass.getDeclaredFields


    val contents = Source.fromString(new Yaml().dump(yamlService))
    contents.getLines().foreach(i => println(i))

//    val yamlDir = new File(new File(classOf[BuildServerShellInvoker].getClassLoader.getResource("./").getPath()), "yamlDir");
//    if (!yamlDir.exists()) {
//      yamlDir.mkdir();
//    }
//
//    val yamlFile = new File(yamlDir.getAbsolutePath, s"zookeeper.yml")
//    yamlFile.setLastModified(yamlVo.getLastDeployTime)
//    val writer = new FileWriter(yamlFile)
//    try {
//      //val content = new Yaml().dump(yamlVo.getYamlService)
//      //writer.write(content)
//      writer.flush();
//    } catch {
//      case e: Exception => println(s" failed to write file.......${e.getMessage}")
//    } finally {
//      writer.close()
//    }
//
//    fields.foreach(field => {
//      val method = yamlService.getClass.getMethod(s"get" + field.getName.charAt(0).toUpper + field.getName.substring(1))
//      val value = method.invoke(yamlService)
//      println(value)
//    })
  }
}
