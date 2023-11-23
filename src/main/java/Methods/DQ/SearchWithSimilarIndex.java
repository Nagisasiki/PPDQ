package Methods.DQ;

import Class.DQ.Dataset;
import Class.DQ.DistriMap;
import Class.DQ.GenQuery;
import Class.DQ.SimilarIndex;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchWithSimilarIndex {
    public static Float searchWithIndex(SimilarIndex similarIndex, GenQuery genQuery){
        float similar =0;
        //BigDecimal similarity = BigDecimal.ZERO;
        Map<GenQuery.Index, Float> query = genQuery.getQuery();
        BloomFilterWithDensity bloomFilterWithDensity = similarIndex.getBloomFilterWithDensity();
        for (Map.Entry<GenQuery.Index, Float> entry : query.entrySet()) {
            GenQuery.Index index  = entry.getKey();
            Float value = entry.getValue();
            if (index.searchInBloomFilter(bloomFilterWithDensity)){
                /*BigDecimal bdValueA = BigDecimal.valueOf(value);
                BigDecimal bdValueB = BigDecimal.valueOf(bloomFilterWithDensity.getDensity(Math.abs(index.getIndex()%(bloomFilterWithDensity.getLength()))));
                BigDecimal minValue = bdValueA.min(bdValueB);
                similarity = similarity.add(minValue);*/
                similar += Math.min(value, bloomFilterWithDensity.getDensity(index.getIndex()));
            }
        }
        return similar;
    }

    public static void searchWithIndex(Dataset dataset, GenQuery genQuery) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        ArrayList<DistriMap> distriMaps = dataset.getDistriMaps();
        Map<Integer, Float> similarMap = new HashMap<>();


        for(DistriMap distriMap : distriMaps){
            SimilarIndex similarIndex = new SimilarIndex(distriMap, 100, 0.01);
            //long stime = System.nanoTime();
            float simiilar = SearchWithSimilarIndex.searchWithIndex(similarIndex, genQuery);

            similarMap.put(distriMap.getId(),simiilar);

            //long etime = System.nanoTime();
            //System.err.println( "过滤器查询时间：" + (etime - stime));

        }

        List<Map.Entry<Integer, Float>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
        sortedSimilarityList.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));


        System.out.println("查询数据集Id：" + genQuery.getId());
        System.out.println("相似度：");
        for (Map.Entry<Integer, Float> entry : sortedSimilarityList) {
            int id = entry.getKey();
            float similarity = entry.getValue();
            System.out.println("数据集 ID: " + id +  "   " + "相似度: " + similarity );
        }
    }

    public static void searchWithIndex(ArrayList<SimilarIndex> similarIndices, GenQuery genQuery) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        Map<Integer, Float> similarMap = new HashMap<>();

        for(SimilarIndex similarIndex : similarIndices){
            //long stime = System.nanoTime();
            float simiilar = SearchWithSimilarIndex.searchWithIndex(similarIndex, genQuery);

            similarMap.put(similarIndex.getId(),simiilar);

            //long etime = System.nanoTime();
            //System.err.println( "过滤器查询时间：" + (etime - stime));
        }
        List<Map.Entry<Integer, Float>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
        sortedSimilarityList.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));

        System.out.println("查询数据集Id：" + genQuery.getId());
        System.out.println("相似度：");
        for (Map.Entry<Integer, Float> entry : sortedSimilarityList) {
            int id = entry.getKey();
            float similarity = entry.getValue();
            System.out.println("数据集 ID: " + id +  "   " + "相似度: " + similarity );
        }
    }


    public static void topKsearchWithIndex(ArrayList<SimilarIndex> similarIndices, GenQuery genQuery, int k) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        Map<Integer, Float> similarMap = new HashMap<>();

        for(SimilarIndex similarIndex : similarIndices){
            //long stime = System.nanoTime();
            float simiilar = SearchWithSimilarIndex.searchWithIndex(similarIndex, genQuery);

            similarMap.put(similarIndex.getId(),simiilar);

            //long etime = System.nanoTime();
            //System.err.println( "过滤器查询时间：" + (etime - stime));
        }
        List<Map.Entry<Integer, Float>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
        sortedSimilarityList.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));

        System.out.println("查询数据集Id：" + genQuery.getId());
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
