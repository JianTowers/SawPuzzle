package com.tours.sawpuzzle.monitor;

@FunctionalInterface
public interface Monitor<T> {
    boolean run(T t);
}
