package com.jaspreetdhanjan.hud.api;

import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;

public class MockJavaSpace implements JavaSpace {
	@Override
	public Lease write(Entry entry, Transaction transaction, long l) throws TransactionException, RemoteException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Entry read(Entry entry, Transaction transaction, long l) throws UnusableEntryException, TransactionException, InterruptedException, RemoteException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Entry readIfExists(Entry entry, Transaction transaction, long l) throws UnusableEntryException, TransactionException, InterruptedException, RemoteException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Entry take(Entry entry, Transaction transaction, long l) throws UnusableEntryException, TransactionException, InterruptedException, RemoteException {
		return null;
	}

	@Override
	public Entry takeIfExists(Entry entry, Transaction transaction, long l) throws UnusableEntryException, TransactionException, InterruptedException, RemoteException {
		return null;
	}

	@Override
	public EventRegistration notify(Entry entry, Transaction transaction, RemoteEventListener remoteEventListener, long l, MarshalledObject marshalledObject) throws TransactionException, RemoteException {
		return null;
	}

	@Override
	public Entry snapshot(Entry entry) throws RemoteException {
		return null;
	}
}
