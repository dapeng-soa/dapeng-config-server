package com.github.dapeng.socket.client

import java.util
import java.util.concurrent.LinkedBlockingQueue

import com.github.dapeng.socket.AgentEvent
import com.github.dapeng.socket.enums.EventType
import com.github.dapeng.socket.listener.{DeployServerOperations, ServerTimeOperations}
import com.google.gson.Gson
import io.socket.client.{IO, Socket}
import io.socket.emitter.Emitter

object ClientMain {

  def main(args: Array[String]): Unit = {

    val opts = new IO.Options()
    opts.forceNew = true

    val socketClient: Socket = IO.socket("http://127.0.0.1:9095", opts)

    val queue = new LinkedBlockingQueue()
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
    })

    socketClient.connect()
    println(" ssssssss")
  }
}
