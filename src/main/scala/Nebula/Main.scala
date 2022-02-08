package Nebula

import HelperUtils.ObtainConfigReference
import NebulaScala2.Compiler.{ActorCodeCompiler, MessageCodeCompiler, ToolboxGenerator}
import NebulaScala2.{Compiler, Scala2Main}
import NebulaScala3.Generator.{ActorCodeGenerator, MessageCodeGenerator}
import NebulaScala3.Parser.JSONParser
import NebulaScala3.Scala3Main
import org.slf4j.LoggerFactory

class Main

object Main:
  //Init the config file to get static params
  val config = ObtainConfigReference("nebula") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }
  @main def nebulaMain: Unit =
    println(Scala2Main.scala2Message)
    println(Scala3Main.scala3Message)
    //Get the current Toolbox from the Scala2 APIs
    val toolbox = ToolboxGenerator.generateToolbox()
    //Generate Actors from the JSON schema
    val actorsJson = JSONParser.getActorSchemaFromJson(config.getString("nebula.actorsJsonFile"))
    val messagesJson = JSONParser.getCaseClassSchemaFromJson(config.getString("nebula.messagesJsonFile"))

    toolbox.eval(
      toolbox.parse(
      """case class Film(title: String) extends NebulaScala3.Scala3Main.SomeTrait;
          |NebulaScala3.Scala3Main.theKeeper = Film("Kingdom")""".stripMargin)
    )
    println(NebulaScala3.Scala3Main.theKeeper)
    
    val actorCode = ActorCodeGenerator.generateActorCode(actorsJson(0))
    println(s"xxx = ${Scala3Main.xxx}")
    //Run the code
    ActorCodeCompiler.runCode(actorCode, toolbox)
    Thread.sleep(3000)
    println(s"xxx = ${Scala3Main.xxx}")






