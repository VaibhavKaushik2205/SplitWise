package com.project.splitwise.listener;

import io.prometheus.client.Histogram;

public abstract class AbstractListenerBase {

    protected static final Histogram requestLatency =
        Histogram.build().name("splitwise_latency")
            .labelNames("requestType")
            .help("Latency for each of the request types.")
            .register();

    protected interface HandlerMethodWithException {

        void handle() throws Exception;
    }

    protected void timeHandler(RequestTypes type,
        HandlerMethodWithException handler) throws Exception {
        try (Histogram.Timer ignored = requestLatency.labels(type.name()).startTimer()) {
            handler.handle();
        }
    }

    protected void timeAsyncHandler(RequestTypes type,
        Runnable runnable) {
        try (Histogram.Timer ignored = requestLatency.labels(type.name()).startTimer()) {
            runnable.run();
        }
    }
}
