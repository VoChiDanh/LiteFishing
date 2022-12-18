package net.danh.litefishing.Utils;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomFishing<String> {

    private final NavigableMap<Integer, String> map = new TreeMap<>();
    private final Random random;
    private int total = 0;

    public RandomFishing() {
        this(new Random());
    }

    public RandomFishing(Random random) {
        this.random = random;
    }

    public void add(int weight, String result) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, result);
    }

    public String next() {
        int value = random.nextInt(total);
        return map.higherEntry(value).getValue();
    }

}
