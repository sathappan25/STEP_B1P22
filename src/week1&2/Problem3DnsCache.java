
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class Problem3DnsCache {
    private final int maxEntries;
    private final LinkedHashMap<String, DnsEntry> cache;
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();
    private long hits = 0;
    private long misses = 0;

    public Problem3DnsCache(int maxEntries) {
        this.maxEntries = maxEntries;
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, DnsEntry> eldest) {
                return size() > Problem3DnsCache.this.maxEntries;
            }
        };
        cleaner.scheduleAtFixedRate(this::evictExpired, 5, 5, TimeUnit.SECONDS);
    }

    public synchronized String resolve(String domain, Function<String, UpstreamResult> upstream) {
        DnsEntry entry = cache.get(domain);
        if (entry != null && !entry.isExpired()) {
            hits++;
            return entry.ip;
        }
        misses++;
        UpstreamResult fetched = upstream.apply(domain);
        cache.put(domain, new DnsEntry(domain, fetched.ip(), fetched.ttlSeconds()));
        return fetched.ip();
    }

    public synchronized CacheStats getStats() {
        long total = hits + misses;
        double hitRate = total == 0 ? 0.0 : (double) hits / total;
        return new CacheStats(hits, misses, hitRate);
    }

    private synchronized void evictExpired() {
        cache.values().removeIf(DnsEntry::isExpired);
    }

    public record UpstreamResult(String ip, long ttlSeconds) {}
    public record CacheStats(long hits, long misses, double hitRate) {}

    private static class DnsEntry {
        final String domain;
        final String ip;
        final long expiryEpochSeconds;

        DnsEntry(String domain, String ip, long ttlSeconds) {
            this.domain = domain;
            this.ip = ip;
            this.expiryEpochSeconds = Instant.now().getEpochSecond() + ttlSeconds;
        }

        boolean isExpired() {
            return Instant.now().getEpochSecond() >= expiryEpochSeconds;
        }
    }

    // Demo
    public static void main(String[] args) throws InterruptedException {
        Problem3DnsCache cache = new Problem3DnsCache(2);
        Function<String, UpstreamResult> upstream = d -> new UpstreamResult("1.1.1.1", 1);
        System.out.println(cache.resolve("google.com", upstream));
        System.out.println(cache.resolve("google.com", upstream));
        Thread.sleep(1100);
        System.out.println(cache.resolve("google.com", upstream));
        System.out.println(cache.getStats());
    }
}