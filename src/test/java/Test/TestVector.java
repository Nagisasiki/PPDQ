package Test;

import Methods.PPDQ.DensityVector;
import Methods.PPDQ.SecureInnerProductCalculator;
//import org.apache.commons.math3.linear.MatrixUtils;
//import org.apache.commons.math3.linear.RealMatrix;

import java.util.Random;

public class TestVector {
    public static void main(String[] args) {

        float density = 0.06f;
        float resolution = 0.01f;
        double[] densityVector = DensityVector.genVector(density,resolution);
        DensityVector.printVector(densityVector);
        System.out.println(densityVector.length);

        double[] vector1 = {1.0,0.0,0.0,0.0};
        double[] vector2 = {1.0,0.0,1.0,0.0};



        SecureInnerProductCalculator secureInnerProductCalculator = new SecureInnerProductCalculator(4);
        secureInnerProductCalculator.printKeyMatrix();

        System.out.println();

        vector1 = secureInnerProductCalculator.multiplyWithKeyMatrix(vector1);
        vector2 = secureInnerProductCalculator.multiplyInverseKeyMatrixWithVector(vector2);

        for (int i=0; i<vector1.length; i++){
            System.out.print(vector1[i] + " ,");
        }
        System.out.println();

        for (int i=0; i<vector1.length; i++){
            System.out.print(vector2[i] + " ,");
        }
        System.out.println();


        double result = DensityVector.calculateInnerProduct(vector1, vector2);
        System.out.println(result);


    }

}
