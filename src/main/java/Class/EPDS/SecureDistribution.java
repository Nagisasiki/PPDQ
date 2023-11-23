package Class.EPDS;

import Class.DQ.DistriMap;
import Class.PPDQ.SecureDistriMap;
import Methods.EPDS.InnerIndex;
import Methods.EPDS.LocationVector;
import Methods.PPDQ.DensityVector;
import Methods.PPDQ.SecureInnerProductCalculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecureDistribution {
    private int id;
    private int num;
    private Map<LocationVector, DensityVector> map;
    private float resolution;  //密度掩码的分辨率，表示每个非零有效位代表的密度大
    private int numberOfHashFunctions; //HMAC哈希函数的数量
    private int dimensional; //位置向量的维度

    private InnerNode innerIndex;


    private double[] globeLocationVector;

    private double priority;

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public InnerNode getInnerIndex() {
        return innerIndex;
    }

    public double getPriority() {
        return priority;
    }

    public double[] getGlobeLocationVector() {
        return globeLocationVector;
    }

    public void setInnerIndex(){
        this.innerIndex = InnerIndex.buildTree(map,numberOfHashFunctions);
    }


    //构造函数，输入id，路径以及安全内积计算器
    public SecureDistribution(int id,float resolution, int numberOfHashFunctions, int dimensional, String filePath) {
        this.id = id;
        this.num = 0;
        this.numberOfHashFunctions = numberOfHashFunctions;
        this.dimensional = dimensional;
        this.resolution = resolution;
        this.map = new HashMap<>();
        this.globeLocationVector = new double[dimensional];
        loadFromFile(filePath);
    }

    private void loadFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        //this.map.toString();
    }

    private void processLine(String line) throws NoSuchAlgorithmException, InvalidKeyException {
        String[] parts = line.split(",");
        if (parts.length == 3) {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            float value = Float.parseFloat(parts[2]);
            Pair pair = new Pair(x,y);

            LocationVector locationVector = new LocationVector(dimensional,numberOfHashFunctions);
            locationVector.add(pair);
            LocationVector.locationVectorORFunction(globeLocationVector,locationVector.getLocationVector());
            DensityVector densityVector = new DensityVector(value, resolution);
            //locationVector.printVector();
            /*for (int i=0;i<20;i++){
                System.out.print(globeLocationVector[i] + ", ");
            }
            System.out.println();*/

            //locationVector.setLocationVector(CalculatorForLocation.encryptVector(locationVector.getLocationVector(),true));
            //densityVector.setDensityVector(CalculatorForDensity.encryptVector(densityVector.getDensityVector(),true));

            //densityVector.setDensityVector(secureInnerProductCalculator.encryptVector(densityVector.getDensityVector(), true));
            map.put(locationVector,densityVector);
            num++;
        }
    }

    public void printMap() {
        System.out.println("id = " + this.id);
        System.out.println("num = " + this.num);

        for (Map.Entry<LocationVector, DensityVector> entry : this.map.entrySet()) {
            LocationVector locationVector = entry.getKey();
            DensityVector densityVector = entry.getValue();
            locationVector.printVector();
            DensityVector.printVector(densityVector.getDensityVector());
        }
    }

    public void encrypt(SecureInnerProductCalculator CalculatorForLocation, SecureInnerProductCalculator CalculatorForDensity){
        for (Map.Entry<LocationVector, DensityVector> entry : this.map.entrySet()) {
            LocationVector locationVector = entry.getKey();
            DensityVector densityVector = entry.getValue();
            locationVector.setLocationVector(CalculatorForLocation.encryptVector(locationVector.getLocationVector(),true));
            densityVector.setDensityVector(CalculatorForDensity.encryptVector(densityVector.getDensityVector(),true));
        }
    }

    public void encryptquery(SecureInnerProductCalculator CalculatorForLocation, SecureInnerProductCalculator CalculatorForDensity){
        for (Map.Entry<LocationVector, DensityVector> entry : this.map.entrySet()) {
            LocationVector locationVector = entry.getKey();
            DensityVector densityVector = entry.getValue();
            locationVector.setLocationVector(CalculatorForLocation.encryptVector(locationVector.getLocationVector(),false));
            densityVector.setDensityVector(CalculatorForDensity.encryptVector(densityVector.getDensityVector(),false));
        }
    }

    public int getId() {
        return this.id;
    }

    public int getNum() {
        return this.num;
    }


    public Map<LocationVector, DensityVector> getMap() {
        return map;
    }

    public int getNumberOfHashFunctions(){
        return this.numberOfHashFunctions;
    }

    public static double calculateSimilarity(SecureDistribution secureDistribution1, SecureDistribution secureDistribution2){
        double similarity = 0;
        float tolerance = 0.001f;
        int numberOfHashFunctions = secureDistribution1.getNumberOfHashFunctions();

        Map<LocationVector, DensityVector> mapA = secureDistribution1.getMap();
        Map<LocationVector, DensityVector> mapB = secureDistribution2.getMap();

        for (Map.Entry<LocationVector, DensityVector> entryA : mapA.entrySet()) {
            LocationVector locationVectorA = entryA.getKey();
            DensityVector densityVectorA = entryA.getValue();
            for (Map.Entry<LocationVector,DensityVector> entryB : mapB.entrySet()){

                LocationVector locationVectorB = entryB.getKey();
                DensityVector densityVectorB = entryB.getValue();

                //System.out.println(Math.abs(LocationVector.calculateInnerProduct(locationVectorA,locationVectorB)));

                if (Math.abs(LocationVector.calculateInnerProduct(locationVectorA,locationVectorB)-numberOfHashFunctions) < tolerance){

                    similarity += DensityVector.calculateInnerProduct(densityVectorA, densityVectorB);
                    break;
                }
            }

        }


        return similarity;
    }

    //通过数据集内部索引计算数据集之间的相似度
    public static double similarityWithInnerIndex(SecureDistribution secureDistribution1, SecureDistribution secureDistribution2){
        double similarity = 0;
        float tolerance = 0.001f;
        int numberOfHashFunctions = secureDistribution1.getNumberOfHashFunctions();
        InnerNode root = secureDistribution1.getInnerIndex();
        Map<LocationVector, DensityVector> mapB = secureDistribution2.getMap();
        for (Map.Entry<LocationVector,DensityVector> entryB : mapB.entrySet()){

            LocationVector locationVectorB = entryB.getKey();
            DensityVector densityVectorB = entryB.getValue();


            double[] densityvectorA =  InnerIndex.findDensityVector(root,locationVectorB.getLocationVector(),numberOfHashFunctions,tolerance);
            if (densityvectorA!=null) {
                //System.out.println("sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

                similarity += DensityVector.calculateInnerProduct(densityvectorA, densityVectorB.getDensityVector());
                //break;
            }

            //System.out.println(Math.abs(LocationVector.calculateInnerProduct(locationVectorA,locationVectorB)));

        }

        return similarity;
    }




    public static class Pair {
        private int x;
        private int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            String binaryX = Integer.toBinaryString(x);
            String binaryY = Integer.toBinaryString(y);
            String combinedBinary = binaryX + "|" + binaryY;
            int hash = combinedBinary.hashCode();
            return hash;
        }

        public byte[] intToBytes(int value) {
            ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
            buffer.putInt(value);
            return buffer.array();
        }

        public void printByteArray(byte[] array) {
            for (byte b : array) {
                System.out.print(b + " ");
            }
            System.out.println();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Pair other = (Pair) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public String toString() {
            return "x = " + this.x + ", y = " + y + ", hashCode = " + hashCode();
        }
    }

   /* public static List<SecureDistribution> createSecureDistriMaps(String directoryPath, int number) {
        List<SecureDistribution> secureDistriMaps = new ArrayList<>();

        for (int i = 1; i <= number; i++) {
            String filePath = directoryPath + "/distribution_" + i + ".txt";
            SecureDistribution secureDistriMap = new SecureDistribution(secureDistriMaps.size(), , filePath);
            secureDistriMaps.add(secureDistriMap);
        }

        return secureDistriMaps;
    }*/

}
