package com.jaspreetdhanjan.hud.util;

import com.jaspreetdhanjan.hud.api.annotation.JavaSpaceModel;
import com.jaspreetdhanjan.hud.api.entry.BaseEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReflectiveAnnotationExtractorTest {
	@JavaSpaceModel()
	private final class TestBaseEntry extends BaseEntry {
	}

	@JavaSpaceModel(lease = JavaSpaceModel.Leases.SHORT_LEASE)
	private final class AnotherTestBaseEntry extends BaseEntry {
	}

	@Test
	public void testLeaseReflection() {
		JavaSpaceModel.Leases leaseTime = ReflectiveAnnotationExtractor.getAnnotationField(new TestBaseEntry(), "lease");
		JavaSpaceModel.Leases anotherLeaseTime = ReflectiveAnnotationExtractor.getAnnotationField(new AnotherTestBaseEntry(), "lease");

		assertEquals("Lease time should have default value if not set.", leaseTime, JavaSpaceModel.Leases.DEFAULT_LEASE);
		assertEquals("Another lease time should a short lease set.", anotherLeaseTime, JavaSpaceModel.Leases.SHORT_LEASE);
	}
}