package frc.robot.util;

import java.util.ArrayList;

// Yeeted from stack overflow
public class SizedQueue<K> extends ArrayList<K> {

    private int maxSize;

    public SizedQueue(int size) {
        this.maxSize = size;
    }

    public boolean add(K k) {
        boolean r = super.add(k);
        if (size() > maxSize) {
            removeRange(0, size() - maxSize);
        }
        return r;
    }

    public K getYoungest() {
        return get(size() - 1);
    }

    public K getOldest() {
        return get(0);
    }
}