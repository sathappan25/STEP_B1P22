import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class Problem5RealTimeAnalytics {
    private final ConcurrentMap<String, LongAdder> pageViews = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, ConcurrentHashMap.KeySetView<String, Boolean>> uniqueVisitors = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, LongAdder> sourceCounts = new ConcurrentHashMap<>();

    public void processEvent(String url, String userId, String source) {
        pageViews.computeIfAbsent(url, k -> new LongAdder()).increment();
        uniqueVisitors.computeIfAbsent(url, k -> ConcurrentHashMap.newKeySet()).add(userId);
        sourceCounts.computeIfAbsent(source, k -> new LongAdder()).increment();
    }

    public Dashboard getDashboard(int topN) {
        List<PageStat> topPages = pageViews.entrySet().stream()
                .map(e -> new PageStat(e.getKey(), e.getValue().longValue(), uniqueVisitors.getOrDefault(e.getKey(), ConcurrentHashMap.newKeySet()).size()))
                .sorted(Comparator.comparingLong(PageStat::views).reversed())
                .limit(topN)
                .toList();
        return new Dashboard(topPages, Map.copyOf(sourceCounts));
    }

    public record PageStat(String url, long views, long unique) {}
    public record Dashboard(List<PageStat> topPages, Map<String, LongAdder> sources) {}

    // Demo
    public static void main(String[] args) {
        Problem5RealTimeAnalytics analytics = new Problem5RealTimeAnalytics();
        analytics.processEvent("/breaking", "u1", "google");
        analytics.processEvent("/breaking", "u2", "facebook");
        analytics.processEvent("/sports", "u1", "google");
        System.out.println(analytics.getDashboard(10));
    }
}