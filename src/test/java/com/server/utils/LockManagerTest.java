package com.server.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LockManagerTest {

	private lockManager lockManager;

	@BeforeEach
	public void setUp() {
		lockManager = new lockManager();
	}

	@Test
	public void testAddAndContainsLock() {
		lockManager.add("testLock");
		assertTrue(lockManager.contains("testLock"));
	}

	@Test
	public void testLockAndUnlock() {
		lockManager.add("testLock");
		lockManager.lock("testLock");
		assertTrue(lockManager.tryLock("testLock"));
		lockManager.unLock("testLock");
		assertTrue(lockManager.tryLock("testLock"));
	}

	@Test
	public void testTryLockWithTimeout() throws InterruptedException {
		lockManager.add("testLock");
		assertTrue(lockManager.tryLock("testLock", 1, TimeUnit.SECONDS));
		lockManager.unLock("testLock");
	}

	@Test
	public void testLockInterruptibly() throws InterruptedException {
		lockManager.add("testLock");
		lockManager.lockInterruptibly("testLock");
		assertTrue(lockManager.tryLock("testLock"));
		lockManager.unLock("testLock");
	}

	@Test
	public void testNewCondition() {
		lockManager.add("testLock");
		Condition condition = lockManager.newCondition("testLock");
		assertNotNull(condition);
	}

	@Test
	public void testForceUnlock() {
		lockManager.add("testLock");
		lockManager.lock("testLock");
		lockManager.forceUnlock("testLock");
		assertTrue(lockManager.tryLock("testLock"));
	}

	@Test
	public void testHasLock() {
		lockManager.add("testLock");
		assertTrue(lockManager.hasLock("testLock"));
		assertFalse(lockManager.hasLock("nonExistentLock"));
	}

	@Test
	public void testUnlockWithoutLock() {
		lockManager.add("testLock");
		lockManager.unLock("testLock"); // Should not throw an exception
	}

	@Test
	public void testUnlockWhenNotHeldByCurrentThread() {
		lockManager.add("testLock");
		lockManager.lock("testLock");
		Thread anotherThread = new Thread(() -> {
			lockManager.unLock("testLock");
		});
		anotherThread.start();
		try {
			anotherThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(lockManager.tryLock("testLock"));
	}
}
