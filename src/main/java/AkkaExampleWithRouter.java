import actors.CalculationActor;
import actors.ClientActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AkkaExampleWithRouter {
    public static void main(String[] args) throws InterruptedException {
        Config config = ConfigFactory.load("actor-system-with-router.conf");
        ActorSystem system = ActorSystem.create("actor-system", config);
        ActorRef calculationActor = system.actorOf(CalculationActor.propsWithRouter(), "calculation-actor");
        ActorRef clientActor = system.actorOf(ClientActor.propsWithRouter(calculationActor), "client-actor");
    }
}
