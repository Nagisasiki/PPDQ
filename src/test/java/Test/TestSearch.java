package Test;

import Class.DQ.*;
import Class.PPDQ.*;
import Methods.DQ.SearchWithDistribution;
import Methods.DQ.SearchWithSimilarIndex;
import Methods.DQ.Tree;
import Methods.PPDQ.SecureInnerProductCalculator;
import Methods.PPDQ.SecureSearchWithSimilarIndex;
import Methods.PPDQ.SecureTree;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class TestSearch {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {

        String path = "D:\\projects\\test\\distributions"; // ָ��ʵ�ʵ�·��
        String datasetName = "public"; // ָ�����ݼ���
        SecureInnerProductCalculator secureInnerProductCalculator = new SecureInnerProductCalculator(200);

        //��ʼ�����ݼ�
        Dataset dataset = new Dataset(datasetName , path);
        ArrayList<SimilarIndex> similarIndices = dataset.getIndices();

        //���ɲ�ѯ����
        DistriMap distriMap = new DistriMap(1,"D:\\projects\\test\\distributions\\Distribution_1.txt");
        GenQuery genQuery = new GenQuery(distriMap,3);



        //��ʼ�����ݼ�
        SecureDataset secureDataset = new SecureDataset(datasetName, path, secureInnerProductCalculator);
        ArrayList<SecureSimilarIndex> secureSimilarIndices = secureDataset.getIndices();
        //secureDataset.printDataset();

        SecureDistriMap secureDistriMap = new SecureDistriMap(1,"D:\\projects\\test\\distributions\\Distribution_1.txt");
        SecureGenQuery secureGenQuery = new SecureGenQuery(secureDistriMap,3, secureInnerProductCalculator);
        //secureGenQuery.printQuery();


        //��ʹ�ð�ȫ�ڻ����㣬ֱ�Ӳ�ѯ
        long stime = System.currentTimeMillis();
        SearchWithSimilarIndex.topKsearchWithIndex(similarIndices,genQuery,10);
        long etime = System.currentTimeMillis();
        System.out.println("��ѯʱ�䣺" + (etime - stime) + " ms");

        long stime1 = System.currentTimeMillis();
        SecureSearchWithSimilarIndex.topKsearchWithIndex(secureSimilarIndices,secureGenQuery,10);
        long etime1 = System.currentTimeMillis();
        System.out.println("��ѯʱ�䣺" + (etime1 - stime1) + " ms");


        ArrayList<IndexNode> indexNodes = Tree.GenIndexNodes(dataset);
        IndexNode root = Tree.buildTree(indexNodes);

        ArrayList<SecureIndexNode> secureIndexNodes = SecureTree.GenIndexNodes(secureDataset);
        SecureIndexNode secureRoot = SecureTree.buildTree(secureIndexNodes);


        stime = System.currentTimeMillis();
        Tree.topKSearchWithTree(root,genQuery,10);
        etime = System.currentTimeMillis();
        System.out.println("��ѯʱ�䣺" + (etime - stime) + " ms");


        stime = System.currentTimeMillis();
        SecureTree.topKSearchWithTree(secureRoot,secureGenQuery,10);
        etime = System.currentTimeMillis();
        System.out.println("��ѯʱ�䣺" + (etime - stime) + " ms");


    }
}
