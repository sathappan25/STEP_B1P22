
import java.util.ArrayList;

class Transaction{
    String id;
    double fee;
    String timestamp;
    public Transaction(String id, double fee, String timestamp){
        this.id = id;
        this.fee = fee;
        this.timestamp = timestamp;
    }    
}

public class TransactionFeeSorting{
    public static void printFee(ArrayList<Transaction> list){
        System.out.print("BubbleSort (fees): ");
        for(Transaction t : list){
            System.out.print(t.id + ":" + t.fee + " ");
        }
        System.out.println();
    }
    public static void printFeeTime(ArrayList<Transaction> list){
        System.out.print("InsertionSort(fee+ts): ");
        for(Transaction t : list){
            System.out.print(t.id + ":" + t.fee + "@" + t.timestamp + " ");
        }
        System.out.println();
    }
    public static void printOutliers(ArrayList<Transaction> list){
        System.out.print("High-fee outliers: ");
        boolean found = false;
        for(Transaction t : list){
            if(t.fee > 50){
                System.out.print(t.id + ":" + t.fee + " ");
                found = true;
            }
        }
        if(!found){
            System.out.print("none");
        }
        System.out.println();
    }
    public static void bubbleSortByFee(ArrayList<Transaction> list){
        boolean swapped = true;
        while(swapped){
            swapped = false;
            for(int i = 0; i < list.size() - 1; i++){
                if(list.get(i).fee > list.get(i+1).fee){
                    Transaction temp = list.get(i);
                    list.set(i, list.get(i+1));
                    list.set(i+1, temp);
                    swapped = true;
                }
            }
        }
    }
    public static void insertionSort(ArrayList<Transaction> list){
        for(int i = 1; i < list.size(); i++){
            Transaction key = list.get(i);
            int j = i-1;
            while(j >= 0 && (list.get(j).fee > key.fee || (list.get(j).fee == key.fee && list.get(j).timestamp.compareTo(key.timestamp) > 0))){
                list.set(j+1, list.get(j));
                j--;
            }
            list.set(j+1, key);
        }
    }
    public static void main(String[] args) {
        ArrayList<Transaction> list = new ArrayList<>();
        list.add(new Transaction("id1", 10.5, "10:00"));
        list.add(new Transaction("id2", 25.0, "09:30"));
        list.add(new Transaction("id3", 5.0, "10:15"));
        int n = list.size();
        if (n <= 100) {
            bubbleSortByFee(list);
            printFee(list);
        } 
        else if (n <= 1000) {
            insertionSort(list);
            printFeeTime(list);
        }
        
    }
}