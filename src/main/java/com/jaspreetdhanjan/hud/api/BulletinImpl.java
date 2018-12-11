package com.jaspreetdhanjan.hud.api;

import com.jaspreetdhanjan.hud.api.annotation.JavaSpaceModel;
import com.jaspreetdhanjan.hud.api.entry.BaseEntry;
import com.jaspreetdhanjan.hud.api.event.EventListener;
import com.jaspreetdhanjan.hud.api.javaspace.JavaSpaceConfig;
import com.jaspreetdhanjan.hud.api.javaspace.JiniBundle;
import com.jaspreetdhanjan.hud.api.javaspace.JiniBundleBuilder;
import com.jaspreetdhanjan.hud.util.ReflectiveAnnotationExtractor;
import net.jini.core.entry.Entry;
import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.export.Exporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;

import java.util.*;

/**
 * The main documentation for this interface can be found in {@link BulletinApi}.
 * <p>
 * This API's implementation strictly uses our our {@link BaseEntry} class. We do this to avoid {@link ClassCastException}
 * from other objects in the JavaSpace, as well as allowing us to keep track of what a client has and has not put into
 * the space.
 */

public class BulletinImpl implements BulletinApi {
	private static BulletinApi instance;

	private final JavaSpace05 javaSpace;
	private final TransactionManager transactionManager;

	private List<Lease> leaseList = new ArrayList<>(1);
	private Map<EventListener, EventRegistration> listenerMap = new HashMap<>();

	private final Object transactionLock = new Object();
	private Transaction transaction;
	private boolean useTransactions;

	public BulletinImpl(JavaSpace05 javaSpace, TransactionManager transactionManager) {
		this.javaSpace = javaSpace;
		this.transactionManager = transactionManager;
	}

	public static synchronized BulletinApi getInstance() {
		if (instance == null) {
			JavaSpaceConfig config = JavaSpaceConfig.getConfig();
			JiniBundle bundle = JiniBundleBuilder.buildBundle(config);

			instance = new BulletinImpl(bundle.getJavaSpace(), bundle.getTransactionManager());
		}
		return instance;
	}

	@Override
	public void writeAll(List<? extends BaseEntry> entries) {
		try {
			Objects.requireNonNull(entries);

			List<Lease> leases = javaSpace.write(entries, getTransaction(), Collections.singletonList(defaultLeaseTime()));
			leaseList.addAll(leases);
		} catch (Exception e) {
			abortTransaction();
			throw new BulletinException("An exception occurred during writing these entries. Refer to the wrapped exception object.", e);
		}
	}

	@Override
	public void write(BaseEntry entry) {
		try {
			Objects.requireNonNull(entry);

			Lease lease = javaSpace.write(entry, getTransaction(), getLeaseTime(entry));
			leaseList.add(lease);
		} catch (Exception e) {
			abortTransaction();
			throw new BulletinException("An exception occurred during writing this entry. Refer to the wrapped exception object.", e);
		}
	}

	@Override
	public <T extends BaseEntry> List<T> takeAll(T template, long maxEntries, long timeout) {
		try {
			Objects.requireNonNull(template);
			return new ArrayList<T>(javaSpace.take(Collections.singletonList(template), getTransaction(), timeout, maxEntries));
		} catch (Exception e) {
			abortTransaction();
			throw new BulletinException("An exception occurred during taking these templates. Refer to the wrapped exception object.", e);
		}
	}

	@Override
	public <T extends BaseEntry> T take(T template, long timeout) {
		try {
			Objects.requireNonNull(template);
			return (T) javaSpace.take(template, getTransaction(), timeout);
		} catch (Exception e) {
			abortTransaction();
			throw new BulletinException("An exception occurred during taking this template. Refer to the wrapped exception object.", e);
		}
	}

	@Override
	public <T extends BaseEntry> T takeIfExists(T template, long timeout) {
		try {
			Objects.requireNonNull(template);
			return (T) javaSpace.takeIfExists(template, getTransaction(), timeout);
		} catch (Exception e) {
			abortTransaction();
			throw new BulletinException("An exception occurred during taking this template. Refer to the wrapped exception object.", e);
		}
	}

	@Override
	public <T extends BaseEntry> List<T> readAll(T template, long timeout, long maxEntries) {
		try {
			Objects.requireNonNull(template);

			List<T> templates = Collections.singletonList(template);
			MatchSet matchSet = javaSpace.contents(templates, getTransaction(), timeout, maxEntries);

			List<T> result = new ArrayList<>(1);

			Entry entry;
			while ((entry = matchSet.next()) != null) {
				result.add((T) entry);
			}

			return result;
		} catch (Exception e) {
			abortTransaction();
			throw new BulletinException("An exception occurred when reading this template. Refer to the wrapped exception object.", e);
		}
	}

	@Override
	public <T extends BaseEntry> T read(T template, long timeout) {
		try {
			Objects.requireNonNull(template);
			return (T) javaSpace.read(template, getTransaction(), timeout);
		} catch (Exception e) {
			abortTransaction();
			throw new BulletinException("An exception occurred during reading this template. Refer to the wrapped exception object.", e);
		}
	}

	@Override
	public <T extends BaseEntry> T readIfExists(T template, long timeout) {
		try {
			Objects.requireNonNull(template);
			return (T) javaSpace.readIfExists(template, getTransaction(), timeout);
		} catch (Exception e) {
			abortTransaction();
			throw new BulletinException("An exception occurred during reading this template. Refer to the wrapped exception object.", e);
		}
	}

	@Override
	public void cancelLocalLeases() {
		leaseList.forEach(lease -> {
			try {
				if (lease != null) {
					lease.cancel();
				}
			} catch (Exception e) {
				throw new BulletinException("An exception occurred when cancelling lease: " + lease + " in the JavaSpace. Refer to the wrapped exception object.", e);
			}
		});

		leaseList.clear();
	}

	@Override
	public void beginTransactionBlock() {
		synchronized (transactionLock) {
			if (useTransactions) {
				throw new BulletinException("The transaction block is already active!");
			}

			try {
				transaction = TransactionFactory.create(transactionManager, defaultLeaseTime()).transaction;
				useTransactions = true;
			} catch (Exception e) {
				throw new BulletinException("An exception occurred when creating the transaction. Refer to the wrapped exception object.", e);
			}
		}
	}

	@Override
	public void endTransactionBlock() {
		synchronized (transactionLock) {
			if (!useTransactions) {
				throw new BulletinException("The transaction block is not active.");
			}

			try {
				transaction.commit();
			} catch (Exception e) {
				throw new BulletinException("An exception occurred when committing the transaction. Refer to the wrapped exception object.", e);
			} finally {
				transaction = null;
				useTransactions = false;
			}
		}
	}

	@Override
	public <T extends BaseEntry> void addEventListener(T template, EventListener listener) {
		try {
			Exporter exporter = new BasicJeriExporter(TcpServerEndpoint.getInstance(0), new BasicILFactory(), false, true);
			RemoteEventListener remoteEventListenerStub = (RemoteEventListener) exporter.export(listener);

			EventRegistration registration = javaSpace.registerForAvailabilityEvent(Collections.singletonList(template), null, true,
					remoteEventListenerStub, getLeaseTime(template), null);

			listenerMap.put(listener, registration);
		} catch (Exception e) {
			throw new BulletinException("An exception occurred when adding the listener. Refer to the wrapped exception object.", e);
		}
	}

	@Override
	public void removeEventListener(EventListener listener) {
		EventRegistration registration = listenerMap.remove(listener);

		if (registration != null) {
			Lease lease = registration.getLease();

			try {
				lease.cancel();
			} catch (Exception e) {
				throw new BulletinException("An exception occurred when canceling the event listening lease. Refer to wrapped exception object.", e);
			}
		}
	}

	private Transaction getTransaction() {
		synchronized (transactionLock) {
			// Protected against misuse of the transaction block.
			if (useTransactions) {
				return transaction;
			}
			return null;
		}
	}

	private void abortTransaction() {
		synchronized (transactionLock) {
			try {
				if (transaction != null) {
					transaction.abort();
				}
			} catch (Exception e) {
				throw new BulletinException("An exception occurred when aborting the transaction. Refer to the wrapped exception object.", e);
			}
		}
	}

	private long getLeaseTime(BaseEntry entry) {
		JavaSpaceModel.Leases value = ReflectiveAnnotationExtractor.getAnnotationField(entry, "lease");
		if (value == null) {
			return defaultLeaseTime();
		}
		return value.getMillis();
	}

	private long defaultLeaseTime() {
		return JavaSpaceModel.Leases.DEFAULT_LEASE.getMillis();
	}
}