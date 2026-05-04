package ru.yandex.practicum.service.snapshot.evaluator;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ConditionTypeProto;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.ScenarioCondition;

import java.util.Map;

@Component
public class ConditionsEvaluator {

    private record Key(String type, Class<?> payloadClass) {}

    private static final Map<Key, ValueExtractor> EXTRACTORS = Map.ofEntries(
            Map.entry(new Key(ConditionTypeProto.TEMPERATURE.name(), TemperatureSensorAvro.class),
                    p -> ((TemperatureSensorAvro)p).getTemperatureC()),
            Map.entry(new Key(ConditionTypeProto.LUMINOSITY.name(), LightSensorAvro.class),
                    p -> ((LightSensorAvro)p).getLuminosity()),
            Map.entry(new Key(ConditionTypeProto.MOTION.name(), MotionSensorAvro.class),
                    p -> ((MotionSensorAvro)p).getMotion() ? 1 : 0),
            Map.entry(new Key(ConditionTypeProto.SWITCH.name(), SwitchSensorAvro.class),
                    p -> ((SwitchSensorAvro)p).getState() ? 1 : 0),
            Map.entry(new Key(ConditionTypeProto.TEMPERATURE.name(), ClimateSensorAvro.class),
                    p -> ((ClimateSensorAvro)p).getTemperatureC()),
            Map.entry(new Key(ConditionTypeProto.CO2LEVEL.name(), ClimateSensorAvro.class),
                    p -> ((ClimateSensorAvro)p).getCo2Level()),
            Map.entry(new Key(ConditionTypeProto.HUMIDITY.name(), ClimateSensorAvro.class),
                    p -> ((ClimateSensorAvro)p).getHumidity())
    );

    public boolean evaluate(ScenarioCondition sc, SensorsSnapshotAvro snapshot) {
        String sensorId = sc.getSensor().getId();
        SensorStateAvro state = snapshot.getSensorsState().get(sensorId);
        if (state == null) {
            return false;
        }

        Object data = state.getData();
        if (data == null) return false;

        Condition conditionType = sc.getCondition();
        Key key = new Key(conditionType.getType(), data.getClass());
        ValueExtractor extractor = EXTRACTORS.get(key);
        if (extractor == null) {
            return false;
        }

        Integer actual = extractor.extract(data);
        Integer expected = sc.getCondition().getValue();
        if (expected == null || actual == null) return false;

        ComparisonOp op = ComparisonOp.from(sc.getCondition().getOperation());
        if (op == null) return false;

        return compare(actual, expected, op);
    }

    private static boolean compare(int actual, int expected, ComparisonOp op) {
        return switch (op) {
            case GREATER_THAN -> actual > expected;
            case LOWER_THAN -> actual < expected;
            case EQUALS -> actual == expected;
        };
    }
}
