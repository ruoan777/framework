package com.ustc.ruoan.framework.web.util;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author ruoan
 */
public class TaskTest {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws Exception {

        long start1 = System.currentTimeMillis();
        System.out.println("test1 begin ");
        test1();
        System.out.println("test1 end, cost " + (System.currentTimeMillis() - start1));


        TimeUnit.SECONDS.sleep(1);
        long start2 = System.currentTimeMillis();
        System.out.println("test2 begin ");
        test2();
        System.out.println("test2 end cost " + (System.currentTimeMillis() - start2));
    }

    private static void test1() throws Exception {
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> {
            sleep(500);
        }, executorService);
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> {
            sleep(900);
        }, executorService);
        task1.get(20000, TimeUnit.MILLISECONDS);
        task2.get(20000, TimeUnit.MILLISECONDS);
    }

    private static void test2() throws Exception {
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> {
            sleep(500);
        }, executorService);
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> {
            sleep(900);
        }, executorService);
        CompletableFuture.allOf(task1, task2).get(20000, TimeUnit.MILLISECONDS);
    }

    private static void sleep(int i) {
        try {
            TimeUnit.MILLISECONDS.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
    }
}
