package Test.Experiment;

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

public class CorrectnessOfPreRanking {
    public static void main(String[] args) {
        String path = "D:\\lpy\\datasets\\public\\Distribution10"; // 指定实际的路径
        String datasetName = "public"; // 指定数据集名
        int number = 100000;//数据集数量
        int k=3;           //top-k查询
        int queryNumber = 10;//重复次数

        //Dataset dataset = new Dataset(datasetName,path,10000);
        SecureDataset secureDataset = new SecureDataset(datasetName,path, number,0.01f,3,400);
        secureDataset.constructInnerINdex();
        secureDataset.constructExternalIndex();

        for (int i = (int) (number*0.1);i<=(int) (number*0.5);i+=(int) (number*0.1)){
            SimilarityCalculate.calculationCorrectness(secureDataset,k,i,queryNumber);
        }


    }
}
