package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
public class EventController {

    private final Map<HubEventType, HubEventHandler> hubEventHandlerMap;
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlerMap;

    public EventController(Set<HubEventHandler> hubEventHandlerSet, Set<SensorEventHandler> sensorEventHandlerSet) {
        this.hubEventHandlerMap = hubEventHandlerSet.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
        this.sensorEventHandlerMap = sensorEventHandlerSet.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
    }

    @PostMapping("/sensors")
    public void collectHubEvent(@Valid @RequestBody SensorEvent request) {
        SensorEventHandler sensorEventHandler = sensorEventHandlerMap.get(request.getType());
        if (sensorEventHandler == null) {
            throw new IllegalArgumentException("Обработчик для события " + request.getType() + "не найден.");
        }
        sensorEventHandler.handle(request);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent request) {
        HubEventHandler hubEventHandler = hubEventHandlerMap.get(request.getType());
        if (hubEventHandler == null) {
            throw new IllegalArgumentException("Обработчик для события " + request.getType() + "не найден.");
        }
        hubEventHandler.handle(request);
    }
}
