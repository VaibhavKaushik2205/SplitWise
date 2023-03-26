package com.project.splitwise.constants;

public class Constants {

    public static class KafkaRequestHeaders {

        public static final String REQUEST_TYPE = "X-Request-Type";
        public static final String REPLY_MESSAGE = "X-Reply-Message";
        public static final String CORRELATION_ID = "kafka_correlationId";
        public static final String MESSAGE_KEY = "kafka_messageKey";
        public static final String TOPIC = "kafka_topic";
        public static final String REPLY_TOPIC = "kafka_replyTopic";
        public static final String SOURCE_ID = "X-Source-Id";
        public static final String REPLY_STATUS = "X-Reply-Status";
        public static final String IDEMPOTENCY_KEY = "X-Idempotency-Key";

        public static final String CUSTOMER_ID = "X-Medici-Customer-Id";
    }

}
