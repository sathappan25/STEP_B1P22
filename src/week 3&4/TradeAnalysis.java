class Trade {
    int id, volume;

    Trade(int id, int volume) {
        this.id = id;
        this.volume = volume;
    }
}

public class TradeAnalysis {

    static void mergeSort(Trade[] a, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(a, l, m);
            mergeSort(a, m + 1, r);
            merge(a, l, m, r);
        }
    }

    static void merge(Trade[] a, int l, int m, int r) {
        int n1 = m - l + 1, n2 = r - m;

        Trade[] L = new Trade[n1];
        Trade[] R = new Trade[n2];

        for (int i = 0; i < n1; i++)
            L[i] = a[l + i];
        for (int j = 0; j < n2; j++)
            R[j] = a[m + 1 + j];

        int i = 0, j = 0, k = l;

        while (i < n1 && j < n2) {
            if (L[i].volume <= R[j].volume)
                a[k++] = L[i++];
            else
                a[k++] = R[j++];
        }

        while (i < n1)
            a[k++] = L[i++];

        while (j < n2)
            a[k++] = R[j++];
    }

    static Trade[] mergeLists(Trade[] a, Trade[] b) {
        int i = 0, j = 0, k = 0;
        Trade[] c = new Trade[a.length + b.length];

        while (i < a.length && j < b.length) {
            if (a[i].volume <= b[j].volume)
                c[k++] = a[i++];
            else
                c[k++] = b[j++];
        }

        while (i < a.length)
            c[k++] = a[i++];

        while (j < b.length)
            c[k++] = b[j++];

        return c;
    }

    static long total(Trade[] a) {
        long s = 0;
        for (Trade t : a)
            s += t.volume;
        return s;
    }

    static void printTrades(Trade[] a) {
        for (Trade t : a) {
            System.out.println("ID: " + t.id + ", Volume: " + t.volume);
        }
    }

    public static void main(String[] args) {

        Trade[] t = {
            new Trade(3, 500),
            new Trade(1, 100),
            new Trade(2, 300)
        };

        System.out.println("Before Sorting:");
        printTrades(t);

        mergeSort(t, 0, t.length - 1);

        System.out.println("\nAfter Sorting (Ascending by Volume):");
        printTrades(t);

        Trade[] m1 = {
            new Trade(1, 100),
            new Trade(2, 300)
        };

        Trade[] m2 = {
            new Trade(3, 500)
        };

        Trade[] merged = mergeLists(m1, m2);

        System.out.println("\nMerged List:");
        printTrades(merged);

        long sum = total(merged);
        System.out.println("\nTotal Volume: " + sum);
    }
}