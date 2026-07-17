package com.example.outbox.actuate;

import com.example.outbox.OutboxDrainer;
import com.example.outbox.OutboxState;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

@Endpoint(id = "outbox")
public class OutboxEndpoint {

    private final OutboxDrainer drainer;

    public OutboxEndpoint(OutboxDrainer drainer) {
        this.drainer = drainer;
    }

    @ReadOperation
    public OutboxState state() {
        return drainer.state();
    }
}
