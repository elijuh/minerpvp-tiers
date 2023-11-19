package dev.elijuh.minerpvp.util;

import com.google.common.collect.ImmutableMap;
import lombok.experimental.UtilityClass;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@UtilityClass
public class DocumentUtil {

    public <T> T getDotNotation(Document document, String path, T def) {
        T value = document.getEmbedded(Arrays.asList(path.split("\\.")), def);
        return value instanceof Document ? (def instanceof Document ? value : def) : value;
    }

    public <T> T getDotNotation(Document document, String path, Class<T> clazz) {
        return document.getEmbedded(Arrays.asList(path.split("\\.")), clazz);
    }

    public void setDotNotation(Document document, String path, Object value) {
        List<String> keys = Arrays.asList(path.split("\\."));
        if (keys.isEmpty()) return;
        String lastKey = keys.get(keys.size() - 1);

        Document object = document;
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            Document next = object.get(key, Document.class);
            if (next == null) {
                next = new Document();
                object.put(key, next);
            }
            object = next;
        }

        object.put(lastKey, value);
    }

    private final Map<Class<? extends Number>, BiFunction<Number, Number, Number>> INCR_OPERATIONS =
        new ImmutableMap.Builder<Class<? extends Number>, BiFunction<Number, Number, Number>>()
            .put(Integer.class,(a,b)->a.intValue()+b.intValue())
            .put(Long.class,(a,b)->a.longValue()+b.longValue())
            .put(Double.class,(a,b)->a.doubleValue()+b.doubleValue())
            .put(Byte.class,(a,b)->a.byteValue()+b.byteValue())
            .put(Float.class,(a,b)->a.floatValue()+b.floatValue())
            .put(Short.class,(a,b)->a.shortValue()+b.shortValue())
            .build();

    public void incrementDotNotation(Document data, String path, Number increment) {
        Number value = getDotNotation(data, path, increment.getClass());
        if (value == null) {
            setDotNotation(data, path, increment);
            return;
        }

        BiFunction<Number, Number, Number> op = INCR_OPERATIONS.get(increment.getClass());
        if (op != null) {
            value = op.apply(value, increment);
        }

        setDotNotation(data, path, value);
    }

    public void removeDotNotation(Document document, String path) {
        List<String> keys = Arrays.asList(path.split("\\."));
        if (keys.isEmpty()) return;
        String lastKey = keys.get(keys.size() - 1);

        Document object = document;
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            Document next = object.get(key, Document.class);
            if (next == null) {
                next = new Document();
                object.put(key, next);
            }
            object = next;
        }

        object.remove(lastKey);
    }
}
