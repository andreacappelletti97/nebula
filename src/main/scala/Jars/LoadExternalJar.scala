package Jars

import java.net.URL
import java.util
import java.util.HashMap
import java.net.URLClassLoader
import java.net.URLClassLoader

object LoadExternalJar extends App {
  val child = new URLClassLoader(Array(new URL("file:///Users/andreacappelletti/hello_java.jar")), this.getClass.getClassLoader)
  // val child = new URLClassLoader(Array[Nothing](myJar.toURI.toURL), this.getClass.getClassLoader)

  val classToLoad = Class.forName("Main", true, child)
  val method = classToLoad.getDeclaredMethod("sayHello", classOf[String])

  val param = new util.HashMap[String, Object]()

  param.put("Mark", 1.asInstanceOf[Integer])
  param.put("Tinka", 2.asInstanceOf[Integer])
  // val instance = classToLoad.newInstance
  val result = method.invoke(classToLoad.getDeclaredConstructor().newInstance(), "Andrea")
  println(result)

}
