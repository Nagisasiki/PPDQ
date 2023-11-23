package Class.EPDS;

import Class.PPDQ.SecureDistriMap;
import Class.PPDQ.SecureSimilarIndex;
import Methods.EPDS.ExernalIndex;
import Methods.PPDQ.SecureInnerProductCalculator;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SecureDataset {
    private ArrayList<SecureDistribution> secureDistributions;
    private String name;

    private float resolution;  //密度掩码的分辨率，表示每个非零有效位代表的密度大
    private int numberOfHashFunctions; //HMAC哈希函数的数量
    private int dimensional; //位置向量的维度

    private String path;

    private ExternalNode externalIndex;

    public String getPath() {
        return path;
    }

    /**
     * 构造方法
     *
     * @param name 名字
     * @param path 路径
     */
    public SecureDataset(String name, String path, float resolution, int dimensional, int numberOfHashFunctions) {
        this.name = name;
        this.secureDistributions = new ArrayList<>();
        this.dimensional = dimensional;
        this.numberOfHashFunctions=numberOfHashFunctions;
        this.resolution=resolution;
        this.path = path;

        File folder = new File(path);
        File[] files = folder.listFiles();

        /*for (File file : files){
            String str = file.getAbsolutePath();
            System.out.println(str);
        }*/

        for (int i=1; i<=files.length; i++){
            String absolutePath = path + "\\Distribution_" + i + ".txt";
            SecureDistribution secureDistribution = new SecureDistribution(i,resolution,numberOfHashFunctions,dimensional,absolutePath);
            secureDistributions.add(secureDistribution);
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


    public SecureDataset(String name, String path, int number, float resolution, int numberOfHashFunctions,int dimensional) {
        this.name = name;
        this.name = name;
        this.secureDistributions = new ArrayList<>();
        this.dimensional = dimensional;
        this.numberOfHashFunctions=numberOfHashFunctions;
        this.resolution=resolution;
        this.path = path;
        int flag=0;
        int a =0;

        File folder = new File(path);
        File[] files = folder.listFiles();

        /*for (File file : files){
            String str = file.getAbsolutePath();
            System.out.println(str);
        }*/

        for (int i=1; i<=number; i++){
            String absolutePath = path + "\\Distribution_" + i + ".txt";
            SecureDistribution secureDistribution = new SecureDistribution(i, resolution, numberOfHashFunctions,dimensional,absolutePath);
            secureDistributions.add(secureDistribution);
            flag++;

            if(flag == 10000){
                a++;
                System.out.println("已将：" + a*10000 + "个数据集转化为分布");
                flag = 0;
            }
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
        System.out.println("Number of DistriMaps: " + secureDistributions.size());

        for (SecureDistribution secureDistribution : secureDistributions) {
            secureDistribution.printMap();
        }
    }

    public void addDistriMap(SecureDistribution secureDistribution) {
        secureDistributions.add(secureDistribution);
    }

    public SecureDistribution getDistribution(int index) {
        if (index > 0 && index <= secureDistributions.size()) {
            return secureDistributions.get(index-1);
        } else {
            throw new IllegalArgumentException("Invalid index");
        }
    }

    public void encrypt(SecureInnerProductCalculator CalculatorForLocation, SecureInnerProductCalculator CalculatorForDensity){
        int length = this.secureDistributions.size();
        for (int i=0;i<length;i++){
            secureDistributions.get(i).encrypt(CalculatorForLocation,CalculatorForDensity);
        }
    }


    public void constructInnerINdex(){
        for(SecureDistribution secureDistribution : this.secureDistributions){
            //System.out.println(secureDistribution.getId());
            secureDistribution.setInnerIndex();
        }
    }

    public void constructExternalIndex(){
        this.externalIndex = ExernalIndex.buildTree(this);
    }

    public void printExternalIndex(){
        this.externalIndex.printNode();
    }

    public ExternalNode getExternalIndex() {
        return externalIndex;
    }

    public ArrayList<SecureDistribution> getSecureDistributions(){
        return this.secureDistributions;
    }

    public String getName() {
        return name;
    }

    public float getResolution() {
        return resolution;
    }

    public int getNumberOfHashFunctions() {
        return numberOfHashFunctions;
    }

    public int getDimensional() {
        return dimensional;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getLength(){
        return this.secureDistributions.size();
    }

}

