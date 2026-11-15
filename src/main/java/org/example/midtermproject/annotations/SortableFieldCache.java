package org.example.midtermproject.annotations;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * used with {@link Sortable} annotation to cache sortable fields in class.
 */
public class SortableFieldCache {

  private static final Map<Class<?>, Map<String, Field>> CACHE = new ConcurrentHashMap<>();
  private static final Map<Class<?>, String> STRING_CACHE = new ConcurrentHashMap<>();

  public static Map<String, Field> getSortableFields(Class<?> entityClass) {
    return CACHE.computeIfAbsent(entityClass, clazz -> {
      Map<String, Field> sortableMap = new HashMap<>();

      for (Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(Sortable.class)) {
          sortableMap.put(field.getName(), field);
        }
      }

      STRING_CACHE.put(clazz, String.join(", ", sortableMap.keySet()));

      return Collections.unmodifiableMap(sortableMap);
    });
  }

  public static Field getSortableField(Class<?> entityClass, String fieldName) {
    return getSortableFields(entityClass).get(fieldName);
  }

  public static String getSortableFieldsString(Class<?> entityClass) {
    return STRING_CACHE.getOrDefault(entityClass, "");
  }
}
