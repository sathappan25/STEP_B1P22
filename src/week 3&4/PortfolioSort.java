

class Asset {
    String name;
    double returnRate, volatility;

    Asset(String n, double r, double v) {
        name = n;
        returnRate = r;
        volatility = v;
    }
}

public class PortfolioSort {

    static void quickSort(Asset[] a, int l, int h) {
        if (h - l <= 10) {
            insertion(a, l, h);
            return;
        }
        if (l < h) {
            int p = partition(a, l, h);
            quickSort(a, l, p - 1);
            quickSort(a, p + 1, h);
        }
    }

    static int partition(Asset[] a, int l, int h) {
        int p = median3(a, l, h);
        swap(a, p, h);

        double pr = a[h].returnRate;
        double pv = a[h].volatility;

        int i = l - 1;

        for (int j = l; j < h; j++) {
            // Sort by: higher return first, lower volatility if tie
            if (a[j].returnRate > pr ||
               (a[j].returnRate == pr && a[j].volatility < pv)) {
                i++;
                swap(a, i, j);
            }
        }

        swap(a, i + 1, h);
        return i + 1;
    }

    static int median3(Asset[] a, int l, int h) {
        int m = (l + h) / 2;

        if (a[l].returnRate > a[m].returnRate) swap(a, l, m);
        if (a[l].returnRate > a[h].returnRate) swap(a, l, h);
        if (a[m].returnRate > a[h].returnRate) swap(a, m, h);

        return m;
    }

    static void insertion(Asset[] a, int l, int h) {
        for (int i = l + 1; i <= h; i++) {
            Asset key = a[i];
            int j = i - 1;

            while (j >= l &&
                  (a[j].returnRate < key.returnRate ||
                  (a[j].returnRate == key.returnRate &&
                   a[j].volatility > key.volatility))) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    static void swap(Asset[] a, int i, int j) {
        Asset t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    static void printAssets(Asset[] a) {
        for (Asset x : a) {
            System.out.println(x.name +
                " | Return: " + x.returnRate +
                " | Volatility: " + x.volatility);
        }
    }

    public static void main(String[] args) {

        Asset[] a = {
            new Asset("AAPL", 12, 5),
            new Asset("TSLA", 8, 7),
            new Asset("GOOG", 15, 4)
        };

        System.out.println("Before Sorting:");
        printAssets(a);

        quickSort(a, 0, a.length - 1);

        System.out.println("\nAfter Sorting (High Return, Low Risk):");
        printAssets(a);
    }
}