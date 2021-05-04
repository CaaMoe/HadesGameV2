package moe.caa.fabric.hadesgame.server;


import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class WeightRandomArrayList<T> {
    private final ArrayList<Entry<T>> resource = new ArrayList<>();
    private final Random RANDOM = new Random();
    private int weightSum = 0;

    public void add(T t, int weight) {
        resource.add(new Entry<>(t, weight));
        weightSum += weight;
    }

    public Stream<Entry<T>> stream() {
        return resource.stream();
    }

    public T randomGet() {
        int n = RANDOM.nextInt(weightSum);
        int currentSum = 0;
        for (int i = 0; i < resource.size(); i++) {
            currentSum += resource.get(i).weight;
            if (n < currentSum) return resource.get(i).value;
        }
        return resource.get(RANDOM.nextInt(resource.size())).value;
    }

    public static class Entry<T> {
        public T value;
        public int weight;

        public Entry(T value, int weight) {
            this.value = value;
            this.weight = weight;
        }
    }
}
