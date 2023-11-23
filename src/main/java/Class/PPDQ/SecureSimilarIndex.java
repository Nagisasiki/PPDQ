package Class.PPDQ;

import Class.DQ.DistriMap;
import Methods.DQ.BloomFilterWithDensity;
import Methods.PPDQ.SecureBloomFilterWithDensity;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SecureSimilarIndex {
    private int id;
    private SecureBloomFilterWithDensity secureBloomFilterWithDensity;

    public SecureSimilarIndex(SecureDistriMap secureDistriMap, int expectedInsertions, double falsePositiveRate) throws NoSuchAlgorithmException, InvalidKeyException {
        this.id = secureDistriMap.getId();
        this.secureBloomFilterWithDensity = new SecureBloomFilterWithDensity(expectedInsertions, falsePositiveRate);
        secureBloomFilterWithDensity.add(secureDistriMap);
    }

    public SecureSimilarIndex(SecureBloomFilterWithDensity secureBloomFilterWithDensity){
        this.secureBloomFilterWithDensity = secureBloomFilterWithDensity;
    }

    public void printSimlarIndex(){
        System.out.println("id = " + this.id);
        this.secureBloomFilterWithDensity.printFilter();
        this.secureBloomFilterWithDensity.calNum();
    }

    public int getId(){
        return this.id;
    }

    public int getLength(){
        return this.secureBloomFilterWithDensity.getLength();
    }

    public SecureBloomFilterWithDensity getBloomFilterWithDensity(){
        return this.secureBloomFilterWithDensity;
    }

    public boolean searchWithIndex(int index){
        return this.secureBloomFilterWithDensity.searchWithIndex(index);
    }

}