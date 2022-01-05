package Week1

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object VotingSystem extends App {

  case class Votes(candidate: String)
  case object VoteStatusRequest
  case class VoteStatusReply(candidate: Option[String])
  class Citizen extends Actor {
    var candidate : Option[String] = None
    override def receive: Receive = {
      case Votes(c) => candidate = Some(c)
      case VoteStatusRequest => sender() ! VoteStatusReply(candidate)
    }
  }

  case class AggregateVotes(citizens: Set[ActorRef])
  class VoteAggregator extends Actor {
    var stillWaiting : Set[ActorRef] = Set()
    var currentStats : Map[String, Int] = Map()
    override def receive: Receive = {
      case AggregateVotes(citizens) =>
        stillWaiting = citizens
        citizens.foreach(
          c => c ! VoteStatusRequest
        )
      case VoteStatusReply(None) =>
        //Citizen has not voted yet
        sender() ! VoteStatusRequest //This might end up in an infinite loop
      case VoteStatusReply(Some(candidate)) =>
        val newStillWaiting = stillWaiting - sender()
        val currentVotesOfCandidate = currentStats.getOrElse(candidate , 0)
        currentStats = currentStats + (candidate -> (currentVotesOfCandidate + 1))
        if(newStillWaiting.isEmpty){
          println(s"[AggregateVotes]: Finished $currentStats ")
        } else {
          stillWaiting = newStillWaiting
        }
    }
  }

  val system = ActorSystem("votingSystem")
  val alice = system.actorOf(Props[Citizen](), "alice")
  val bob = system.actorOf(Props[Citizen](), "bob")
  val charlie = system.actorOf(Props[Citizen](), "charlie")
  val daniel = system.actorOf(Props[Citizen](), "daniel")

  alice ! Votes("Martin")
  bob ! Votes("Jonas")
  charlie ! Votes("Roland")
  daniel ! Votes("Roland")

  val voteAggregator = system.actorOf(Props[VoteAggregator](), "voteAggregator")
  voteAggregator ! AggregateVotes(Set(alice, bob, charlie, daniel))

  /*
  Map of votes
  Martin -> 1
  Jonas -> 1
  Roland -> 2
  */

}

