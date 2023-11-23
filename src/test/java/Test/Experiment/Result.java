package Test.Experiment;

import Class.EPDS.SecureDataset;
import Methods.EPDS.SimilarityCalculate;

public class Result {
    public static void main(String[] args) {
        String path = "D:\\lpy\\datasets\\public\\Distribution14"; // 指定实际的路径
        String datasetName = "public"; // 指定数据集名
        int number = 100000;//数据集数量
        int k=10;           //top-k查询
        //int queryNumber = 1;//重复次数
        float resolution = 0.01f;
        int numberOfHashFunctions=3;
        int dimensional = 200;

        SecureDataset secureDataset = new SecureDataset(datasetName,path, number,resolution,numberOfHashFunctions,dimensional);
        secureDataset.constructInnerINdex();
        secureDataset.constructExternalIndex();

        SimilarityCalculate.oneSimilarityCalculateWithInnerTree(secureDataset,k,1);
    }
}
