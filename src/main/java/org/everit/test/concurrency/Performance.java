package org.everit.test.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Performance {

    private final static int ARRAY_LENGTH = 10000;
    private final static int CYCLES_PER_THREAD = 10000000;
    private final static int THREAD_NUM = 3;

    private static void doIt(final List<Integer> list) {
        final CountDownLatch endSignal = new CountDownLatch(THREAD_NUM);

        for (int i = 0; i < THREAD_NUM; i++) {
            final Random r = new Random();
            new Thread(new Runnable() {

                public void run() {

                    long result = 0;
                    for (int j = 0; j < CYCLES_PER_THREAD; j++) {
                        // result += list.put(r.nextInt(9999), r.nextInt());
                        result += list.set(r.nextInt(ARRAY_LENGTH), r.nextInt());
                    }
                    System.out.println(result);
                    endSignal.countDown();
                }
            }).start();
        }

        try {
            endSignal.await();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        List<Integer> list = new ArrayList<Integer>(ARRAY_LENGTH);

        Random r = new Random();
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            list.add(i, r.nextInt());
        }

        long startTime = System.currentTimeMillis();

        doIt(list);

        long endTime = System.currentTimeMillis();

        long length = endTime - startTime;

        System.out.println(length + "ms; " + ((double) CYCLES_PER_THREAD * THREAD_NUM / length) + " db/ms");
    }
}
