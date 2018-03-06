package models;

public class NonBlockingRequestStatusFor {
    private final String requestId;

    public NonBlockingRequestStatusFor(final String requestId) {
        this.requestId = requestId;
    }

    public String getEntityId() {
        return requestId;
    }
}
