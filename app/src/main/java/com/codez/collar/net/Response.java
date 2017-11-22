package com.codez.collar.net;

/**
 * Created by codez on 2017/11/22.
 * Description:
 */

public class Response<T> {
    private T subjects;
    private int count;
    private int start;
    private int total;

    public T getSubjects() {
        return subjects;
    }

    public void setSubjects(T subjects) {
        this.subjects = subjects;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
