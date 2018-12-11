package com.jaspreetdhanjan.hud.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * A generic utility class used to extract RUNTIME-declared annotation types.
 */

public class ReflectiveAnnotationExtractor {
	private ReflectiveAnnotationExtractor() {
	}

	public static <T> T getAnnotationField(Object object, String fieldName) {
		final Annotation[] annotations = object.getClass().getDeclaredAnnotations();

		for (Annotation annotation : annotations) {
			Class<? extends Annotation> type = annotation.annotationType();

			for (Method method : type.getDeclaredMethods()) {
				try {
					if (method.getName().equals(fieldName)) {
						return (T) method.invoke(annotation, (Object[]) null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}