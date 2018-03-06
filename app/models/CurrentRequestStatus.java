package models;

import java.io.Serializable;

public class CurrentRequestStatus implements Serializable {

    public final String id;
    public final RequestStatus status;
    public final String paymentId;

    public CurrentRequestStatus(final String id, final RequestStatus status, final String paymentId) {
        this.id = id;
        this.status = status;
        this.paymentId = paymentId;
    }
}
