package Test.Experiment;

import Class.DQ.Dataset;
import Class.EPDS.SecureDataset;
import Methods.EPDS.SimilarityCalculate;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TimeCost {
    public static void main(String[] args) {
        //String path = "D:\\lpy\\datasets\\trackable\\Distribution14"; // 指定实际的路径
        //String datasetName = "trackable"; // 指定数据集名
        //int number = 60000;//数据集数量
        //int k=10;           //top-k查询
        int queryNumber = 100;//重复次数
        //float resolution = 0.01f;
        //int numberOfHashFunctions=3;
        //int diemnsional = 200;

        String[] datasets = {"public", "identifiable", "trackable"};
        for (String dataset: datasets){
            String path = "D:\\lpy\\datasets\\" + dataset + "\\Distribution14";
            //int k=10;
            int dimensional = 600;
            int number;
            if (dataset.equals("trackable")){
                number=60000;
            }else {
                number=100000;
            }
            int numberOfHashFunctions=3;
            float resolution = 0.01f;
            //int dimensional;
            int k=10;

            timeExperiment(k,dataset,path,number,resolution,numberOfHashFunctions,dimensional);
        }

        /*SecureDataset secureDataset = new SecureDataset(datasetName,path, number,resolution,numberOfHashFunctions,diemnsional);
        secureDataset.constructInnerINdex();
        secureDataset.constructExternalIndex();

        *//*List<Integer> queryId = new ArrayList<>(100);

        for (int n=0;n<100;n++){
            queryId.add(n+1);
        }*//*

        SimilarityCalculate.similarityCalculate(secureDataset,k,queryNumber);

        SimilarityCalculate.similarityCalculateWithInnerTree(secureDataset,k,queryNumber);
        SimilarityCalculate.RankSearch(secureDataset,k,0.2f,queryNumber);
            //SimilarityCalculate.RankSearchWithExternalTree(secureDataset,k,0.3f,queryNumber);
        SimilarityCalculate.RankSearchWithRankAndTree(secureDataset,k,0.2f,queryNumber);
        //SimilarityCalculate.RankSearchWithDoubleTree(secureDataset,k,0.2f,queryNumber);
        System.out.println();*/

    }

    public static void timeExperiment(int k, String datasetName, String path, int number, float resolution, int numberOfHash, int dimensional){
        /*SecureDataset secureDataset = new SecureDataset(datasetName,path, number,resolution,numberOfHash,dimensional);
        System.out.println((double) ObjectSizeCalculator.getObjectSize(secureDataset)/1073741824);
        secureDataset.constructInnerINdex();
        secureDataset.constructExternalIndex();*/

        Dataset dataset = new Dataset(datasetName,path,number);

        //System.out.println(ObjectSizeCalculator.getObjectSize(secureDataset));

        /*List<Integer> queryId = new ArrayList<>(100);

        for (int n=0;n<100;n++){
            queryId.add(n+1);
        }*/
        System.out.println("查询数据集" + datasetName);
        //System.out.println("tip-k:" + k);
        System.out.println((double)ObjectSizeCalculator.getObjectSize(dataset)/1073741824);

        //SimilarityCalculate.similarityCalculate(secureDataset,k,100);

        //SimilarityCalculate.similarityCalculateWithInnerTree(secureDataset,k,100);
        //SimilarityCalculate.RankSearch(secureDataset,k,0.2f,100);
        //SimilarityCalculate.RankSearchWithExternalTree(secureDataset,k,0.3f,queryNumber);
        //SimilarityCalculate.RankSearchWithRankAndTree(secureDataset,k,0.2f,100);
        //SimilarityCalculate.RankSearchWithDoubleTree(secureDataset,k,0.2f,queryNumber);
        System.out.println();
    }
}
