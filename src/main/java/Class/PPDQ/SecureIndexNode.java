package Class.PPDQ;

import Class.DQ.SimilarIndex;

import java.util.BitSet;

public class SecureIndexNode {
    private SecureSimilarIndex Index;
    private SecureIndexNode left;
    private SecureIndexNode right;
    private int flag;
    private BitSet bitSet;


    public SecureIndexNode(SecureSimilarIndex Index){
        this.flag = 0;
        int size = Index.getLength();
        this.bitSet = new BitSet(size);
        this.left = null;
        this.right = null;
        this.Index = Index;

        for (int i=0; i<size; i++){
            if (Index.searchWithIndex(i))
                bitSet.set(i);
        }

    }

    //两个非叶子节点合并
    public SecureIndexNode(SecureIndexNode left, SecureIndexNode right){
        this.flag = 1;
        BitSet bitSet1 = left.getBitSet();
        BitSet bitSet2 = right.getBitSet();
        BitSet mergeBitSet = mergeBitSet(bitSet1, bitSet2);

        this.bitSet = mergeBitSet;
        this.Index = null;
        this.left = left;
        this.right = right;
    }

    public BitSet mergeBitSet(BitSet bitSet1, BitSet bitSet2){
        BitSet mergeBitSet = new BitSet();

        for (int i=0; i<bitSet1.size(); i++){
            if (bitSet1.get(i) || bitSet2.get(i))
                mergeBitSet.set(i);
        }

        return mergeBitSet;
    }

    public void printBitSet(){
        System.out.println("BitSet Content: " + this.bitSet);
    }


    public SecureSimilarIndex getIndex() {
        return Index;
    }

    public SecureIndexNode getLeft() {
        return left;
    }

    public SecureIndexNode getRight() {
        return right;
    }

    public BitSet getBitSet(){
        return this.bitSet;
    }

    public int getFlag(){
        return this.flag;
    }


}
