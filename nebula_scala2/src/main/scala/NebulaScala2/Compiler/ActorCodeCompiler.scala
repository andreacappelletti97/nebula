package NebulaScala2.Compiler

import NebulaScala2.SimpleActor

import scala.tools.reflect.ToolBox
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.lightbend.cinnamon.akka.CinnamonMetrics
import com.lightbend.cinnamon.metric.Counter
import com.lightbend.cinnamon.metric.Recorder
import com.lightbend.cinnamon.metric.GaugeDouble
import com.lightbend.cinnamon.metric.GaugeLong
import com.lightbend.cinnamon.metric.Rate

class ActorCodeCompiler

object ActorCodeCompiler {

  //This function compile the Scala string code and returns the Actors props as a Seq compiled in the same Toolbox
  def compileActors(actorCodeList : Seq[String] ,toolbox:  ToolBox[scala.reflect.runtime.universe.type], iterator: Int, compiledActorsList : Seq[Props]) : Seq[Props] = {
    if(iterator >= actorCodeList.size) compiledActorsList
    else {
      val tree = toolbox.parse(actorCodeList(iterator))
      val actorProps = toolbox.compile(tree)().asInstanceOf[Props]
      compileActors(actorCodeList, toolbox, iterator + 1, compiledActorsList :+ actorProps)
    }
  }
  //This function compiles the code of a single actor
  def compileSingleActor(actorCode : String, toolbox:  ToolBox[scala.reflect.runtime.universe.type]) : Props = {
    val tree = toolbox.parse(actorCode)
    toolbox.compile(tree)().asInstanceOf[Props]
  }


  var reference : ActorRef = _

  //This function is an example on how to run a ActorCode reference
  def runExample(actorProps : Props, toolbox:  ToolBox[scala.reflect.runtime.universe.type]): Unit ={
    //Define ActorSystem
    val actorSystem = ActorSystem("system")
    createActorSystemMetrics(actorSystem)
    val simpleActor = actorSystem.actorOf(Props[SimpleActor], "SimpleActor")
    reference = simpleActor
    val helloActor = actorSystem.actorOf(actorProps, "HelloActor")
    helloActor ! "getReference"
    //helloActor ! "sendMessage"
    //actorSystem.terminate()
  }

  //Attach cinnamon monitoring metrics via the API to the ActorSystem
  private def createActorSystemMetrics(actorSystem: ActorSystem) : Unit = {
    val sysCounter: Counter = CinnamonMetrics(actorSystem).createCounter("sysCounter")
    val sysGaugeDouble: GaugeDouble = CinnamonMetrics(actorSystem).createGaugeDouble("sysGaugeDouble")
    val sysGaugeLong: GaugeLong = CinnamonMetrics(actorSystem).createGaugeLong("sysGaugeLong")
    //val sysProvidingGaugeDouble: ProvidingGaugeDouble = CinnamonMetrics(actorSystem).createProvidingGaugeDouble("sysProvidingGaugeDouble", doubleValueProvider)
    //val sysProvidingGaugeLong: ProvidingGaugeLong = CinnamonMetrics(actorSystem).createProvidingGaugeLong("sysProvidingGaugeLong", longValueProvider)
    val sysRate: Rate = CinnamonMetrics(actorSystem).createRate("sysRate")
    val sysRecorder: Recorder = CinnamonMetrics(actorSystem).createRecorder("sysRecorder")
  }

  //Attach cinnamon monitoring metrics via the API to the Actor
  private def createActorMetrics(actor : Actor) : Unit = {
    val counter: Counter = CinnamonMetrics(actor.context).createCounter("counter")
    val gaugeDouble: GaugeDouble = CinnamonMetrics(actor.context).createGaugeDouble("gaugeDouble")
    val gaugeLong: GaugeLong = CinnamonMetrics(actor.context).createGaugeLong("gaugeLong")
    //val providingGaugeDouble: ProvidingGaugeDouble = CinnamonMetrics(actor.context).createProvidingGaugeDouble("providingGaugeDouble", doubleValueProvider)
    //val providingGaugeLong: ProvidingGaugeLong = CinnamonMetrics(actor.context).createProvidingGaugeLong("providingGaugeLong", longValueProvider)
    val rate: Rate = CinnamonMetrics(actor.context).createRate("rate")
    val recorder: Recorder = CinnamonMetrics(actor.context).createRecorder("recorder")
  }

/*
  val doubleValueProvider: DoubleValueProvider = new DoubleValueProvider {
    val cnt: AtomicReference[Double] = new AtomicReference(0.0)
    override def currentValue(): Double = cnt.get()
  }

  val longValueProvider: LongValueProvider = new LongValueProvider {
    val cnt: AtomicLong = new AtomicLong(0)
    override def currentValue(): Long = cnt.getAndIncrement()
  }
*/

}

