import java.util.ArrayList;

class Client{
    String CName;
    double riskScore;
    public Client(String CName, double riskScore){
        this.CName = CName;
        this.riskScore = riskScore;
    }
}
public class ClientRiskScoreRanking {
    public static void BubbleSort(ArrayList<Client> list){
        boolean swapped = true;
        while(swapped){
            swapped = false;
            for(int i = 0; i < list.size()-1; i++){
                if(list.get(i).riskScore > list.get(i+1).riskScore){
                    Client temp = list.get(i);
                    list.set(i, list.get(i+1));
                    list.set(i+1, temp);
                    swapped = true;
                }
            }
        }
    }
    public static void InsertionSort(ArrayList<Client> list){
        for(int i = 0; i < list.size(); i++){
            Client key = list.get(i);
            int j = i-1;
            while(j >= 0 && (list.get(j).riskScore < key.riskScore)){
                list.set(j+1, list.get(j));
                j--;
            }
            list.set(j+1, key);
        }
    }
    public static void RiskCalc(ArrayList<Client> list){
        InsertionSort(list);
        for(Client c : list){
            System.out.print(c.CName.charAt(c.CName.length()-1)+"("+c.riskScore+") ");
        }
        System.out.println();
    }
    public static void PrintClient(ArrayList<Client> list){
        for(Client c : list){
            System.out.print(c.CName + " : "+ c.riskScore +" ");
        }
        System.out.println();
    }
    public static void main(String[] args) {
        ArrayList<Client> list = new ArrayList<>();
        list.add(new Client("clientC", 80));
        list.add(new Client("clientA", 20));
        list.add(new Client("clientB", 50));
        System.out.print("Bubble Sort: "); BubbleSort(list); PrintClient(list);
        System.out.print("Insertion Sort: "); InsertionSort(list); PrintClient(list);
        RiskCalc(list);
    }
}