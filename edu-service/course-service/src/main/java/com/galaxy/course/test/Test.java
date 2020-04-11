package com.galaxy.course.test;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Test {

    @Async
    public void test(int i){
        LocalDate now = LocalDate.now();
        try {
            Thread.sleep(1000);
            System.out.println("多线程异步-"+i+"\t"+Thread.currentThread().getName()+ "\t"+now);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    @Async
    public void tet(int x){
        LocalDate now = LocalDate.now();

        try {
            Thread.sleep(1000);
            System.out.println("多线程异步test-"+x+"\t"+Thread.currentThread().getName()+ "\t"+now);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
