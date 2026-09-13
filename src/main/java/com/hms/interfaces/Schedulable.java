package com.hms.interfaces;

public interface Schedulable {
    void schedule();

    void cancel();

    void reschedule(String newDateTime);
}