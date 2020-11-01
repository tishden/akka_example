package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.FromConfig;
import messages.CalculationMessage;
import messages.CalculationResult;

public class CalculationActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public void preStart() throws Exception {
        log.info("CalculationActor with name {} is started", self().path());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CalculationMessage.class, msg -> onCalculate(msg, sender())).build();
    }

    private void onCalculate(CalculationMessage msg, ActorRef sender) {
        log.info("Received CalculationMessage. Actor name: {}, Thread name: {}", self().path(), Thread.currentThread().getName());
        sender.tell(new CalculationResult(), ActorRef.noSender());
    }

    public static Props props() {
        return Props.create(CalculationActor.class);
    }

    public static Props propsWithRouter() {
        return FromConfig.getInstance().props(Props.create(CalculationActor.class));
    }
}
