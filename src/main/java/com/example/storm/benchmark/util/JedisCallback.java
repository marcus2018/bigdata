package com.example.storm.benchmark.util;

public interface JedisCallback<T, E> {

    T callBack(E e);

}

