import java.time.*;
import java.util.*;

public class Problem9TwoSumVariants {
    public static List<int[]> twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        List<int[]> pairs = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (seen.containsKey(need)) pairs.add(new int[]{seen.get(need), i});
            seen.put(nums[i], i);
        }
        return pairs;
    }

    public static List<Transaction[]> twoSumWithinWindow(List<Transaction> tx, int target, long windowSeconds) {
        List<Transaction[]> pairs = new ArrayList<>();
        Map<Integer, Transaction> map = new HashMap<>();
        for (Transaction t : tx) {
            map.entrySet().removeIf(e -> t.epochSeconds - e.getValue().epochSeconds > windowSeconds);
            int need = target - t.amount;
            Transaction other = map.get(need);
            if (other != null) pairs.add(new Transaction[]{other, t});
            map.put(t.amount, t);
        }
        return pairs;
    }

    public static List<List<Integer>> kSum(int[] nums, int k, int target) {
        Arrays.sort(nums);
        return kSumHelper(nums, 0, k, target);
    }

    private static List<List<Integer>> kSumHelper(int[] nums, int start, int k, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (k == 2) {
            int l = start, r = nums.length - 1;
            while (l < r) {
                int sum = nums[l] + nums[r];
                if (sum == target) {
                    res.add(List.of(nums[l], nums[r]));
                    l++; r--;
                } else if (sum < target) l++; else r--;
            }
            return res;
        }
        for (int i = start; i <= nums.length - k; i++) {
            if (i > start && nums[i] == nums[i - 1]) continue;
            for (List<Integer> subset : kSumHelper(nums, i + 1, k - 1, target - nums[i])) {
                List<Integer> combined = new ArrayList<>();
                combined.add(nums[i]);
                combined.addAll(subset);
                res.add(combined);
            }
        }
        return res;
    }

    public static Map<String, Set<String>> detectDuplicates(List<Transaction> tx) {
        Map<String, Set<String>> dupes = new HashMap<>();
        for (Transaction t : tx) {
            String key = t.amount + "|" + t.merchant;
            dupes.computeIfAbsent(key, k -> new HashSet<>()).add(t.accountId);
        }
        dupes.entrySet().removeIf(e -> e.getValue().size() < 2);
        return dupes;
    }

    public record Transaction(int id, int amount, String merchant, long epochSeconds, String accountId) {}

    // Demo
    public static void main(String[] args) {
        int[] nums = {500, 300, 200};
        System.out.println(twoSum(nums, 500));
        List<Transaction> tx = List.of(
                new Transaction(1, 300, "A", Instant.now().getEpochSecond(), "acc1"),
                new Transaction(2, 200, "A", Instant.now().getEpochSecond() + 10, "acc2"),
                new Transaction(3, 300, "A", Instant.now().getEpochSecond() + 20, "acc3")
        );
        System.out.println(twoSumWithinWindow(tx, 500, 3600));
        System.out.println(detectDuplicates(tx));
    }
}