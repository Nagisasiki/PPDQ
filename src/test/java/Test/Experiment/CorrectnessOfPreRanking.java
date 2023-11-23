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
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CorrectnessOfPreRanking {
    public static void main(String[] args) {
        String path = "D:\\lpy\\datasets\\trackable\\Distribution14"; // 指定实际的路径
        String datasetName = "trackable"; // 指定数据集名
        int number = 60000;//数据集数量
        int k=10;           //top-k查询
        int queryNumber = 100;//重复次数

        //Dataset dataset = new Dataset(datasetName,path,10000);
        SecureDataset secureDataset = new SecureDataset(datasetName,path, number,0.01f,3,600);
        secureDataset.constructInnerINdex();
        secureDataset.constructExternalIndex();

        List<Integer> queryId = new ArrayList<>(50000);

        for (int n=0;n<50000;n++){
            queryId.add(n,generateRandomInt(1,50000));
        }
        /*for (int n=0;n<100;n++){
            queryId.add(n+1);
        }*/

        /*for (int i = (int) (number*0.02);i<=(int) (number*0.3);i+=(int) (number*0.02)){

            SimilarityCalculate.calculationCorrectness(secureDataset,k,i,queryNumber,queryId);
        }*/

        for (int j=9;j<k;j+=2){
            if (j==9)
                j++;
            for (int i = (int) (number*0.02);i<=(int) (number*0.3);i+=(int) (number*0.02)){

                SimilarityCalculate.calculationCorrectness(secureDataset,j,i,queryNumber,queryId);
            }

        }
        /*for (int i = (int) (number*0.01);i<=(int) (number*0.1);i+=(int) (number*0.01)){

            SimilarityCalculate.calculationCorrectness(secureDataset,k,i,queryNumber);
        }*/

    }
    public static int generateRandomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
