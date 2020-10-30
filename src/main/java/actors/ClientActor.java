package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.FromConfig;
import messages.CalculationMessage;
import messages.CalculationResult;

import java.time.Duration;

public class ClientActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final ActorRef calculationActor;

    public ClientActor(final ActorRef calculationActor) {
        this.calculationActor = calculationActor;
    }

    @Override
    public void preStart() {
        getContext().system().scheduler().scheduleAtFixedRate(
                Duration.ofMillis(100),
                Duration.ofMillis(100),
                calculationActor,
                new CalculationMessage(),
                getContext().system().dispatcher(),
                self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CalculationMessage.class, msg -> onCalculationResult(msg, sender())).build();
    }

    private void onCalculationResult(CalculationMessage msg, ActorRef sender) {
        log.info("Received CalculationResult");
        sender.tell(new CalculationResult(), ActorRef.noSender());
    }

    public static Props props(final ActorRef calculationActor) {
        return FromConfig.getInstance().props(Props.create(ClientActor.class, calculationActor));
    }
}
