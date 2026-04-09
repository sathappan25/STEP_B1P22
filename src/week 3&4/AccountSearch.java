import java.util.Arrays;

public class AccountSearch{
    static int linearFirst(String[] a,String t){
        int c=0;
        for(int i=0;i<a.length;i++){
            c++;
            if(a[i].equals(t)){
                System.out.println("LinearFirst idx="+i+" comp="+c);
                return i;
            }
        }
        System.out.println("LinearFirst idx=-1 comp="+c);
        return -1;
    }
    static int linearLast(String[] a,String t){
        int c=0,idx=-1;
        for(int i=0;i<a.length;i++){
            c++;
            if(a[i].equals(t))idx=i;
        }
        System.out.println("LinearLast idx="+idx+" comp="+c);
        return idx;
    }
    static int binarySearch(String[] a,String t){
        int l=0,h=a.length-1,c=0;
        while(l<=h){
            int m=(l+h)/2;
            c++;
            int cmp=a[m].compareTo(t);
            if(cmp==0){
                System.out.println("Binary idx="+m+" comp="+c);
                return m;
            }else if(cmp<0)l=m+1;
            else h=m-1;
        }
        System.out.println("Binary idx=-1 comp="+c);
        return -1;
    }
    static int firstOcc(String[] a,String t){
        int l=0,h=a.length-1,res=-1;
        while(l<=h){
            int m=(l+h)/2;
            if(a[m].compareTo(t)>=0){
                if(a[m].equals(t))res=m;
                h=m-1;
            }else l=m+1;
        }
        return res;
    }
    static int lastOcc(String[] a,String t){
        int l=0,h=a.length-1,res=-1;
        while(l<=h){
            int m=(l+h)/2;
            if(a[m].compareTo(t)<=0){
                if(a[m].equals(t))res=m;
                l=m+1;
            }else h=m-1;
        }
        return res;
    }
    static int count(String[] a,String t){
        int f=firstOcc(a,t);
        if(f==-1)return 0;
        int l=lastOcc(a,t);
        return l-f+1;
    }
    public static void main(String[] args){
        String[] logs={"accB","accA","accB","accC"};
        linearFirst(logs,"accB");
        linearLast(logs,"accB");
        Arrays.sort(logs);
        int idx=binarySearch(logs,"accB");
        int cnt=count(logs,"accB");
        System.out.println("Count="+cnt);
    }
}