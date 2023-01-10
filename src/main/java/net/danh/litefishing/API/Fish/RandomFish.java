package net.danh.litefishing.API.Fish;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class RandomFish<String> {

    private final NavigableMap<Long, String> map = new TreeMap<>();
    private final ThreadLocalRandom random;
    private long total = 0;

    public RandomFish() {
        this(ThreadLocalRandom.current());
    }

    public RandomFish(ThreadLocalRandom random) {
        this.random = random;
    }

    public void add(long weight, String resultMaterial) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, resultMaterial);
    }

    public String next() {
        long value = random.nextLong(total);
        return map.higherEntry(value).getValue();
    }

    public int total() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }


}
