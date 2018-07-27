package com.github.dapeng.socket.client

import java.io.{File, FileWriter}
import java.util.concurrent.LinkedBlockingQueue

import com.github.dapeng.socket.entity.DockerVo
import com.github.dapeng.socket.enums.EventType
import com.github.dapeng.socket.listener.{DeployServerOperations, ServerTimeOperations}
import com.github.dapeng.socket.server.BuildServerShellInvoker
import com.github.dapeng.vo.YamlVo
import com.google.gson.Gson
import io.socket.client.{IO, Socket}
import io.socket.emitter.Emitter
import org.yaml.snakeyaml.{DumperOptions, Yaml}

import scala.io.Source

object ClientMain {

  def main(args: Array[String]): Unit = {

    val opts = new IO.Options()
    opts.forceNew = true

    val socketClient: Socket = IO.socket("http://127.0.0.1:9095", opts)

    val queue = new LinkedBlockingQueue[String]()
    val cmdExecutor = new CmdExecutor(queue, socketClient)

    new Thread(cmdExecutor).start()

    socketClient.on(Socket.EVENT_CONNECT, new Emitter.Listener {
      override def call(args: AnyRef*): Unit = {
        println(s" connected......clientId: ${socketClient.id()}...")

        socketClient.emit(EventType.NODE_REG.name, "app1:127.0.0.1")
      }
    }).on(EventType.GET_SERVER_TIME.name, new ServerTimeOperations(queue,socketClient)).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
      override def call( args: AnyRef*) {
        println(" disconnected ........")
      }
    }).on("webCmd", new DeployServerOperations(queue,socketClient)).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
      override def call( args: AnyRef*) {
        println(" disconnected ........")
      }
    }).on(EventType.DEPLOY.name, new Emitter.Listener {
      override def call(objects: AnyRef*): Unit = {
        val voString = objects(0).asInstanceOf[String];
        val vo = new Gson().fromJson(voString, classOf[DockerVo])
        val yamlDir = new File(new File(classOf[BuildServerShellInvoker].getClassLoader.getResource("./").getPath()), "yamlDir");
        if (!yamlDir.exists()) {
          yamlDir.mkdir();
        }

        val yamlFile = new File(yamlDir.getAbsolutePath, s"${vo.getDockerYaml.getServices.keySet().iterator().next()}.yml")
        yamlFile.setLastModified(vo.getLastDeployTime)
        val writer = new FileWriter(yamlFile)
        try {
          val dumperOptions = new DumperOptions
          dumperOptions.setPrettyFlow(true)
          val content = new Yaml(dumperOptions).dump(vo.getDockerYaml)
          val finalContent = Source.fromString(content).getLines().filterNot(_.startsWith("!!")).filterNot(_.contains("null"))

          finalContent.foreach(i => {
            writer.write(i)
            writer.write("\n")
          })

          writer.flush();
        } catch {
          case e: Exception => println(s" failed to write file.......${e.getMessage}")
        } finally {
          writer.close()
        }

        //exec cmd.....
        val cmd = s"${EventType.DEPLOY.name.toLowerCase()} -f ${yamlDir.getAbsolutePath}/${vo.getDockerYaml.getServices.keySet().iterator().next()}.yml up -d"
        queue.put(cmd)
      }
    })

    socketClient.connect()
  }


  def writeYmlFile(yamlVo: YamlVo) = {

  }
}
