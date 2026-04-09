import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class Problem2FlashSaleInventory {
    private final ConcurrentHashMap<String, AtomicInteger> stock = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Queue<Long>> waitlists = new ConcurrentHashMap<>();

    public void setInitialStock(String productId, int count) {
        stock.put(productId, new AtomicInteger(count));
    }

    public int checkStock(String productId) {
        AtomicInteger remaining = stock.get(productId);
        return remaining == null ? 0 : remaining.get();
    }

    public PurchaseResult purchaseItem(String productId, long userId) {
        stock.putIfAbsent(productId, new AtomicInteger(0));
        AtomicInteger remaining = stock.get(productId);
        while (true) {
            int current = remaining.get();
            if (current <= 0) {
                waitlists.computeIfAbsent(productId, k -> new ConcurrentLinkedQueue<>()).add(userId);
                return new PurchaseResult(false, 0, waitlists.get(productId).size());
            }
            if (remaining.compareAndSet(current, current - 1)) {
                return new PurchaseResult(true, current - 1, -1);
            }
        }
    }

    public Queue<Long> getWaitlist(String productId) {
        return waitlists.getOrDefault(productId, new ConcurrentLinkedQueue<>());
    }

    public record PurchaseResult(boolean success, int remaining, int waitlistPosition) {}

    // Demo
    public static void main(String[] args) {
        Problem2FlashSaleInventory inv = new Problem2FlashSaleInventory();
        inv.setInitialStock("IPHONE15_256GB", 2);
        System.out.println(inv.purchaseItem("IPHONE15_256GB", 1));
        System.out.println(inv.purchaseItem("IPHONE15_256GB", 2));
        System.out.println(inv.purchaseItem("IPHONE15_256GB", 3));
        System.out.println("Waitlist: " + inv.getWaitlist("IPHONE15_256GB"));
    }
}