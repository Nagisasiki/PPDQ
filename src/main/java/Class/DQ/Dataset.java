package Class.DQ;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Dataset {
    private ArrayList<DistriMap> distriMaps;
    private String name;

    public Dataset(String name) {
        this.name = name;
        this.distriMaps = new ArrayList<>();
    }

    /**
     * 构造方法
     *
     * @param name 名字
     * @param path 路径
     */
    public Dataset(String name, String path) {
        this.name = name;
        this.distriMaps = new ArrayList<>();

        File folder = new File(path);
        File[] files = folder.listFiles();

        /*for (File file : files){
            String str = file.getAbsolutePath();
            System.out.println(str);
        }*/

        for (int i=1; i<=files.length; i++){
            String absolutePath = path + "\\Distribution_" + i + ".txt";
            DistriMap distriMap = new DistriMap(i, absolutePath);
            distriMaps.add(distriMap);
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

    public Dataset(String name, String path, int number) {
        this.name = name;
        this.distriMaps = new ArrayList<>();

        File folder = new File(path);
        File[] files = folder.listFiles();

        /*for (File file : files){
            String str = file.getAbsolutePath();
            System.out.println(str);
        }*/

        for (int i=1; i<=number; i++){
            String absolutePath = path + "\\Distribution_" + i + ".txt";
            DistriMap distriMap = new DistriMap(i, absolutePath);
            distriMaps.add(distriMap);
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
        System.out.println("Number of DistriMaps: " + distriMaps.size());

        for (DistriMap distriMap : distriMaps) {
            distriMap.printMap();
        }
    }

    public void addDistriMap(DistriMap distriMap) {
        distriMaps.add(distriMap);
    }

    public DistriMap getDistriMap(int index) {
        if (index > 0 && index <= distriMaps.size()) {
            return distriMaps.get(index-1);
        } else {
            throw new IllegalArgumentException("Invalid index");
        }
    }

    public ArrayList<DistriMap> getDistriMaps(){
        return this.distriMaps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SimilarIndex> getIndices() throws NoSuchAlgorithmException, InvalidKeyException {
        ArrayList<SimilarIndex> indices = new ArrayList<>();
        for (DistriMap distriMap : distriMaps){
            SimilarIndex similarIndex = new SimilarIndex(distriMap,100,0.01);
            indices.add(similarIndex);
        }
        return indices;
    }

    public int getLength(){
        return this.distriMaps.size();
    }

}

