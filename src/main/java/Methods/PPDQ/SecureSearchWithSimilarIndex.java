package Methods.PPDQ;

import Class.DQ.Dataset;
import Class.DQ.DistriMap;
import Class.DQ.GenQuery;
import Class.DQ.SimilarIndex;
import Class.PPDQ.SecureDataset;
import Class.PPDQ.SecureDistriMap;
import Class.PPDQ.SecureGenQuery;
import Class.PPDQ.SecureSimilarIndex;
import Methods.DQ.BloomFilterWithDensity;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecureSearchWithSimilarIndex {
    public static Float searchWithIndex(SecureSimilarIndex secureSimilarIndex, SecureGenQuery secureGenQuery){
        float similar =0;
        //BigDecimal similarity = BigDecimal.ZERO;
        Map<SecureGenQuery.Index, DensityVector> query = secureGenQuery.getQuery();
        SecureBloomFilterWithDensity secureBloomFilterWithDensity = secureSimilarIndex.getBloomFilterWithDensity();
        for (Map.Entry<SecureGenQuery.Index, DensityVector> entry : query.entrySet()) {
            SecureGenQuery.Index index  = entry.getKey();
            DensityVector densityVector = entry.getValue();
            if (index.searchInBloomFilter(secureBloomFilterWithDensity)){

                double temp = DensityVector.calculateInnerProduct(densityVector,secureBloomFilterWithDensity.getDensityVector(index.getIndex()) );
                similar += (float) temp;
            }
        }
        return similar;
    }

    public static void searchWithIndex(SecureDataset secureDataset, SecureGenQuery secureGenQuery) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        ArrayList<SecureDistriMap> secureDistriMaps = secureDataset.getDistriMaps();
        Map<Integer, Float> similarMap = new HashMap<>();


        for(SecureDistriMap secureDistriMap : secureDistriMaps){
            SecureSimilarIndex secureSimilarIndex = new SecureSimilarIndex(secureDistriMap, 100, 0.01);
            //long stime = System.nanoTime();
            float simiilar = SecureSearchWithSimilarIndex.searchWithIndex(secureSimilarIndex, secureGenQuery);

            similarMap.put(secureDistriMap.getId(),simiilar);

            //long etime = System.nanoTime();
            //System.err.println( "过滤器查询时间：" + (etime - stime));

        }

        List<Map.Entry<Integer, Float>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
        sortedSimilarityList.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));


        System.out.println("查询数据集Id：" + secureGenQuery.getId());
        System.out.println("相似度：");
        for (Map.Entry<Integer, Float> entry : sortedSimilarityList) {
            int id = entry.getKey();
            float similarity = entry.getValue();
            System.out.println("数据集 ID: " + id +  "   " + "相似度: " + similarity );
        }
    }

    public static void searchWithIndex(ArrayList<SecureSimilarIndex> secureSimilarIndices, SecureGenQuery secureGenQuery) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        Map<Integer, Float> similarMap = new HashMap<>();

        for(SecureSimilarIndex secureSimilarIndex : secureSimilarIndices){
            //long stime = System.nanoTime();
            float simiilar = SecureSearchWithSimilarIndex.searchWithIndex(secureSimilarIndex, secureGenQuery);

            similarMap.put(secureSimilarIndex.getId(),simiilar);

            //long etime = System.nanoTime();
            //System.err.println( "过滤器查询时间：" + (etime - stime));
        }
        List<Map.Entry<Integer, Float>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
        sortedSimilarityList.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));

        System.out.println("查询数据集Id：" + secureGenQuery.getId());
        System.out.println("相似度：");
        for (Map.Entry<Integer, Float> entry : sortedSimilarityList) {
            int id = entry.getKey();
            float similarity = entry.getValue();
            System.out.println("数据集 ID: " + id +  "   " + "相似度: " + similarity );
        }
    }


    public static void topKsearchWithIndex(ArrayList<SecureSimilarIndex> secureSimilarIndices, SecureGenQuery secureGenQuery, int k) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        Map<Integer, Float> similarMap = new HashMap<>();

        for(SecureSimilarIndex secureSimilarIndex : secureSimilarIndices){
            //long stime = System.nanoTime();
            float simiilar = SecureSearchWithSimilarIndex.searchWithIndex(secureSimilarIndex, secureGenQuery);

            similarMap.put(secureSimilarIndex.getId(),simiilar);

            //long etime = System.nanoTime();
            //System.err.println( "过滤器查询时间：" + (etime - stime));
        }
        List<Map.Entry<Integer, Float>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
        sortedSimilarityList.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));

        /*System.out.println("查询数据集Id：" + secureGenQuery.getId());
        System.out.println("相似度：");*/

        /*int count = 0;
        for (Map.Entry<Integer, Float> entry : sortedSimilarityList) {
            if (count >= k) {
                break;
            }

            int id = entry.getKey();
            float similarity = entry.getValue();
            System.out.println("数据集 ID: " + id + "   " + "相似度: " + similarity);
            count++;
        }*/
    }

    public static void topKsearchWithIndex(ArrayList<SecureSimilarIndex> secureSimilarIndices, SecureGenQuery secureGenQuery, int k, List<Map.Entry<Integer, Float>> sortedSimilarityList) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        Map<Integer, Float> similarMap = new HashMap<>();
        sortedSimilarityList.clear();

        for(SecureSimilarIndex secureSimilarIndex : secureSimilarIndices){
            //long stime = System.nanoTime();
            float simiilar = SecureSearchWithSimilarIndex.searchWithIndex(secureSimilarIndex, secureGenQuery);

            similarMap.put(secureSimilarIndex.getId(),simiilar);

            //long etime = System.nanoTime();
            //System.err.println( "过滤器查询时间：" + (etime - stime));
        }
        for (Map.Entry<Integer,Float> entry : similarMap.entrySet()){
            sortedSimilarityList.add(entry);
        }
        sortedSimilarityList.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));

        System.out.println("查询数据集Id：" + secureGenQuery.getId());
        System.out.println("相似度：");

        int count = 0;
        for (Map.Entry<Integer, Float> entry : sortedSimilarityList) {
            if (count >= k) {
                break;
            }

            int id = entry.getKey();
            float similarity = entry.getValue();
            System.out.println("数据集 ID: " + id + "   " + "相似度: " + similarity);
            count++;
        }
    }


}
