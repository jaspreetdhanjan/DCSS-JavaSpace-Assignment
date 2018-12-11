package com.jaspreetdhanjan.hud.api.javaspace;

import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace05;

/**
 * This contract contains required APIs which are given to us by Jini.
 */

public interface JiniBundle {
	JavaSpace05 getJavaSpace();

	TransactionManager getTransactionManager();
}