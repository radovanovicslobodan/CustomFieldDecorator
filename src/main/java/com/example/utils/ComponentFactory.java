package com.example.utils;

import com.example.annotations.FindInside;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ComponentFactory {

  /**
   * Initializes elements of a component object by locating them within a given container element.
   *
   * @param container The parent WebElement that defines the scope (e.g., header, sidebar)
   * @param component The component instance whose fields should be initialized
   */
  public static void initElements(WebElement container, Object component) {
    if (container == null) {
      throw new IllegalArgumentException("Container cannot be null");
    }
    if (component == null) {
      throw new IllegalArgumentException("Component cannot be null");
    }

    Class<?> clazz = component.getClass();
    while (clazz != null) {
      for (Field field : clazz.getDeclaredFields()) {
				if (!field.isAnnotationPresent(FindInside.class)) {
					continue;
				}

        field.setAccessible(true);
        try {
          FindInside ann = field.getAnnotation(FindInside.class);
          By by = FindInsideUtils.buildByFromAnnotation(ann);

          Class<?> rawType = field.getType();
          Type genericType = field.getGenericType();

          // CASE 1: List<WebElement>
          if (List.class.isAssignableFrom(rawType) &&
              genericType instanceof ParameterizedType) {

            ParameterizedType paramType = (ParameterizedType) genericType;
            Type actualType = paramType.getActualTypeArguments()[0];

            if (actualType == WebElement.class) {
              List<WebElement> elements = container.findElements(by);
              field.set(component, elements);
              continue;
            }
          }

          // CASE 2: List<CustomComponent>
          if (List.class.isAssignableFrom(rawType) &&
              genericType instanceof ParameterizedType) {

            ParameterizedType paramType = (ParameterizedType) genericType;
            Type actualType = paramType.getActualTypeArguments()[0];

            if (actualType instanceof Class<?>) {
              Class<?> componentClass = (Class<?>) actualType;
              try {
                Constructor<?> ctor = componentClass.getConstructor(WebElement.class);

                List<WebElement> containers = container.findElements(by);
                List<Object> instances = new ArrayList<>();
                for (WebElement el : containers) {
                  instances.add(ctor.newInstance(el));
                }
                field.set(component, instances);
                continue;
              } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(
                    "Component class " + componentClass.getSimpleName() +
                        " must have public constructor: " + componentClass.getSimpleName() +
                        "(WebElement container)", e);
              }
            }
          }

          // CASE 3: Single Custom Component (e.g., RadioButton)
          if (!rawType.equals(WebElement.class) &&
              !List.class.isAssignableFrom(rawType)) {

            try {
              Constructor<?> ctor = rawType.getConstructor(WebElement.class);
              WebElement el = container.findElement(by);
              field.set(component, ctor.newInstance(el));
              continue;
            } catch (NoSuchMethodException e) {
              throw new IllegalArgumentException(
                  "Component class " + rawType.getSimpleName() +
                      " must have public constructor: " + rawType.getSimpleName() +
                      "(WebElement container)", e);
            }
          }

          // CASE 4: Single WebElement
          if (rawType.equals(WebElement.class)) {
            field.set(component, container.findElement(by));
            continue;
          }

          throw new IllegalArgumentException(
              "Unsupported field type: " + rawType.getSimpleName() +
                  " for field: " + field.getName());

        } catch (Exception e) {
          throw new RuntimeException(
              "Failed to initialize field: '" + field.getName() + "' in " + component.getClass()
                  .getSimpleName(), e);
        }
      }
      clazz = clazz.getSuperclass();
    }
  }
}
