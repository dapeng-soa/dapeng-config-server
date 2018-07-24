/*
package com.github.dapeng.socket

import java.util
import java.util.{Iterator, Map, UUID}
import java.util.concurrent.{ConcurrentHashMap, LinkedBlockingQueue}

import com.corundumstudio.socketio.listener.DataListener
import com.corundumstudio.socketio.{AckRequest, Configuration, SocketIOClient, SocketIOServer}
import com.deploy.{AgentEvent, HostAgent}

import collection.JavaConverters._

object DeployServerMain {

  val port = 9095

  def main(args: Array[String]): Unit = {

    val configuration = new Configuration()
    configuration.setPort(port)
    configuration.setAllowCustomRequests(true)

    val agentMap = new ConcurrentHashMap[String, HostAgent]()
    val server = new SocketIOServer(configuration);
    val msgQueue = new LinkedBlockingQueue()


    server.addConnectListener(socket => println(s" ${socket.getRemoteAddress} => join room ${socket.getSessionId}"))

    server.addDisconnectListener(socket => {
      if (agentMap.containsKey(socket.getSessionId.toString)) {
        socket.leaveRoom("")
      }
    })

    server.addEventListener("nodeReg", classOf[String], new DataListener[String]() {
      override def onData(client: SocketIOClient, data: String, ackRequest: AckRequest): Unit = {
        client.joinRoom("nodes")
        System.out.println("nodes Reg")
        val name = data.split(":")(0)
        val ip = data.split(":")(1)
        agentMap.put(client.getSessionId.toString, new HostAgent(name, ip, client.getSessionId.toString))
        notifyWebClients(agentMap, server)
      }
    })

    server.addEventListener("webReg", classOf[AgentEvent], new DataListener[AgentEvent]() {
      override def onData(client: SocketIOClient, data: AgentEvent, ackRequest: AckRequest): Unit = {
        client.joinRoom("web")
        println(" step into web reg.................")
        client.sendEvent("serverList", agentMap.values())
      }
    })

    //收到web指令，获取当前node节点，发布事件
    server.addEventListener("webEvent", classOf[AgentEvent], new DataListener[AgentEvent]() {
      override def onData(client: SocketIOClient, data: AgentEvent, ackRequest: AckRequest): Unit = {
        println(s" client: ${client.getSessionId}")
        println(s" received event: ${data}")
        val clientSessionIds = data.clientSessionIds
        clientSessionIds.foreach(clientId => {
          val socketClient = server.getClient(UUID.fromString(clientId.trim))
         // socketClient.sendEvent()
        })
      }
    })

    server.start()

    val cmdExecutor = new CmdExecutor(msgQueue, server)

    new Thread(cmdExecutor).start()
    Thread.sleep(Integer.MAX_VALUE)

    server.stop()

  }

  private def notifyWebClients(map: util.Map[String, HostAgent], server: SocketIOServer): Unit = {
    val clients = map.asScala.map(_._2)
    println(s" ar: ${clients}")
    if (clients.size >= 0 && server.getRoomOperations("web").getClients.size > 0) server.getRoomOperations("web").sendEvent("serverList", clients)
  }
}
*/
