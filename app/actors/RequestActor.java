package actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import models.CurrentRequestStatus;
import models.NonBlockingRequestStatusFor;
import models.RequestStatus;

public class RequestActor extends AbstractLoggingActor {

    private RequestStatus requestStatus = RequestStatus.COMPLETED;
    private String paymentId = "AB10";

    @Override public Receive createReceive() {
        return receiveBuilder()
                .match(NonBlockingRequestStatusFor.class, this::handle)
                .build();
    }

    private void handle(NonBlockingRequestStatusFor request) {
        CurrentRequestStatus status = new CurrentRequestStatus(request.getEntityId(), requestStatus, paymentId);
        sender().tell(status, self());
    }

    public static Props props() {
        return Props.create(RequestActor.class);
    }
}
