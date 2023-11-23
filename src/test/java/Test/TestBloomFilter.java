package Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import Class.DQ.Dataset;
import Class.DQ.GenQuery;
import Class.DQ.SimilarIndex;

public class TestBloomFilter {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        /*DistriMap distriMap = new DistriMap(1, "D:\\projects\\test\\distribution\\Distribution_1.txt");
        distriMap.printMap();
        MyBloomFilter bloomFilter = new MyBloomFilter(100, 0.01);
        bloomFilter.add(distriMap);
        bloomFilter.printFilter();
        DistriMap.Pair pair = new DistriMap.Pair(23127, 10079);
        if (bloomFilter.contains(pair))
            System.out.println("����");*/

        String path = "D:\\projects\\test\\distribution"; // ָ��ʵ�ʵ�·��
        String datasetName = "public"; // ָ�����ݼ���

        Dataset dataset = new Dataset(datasetName , path);
        //dataset.printDataset();
        for (int i = 0; i<20; i++){
            //BloomFilterWithDensity bloomFilter = new BloomFilterWithDensity(100, 0.01);
            //bloomFilter.add(dataset.getDistriMap(i));
            //bloomFilter.printFilter();
            //bloomFilter.calNum();
            dataset.getDistriMap(i).printMap();
            SimilarIndex similarIndex = new SimilarIndex(dataset.getDistriMap(i),100,0.01);
            similarIndex.printSimlarIndex();

        }
        GenQuery genQuery = new GenQuery(dataset.getDistriMap(19),3);
        genQuery.printQuery();
    }

}
