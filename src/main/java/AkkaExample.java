import actors.CalculationActor;
import actors.ClientActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AkkaExample {
    public static void main(String[] args) throws InterruptedException {
        Config config = ConfigFactory.load("actor-system.conf");
        ActorSystem system = ActorSystem.create("actor-system", config);
        ActorRef calculationActor = system.actorOf(CalculationActor.props(), "calculation-actor");
        ActorRef clientActor = system.actorOf(ClientActor.props(calculationActor), "client-actor");
        Thread.sleep(1000 * 60);
        system.terminate();
    }
}
