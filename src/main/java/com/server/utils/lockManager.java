package com.server.utils;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class lockManager {

	private Hashtable<String, ReentrantLock> locks = null;

	public lockManager() {
		locks = new Hashtable<>();
	}

	public void add(String lockName) {
		try {
			locks.put(lockName, new ReentrantLock());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean contains(String lockName) {
		boolean b = false;
		try {
			b = locks.containsKey(lockName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public void lock(String lockName) {
		try {
			locks.get(lockName).lock();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void lockInterruptibly(String lockName) throws InterruptedException {
		try {
			locks.get(lockName).lockInterruptibly();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Condition newCondition(String lockName) {
		Condition condition = null;
		try {
			condition = locks.get(lockName).newCondition();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return condition;
	}

	public boolean tryLock(String lockName) {
		boolean b = false;
		try {
			b = locks.get(lockName).tryLock();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public boolean tryLock(String lockName, long time, TimeUnit timeUnit) throws InterruptedException {
		boolean b = false;
		try {
			b = locks.get(lockName).tryLock(time, timeUnit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public void unLock(String lockName) {
		try {
			if (locks.get(lockName).isHeldByCurrentThread()) {
				locks.get(lockName).unlock();
			} else {
				forceUnlock(lockName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void forceUnlock(String lockName) {
		try {
			if (locks.get(lockName) instanceof ReentrantLock) {
				locks.put(lockName, new ReentrantLock());
			} else {
				throw new UnsupportedOperationException(
						"Cannot force unlock of lock type " + locks.get(lockName).getClass().getSimpleName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasLock(String lockName) {
		try {
			if (locks.containsKey(lockName)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
