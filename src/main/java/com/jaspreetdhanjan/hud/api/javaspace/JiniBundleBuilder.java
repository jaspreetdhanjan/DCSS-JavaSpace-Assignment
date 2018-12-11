package com.jaspreetdhanjan.hud.api.javaspace;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace05;

import java.util.Objects;

public class JiniBundleBuilder {
	public static final class JiniBundleException extends RuntimeException {
		private JiniBundleException(String msg, Exception e) {
			super(msg, e);
		}
	}

	private JiniBundleBuilder() {
	}

	/**
	 * Adapted from SpaceUtils.java (credit to Gary Allen).
	 */
	public static JiniBundle buildBundle(JavaSpaceConfig config) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			LookupLocator lookupLocator = new LookupLocator("jini://" + config.getHostname());

			ServiceRegistrar serviceRegistrar = lookupLocator.getRegistrar();

			Class[] javaSpaceClassTemplate = {Class.forName("net.jini.space.JavaSpace")};
			Class[] transactionManagerClassTemplate = {Class.forName("net.jini.core.transaction.server.TransactionManager")};

			return new JiniBundleImpl(
					(JavaSpace05) serviceRegistrar.lookup(new ServiceTemplate(null, javaSpaceClassTemplate, null)),
					(TransactionManager) serviceRegistrar.lookup(new ServiceTemplate(null, transactionManagerClassTemplate, null))
			);
		} catch (Exception e) {
			throw new JiniBundleException("Could not retrieve JiniBundle!", e);
		}
	}

	private static class JiniBundleImpl implements JiniBundle {
		private final JavaSpace05 javaSpace;
		private final TransactionManager transactionManager;

		public JiniBundleImpl(JavaSpace05 javaSpace, TransactionManager transactionManager) {
			Objects.requireNonNull(javaSpace);
			Objects.requireNonNull(transactionManager);

			this.javaSpace = javaSpace;
			this.transactionManager = transactionManager;
		}

		public JavaSpace05 getJavaSpace() {
			return javaSpace;
		}

		public TransactionManager getTransactionManager() {
			return transactionManager;
		}
	}
}