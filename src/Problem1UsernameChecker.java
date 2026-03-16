import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class Problem1UsernameChecker {
    private final ConcurrentHashMap<String, Integer> userDirectory = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LongAdder> attemptFreq = new ConcurrentHashMap<>();

    public boolean checkAvailability(String username) {
        recordAttempt(username);
        return !userDirectory.containsKey(username);
    }

    public void addUser(String username, int userId) {
        userDirectory.putIfAbsent(username, userId);
    }

    public List<String> suggestAlternatives(String base, int limit) {
        List<String> suggestions = new ArrayList<>(limit);
        int suffix = 1;
        while (suggestions.size() < limit) {
            String candidate = base + suffix;
            if (!userDirectory.containsKey(candidate)) {
                suggestions.add(candidate);
            }
            suffix++;
        }
        String dotted = base.replace('_', '.');
        if (suggestions.size() < limit && !userDirectory.containsKey(dotted)) {
            suggestions.add(dotted);
        }
        return suggestions;
    }

    public String getMostAttempted() {
        return attemptFreq.entrySet().stream()
                .max(Map.Entry.comparingByValue(Comparator.comparingLong(LongAdder::longValue)))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private void recordAttempt(String username) {
        attemptFreq.computeIfAbsent(username, k -> new LongAdder()).increment();
    }

    // Demo
    public static void main(String[] args) {
        Problem1UsernameChecker checker = new Problem1UsernameChecker();
        checker.addUser("john_doe", 1);
        checker.addUser("admin", 2);
        System.out.println("john_doe available? " + checker.checkAvailability("john_doe"));
        System.out.println("jane_smith available? " + checker.checkAvailability("jane_smith"));
        System.out.println("Suggestions for john_doe: " + checker.suggestAlternatives("john_doe", 3));
        System.out.println("Most attempted: " + checker.getMostAttempted());
    }
}