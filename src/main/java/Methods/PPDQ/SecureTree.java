package Methods.PPDQ;

import Class.DQ.Dataset;
import Class.DQ.GenQuery;
import Class.DQ.IndexNode;
import Class.DQ.SimilarIndex;
import Class.PPDQ.SecureDataset;
import Class.PPDQ.SecureGenQuery;
import Class.PPDQ.SecureIndexNode;
import Class.PPDQ.SecureSimilarIndex;
import Methods.DQ.SearchWithSimilarIndex;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SecureTree {


    //创建最底层非叶子节点
    public static ArrayList<SecureIndexNode> GenIndexNodes(SecureDataset secureDataset) throws NoSuchAlgorithmException, InvalidKeyException {
        int size = secureDataset.getLength();
        ArrayList<SecureIndexNode> secureIndexNodes = new ArrayList<>();

        for (int i=0; i < size; i ++){
            SecureSimilarIndex secureSimilarIndex = new SecureSimilarIndex(secureDataset.getDistriMap(i+1),100,0.01);
            SecureIndexNode secureIndexNode = new SecureIndexNode(secureSimilarIndex);
            secureIndexNodes.add(secureIndexNode);
        }
        return secureIndexNodes;
    }

    public static SecureIndexNode buildTree(ArrayList<SecureIndexNode> secureIndexNodes){
        while (secureIndexNodes.size() > 1){

            ArrayList<SecureIndexNode> temp = new ArrayList<>();


            for (int i = 0; i < (secureIndexNodes.size()-1) ; i += 2) {
                SecureIndexNode left = secureIndexNodes.get(i);
                SecureIndexNode right = secureIndexNodes.get(i+1);
                SecureIndexNode merged = new SecureIndexNode(left, right);
                temp.add(merged);
            }

            if (secureIndexNodes.size() % 2 == 1){
                temp.add(secureIndexNodes.get(secureIndexNodes.size()-1));
            }

            secureIndexNodes = temp;

        }

        return secureIndexNodes.get(0);
    }


    public static void traversalTree(SecureIndexNode root){
        if (root != null){
            if (root.getFlag() == 1){
                root.printBitSet();
            }
            traversalTree(root.getLeft());
            traversalTree(root.getRight());
        }
    }


    public static void printNodes(ArrayList<SecureIndexNode> secureIndexNodes){
        for (SecureIndexNode secureIndexNode : secureIndexNodes){
            secureIndexNode.printBitSet();
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

    public static boolean isContain(SecureGenQuery secureGenQuery, BitSet bitSet){
        Map<SecureGenQuery.Index, DensityVector> query = secureGenQuery.getQuery();
        for (Map.Entry<SecureGenQuery.Index, DensityVector> entry : query.entrySet()) {
            SecureGenQuery.Index index  = entry.getKey();
            //Float value = entry.getValue();
            if (index.searchInBitSet(bitSet)){
                return true;
            }
        }
        return false;
    }

    public static void similarSearchWithTree(SecureIndexNode root, SecureGenQuery secureGenQuery, PriorityQueue<Pair> priorityQueue){
        if (root.getFlag() == 1){
            if (SecureTree.isContain(secureGenQuery,root.getBitSet())){
                similarSearchWithTree(root.getRight(),secureGenQuery,priorityQueue);
                similarSearchWithTree(root.getLeft(),secureGenQuery,priorityQueue);
            }
        }else {
            if (SecureTree.isContain(secureGenQuery,root.getBitSet())){
                float similar = SecureSearchWithSimilarIndex.searchWithIndex(root.getIndex(),secureGenQuery);
                Pair pair = new Pair(root.getIndex().getId(), similar);
                priorityQueue.add(pair);
            }
        }
    }

    public static void topKSearchWithTree(SecureIndexNode root, SecureGenQuery secureGenQuery, int k){

        //存储结果的优先权队列
        PriorityQueue<Pair> priorityQueue = new PriorityQueue<>(k, new Comparator<Pair>() {
            @Override
            public int compare(Pair f1, Pair f2) {
                // 根据浮点数的大小比较优先级，越大的浮点数优先级越高
                return Float.compare(f2.getSimilar(),f1.getSimilar());
            }
        });


        SecureTree.similarSearchWithTree(root,secureGenQuery,priorityQueue);

        /*int count = 0;

        while (!priorityQueue.isEmpty() && count <= k) {
            Pair pair = priorityQueue.poll();
            System.out.println("id= " + pair.getId() + ", similar= " + pair.getSimilar());
            count++;
        }*/


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
