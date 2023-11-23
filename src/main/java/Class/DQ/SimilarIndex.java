package Class.DQ;

import Methods.DQ.BloomFilterWithDensity;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SimilarIndex {
    private int id;
    private BloomFilterWithDensity bloomFilterWithDensity;

    public SimilarIndex(DistriMap distriMap, int expectedInsertions, double falsePositiveRate) throws NoSuchAlgorithmException, InvalidKeyException {
        this.id = distriMap.getId();
        this.bloomFilterWithDensity = new BloomFilterWithDensity(expectedInsertions, falsePositiveRate);
        bloomFilterWithDensity.add(distriMap);
    }

    public SimilarIndex(BloomFilterWithDensity bloomFilterWithDensity){
        this.bloomFilterWithDensity = bloomFilterWithDensity;
    }

    public void printSimlarIndex(){
        System.out.println("id = " + this.id);
        this.bloomFilterWithDensity.printFilter();
        this.bloomFilterWithDensity.calNum();
    }

    public int getId(){
        return this.id;
    }

    public int getLength(){
        return this.bloomFilterWithDensity.getLength();
    }

    public BloomFilterWithDensity getBloomFilterWithDensity(){
        return this.bloomFilterWithDensity;
    }

    public boolean searchWithIndex(int index){
        return this.bloomFilterWithDensity.searchWithIndex(index);
    }

}