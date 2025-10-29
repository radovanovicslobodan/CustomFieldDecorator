package com.example.utils;

import com.example.annotations.FindInside;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
				if (!field.isAnnotationPresent(FindInside.class))
					continue;

				field.setAccessible(true);
				try {
					FindInside ann = field.getAnnotation(FindInside.class);
					By by = FindInsideUtils.buildByFromAnnotation(ann);

					Type fieldType = field.getGenericType();
					Class<?> rawType = field.getType();

					// Case 1: List<CustomComponent>
					if (List.class.isAssignableFrom(rawType) && fieldType instanceof ParameterizedType) {
						ParameterizedType paramType = (ParameterizedType) fieldType;
						Type actualType = paramType.getActualTypeArguments()[0];
						if (actualType instanceof Class) {
							Class<?> componentClass = (Class<?>) actualType;
							// Verify it has a (WebElement) constructor
							Constructor<?> ctor = componentClass.getConstructor(WebElement.class);

							List<WebElement> elements = container.findElements(by);
							List<Object> components = new ArrayList<>();
							for (WebElement el : elements) {
								components.add(ctor.newInstance(el));
							}
							field.set(component, components);
							continue;
						}
					}

					// Case 2: Single CustomComponent
					if (!rawType.equals(WebElement.class) && !rawType.equals(List.class)) {
						// Assume it's a component class
						Constructor<?> ctor = rawType.getConstructor(WebElement.class);
						WebElement el = container.findElement(by);
						field.set(component, ctor.newInstance(el));
						continue;
					}

					// Case 3: WebElement or List<WebElement> (legacy)
					if (rawType.equals(WebElement.class)) {
						field.set(component, container.findElement(by));
					} else if (rawType.equals(List.class)) {
						field.set(component, container.findElements(by));
					}

				} catch (Exception e) {
					throw new RuntimeException("Failed to initialize field: " + field.getName(), e);
				}
			}
			clazz = clazz.getSuperclass();
		}
	}
}
