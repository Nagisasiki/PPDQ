package Class.DQ;

import Methods.DQ.BloomFilterWithDensity;
import Methods.DQ.HMACHashGenerator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class GenQuery {
    private int id;
    private Map<Index, Float> query;
    private int numHashFunctions;

    public GenQuery(DistriMap distriMap, int numHashFunctions) throws NoSuchAlgorithmException, InvalidKeyException {
        this.id = distriMap.getId();
        this.numHashFunctions = numHashFunctions;
        Map<DistriMap.Pair, Float> map = new HashMap<>();
        map = distriMap.getMap();
        query = new HashMap<>();

        for (Map.Entry<DistriMap.Pair,Float> entry : map.entrySet()){
            DistriMap.Pair key = entry.getKey();
            Float value = entry.getValue();
            Index index = new Index(numHashFunctions, key);
            query.put(index,value);
        }

    }

    public int getId(){
        return this.id;
    }

    public Map<Index, Float> getQuery(){
        return this.query;
    }

    public void printQuery() {
        for (Map.Entry<Index, Float> entry : query.entrySet()) {
            Index index = entry.getKey();
            Float value = entry.getValue();
            index.printIndex();
            System.out.println("Value: " + value);
            System.out.println("----------------------");
        }
    }

    public static class Index{
        private int[] index;
        private byte[][] keys = {{ 0x12, 0x34, 0x56},{ 0x4, 0x36, 0x68},{ 0x36, 0x38, 0x60}, { 0x6, 0x68, 0x50}};
        private HMACHashGenerator[] hashFunctions;
        private int numHashFunctions;

        public Index(int numHashFunctions, DistriMap.Pair pair) throws NoSuchAlgorithmException, InvalidKeyException {
            this.numHashFunctions = numHashFunctions;
            index = new int[numHashFunctions];
            hashFunctions = new HMACHashGenerator[numHashFunctions];
            for (int i = 0; i<numHashFunctions; i++){
                hashFunctions[i] = new HMACHashGenerator(keys[i]);
            }

            for (int i=0; i<numHashFunctions; i++){
                int hash = hashFunctions[i].generateHMAC(pair.intToBytes(pair.hashCode()));
                //index[i] = hash;
                index[i] = Math.abs(hash % BloomFilterWithDensity.getScale());
            }

        }
        public boolean searchInBloomFilter(BloomFilterWithDensity bloomFilterWithDensity){
            for (int i=0; i<numHashFunctions; i++){
                //int code = Math.abs(index[i]%(bloomFilterWithDensity.getLength()));
                if(!bloomFilterWithDensity.searchWithIndex(index[i]))
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
