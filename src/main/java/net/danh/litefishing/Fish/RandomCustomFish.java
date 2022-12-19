package net.danh.litefishing.Fish;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCustomFish<String> {

    private final NavigableMap<Integer, String> map = new TreeMap<>();
    private final Random random;
    private int total = 0;

    public RandomCustomFish() {
        this(new Random());
    }

    public RandomCustomFish(Random random) {
        this.random = random;
    }

    public void add(int weight, String resultMaterial) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, resultMaterial);
    }

    public String next() {
        int value = random.nextInt(total);
        return map.higherEntry(value).getValue();
    }

    public int total() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }


}
