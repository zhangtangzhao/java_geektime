package geektime.concurrent.race;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleSyncGen implements GenRunnable {

	SimpleShareData ssd;
	int size;
	int offset;
	Lock lock = new ReentrantLock();
	public SimpleSyncGen(SimpleShareData ssd, int size, int offset) {
		this.ssd = ssd;
		this.size = size;
		this.offset = offset;
	}
	
	@Override
	public void run() {
		gen();
	}

	@Override
	public void gen() {
		//System.out.println("开始产生随机数: " + size);
//		Random rand = new Random(System.currentTimeMillis());
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		int r;
		int i;
    	for (i = 0; i < size; i++) {
    		r = rand.nextInt(SimpleShareData.COUNT);
//    		synchronized (ssd.getScore()) {
//    			ssd.getScore().add(new Integer(r));
//    		}
			try {
				lock.lock();
				ssd.getScore().add(r);
			}finally {
				lock.unlock();
			}
    	}
    	//System.out.println("产生随机数个数: " + i);
    	ssd.getGenSig().countDown();
	}

}
