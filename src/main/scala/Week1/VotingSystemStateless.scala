package Week1
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object VotingSystemStateless extends  App {

  case class Votes(candidate: String)
  case object VoteStatusRequest
  case class VoteStatusReply(candidate: Option[String])

  class Citizen extends Actor {
    override def receive: Receive = {
      case Votes(c) => context.become(voted((c)))
      case VoteStatusRequest => sender() ! VoteStatusReply(None)
    }
    def voted(candidate: String): Receive ={
      case VoteStatusRequest => sender() ! VoteStatusReply(Some(candidate))
    }
  }

  case class AggregateVotes(citizens: Set[ActorRef])
  class VoteAggregator extends Actor {
    override def receive: Receive = awaitingCommand
    def awaitingCommand : Receive = {
      case AggregateVotes(citizens) =>
        citizens.foreach(c => c ! VoteStatusRequest)
        context.become(awaitingStatuses(citizens, Map()))
    }
    def awaitingStatuses(stillWaiting: Set[ActorRef], currentStats: Map[String, Int]): Receive ={
      case VoteStatusReply(None) =>
        //Citizen has not voted yet
        sender() ! VoteStatusRequest //This might end up in an infinite loop
      case VoteStatusReply(Some(candidate)) =>
        val newStillWaiting = stillWaiting - sender()
        val currentVotesOfCandidate = currentStats.getOrElse(candidate , 0)
        val newStats = currentStats + (candidate -> (currentVotesOfCandidate + 1))
        if(newStillWaiting.isEmpty){
          println(s"[AggregateVotes]: Finished $newStats ")
        } else {
          context.become(awaitingStatuses(newStillWaiting, newStats))
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

