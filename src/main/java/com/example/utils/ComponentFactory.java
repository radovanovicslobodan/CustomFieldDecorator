package com.example.utils;

import com.example.annotations.FindInside;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.util.List;

public class ComponentFactory {

	/**
	 * Initializes elements of a component object by locating them within a given container element.
	 *
	 * @param container The parent WebElement that defines the scope (e.g., header, sidebar)
	 * @param component The component instance whose fields should be initialized
	 */
	public static void initElements(WebElement container, Object component) {

		if (container == null || component == null) {
			throw new IllegalArgumentException("Container and component must not be null");
		}

		Class<?> clazz = component.getClass();
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(FindInside.class)) {
					field.setAccessible(true);
					try {
						ScopedElementLocator locator = new ScopedElementLocator(container, field);
						if (List.class.isAssignableFrom(field.getType())) {
							field.set(component, locator.findElements());
						} else {
							field.set(component, locator.findElement());
						}
					} catch (Exception e) {
						throw new RuntimeException("Failed to initialize field: " + field.getName(), e);
					}
				}
			}
			clazz = clazz.getSuperclass(); // support inheritance
		}
	}
}
