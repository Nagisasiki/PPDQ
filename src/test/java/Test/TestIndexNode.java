package Test;

import Class.DQ.Dataset;
import Class.DQ.GenQuery;
import Class.DQ.IndexNode;
import Class.DQ.SimilarIndex;
import Methods.DQ.BloomFilterWithDensity;
import Methods.DQ.Tree;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class TestIndexNode {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {

        /*Float[] floats1 = {0.1f, null, null, null, null, null, null, null, null, null};
        Float[] floats2 = {null, 0.2f, null, null, null, null, null, null, null, null};
        Float[] floats3 = {null, null, 0.3f, null, null, null, null, null, null, null};
        Float[] floats4 = {null, null, null, 0.4f, null, null, null, null, null, null};
        Float[] floats5 = {null, null, null, null, 0.5f, null, null, null, null, null};
        Float[] floats6 = {null, null, null, null, null, 0.6f, null, null, null, null};
        Float[] floats7 = {null, null, null, null, null, null, 0.7f, null, null, null};
        Float[] floats8 = {null, null, null, null, null, null, null, 0.8f, null, null};

        BloomFilterWithDensity bloomFilterWithDensity1 = new BloomFilterWithDensity(floats1);
        BloomFilterWithDensity bloomFilterWithDensity2 = new BloomFilterWithDensity(floats2);
        BloomFilterWithDensity bloomFilterWithDensity3 = new BloomFilterWithDensity(floats3);
        BloomFilterWithDensity bloomFilterWithDensity4 = new BloomFilterWithDensity(floats4);
        BloomFilterWithDensity bloomFilterWithDensity5 = new BloomFilterWithDensity(floats5);
        BloomFilterWithDensity bloomFilterWithDensity6 = new BloomFilterWithDensity(floats6);
        BloomFilterWithDensity bloomFilterWithDensity7 = new BloomFilterWithDensity(floats7);
        BloomFilterWithDensity bloomFilterWithDensity8 = new BloomFilterWithDensity(floats8);

        SimilarIndex similarIndex1 = new SimilarIndex(bloomFilterWithDensity1);
        SimilarIndex similarIndex2 = new SimilarIndex(bloomFilterWithDensity2);
        SimilarIndex similarIndex3 = new SimilarIndex(bloomFilterWithDensity3);
        SimilarIndex similarIndex4 = new SimilarIndex(bloomFilterWithDensity4);
        SimilarIndex similarIndex5 = new SimilarIndex(bloomFilterWithDensity5);
        SimilarIndex similarIndex6 = new SimilarIndex(bloomFilterWithDensity6);
        SimilarIndex similarIndex7 = new SimilarIndex(bloomFilterWithDensity7);
        SimilarIndex similarIndex8 = new SimilarIndex(bloomFilterWithDensity8);

        IndexNode indexNode1 = new IndexNode(similarIndex1);
        IndexNode indexNode2 = new IndexNode(similarIndex2);
        IndexNode indexNode3 = new IndexNode(similarIndex3);
        IndexNode indexNode4 = new IndexNode(similarIndex4);
        IndexNode indexNode5 = new IndexNode(similarIndex5);
        IndexNode indexNode6 = new IndexNode(similarIndex6);
        IndexNode indexNode7 = new IndexNode(similarIndex7);
        IndexNode indexNode8 = new IndexNode(similarIndex8);

        ArrayList<IndexNode> indexNodes = new ArrayList<>();
        indexNodes.add(indexNode1);
        indexNodes.add(indexNode2);
        indexNodes.add(indexNode3);
        indexNodes.add(indexNode4);
        indexNodes.add(indexNode5);
        indexNodes.add(indexNode6);
        indexNodes.add(indexNode7);
        indexNodes.add(indexNode8);

        Tree.printNodes(indexNodes);

        System.out.println("********************");

        IndexNode root = Tree.buildTree(indexNodes);
        Tree.traversalTree(root);*/




        String path = "D:\\projects\\test\\distribution"; // 指定实际的路径
        String datasetName = "public"; // 指定数据集名

        Dataset dataset = new Dataset(datasetName , path);

        //构建索引树
        ArrayList<IndexNode> indexNodes = Tree.GenIndexNodes(dataset);
        IndexNode root = Tree.buildTree(indexNodes);

        //生成查询陷门
        GenQuery genQuery = new GenQuery(dataset.getDistriMap(1),3);
        //Tree.traversalTree(root);

        //System.out.println("***************");

        //indexNodes.get(0).printBitSet();
        //indexNodes.get(1).printBitSet();

        Tree.topKSearchWithTree(root,genQuery,5);
    }



}
