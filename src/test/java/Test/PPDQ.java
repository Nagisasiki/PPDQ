package Test;

import Class.DQ.Dataset;
import Class.PPDQ.SecureDataset;
import Class.PPDQ.SecureDistriMap;
import Class.PPDQ.SecureGenQuery;
import Class.PPDQ.SecureSimilarIndex;
import Methods.DQ.SearchWithDistribution;
import Methods.PPDQ.SecureInnerProductCalculator;
import Methods.PPDQ.SecureSearchWithSimilarIndex;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PPDQ {
    public static void main(String[] args) {


        String path = "E:\\lpy\\datasets\\public\\distributions"; // 指定实际的路径
        String datasetName = "public"; // 指定数据集名

        /*try {
            List<Map.Entry<Integer, Float>>[] sortedSimilarityArray = SecureSearchUnderd(0.01F, datasetName,10,20);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/



        try {
            List<Map.Entry<Integer, Float>>[] sortedSimilarityArray = SearchUnderd(0.01F, datasetName,10,20);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }




    public void AccurUnderDiffer_d(float d, String datasetname){
        String path = "E:\\lpy\\datasets\\+ datasetname +\\distributions"; // 指定实际的路径
        String datasetName = datasetname; // 指定数据集名
        SecureInnerProductCalculator secureInnerProductCalculator = new SecureInnerProductCalculator(200);

        SecureDataset secureDataset = new SecureDataset(datasetName, path,secureInnerProductCalculator,1000);

    }

    public static List<Map.Entry<Integer, Float>>[] SecureSearchUnderd(float d, String datasetname, int k, int datasetnumber) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        //Map<Integer, Float> similars = new HashMap<>();
        //Map<Integer, Float> securesimilars = new HashMap<>();
        //List<List<Map.Entry<Integer, Float>>> similars = new ArrayList<>();

        List<Map.Entry<Integer, Float>>[] sortedSimilarityArray = new ArrayList[datasetnumber];
        String path = "E:\\lpy\\datasets\\" + datasetname + "\\distributions"; // 指定实际的路径

        //int datasetNumber = datasetnumber;

        //String datasetName = datasetname; // 指定数据集名
        SecureInnerProductCalculator secureInnerProductCalculator = new SecureInnerProductCalculator(200);

        SecureDataset secureDataset = new SecureDataset(datasetname, path,secureInnerProductCalculator,datasetnumber);
        ArrayList<SecureSimilarIndex> secureSimilarIndices = secureDataset.getIndices();

        for (int i=1; i<= datasetnumber; i++){

            SecureDistriMap secureDistriMap = new SecureDistriMap(i,"E:\\lpy\\datasets\\" + datasetname + "\\distributions\\Distribution_" + i + ".txt");
            SecureGenQuery secureGenQuery = new SecureGenQuery(secureDistriMap,3,secureInnerProductCalculator);

            sortedSimilarityArray[i-1] = new ArrayList<>();
            SecureSearchWithSimilarIndex.topKsearchWithIndex(secureSimilarIndices,secureGenQuery,k, sortedSimilarityArray[i-1]);
        }

        return sortedSimilarityArray;

    }

    public static List<Map.Entry<Integer, Float>>[] SearchUnderd(float d, String datasetname, int k, int datasetnumber) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {

        List<Map.Entry<Integer, Float>>[] sortedSimilarityArray = new ArrayList[datasetnumber];


        String path = "E:\\lpy\\datasets\\" + datasetname + "\\distributions"; // 指定实际的路径

        //int datasetNumber = datasetnumber;

        //String datasetName = datasetname; // 指定数据集名

        Dataset dataset = new Dataset(datasetname,path,datasetnumber);

        System.out.println("asdasfdsaf");

        for (int i=1; i<= datasetnumber; i++){

            sortedSimilarityArray[i-1] = new ArrayList<>();
            SearchWithDistribution.topkSearch(dataset,i,k,sortedSimilarityArray[i-1]);
        }

        return sortedSimilarityArray;

    }

}
