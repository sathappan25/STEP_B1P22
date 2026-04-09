import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class Problem6RateLimiter {
    private final long windowSeconds;
    private final int maxTokens;
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public Problem6RateLimiter(int maxTokens, long windowSeconds) {
        this.maxTokens = maxTokens;
        this.windowSeconds = windowSeconds;
    }

    public RateLimitResult check(String clientId) {
        long now = Instant.now().getEpochSecond();
        TokenBucket bucket = buckets.computeIfAbsent(clientId, k -> new TokenBucket(maxTokens, now));
        return bucket.tryConsume(now, maxTokens, windowSeconds);
    }

    private static class TokenBucket {
        private int tokens;
        private long windowStart;

        TokenBucket(int tokens, long windowStart) {
            this.tokens = tokens;
            this.windowStart = windowStart;
        }

        synchronized RateLimitResult tryConsume(long now, int capacity, long windowSeconds) {
            if (now - windowStart >= windowSeconds) {
                tokens = capacity;
                windowStart = now;
            }
            if (tokens > 0) {
                tokens--;
                return new RateLimitResult(true, tokens, windowStart + windowSeconds - now);
            }
            return new RateLimitResult(false, 0, windowStart + windowSeconds - now);
        }
    }

    public record RateLimitResult(boolean allowed, int remaining, long retryAfterSeconds) {}

    // Demo
    public static void main(String[] args) {
        Problem6RateLimiter limiter = new Problem6RateLimiter(2, 3600);
        System.out.println(limiter.check("abc"));
        System.out.println(limiter.check("abc"));
        System.out.println(limiter.check("abc"));
    }
}