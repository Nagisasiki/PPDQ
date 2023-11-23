package Methods.DQ;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import Class.DQ.DistriMap;

import java.util.Map;

public class BloomFilterWithDensity {
    //private BitSet bitSet;
    private Float[] list;
    private byte[][] keys = {{ 0x12, 0x34, 0x56},{ 0x4, 0x36, 0x68},{ 0x36, 0x38, 0x60}, { 0x6, 0x68, 0x50}};
    private HMACHashGenerator[] hashFunctions;
    private int numHashFunctions;
    private int number;
    private int scale;
    //private HashFunction<T>[] hashFunctions;

    /**
     * 密度感知布隆过滤器
     *
     * @param expectedInsertions 预期插入元素数量
     * @param falsePositiveRate  预期假阳性率
     */
    public BloomFilterWithDensity(int expectedInsertions, double falsePositiveRate) {
        this.number = 0;
        int numBits = optimalNumOfBits(expectedInsertions, falsePositiveRate);
        list = new Float[numBits*2];
        //System.out.println(numBits*2);
        //bitSet = new BitSet(numBits);
//        list.ensureCapacity(numBits);

        //numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
        numHashFunctions =3;
        hashFunctions = new HMACHashGenerator[numHashFunctions];
        for (int i = 0; i<3; i++){
            hashFunctions[i] = new HMACHashGenerator(keys[i]);
        }
        //hashFunctions = createHashFunctions(numHashFunctions);
    }

    public BloomFilterWithDensity(Float[] floats){
        this.number = 0;
        this.list = floats;
    }

    public void calNum(){
        int num =0;
        for (int i=0; i<list.length; i++){
            if (list[i] != null)
                num++;
        }
        System.out.println(num);
    }

    /**
     * 布隆过滤器中添加元素（distrimap）
     *
     * @param distriMap distrimap
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public void add(DistriMap distriMap) throws NoSuchAlgorithmException, InvalidKeyException {
        Map<DistriMap.Pair, Float> map = distriMap.getMap();
        for (Map.Entry<DistriMap.Pair, Float> entry :map.entrySet()){
            DistriMap.Pair pair = entry.getKey();
            Float value = entry.getValue();

            for(HMACHashGenerator hmacHashGenerator : hashFunctions){
                int hash = hmacHashGenerator.generateHMAC(pair.intToBytes(pair.hashCode()));
                //System.out.print(hash + ",");
                if (list[Math.abs(hash % list.length)] !=null){
                    number++;
                }
                list[Math.abs(hash % list.length)] = value;
            }
            //System.out.println();
        }
        //System.out.println("Number = " + number);
        /*for (HashFunction<T> hashFunction : hashFunctions) {
            int hash = hashFunction.hash(element);
            bitSet.set(Math.abs(hash % bitSet.size()), true);
        }*/
    }

    /**
     * 根据下标判断是否存在
     *
     * @param index 下标
     * @return boolean
     */
    public boolean searchWithIndex(int index){
        if (list[index] == null){
            return false;
        }
        return true;
    }

    public int getNumHashFunctions(){
        return numHashFunctions;
    }


    public boolean contains(DistriMap.Pair pair) throws NoSuchAlgorithmException, InvalidKeyException {
        for(HMACHashGenerator hmacHashGenerator : hashFunctions){
            int hash = hmacHashGenerator.generateHMAC(pair.intToBytes(pair.hashCode()));
            if(list[Math.abs(hash % list.length)] == null){
                return false;
            }
        }
        return true;
    }

    private int optimalNumOfHashFunctions(int expectedInsertions, int numBits) {
        // formula to calculate optimal number of hash functions
        return Math.max(1, (int) Math.round((double) numBits / expectedInsertions * Math.log(2)));
    }

    //根据预期元素数量和误判率调整大小
    private static int optimalNumOfBits(int expectedInsertions, double falsePositiveRate) {
        // formula to calculate optimal number of bits
        return (int) (-expectedInsertions * Math.log(falsePositiveRate) / (Math.log(2) * Math.log(2)));
    }

    public void printFilter(){
        for (int i=0; i<list.length; i++){
            System.out.print(list[i]);
            System.out.print(" ");
        }
        System.out.println();
    }

    public int getLength(){
        return list.length;
    }

    public int getNumber(){
        return this.number;
    }

    public Float getDensity(int index){
        return list[index];
    }

    public static int getScale(){
        return optimalNumOfBits(100,0.01)*2;
    }

}


