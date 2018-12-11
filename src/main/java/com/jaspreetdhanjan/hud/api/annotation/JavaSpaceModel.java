package com.jaspreetdhanjan.hud.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * This annotation defines to our compiler that a class will be used as a JavaSpace model object.
 * <p>
 * Allows us to define properties of the JavaSpace Entry objects that do not need to be in the class, subsequently
 * wasting memory in the JavaSpace.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JavaSpaceModel {
	enum Leases {
		DEFAULT_LEASE(TimeUnit.DAYS.toMillis(90)),
		SHORT_LEASE(TimeUnit.DAYS.toMillis(1)),
		DEBUG_LEASE(TimeUnit.MINUTES.toMillis(5));

		private final long millis;

		Leases(long millis) {
			this.millis = millis;
		}

		public long getMillis() {
			return millis;
		}
	}

	Leases lease() default Leases.DEFAULT_LEASE;
}