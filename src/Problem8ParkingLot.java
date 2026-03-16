import java.time.*;

public class Problem8ParkingLot {
    private final Slot[] table;
    private int occupied = 0;
    private long totalProbes = 0;

    public Problem8ParkingLot(int size) {
        this.table = new Slot[size];
    }

    public synchronized ParkingResult park(String plate) {
        int idx = hash(plate);
        int probes = 0;
        for (int i = 0; i < table.length; i++) {
            int pos = (idx + i) % table.length;
            probes++;
            if (table[pos] == null || table[pos].status == Status.DELETED) {
                table[pos] = new Slot(plate, Instant.now(), Status.OCCUPIED);
                occupied++;
                totalProbes += probes;
                return new ParkingResult(true, pos, probes - 1);
            }
        }
        return new ParkingResult(false, -1, probes - 1);
    }

    public synchronized ExitResult exit(String plate) {
        int idx = hash(plate);
        for (int i = 0; i < table.length; i++) {
            int pos = (idx + i) % table.length;
            Slot slot = table[pos];
            if (slot == null) return new ExitResult(false, Duration.ZERO, 0);
            if (slot.status == Status.OCCUPIED && slot.plate.equals(plate)) {
                Duration stay = Duration.between(slot.start, Instant.now());
                table[pos] = new Slot(null, null, Status.DELETED);
                occupied--;
                return new ExitResult(true, stay, pos);
            }
        }
        return new ExitResult(false, Duration.ZERO, -1);
    }

    public synchronized Stats stats() {
        double occupancy = 100.0 * occupied / table.length;
        double avgProbes = occupied == 0 ? 0 : (double) totalProbes / occupied;
        return new Stats(occupancy, avgProbes);
    }

    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % table.length;
    }

    private enum Status { EMPTY, OCCUPIED, DELETED }
    private record Slot(String plate, Instant start, Status status) {}
    public record ParkingResult(boolean parked, int spot, int probes) {}
    public record ExitResult(boolean exited, Duration duration, int spot) {}
    public record Stats(double occupancyPct, double avgProbes) {}

    // Demo
    public static void main(String[] args) throws InterruptedException {
        Problem8ParkingLot lot = new Problem8ParkingLot(5);
        System.out.println(lot.park("ABC"));
        System.out.println(lot.park("ABD"));
        Thread.sleep(10);
        System.out.println(lot.exit("ABC"));
        System.out.println(lot.stats());
    }
}