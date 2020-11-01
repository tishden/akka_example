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
        log.info("ClientActor with name {} is started", self().path());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CalculationResult.class, msg -> onCalculationResult(msg, sender())).build();
    }

    private void onCalculationResult(CalculationResult msg, ActorRef sender) {
        log.info("Received CalculationResult. Actor path: {}, Thread name: {}", self().path(), Thread.currentThread().getName());
    }

    public static Props props(final ActorRef calculationActor) {
        return Props.create(ClientActor.class, calculationActor);
    }

    public static Props propsWithRouter(final ActorRef calculationActor) {
        return FromConfig.getInstance().props(Props.create(ClientActor.class, calculationActor));
    }
}
