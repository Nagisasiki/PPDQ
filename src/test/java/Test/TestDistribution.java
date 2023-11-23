package Test;
import Class.DQ.Dataset;
import Class.DQ.DistriMap;
import Class.DQ.GenQuery;
import Class.DQ.SimilarIndex;
import Class.EPDS.Distribution;
import Class.EPDS.SecureDataset;
import Class.EPDS.SecureDistribution;
import Class.PPDQ.SecureDistriMap;
import Methods.DQ.SearchWithDistribution;
import Methods.DQ.SearchWithSimilarIndex;
import Methods.EPDS.DensityVector;
import Methods.EPDS.LocationVector;
import Methods.EPDS.SimilarityCalculate;
import Methods.PPDQ.SecureInnerProductCalculator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

public class TestDistribution {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        String path = "D:\\lpy\\datasets\\public\\distributions"; // 指定实际的路径
        String datasetName = "public"; // 指定数据集名

        Dataset dataset = new Dataset(datasetName,path,10000);
        SimilarityCalculate.oneSimilarityCalculateOnPlaintext(dataset,10,545);
        SecureDataset secureDataset = new SecureDataset(datasetName,path, 10000,0.01f,3,400);
        secureDataset.constructInnerINdex();
        secureDataset.constructExternalIndex();

        //SimilarityCalculate.similarityCalculateOnPlaintext(dataset,10,1);
        //secureDataset.printDataset();
        //SimilarityCalculate.similarityCalculate(secureDataset,5,1);

        //SimilarityCalculate.RankSearch(secureDataset,10,2500f,1);

        //SimilarityCalculate.similarityCalculateWithInnerTree(secureDataset,10,1);

        //SimilarityCalculate.RankSearchWithExternalTree(secureDataset,10,2500f,1);

        //SimilarityCalculate.RankSearchWithDoubleTree(secureDataset,10,2500f,1);*/

        //SimilarityCalculate.oneRankSearchWithDoubleTree(secureDataset,10,100,1);

        SimilarityCalculate.oneSimilarityCalculateWithInnerTree(secureDataset,10,545);

       SimilarityCalculate.vectorCorrectness(SimilarityCalculate.oneSimilarityCalculateOnPlaintext(dataset,10,2),SimilarityCalculate.oneSimilarityCalculateWithInnerTree(secureDataset,10,2),10);

        //System.out.println(SimilarityCalculate.correctness(SimilarityCalculate.oneRankSearchWithDoubleTree(secureDataset,10,500,1),SimilarityCalculate.oneSimilarityCalculateWithInnerTree(secureDataset,10,1),10));

        //String path = "E:\\lpy\\datasets\\public\\distributions\\distribution_1.txt"; // 指定实际的路径
        //SecureDistribution secureDistribution = new SecureDistribution(1,0.01f,3,20,path);

        //SimilarityCalculate.calculationCorrectness(secureDataset,10,300,30);


        /*Dataset dataset = new Dataset(datasetName,path,20);
        //SearchWithDistribution.topkSearch(dataset,1,10);
        SimilarityCalculate.similarityCalculateOnPlaintext(dataset,10);*/


        /*LocationVector locationVector1 = new LocationVector(20,3);
        LocationVector locationVector2 = new LocationVector(20,3);
        locationVector1.printVector();
        locationVector2.printVector();
        SecureDistribution.Pair pair1 = new SecureDistribution.Pair(12,34);
        SecureDistribution.Pair pair2 = new SecureDistribution.Pair(24,74);
        locationVector1.add(pair1);
        locationVector2.add(pair2);
        locationVector1.printVector();
        locationVector2.printVector();
        //LocationVector.locationVectorORFunction(locationVector1,locationVector2);
        LocationVector.locationVectorORFunction(locationVector1.getLocationVector(),locationVector2.getLocationVector());
        locationVector1.printVector();
        locationVector2.printVector();*/

        /*DistriMap distriMap = new DistriMap(1,path);
        float similar1 = SearchWithDistribution.calculateSimilarity(distriMap,distriMap);
        System.out.println("相似度：" + similar1);

        SecureInnerProductCalculator CalculatorForLocation = new SecureInnerProductCalculator(200);

        SecureInnerProductCalculator CalculatorForDensity = new SecureInnerProductCalculator(200);

        SecureDistribution secureDistribution = new SecureDistribution(1,0.01f,3,200, path);


        SecureDistribution secureDistribution1 = new SecureDistribution(1,0.01f,3,200, path);

        *//*double similar = SecureDistribution.calculateSimilarity(secureDistribution,secureDistribution);
        System.out.println("相似度：" + similar);*//*

        secureDistribution.encrypt(CalculatorForLocation,CalculatorForDensity);
        secureDistribution1.encryptquery(CalculatorForLocation,CalculatorForDensity);


        double similar2 = SecureDistribution.calculateSimilarity(secureDistribution,secureDistribution1);
        System.out.println("相似度：" + similar2);*/

        /*LocationVector locationVector1 = new LocationVector(200,3);
        LocationVector locationVector2 = new LocationVector(200,3);
        SecureDistriMap.Pair pair = new SecureDistriMap.Pair(12,23);
        locationVector1.add(pair);
        locationVector2.add(pair);
        locationVector1.printVector();
        locationVector2.printVector();
        System.out.println(locationVector1.getLocationVector().length);

        locationVector1.setLocationVector(CalculatorForLocation.encryptVector(locationVector1.getLocationVector(),true));
        locationVector2.setLocationVector(CalculatorForLocation.encryptVector(locationVector2.getLocationVector(),false));
        locationVector1.printVector();
        locationVector2.printVector();

        double result = LocationVector.calculateInnerProduct(locationVector1,locationVector2);
        System.out.println(result);*/

    }
}
