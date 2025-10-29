package com.example.utils;

import com.example.annotations.FindInside;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class ComponentFieldDecorator implements FieldDecorator {

	private final WebElement container;

	public ComponentFieldDecorator(WebElement container) {

		this.container = container;
	}

	@Override
	public ElementLocator decorate(ClassLoader loader, Field field) {

		// Only handle fields annotated with @FindInside
		if (!field.isAnnotationPresent(FindInside.class)) {
			return null; // Let other decorators (or nothing) handle it
		}

		Class<?> fieldType = field.getType();

		// Support both WebElement and List<WebElement>
		if (WebElement.class.isAssignableFrom(fieldType)) {
			return new ScopedElementLocator(container, field);
		} else if (List.class.isAssignableFrom(fieldType)) {
			// Optional: Validate generic type is WebElement
			Type genericType = field.getGenericType();
			if (genericType instanceof ParameterizedType) {
				Type[] typeArgs = ((ParameterizedType) genericType).getActualTypeArguments();
				if (typeArgs.length == 1 && typeArgs[0] == WebElement.class) {
					return new ScopedElementLocator(container, field);
				}
			}
			// Still allow it even if generic type isn't perfect—Selenium does this too
			return new ScopedElementLocator(container, field);
		}

		return null; // Not handled
	}
}
