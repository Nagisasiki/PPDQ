package Test;
import Class.DQ.Dataset;
import Class.DQ.DistriMap;
import Class.DQ.GenQuery;
import Class.DQ.SimilarIndex;
import Class.PPDQ.SecureDataset;
import Class.PPDQ.SecureDistriMap;
import Methods.DQ.SearchWithDistribution;
import Methods.DQ.SearchWithSimilarIndex;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class TestDistribution {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        /*DistriMap distriMap1 = new DistriMap(1,2);
        distriMap1.addToMap(3,1,  0.03f);
        distriMap1.addToMap(1,3,0.02f);
        distriMap1.printMap();*/

        /*DistriMap distriMap1 = new DistriMap(1, "D:\\projects\\test\\distribution\\Distribution_1.txt");
        distriMap1.printMap();

        System.out.println("*****************");

        SecureDistriMap secureDistriMap = new SecureDistriMap(1,"D:\\projects\\test\\distribution\\Distribution_1.txt");
        secureDistriMap.printMap();*/



        String path = "D:\\projects\\test\\distribution"; // 指定实际的路径
        String datasetName = "public"; // 指定数据集名

        SecureDataset secureDataset = new SecureDataset(datasetName,path);
        secureDataset.printDataset();


        /*DistriMap distriMap2 = new DistriMap(2, "D:\\projects\\test\\distribution\\Distribution_2.txt");
        //distriMap2.printMap();*/

        /*float similar = SearchWithDistribution.calculateSimilarity(distriMap1,distriMap2);
        System.out.println(similar);

        String path = "D:\\projects\\test\\distribution"; // 指定实际的路径
        String datasetName = "public"; // 指定数据集名

        Dataset dataset = new Dataset(datasetName , path);

        long stime1 = System.nanoTime();
        SearchWithDistribution.searchSimilar(dataset,distriMap1);
        long etime1 = System.nanoTime();


        GenQuery genQuery = new GenQuery(distriMap1,3);

        long stime2 = System.nanoTime();
        SearchWithSimilarIndex.searchWithIndex(dataset,genQuery);
        long etime2 = System.nanoTime();

        ArrayList<SimilarIndex> similarIndices = dataset.getIndices();

        long stime3 = System.nanoTime();
        SearchWithSimilarIndex.searchWithIndex(similarIndices,genQuery);
        long etime3 = System.nanoTime();


        System.out.println("直接查询时间：" + (etime1 - stime1));
        System.out.println("过滤器查询时间：" + (etime2 - stime2));
        System.out.println("索引查询时间：" + (etime3 - stime3));*/

    }
}
