public class RiskLookup{
    static int linearSearch(int[] a,int t){
        int c=0;
        for(int i=0;i<a.length;i++){
            c++;
            if(a[i]==t){
                System.out.println("Linear idx="+i+" comp="+c);
                return i;
            }
        }
        System.out.println("Linear idx=-1 comp="+c);
        return -1;
    }
    static int lowerBound(int[] a,int t){
        int l=0,h=a.length;
        while(l<h){
            int m=(l+h)/2;
            if(a[m]<t)l=m+1;
            else h=m;
        }
        return l;
    }
    static int upperBound(int[] a,int t){
        int l=0,h=a.length;
        while(l<h){
            int m=(l+h)/2;
            if(a[m]<=t)l=m+1;
            else h=m;
        }
        return l;
    }
    static int floor(int[] a,int t){
        int i=lowerBound(a,t);
        if(i<a.length&&a[i]==t)return a[i];
        return i-1>=0?a[i-1]:-1;
    }
    static int ceil(int[] a,int t){
        int i=lowerBound(a,t);
        return i<a.length?a[i]:-1;
    }
    static int insertionPoint(int[] a,int t){
        return lowerBound(a,t);
    }
    public static void main(String[] args){
        int[] risks={10,25,50,100};
        linearSearch(risks,30);
        int f=floor(risks,30);
        int c=ceil(risks,30);
        int pos=insertionPoint(risks,30);
        System.out.println("floor="+f+" ceil="+c+" insert="+pos);
    }
}