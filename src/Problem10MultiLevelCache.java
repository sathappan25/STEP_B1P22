import java.util.*;

public class Problem10MultiLevelCache {
    private final LruMap<String, byte[]> l1;
    private final LruMap<String, byte[]> l2;
    private final Map<String, byte[]> l3;
    private long l1Hits, l2Hits, l3Hits;

    public Problem10MultiLevelCache(int l1Size, int l2Size) {
        this.l1 = new LruMap<>(l1Size);
        this.l2 = new LruMap<>(l2Size);
        this.l3 = new LinkedHashMap<>();
    }

    public synchronized byte[] get(String videoId) {
        byte[] data = l1.get(videoId);
        if (data != null) { l1Hits++; return data; }
        data = l2.get(videoId);
        if (data != null) { l2Hits++; l1.put(videoId, data); return data; }
        data = l3.get(videoId);
        if (data != null) { l3Hits++; l2.put(videoId, data); return data; }
        return null;
    }

    public synchronized void put(String videoId, byte[] data) {
        l3.put(videoId, data);
        l2.put(videoId, data);
    }

    public synchronized CacheStats stats() {
        long total = l1Hits + l2Hits + l3Hits;
        double l1Rate = total == 0 ? 0 : (double) l1Hits / total;
        double l2Rate = total == 0 ? 0 : (double) l2Hits / total;
        double l3Rate = total == 0 ? 0 : (double) l3Hits / total;
        return new CacheStats(l1Rate, l2Rate, l3Rate);
    }

    private static class LruMap<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;
        LruMap(int capacity) { super(16, 0.75f, true); this.capacity = capacity; }
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    public record CacheStats(double l1HitRate, double l2HitRate, double l3HitRate) {}

    // Demo
    public static void main(String[] args) {
        Problem10MultiLevelCache cache = new Problem10MultiLevelCache(2, 3);
        cache.put("v1", new byte[]{1});
        cache.put("v2", new byte[]{2});
        cache.put("v3", new byte[]{3});
        System.out.println(cache.get("v1"));
        System.out.println(cache.get("v3"));
        System.out.println(cache.stats());
    }
}