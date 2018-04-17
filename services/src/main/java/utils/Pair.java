package utils;

public class Pair<K, V> {

    private final K first;
    private final V second;

    public static <K, V> Pair<K, V> createPair(K element0, V element1) {
        return new Pair<K, V>(element0, element1);
    }

    public Pair(K element0, V element1) {
        this.first = element0;
        this.second = element1;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

}