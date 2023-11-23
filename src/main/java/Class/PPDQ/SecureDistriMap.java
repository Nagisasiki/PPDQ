package Class.PPDQ;

import Methods.PPDQ.DensityVector;
import Methods.PPDQ.SecureInnerProductCalculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class SecureDistriMap {
    private int id;
    private int num;
    private Map<Pair, DensityVector> map;
    private float resolution = 0.01f;

    public float getResolution() {
        return resolution;
    }

    public SecureDistriMap(int id, int num) {
        this.id = id;
        this.num = num;
        this.map = new HashMap<>();
    }

    /**
     * distriMap
     *
     * @param id       id
     * @param filePath 文件路径
     */
    public SecureDistriMap(int id, String filePath) {
        this.id = id;
        this.num = 0;
        this.map = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");
                if (parts.length == 3) {
                    //System.out.println("aaa");
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    float value = Float.parseFloat(parts[2]);
                    DensityVector densityVector = new DensityVector(value,resolution);
                    //DensityVector.printVector(density);
                    addToMap(x, y, densityVector);
                    num++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.map.toString();
    }


    public SecureDistriMap(int id, String filePath, SecureInnerProductCalculator secureInnerProductCalculator) {
        this.id = id;
        this.num = 0;
        this.map = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");
                if (parts.length == 3) {
                    //System.out.println("aaa");
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    float value = Float.parseFloat(parts[2]);
                    DensityVector densityVector = new DensityVector(value,resolution);
                    densityVector.setDensityVector(secureInnerProductCalculator.multiplyWithKeyMatrix(densityVector.getDensityVector()));
                    //DensityVector.printVector(density);
                    addToMap(x, y, densityVector);
                    num++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.map.toString();
    }

    public void printMap() {
        System.out.println("id = " + this.id);
        System.out.println("num = " + this.num);

        for (Map.Entry<Pair, DensityVector> entry : this.map.entrySet()) {
            Pair key = entry.getKey();
            DensityVector densityVector = entry.getValue();
            System.out.println("Key: " + key.toString() + ", Value: ");
            DensityVector.printVector(densityVector.getDensityVector());
            //key.printByteArray(key.intToBytes(key.hashCode()));
        }
    }

    public int getId(){
        return this.id;
    }

    public int getNum(){
        return this.num;
    }



    public void addToMap(int x, int y, DensityVector value) {
        Pair pair = new Pair(x, y);
        map.put(pair, value);
    }



    public Map<Pair, DensityVector> getMap() {
        return map;
    }

    // Getter and Setter methods for id and num

    public static class Pair {
        private int x;
        private int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            String binaryX = Integer.toBinaryString(x);
            String binaryY = Integer.toBinaryString(y);
            //System.out.println(binaryX + " " + binaryY);

            String combinedBinary = binaryX + "|" + binaryY;  // 添加分隔符

            // 将二进制编码转换为整数哈希码
            int hash = combinedBinary.hashCode();

            return hash;
        }

        public byte[] intToBytes(int value) {
            ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
            buffer.putInt(value);
            return buffer.array();
        }

        public void printByteArray(byte[] array) {
            for (byte b : array) {
                System.out.print(b + " ");
            }
            System.out.println();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Pair other = (Pair) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public String toString() {
            //printByteArray(intToBytes(hashCode()));
            return "x = " + this.x + ", y = " + y + ", hashCode = " + hashCode() ;
        }
    }
}

