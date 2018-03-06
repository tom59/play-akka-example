package controllers;

import actors.RequestActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import models.CurrentRequestStatus;
import models.NonBlockingRequestStatusFor;
import com.typesafe.config.Config;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

import static akka.pattern.PatternsCS.ask;

@Singleton
public class RequestController extends Controller {
    ActorRef requestStatus;
    int servicingTimeout;

    @Inject
    public RequestController(ActorSystem system, Config configuration) {
        requestStatus = system.actorOf(RequestActor.props());
        servicingTimeout = configuration.getInt("servicingTimeout");
    }

    public CompletionStage<Result> getRequest(String requestId) {
        return ask(requestStatus, new NonBlockingRequestStatusFor(requestId), servicingTimeout)
                .thenApplyAsync(CurrentRequestStatus.class::cast)
                .thenApplyAsync(this::toHttpResponse)
                .toCompletableFuture();
    }

    private Result toHttpResponse(final CurrentRequestStatus result) {
        final String link = toLinkHeader(result);
        return ok(Json.stringify(Json.toJson(result))).withHeader("Link", link);
    }

    private String toLinkHeader(final CurrentRequestStatus result) {
        return String.format("<%s>; Rel=%s", "/payments/" + result.paymentId, "payment");
    }

}
