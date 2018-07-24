package com.github.dapeng.socket.client

import java.util.concurrent.LinkedBlockingQueue

import com.github.dapeng.socket.enums.EventType
import io.socket.client.{IO, Socket}
import io.socket.emitter.Emitter

object ClientMain {

  def main(args: Array[String]): Unit = {

    val opts = new IO.Options()
    opts.forceNew = true

    val socketClient = IO.socket("http://172.16.16.250:9095", opts)

    val queue = new LinkedBlockingQueue()
    val cmdExecutor = new CmdExecutor(queue, socketClient)

    socketClient.on(Socket.EVENT_CONNECT, new Emitter.Listener {
      override def call(args: AnyRef*): Unit = {
        println(s" connected......clientId: ${socketClient.id()}...")

        socketClient.emit(EventType.NODE_REG.name, "app1:172.28.109.225")
      }
    })

    socketClient.connect()

  }
}
