import java.util.ArrayList;
import java.util.List;

public class Kshi {
    public static void main(String[] args) {
        List list=new ArrayList();
        double a=-120.00;
        while(true){
            a=a+1;
            double b=Math.sqrt(a+100);
            double c=Math.sqrt(a+268);
            if ((int)b==b&&(int)c==c){
                list.add(a);
            }
            double e=c*c-(c-1)*(c-1);
            if (c*c-(c-1)*(c-1)>168){
                break;
            }

        }
//        while(true){
//            a=a-1;
//            double b=Math.sqrt(a+100);
//            double c=Math.sqrt(a+268);
//            if ((int)b==b&&(int)c==c){
//                list.add(a);
//            }
//            double e=c*c-(c-1)*(c-1);
//            if (c*c-(c-1)*(c-1)>168){
//                break;
//            }
//        }
        System.out.println(list);

    }
}
