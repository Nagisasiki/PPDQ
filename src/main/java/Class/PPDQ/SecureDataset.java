package Class.PPDQ;

import Class.DQ.DistriMap;
import Class.DQ.SimilarIndex;
import Methods.PPDQ.SecureInnerProductCalculator;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SecureDataset {
    private ArrayList<SecureDistriMap> secureDistriMaps;
    private String name;

    public SecureDataset(String name) {
        this.name = name;
        this.secureDistriMaps = new ArrayList<>();
    }

    /**
     * 构造方法
     *
     * @param name 名字
     * @param path 路径
     */
    public SecureDataset(String name, String path) {
        this.name = name;
        this.secureDistriMaps = new ArrayList<>();

        File folder = new File(path);
        File[] files = folder.listFiles();

        /*for (File file : files){
            String str = file.getAbsolutePath();
            System.out.println(str);
        }*/

        for (int i=1; i<=files.length; i++){
            String absolutePath = path + "\\Distribution_" + i + ".txt";
            SecureDistriMap secureDistriMap = new SecureDistriMap(i, absolutePath);
            secureDistriMaps.add(secureDistriMap);
        }

        /*if (files != null) {
            int id = 0;

            for (File file : files) {
                if (file.isFile()) {
                    DistriMap distriMap = new DistriMap(id, file.getAbsolutePath());
                    distriMaps.add(distriMap);
                    id++;
                }
            }
        }*/
    }


    public SecureDataset(String name, String path, SecureInnerProductCalculator secureInnerProductCalculator) {
        this.name = name;
        this.secureDistriMaps = new ArrayList<>();

        File folder = new File(path);
        File[] files = folder.listFiles();

        /*for (File file : files){
            String str = file.getAbsolutePath();
            System.out.println(str);
        }*/

        for (int i=1; i<=files.length; i++){
            String absolutePath = path + "\\Distribution_" + i + ".txt";
            SecureDistriMap secureDistriMap = new SecureDistriMap(i, absolutePath, secureInnerProductCalculator);
            secureDistriMaps.add(secureDistriMap);
        }

        /*if (files != null) {
            int id = 0;

            for (File file : files) {
                if (file.isFile()) {
                    DistriMap distriMap = new DistriMap(id, file.getAbsolutePath());
                    distriMaps.add(distriMap);
                    id++;
                }
            }
        }*/
    }

    public void printDataset() {
        System.out.println("Dataset Name: " + name);
        System.out.println("Number of DistriMaps: " + secureDistriMaps.size());

        for (SecureDistriMap secureDistriMap : secureDistriMaps) {
            secureDistriMap.printMap();
        }
    }

    public void addDistriMap(SecureDistriMap secureDistriMap) {
        secureDistriMaps.add(secureDistriMap);
    }

    public SecureDistriMap getDistriMap(int index) {
        if (index > 0 && index <= secureDistriMaps.size()) {
            return secureDistriMaps.get(index-1);
        } else {
            throw new IllegalArgumentException("Invalid index");
        }
    }

    public ArrayList<SecureDistriMap> getDistriMaps(){
        return this.secureDistriMaps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SecureSimilarIndex> getIndices() throws NoSuchAlgorithmException, InvalidKeyException {
        ArrayList<SecureSimilarIndex> indices = new ArrayList<>();
        for (SecureDistriMap secureDistriMap : secureDistriMaps){
            SecureSimilarIndex secureSimilarIndex = new SecureSimilarIndex(secureDistriMap,100,0.01);
            indices.add(secureSimilarIndex);
        }
        return indices;
    }

    public int getLength(){
        return this.secureDistriMaps.size();
    }

}

