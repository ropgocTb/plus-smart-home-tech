package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;

public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    @Value("${kafka.topic.telemetry.hubs-topic}")
    private String topic;

    protected final KafkaEventProducer eventProducer;

    public BaseHubEventHandler(KafkaEventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    protected abstract T mapToAvro(HubEvent event);

    @Override
    public void handle(HubEvent event) {

        if (event == null)
            throw new IllegalArgumentException("null event");

        T hubEventPayload = mapToAvro(event);
        eventProducer.send(topic, event.getHubId(), HubEventAvro.newBuilder()
                        .setHubId(event.getHubId())
                        .setTimestamp(event.getTimestamp())
                        .setPayload(hubEventPayload)
                .build());
    }
}
