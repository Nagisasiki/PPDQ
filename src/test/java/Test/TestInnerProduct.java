package Test;

import Methods.PPDQ.DensityVector;
import Methods.PPDQ.SecureInnerProductCalculator;

public class TestInnerProduct {
    public static void main(String[] args) {
        DensityVector densityVector1 = new DensityVector(0.5f, 0.01f);
        DensityVector densityVector2 = new DensityVector(0.3f,0.01f);

        SecureInnerProductCalculator secureInnerProductCalculator = new SecureInnerProductCalculator(200);

        densityVector1.setDensityVector(secureInnerProductCalculator.encryptVector(densityVector1.getDensityVector(),true));
        densityVector2.setDensityVector(secureInnerProductCalculator.encryptVector(densityVector2.getDensityVector(),false ));


        DensityVector.printVector(densityVector1.getDensityVector());
        DensityVector.printVector(densityVector2.getDensityVector());

        System.out.println(densityVector1.getDensityVector().length);
        System.out.println(densityVector2.getDensityVector().length);


        long stime = System.nanoTime();
        DensityVector.calculateInnerProduct(densityVector1,densityVector2);
        long etime = System.nanoTime();
        System.out.println("内积时间：" + (etime - stime));


        float a = 0.01f;
        float b = 0.02f;

        stime = System.nanoTime();
        float c = Math.min(a,b);
        etime = System.nanoTime();
        System.out.println("比较时间：" + (etime - stime));

        System.out.println(c);
    }
}
