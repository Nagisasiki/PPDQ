package Methods.DQ;

import Class.DQ.Dataset;
import Class.DQ.GenQuery;
import Class.DQ.IndexNode;
import Class.DQ.SimilarIndex;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Tree {


    //创建最底层非叶子节点
    public static ArrayList<IndexNode> GenIndexNodes(Dataset dataset) throws NoSuchAlgorithmException, InvalidKeyException {
        int size = dataset.getLength();
        ArrayList<IndexNode> indexNodes = new ArrayList<>();

        for (int i=0; i < size; i ++){
            SimilarIndex similarIndex = new SimilarIndex(dataset.getDistriMap(i+1),100,0.01);
            IndexNode indexNode = new IndexNode(similarIndex);
            indexNodes.add(indexNode);
        }
        return indexNodes;
    }

    public static IndexNode buildTree(ArrayList<IndexNode> indexNodes){
        while (indexNodes.size() > 1){

            ArrayList<IndexNode> temp = new ArrayList<>();


            for (int i = 0; i < (indexNodes.size()-1) ; i += 2) {
                IndexNode left = indexNodes.get(i);
                IndexNode right = indexNodes.get(i+1);
                IndexNode merged = new IndexNode(left, right);
                temp.add(merged);
            }

            if (indexNodes.size() % 2 == 1){
                temp.add(indexNodes.get(indexNodes.size()-1));
            }

            indexNodes = temp;

        }

        return indexNodes.get(0);
    }


    public static void traversalTree(IndexNode root){
        if (root != null){
            if (root.getFlag() == 1){
                root.printBitSet();
            }
            traversalTree(root.getLeft());
            traversalTree(root.getRight());
        }
    }


    public static void printNodes(ArrayList<IndexNode> indexNodes){
        for (IndexNode indexNode : indexNodes){
            indexNode.printBitSet();
        }
    }

    /*public boolean isOverLeap(BitSet bitSet1, BitSet bitSet2){
        int count =0;
        for (int i=0; i< bitSet1.size(); i++){
            if (bitSet1.get(i) && bitSet2.get(i))
                count++;
        }
        if (count>3){
            return true;
        }else {
            return false;
        }
    } */

    public static boolean isContain(GenQuery genQuery, BitSet bitSet){
        Map<GenQuery.Index, Float> query = genQuery.getQuery();
        for (Map.Entry<GenQuery.Index, Float> entry : query.entrySet()) {
            GenQuery.Index index  = entry.getKey();
            //Float value = entry.getValue();
            if (index.searchInBitSet(bitSet)){
                return true;
            }
        }
        return false;
    }

    public static void similarSearchWithTree(IndexNode root, GenQuery genQuery, PriorityQueue<Pair> priorityQueue){
        if (root.getFlag() == 1){
            if (Tree.isContain(genQuery,root.getBitSet())){
                similarSearchWithTree(root.getRight(),genQuery,priorityQueue);
                similarSearchWithTree(root.getLeft(),genQuery,priorityQueue);
            }
        }else {
            if (Tree.isContain(genQuery,root.getBitSet())){
                float similar = SearchWithSimilarIndex.searchWithIndex(root.getIndex(),genQuery);
                Pair pair = new Pair(root.getIndex().getId(), similar);
                priorityQueue.add(pair);
            }
        }
    }

    public static void topKSearchWithTree(IndexNode root, GenQuery genQuery, int k){

        //存储结果的优先权队列
        PriorityQueue<Pair> priorityQueue = new PriorityQueue<>(k, new Comparator<Pair>() {
            @Override
            public int compare(Pair f1, Pair f2) {
                // 根据浮点数的大小比较优先级，越大的浮点数优先级越高
                return Float.compare(f2.getSimilar(),f1.getSimilar());
            }
        });


        Tree.similarSearchWithTree(root,genQuery,priorityQueue);

        int count = 0;

        while (!priorityQueue.isEmpty() && count <= k) {
            Pair pair = priorityQueue.poll();
            System.out.println("id= " + pair.getId() + ", similar= " + pair.getSimilar());
            count++;
        }


    }

    public static class Pair{
        private int id;
        private float similar;

        public Pair(int id, float similar) {
            this.id = id;
            this.similar = similar;
        }

        public int getId() {
            return this.id;
        }

        public float getSimilar() {
            return similar;
        }
    }


}
