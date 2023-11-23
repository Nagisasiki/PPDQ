package Methods.EPDS;

import Class.EPDS.SecureDistribution;
import Class.PPDQ.SecureDistriMap;
import Class.PPDQ.SecureDistriMap.Pair;
import Methods.PPDQ.DensityVector;
import Methods.PPDQ.SecureHMACHashGenerator;
import Methods.PPDQ.SecureInnerProductCalculator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class LocationVector {
    //private BitSet bitSet;
    private byte[][] keys = {{ 0x12, 0x34, 0x56},{ 0x4, 0x36, 0x68},{ 0x36, 0x38, 0x60}, { 0x6, 0x68, 0x50},{ 0x42, 0x24, 0x56},{ 0x62, 0x12, 0x36}};
    private SecureHMACHashGenerator[] hashFunctions;
    private int numHashFunctions;
    private int number;
    private int dimensional;

    private double[] locationVector;


    //private HashFunction<T>[] hashFunctions;



    //构造函数，输入向量维度和哈希函数数量
    public LocationVector(int dimensional, int numHashFunctions) {
        this.number = 0;
        this.dimensional = dimensional;
        this.numHashFunctions = numHashFunctions;
        locationVector = new double[dimensional];
        //System.out.println(numBits*2);
        //bitSet = new BitSet(numBits);
//        list.ensureCapacity(numBits);

        //numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
        this.hashFunctions = new SecureHMACHashGenerator[numHashFunctions];
        for (int i = 0; i<numHashFunctions; i++){
            hashFunctions[i] = new SecureHMACHashGenerator(keys[i]);
        }
        //hashFunctions = createHashFunctions(numHashFunctions);
    }



    public void add(Pair pair) throws NoSuchAlgorithmException, InvalidKeyException {
        for(SecureHMACHashGenerator secureHMACHashGenerator : hashFunctions){
            int hash = secureHMACHashGenerator.generateHMAC(pair.intToBytes(pair.hashCode()));
            //System.out.print(hash + ",");
            locationVector[Math.abs(hash % locationVector.length)] = 1;
        }

    }

    public void add(SecureDistribution.Pair pair) throws NoSuchAlgorithmException, InvalidKeyException {
        for(SecureHMACHashGenerator secureHMACHashGenerator : hashFunctions){
            int hash = secureHMACHashGenerator.generateHMAC(pair.intToBytes(pair.hashCode()));
            //System.out.print(hash + ",");
            if (locationVector[Math.abs(hash % locationVector.length)]==0){
                locationVector[Math.abs(hash % locationVector.length)] = 1;
            }else {
                //System.out.println("********************************************");
                do {
                    hash++;
                }while (locationVector[Math.abs(hash % locationVector.length)]!=0);
                locationVector[Math.abs(hash % locationVector.length)] = 1;
            }
        }

    }

    public void printVector(){
        System.out.println("location cevtor :");
        for (int i = 0; i<locationVector.length; i++){
            System.out.print(locationVector[i] + " ,");
        }
        System.out.println();
    }

    public void printNonZeroLocation(){
        double[] vector = this.locationVector;
        for (int i=0; i<vector.length; i++){
            if (vector[i] != 0)
                System.out.print(i + ",  ");
        }
        System.out.println();
    }


    /*public void encryrtVector(boolean flag){
        SecureInnerProductCalculator secureInnerProductCalculator = new SecureInnerProductCalculator(this.dimensional);
        locationVector = secureInnerProductCalculator.encryptVector(locationVector,flag);
    }*/


    public double[] getLocationVector(){
        return this.locationVector;
    }

    public void setLocationVector(double[] vector){
        this.locationVector = vector;
    }

    public static double calculateInnerProduct(LocationVector vector1, LocationVector vector2) {
        double[] v1 = vector1.getLocationVector();
        double[] v2 = vector2.getLocationVector();
        if (v1.length != v2.length) {
            throw new IllegalArgumentException("Vector dimensions do not match.");
        }

        double result = 0.0;
        for (int i = 0; i < v1.length; i++) {
            result += v1[i] * v2[i];
        }

        VectorOperations600.calculateDotProduct();


        return result;
    }



    //两个位置向量或运算
    public static void locationVectorORFunction(LocationVector resultVector, LocationVector locationVector){
        double[] result = resultVector.locationVector;
        double[] vector = locationVector.locationVector;
        for (int i=0; i<result.length;i++) {
            if (vector[i] != 0)
                result[i]=1;
        }
        resultVector.setLocationVector(result);

    }

    public static void locationVectorORFunction(double[] resultVector, double[] locationVector){

        for (int i=0; i<resultVector.length;i++) {
            if (locationVector[i] != 0)
                resultVector[i]=1;
        }

    }

    public static double[] innerNodeORFunction(double[] left, double[] right){

        double[] result = new double[left.length];

        for (int i=0; i<left.length;i++) {
            if (left[i] != 0 || right[i] !=0 )
                result[i]=1;
        }

        return result;
    }

    public static double calculateInnerProduct(double[] vector1, double[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vector dimensions do not match.");
        }

        double result = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            result += vector1[i] * vector2[i];
        }

        VectorOperations600.calculateDotProduct();


        return result;
    }


}


