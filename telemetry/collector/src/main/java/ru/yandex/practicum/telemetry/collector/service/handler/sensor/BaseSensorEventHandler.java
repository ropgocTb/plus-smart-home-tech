package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {

    @Value("${kafka.topic.telemetry.sensors-topic}")
    private String topic;

    protected final KafkaEventProducer eventProducer;

    public BaseSensorEventHandler(KafkaEventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    protected abstract T mapToAvro(SensorEvent event);

    @Override
    public void handle(SensorEvent event) {

        if (event == null)
            throw new IllegalArgumentException("null event");

        T sensorEventPayload = mapToAvro(event);
        eventProducer.send(topic, event.getHubId(), SensorEventAvro.newBuilder()
                        .setId(event.getId())
                        .setHubId(event.getHubId())
                        .setTimestamp(event.getTimestamp())
                        .setPayload(sensorEventPayload)
                .build());
    }
}
