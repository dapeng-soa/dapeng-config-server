package com.github.test

import java.io.{File, FileWriter}
import java.util

import com.github.dapeng.socket.server.BuildServerShellInvoker
import com.github.dapeng.vo.YamlVo
import com.google.gson.Gson
import org.yaml.snakeyaml.Yaml

import collection.JavaConverters._
import scala.io.Source

object YamlFileTest {

  def main(args: Array[String]): Unit = {
    testDockerYaml()
  }


  /**
    * redis:
    * container_name: redis
    * image: dapengsoa/redis-wzx-alpine:3.2.10
    * restart: on-failure:3
    * environment:
    *       - LANG=zh_CN.UTF-8
    *       - TZ=CST-8
    * ports:
    *       - "${redis_port}:${redis_port}"
    * command: redis-server /etc/redis/redis.conf
    * volumes:
    *       - /home/today/tscompose/config/redis_config:/etc/redis
    * labels:
    *       - project.source=
    *       - project.depends=
    *       - project.extra=public-image
    *       - project.owner=Ever
    */
  def testDockerYaml() = {
    val dockerYaml = new DockerYaml()
    val service = new Service()
    service.setImage("dapengsoa/redis-wzx-alpine:3.2.10")
    service.setContainer_name("redis")
    service.setRestart("on-failure:3")

    val env = new util.HashMap[String,String]()
    env.put("LANG", "zh_CN.UTF-8")
    env.put("TZ", "CST-8")
    service.setEnvironment(env)

    val ports = new util.ArrayList[String]()
    ports.add("3306:3306")
    service.setPorts(ports)

    service.setCommand("redis-server /etc/redis/redis.conf")

    val volumes = new util.ArrayList[String]()
    volumes.add("/home/today/tscompose/config/redis_config:/etc/redis")
    service.setVolumes(volumes)
    val labels = new util.ArrayList[String]()
    labels.add("project.extra=public-image")
    labels.add("project.source=")
    service.setLabels(labels)

    dockerYaml.setVersion("2")

    val serviceMap = new util.HashMap[String, Service]()
    serviceMap.put("redis", service)
    dockerYaml.setServices(serviceMap)
    val result = new Yaml().dump(dockerYaml)

    val content = Source.fromString(result)

    val yamlDir = new File(new File(classOf[BuildServerShellInvoker].getClassLoader.getResource("./").getPath()), "yamlDir");
    if (!yamlDir.exists()) {
      yamlDir.mkdir();
    }

    new Yaml().represent()

    val yamlFile = new File(yamlDir.getAbsolutePath, s"redis.yml")
    val fileWriter = new FileWriter(yamlFile)

    val newContent: Iterator[String] = content.getLines().filterNot(_.startsWith("!!"))
    newContent.foreach(i => {
      fileWriter.write(i)
      fileWriter.write("\n")
    })

    fileWriter.flush()
    fileWriter.close()
  }
}
