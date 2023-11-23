package Methods.EPDS;

import java.util.Random;

public class VectorOperations600 {
    private static double[][] vectors; // �洢���600ά����

    // ��ʼ����������
    static {
        int numVectors = 10; // ������10��600ά����
        int vectorSize = 500;
        vectors = new double[numVectors][vectorSize];
        Random random = new Random();

        for (int i = 0; i < numVectors; i++) {
            for (int j = 0; j < vectorSize; j++) {
                vectors[i][j] = random.nextDouble(); // ����0��1֮��������
            }
        }
    }

    // ���������������ڻ�
    public static void calculateDotProduct() {
        Random random = new Random();
        int vector1Index = random.nextInt(vectors.length);
        int vector2Index;

        do {
            vector2Index = random.nextInt(vectors.length);
        } while (vector2Index == vector1Index);

        double[] vector1 = vectors[vector1Index];
        double[] vector2 = vectors[vector2Index];

        double dotProduct = 0;
        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
        }

        //return dotProduct;
    }

    public static void main(String[] args) {
        
        long s= System.nanoTime();
        calculateDotProduct();
        long e =System.nanoTime();
        System.out.println(e-s);
        LocationVector locationVector1 = new LocationVector(600,3);
        LocationVector locationVector2 = new LocationVector(600,3);
        s= System.nanoTime();
        LocationVector.calculateInnerProduct(locationVector1,locationVector2);
        e =System.nanoTime();
        System.out.println(e-s);
        //System.out.println("�ڻ����: " + result);
    }
}

