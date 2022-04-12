package Nebula

import GUI.MainActivity

object KeyBoardInterrupt extends App {
  val serverResponseDelayTimeMs = 0
  val serverPort = 5150

  val ui = new MainActivity
  ui.visible = true
  println("End of main function")

  Runtime.getRuntime().addShutdownHook(new Thread {
    override def run = {
      System.out.println("Shutdown hook ran")
      // if (socket != null) socket.close()
      Thread.sleep(1000)
      print4ever()
    }
  })

  def print4ever() = {
    while (true) {
      System.out.println("Hello, world!")
      Thread.sleep(1000)
    }
  }


}
