package Class.PPDQ;

import Methods.PPDQ.DensityVector;
import Methods.PPDQ.SecureInnerProductCalculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public SecureDistriMap(int id, String filePath) {
        this.id = id;
        this.num = 0;
        this.map = new HashMap<>();
        loadFromFile(filePath);
    }


    //构造函数，输入id，路径以及安全内积计算器
    public SecureDistriMap(int id, String filePath, SecureInnerProductCalculator secureInnerProductCalculator) {
        this.id = id;
        this.num = 0;
        this.map = new HashMap<>();
        loadFromFile(filePath, secureInnerProductCalculator);
    }

    private void loadFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.map.toString();
    }

    private void loadFromFile(String filePath, SecureInnerProductCalculator secureInnerProductCalculator) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line, secureInnerProductCalculator);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.map.toString();
    }

    private void processLine(String line) {
        String[] parts = line.split(",");
        if (parts.length == 3) {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            float value = Float.parseFloat(parts[2]);
            DensityVector densityVector = new DensityVector(value, resolution);
            addToMap(x, y, densityVector);
            num++;
        }
    }

    private void processLine(String line, SecureInnerProductCalculator secureInnerProductCalculator) {
        String[] parts = line.split(",");
        if (parts.length == 3) {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            float value = Float.parseFloat(parts[2]);
            DensityVector densityVector = new DensityVector(value, resolution);
            densityVector.setDensityVector(secureInnerProductCalculator.encryptVector(densityVector.getDensityVector(), true));
            addToMap(x, y, densityVector);
            num++;
        }
    }

    public void printMap() {
        System.out.println("id = " + this.id);
        System.out.println("num = " + this.num);

        for (Map.Entry<Pair, DensityVector> entry : this.map.entrySet()) {
            Pair key = entry.getKey();
            DensityVector densityVector = entry.getValue();
            System.out.println("Key: " + key.toString() + ", Value: ");
            DensityVector.printVector(densityVector.getDensityVector());
        }
    }

    public int getId() {
        return this.id;
    }

    public int getNum() {
        return this.num;
    }

    public void addToMap(int x, int y, DensityVector value) {
        Pair pair = new Pair(x, y);
        map.put(pair, value);
    }

    public Map<Pair, DensityVector> getMap() {
        return map;
    }

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
            String combinedBinary = binaryX + "|" + binaryY;
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
            return "x = " + this.x + ", y = " + y + ", hashCode = " + hashCode();
        }
    }

    public static List<SecureDistriMap> createSecureDistriMaps(String directoryPath, int number) {
        List<SecureDistriMap> secureDistriMaps = new ArrayList<>();

        for (int i = 1; i <= number; i++) {
            String filePath = directoryPath + "/distribution_" + i + ".txt";
            SecureDistriMap secureDistriMap = new SecureDistriMap(secureDistriMaps.size(), filePath);
            secureDistriMaps.add(secureDistriMap);
        }

        return secureDistriMaps;
    }

}
