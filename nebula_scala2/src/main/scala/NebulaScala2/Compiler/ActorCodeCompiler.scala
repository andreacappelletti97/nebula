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
  var reference : ActorRef = _

  def runCode(codeToRun : String, toolbox:  ToolBox[scala.reflect.runtime.universe.type]): Unit ={
    //Compile Actor code and get the Props object
    val tree = toolbox.parse(codeToRun)
    val actorProps =  toolbox.compile(tree)().asInstanceOf[Props]
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

  def createActorSystemMetrics(actorSystem: ActorSystem) : Unit = {
    val sysCounter: Counter = CinnamonMetrics(actorSystem).createCounter("sysCounter")
    val sysGaugeDouble: GaugeDouble = CinnamonMetrics(actorSystem).createGaugeDouble("sysGaugeDouble")
    val sysGaugeLong: GaugeLong = CinnamonMetrics(actorSystem).createGaugeLong("sysGaugeLong")
    //val sysProvidingGaugeDouble: ProvidingGaugeDouble = CinnamonMetrics(actorSystem).createProvidingGaugeDouble("sysProvidingGaugeDouble", doubleValueProvider)
    //val sysProvidingGaugeLong: ProvidingGaugeLong = CinnamonMetrics(actorSystem).createProvidingGaugeLong("sysProvidingGaugeLong", longValueProvider)
    val sysRate: Rate = CinnamonMetrics(actorSystem).createRate("sysRate")
    val sysRecorder: Recorder = CinnamonMetrics(actorSystem).createRecorder("sysRecorder")
  }

  def createActorMetrics(actor : Actor) : Unit = {
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

