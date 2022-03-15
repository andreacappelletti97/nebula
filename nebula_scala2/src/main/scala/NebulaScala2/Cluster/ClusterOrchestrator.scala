/*
package NebulaScala2.Cluster

import akka.actor.{Actor, ActorLogging, ActorRef, Address}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{InitialStateAsEvents, MemberEvent, MemberRemoved, MemberUp, UnreachableMember}
import akka.util.Timeout

import scala.concurrent.duration.*
import akka.pattern.pipe

import scala.language.postfixOps

class ClusterOrchestrator extends Actor with ActorLogging {
  import context.dispatcher
  implicit val timeout: Timeout = akka.util.Timeout.durationToTimeout(3 seconds)

  val cluster: Cluster = Cluster(context.system)
  var workers : Map[Address, ActorRef] = Map()
  var pendingRemoval : Map[Address, ActorRef] = Map()

  override def preStart(): Unit = {
    cluster.subscribe(
      self,
      initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent],
      classOf[UnreachableMember]
    )
  }

  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  override def receive: Receive = handleClusterEvents.orElse(handleWorkerRegistration)

  def handleClusterEvents: Receive = {
    case MemberUp(member) if(member.hasRole("worker")) =>
      log.info(s"Member is up: ${member.address}")
      if(pendingRemoval.contains(member.address)){
        pendingRemoval = pendingRemoval - member.address
      } else {
        val workerSelection = context.actorSelection(s"${member.address}/user/worker")
        workerSelection.resolveOne().map(ref => (member.address, ref)).pipeTo(self)
      }

    case UnreachableMember(member) if(member.hasRole("worker")) =>
      log.info(s"Member detected as unreachable: ${member.address}")
      val workerOption = workers.get(member.address)
      workerOption.foreach { ref =>
        pendingRemoval = pendingRemoval + (member.address -> ref)
      }

    case MemberRemoved(member, previousStatus) =>
      log.info(s"Member removed: ${member.address} after $previousStatus")
      workers = workers - member.address

    case m : MemberEvent => log.info(s"Another member event: ${m}")
  }

  def handleWorkerRegistration : Receive = {
    case pair : (Address, ActorRef) =>
      log.info(s"Registering worker: $pair")
      workers = workers + pair
  }
}

 */