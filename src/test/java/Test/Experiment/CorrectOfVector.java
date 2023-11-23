package Test.Experiment;

import Class.DQ.Dataset;
import Class.EPDS.SecureDataset;
import Methods.EPDS.SimilarityCalculate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CorrectOfVector {
    public static void main(String[] args) {
        String path = "D:\\lpy\\datasets\\forecasting_train_v1.1\\train\\Distribution8"; // 指定实际的路径
        String datasetName = "forecasting"; // 指定数据集名
        int number = 100000;//数据集数量
        int k=10;           //top-k查询
        int queryNumber = 10;//重复次数
        float resolution = 0.01f;
        int numberOfHashFunctions=1;
        int diemnsional = 600;

        Dataset dataset = new Dataset(datasetName,path,number);
        SecureDataset secureDataset = new SecureDataset(datasetName,path, number,resolution,numberOfHashFunctions,diemnsional);
        secureDataset.constructInnerINdex();
        secureDataset.constructExternalIndex();

        List<Integer> queryId = new ArrayList<>(100);

        for (int n=0;n<100;n++){
            queryId.add(n+1);
        }


        SimilarityCalculate.calcuVectorCorrectness(dataset,secureDataset,10,queryNumber,queryId);

        /*for (int j=1;j<k;j+=2){
            if (j==19)
                j++;
            if (j==9){
                j++;
                SimilarityCalculate.calcuVectorCorrectness(dataset,secureDataset,j,queryNumber,queryId);
                j++;
            }else if (j==19){
                j++;
                SimilarityCalculate.calcuVectorCorrectness(dataset,secureDataset,j,queryNumber,queryId);
            }else {
                SimilarityCalculate.calcuVectorCorrectness(dataset,secureDataset,j,queryNumber,queryId);
            }


        }*/

        /*for (int i = (int) (number*0.01);i<=(int) (number*0.1);i+=(int) (number*0.01)){

            SimilarityCalculate.calculationCorrectness(secureDataset,k,i,queryNumber);
        }*/

    }
    public static int generateRandomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
