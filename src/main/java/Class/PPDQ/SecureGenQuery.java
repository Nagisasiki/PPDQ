package Class.PPDQ;

import Class.DQ.DistriMap;
import Methods.DQ.BloomFilterWithDensity;
import Methods.DQ.HMACHashGenerator;
import Methods.PPDQ.DensityVector;
import Methods.PPDQ.SecureBloomFilterWithDensity;
import Methods.PPDQ.SecureHMACHashGenerator;
import Methods.PPDQ.SecureInnerProductCalculator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class SecureGenQuery {
    private int id;
    private Map<Index, DensityVector> query;
    private int numHashFunctions;

    public SecureGenQuery(SecureDistriMap secureDistriMap, int numHashFunctions) throws NoSuchAlgorithmException, InvalidKeyException {
        this.id = secureDistriMap.getId();
        this.numHashFunctions = numHashFunctions;
        Map<SecureDistriMap.Pair, DensityVector> map = new HashMap<>();
        map = secureDistriMap.getMap();
        query = new HashMap<>();

        for (Map.Entry<SecureDistriMap.Pair,DensityVector> entry : map.entrySet()){
            SecureDistriMap.Pair key = entry.getKey();
            DensityVector densityVector = entry.getValue();
            Index index = new Index(numHashFunctions, key);
            query.put(index,densityVector);
        }

    }


    public SecureGenQuery(SecureDistriMap secureDistriMap, int numHashFunctions, SecureInnerProductCalculator secureInnerProductCalculator) throws NoSuchAlgorithmException, InvalidKeyException {
        this.id = secureDistriMap.getId();
        this.numHashFunctions = numHashFunctions;
        Map<SecureDistriMap.Pair, DensityVector> map = new HashMap<>();
        map = secureDistriMap.getMap();
        query = new HashMap<>();

        for (Map.Entry<SecureDistriMap.Pair,DensityVector> entry : map.entrySet()){
            SecureDistriMap.Pair key = entry.getKey();
            DensityVector densityVector = entry.getValue();
            densityVector.setDensityVector(secureInnerProductCalculator.encryptVector(densityVector.getDensityVector(),false));
            Index index = new Index(numHashFunctions, key);
            query.put(index,densityVector);
        }

    }

    public int getId(){
        return this.id;
    }

    public Map<Index, DensityVector> getQuery(){
        return this.query;
    }

    public void printQuery() {
        for (Map.Entry<Index, DensityVector> entry : query.entrySet()) {
            Index index = entry.getKey();
            DensityVector densityVector = entry.getValue();
            index.printIndex();
            System.out.print("Value: " );
            DensityVector.printVector(densityVector.getDensityVector());
            System.out.println("----------------------");
        }
    }

    public static class Index{
        private int[] index;
        private byte[][] keys = {{ 0x12, 0x34, 0x56},{ 0x4, 0x36, 0x68},{ 0x36, 0x38, 0x60}, { 0x6, 0x68, 0x50}};
        private SecureHMACHashGenerator[] hashFunctions;
        private int numHashFunctions;

        public Index(int numHashFunctions, SecureDistriMap.Pair pair) throws NoSuchAlgorithmException, InvalidKeyException {
            this.numHashFunctions = numHashFunctions;
            index = new int[numHashFunctions];
            hashFunctions = new SecureHMACHashGenerator[numHashFunctions];
            for (int i = 0; i<numHashFunctions; i++){
                hashFunctions[i] = new SecureHMACHashGenerator(keys[i]);
            }

            for (int i=0; i<numHashFunctions; i++){
                int hash = hashFunctions[i].generateHMAC(pair.intToBytes(pair.hashCode()));
                //index[i] = hash;
                index[i] = Math.abs(hash % SecureBloomFilterWithDensity.getScale());
            }

        }
        public boolean searchInBloomFilter(SecureBloomFilterWithDensity secureBloomFilterWithDensity){
            for (int i=0; i<numHashFunctions; i++){
                //int code = Math.abs(index[i]%(bloomFilterWithDensity.getLength()));
                if(!secureBloomFilterWithDensity.searchWithIndex(index[i]))
                    return false;
            }
            return true;
        }

        public boolean searchInBitSet(BitSet bitSet){
            for (int i=0; i<numHashFunctions; i++){
                //int code = Math.abs(index[i]%(bloomFilterWithDensity.getLength()));
                if(!bitSet.get(index[i]))
                    return false;
            }
            return true;
        }



        public int getIndex(){
            return index[0];
        }

        public void printIndex(){
            for (int i=0; i<numHashFunctions; i++){
                System.out.print(index[i] + ", ");
            }
            System.out.println();
        }
    }
}
