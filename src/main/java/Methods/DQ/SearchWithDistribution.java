package Methods.DQ;

import Class.DQ.Dataset;
import Class.DQ.DistriMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchWithDistribution {


    /**
     * ���ݷֲ�ֱ�Ӽ����������ݼ�֮������ƶ�
     *
     * @param a
     * @param b
     * @return float
     */
    public static float calculateSimilarity(DistriMap a, DistriMap b) {
        BigDecimal similarity = BigDecimal.ZERO;

        Map<DistriMap.Pair, Float> mapA = a.getMap();
        Map<DistriMap.Pair, Float> mapB = b.getMap();

        for (Map.Entry<DistriMap.Pair, Float> entryA : mapA.entrySet()) {
            DistriMap.Pair pairA = entryA.getKey();
            float valueA = entryA.getValue();

            if (mapB.containsKey(pairA)) {
                float valueB = mapB.get(pairA);
                BigDecimal bdValueA = BigDecimal.valueOf(valueA);
                BigDecimal bdValueB = BigDecimal.valueOf(valueB);
                BigDecimal minValue = bdValueA.min(bdValueB);
                similarity = similarity.add(minValue);
            }
        }

        return similarity.floatValue();
    }


    /**
     * ���ƶ������������������ݼ������ƶ�
     *
     * @param dataset ���ݼ�
     * @param queryId ��ѯid
     */
    public static void searchSimilar(Dataset dataset, int queryId) {
        DistriMap queryDistriMap = dataset.getDistriMap(queryId);

        Map<Integer, Float> similarityMap = new HashMap<>();

        for (int i = 0; i < dataset.getDistriMaps().size(); i++) {
            if (i != queryId) {
                DistriMap distriMap = dataset.getDistriMap(i);
                float similarity = calculateSimilarity(queryDistriMap, distriMap);
                similarityMap.put(i, similarity);
            }
        }

        List<Map.Entry<Integer, Float>> sortedSimilarityList = new ArrayList<>(similarityMap.entrySet());
        sortedSimilarityList.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));

        System.out.println("Query ID: " + queryId);
        System.out.println("Similarity from high to low:");

        for (Map.Entry<Integer, Float> entry : sortedSimilarityList) {
            int id = entry.getKey();
            float similarity = entry.getValue();
            System.out.println("���ݼ� ID: " + id +  "   " + "���ƶ�: " + similarity );
        }
    }

    public static void searchSimilar(Dataset dataset, DistriMap queryDistriMap) {

        Map<Integer, Float> similarityMap = new HashMap<>();

        for (DistriMap distriMap : dataset.getDistriMaps()) {
            float similarity = calculateSimilarity(queryDistriMap, distriMap);
            similarityMap.put(distriMap.getId(), similarity);

        }

        List<Map.Entry<Integer, Float>> sortedSimilarityList = new ArrayList<>(similarityMap.entrySet());
        sortedSimilarityList.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));

        System.out.println("Query ID: " +queryDistriMap.getId());
        System.out.println("Similarity from high to low:");

        for (Map.Entry<Integer, Float> entry : sortedSimilarityList) {
            int id = entry.getKey();
            float similarity = entry.getValue();
            System.out.println("���ݼ� ID: " + id +  "   " + "���ƶ�: " + similarity );
        }
    }

    /**
     * topk����
     *
     * @param dataset ���ݼ�
     * @param queryId ��ѯid
     * @param k       ǰk��������
     */
    public static void topkSearch(Dataset dataset, int queryId, int k) {
        DistriMap queryDistriMap = dataset.getDistriMap(queryId);

        Map<Integer, Float> similarityMap = new HashMap<>();

        for (int i = 0; i < dataset.getDistriMaps().size(); i++) {
            if (i != queryId) {
                DistriMap distriMap = dataset.getDistriMap(i);
                float similarity = calculateSimilarity(queryDistriMap, distriMap);
                similarityMap.put(i, similarity);
            }
        }

        List<Map.Entry<Integer, Float>> sortedSimilarityList = new ArrayList<>(similarityMap.entrySet());
        sortedSimilarityList.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue()));

        System.out.println("Query ID: " + queryId);
        System.out.println("Top " + k + " similar items:");

        int count = 0;
        for (Map.Entry<Integer, Float> entry : sortedSimilarityList) {
            if (count >= k) {
                break;
            }

            int id = entry.getKey();
            float similarity = entry.getValue();
            System.out.println("���ݼ� ID: " + id + "   " + "���ƶ�: " + similarity);
            count++;
        }
    }

}
