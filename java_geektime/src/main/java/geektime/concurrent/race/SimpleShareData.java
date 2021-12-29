package geektime.concurrent.race;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleShareData extends ShareData {
	final String GEN = "genlatch";
	final String COMP = "complatch";

	Lock lock = new ReentrantLock();

	void initGenSignals(int genThreadNum) {
		CountDownLatch latch = new CountDownLatch(genThreadNum);
		if (signals == null) {
			signals = new HashMap<String, Object>();
		}
		signals.put(GEN, latch);
	}

	CountDownLatch getGenSig() {
		if (signals != null) {
			return (CountDownLatch)signals.get(GEN);
		}
		return null;
	}
	
	void initCompSignals(int genThreadNum) {
		CountDownLatch latch = new CountDownLatch(genThreadNum);
		if (signals == null) {
			signals = new HashMap<String, Object>();
		}
		signals.put(COMP, latch);
	}

	CountDownLatch getCompSig() {
		if (signals != null) {
			return (CountDownLatch)signals.get(COMP);
		}
		return null;
	}
	
	void initExchange() {
//		exchange = new CopyOnWriteArrayList<Integer>();
		exchange = new ConcurrentSkipListSet<Integer>(Collections.reverseOrder());
	}
	
	void addExchange(int data) {
		exchange.add(data);
	}
	
	void  addExchangeAll(List<Integer> datas) {
		try {
			lock.lock();
			exchange.addAll(datas);
		}finally {
			lock.unlock();
		}

	}

//	List<Integer> getShare() {
//		return exchange;
//	}
	ConcurrentSkipListSet<Integer> getShare() {
		return exchange;
	}
}
