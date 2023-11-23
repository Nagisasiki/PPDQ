package Test.Experiment;

import Class.EPDS.SecureDataset;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

public class Memory {
    public static void main(String[] args) {
        String path = "D:\\lpy\\datasets\\public\\Distribution14"; // 指定实际的路径
        String datasetName = "trackable"; // 指定数据集名
        int number = 100000;//数据集数量
        int k=10;           //top-k查询
        int queryNumber = 100;//重复次数
        float resolution = 0.01f;
        int numberOfHashFunctions=3;
        int diemnsional = 200;
        SecureDataset secureDataset = new SecureDataset(datasetName,path,0.01f,600,3);
        System.out.println(ObjectSizeCalculator.getObjectSize(secureDataset)/1073741824);
        secureDataset.constructInnerINdex();
        secureDataset.constructExternalIndex();
        System.out.println(ObjectSizeCalculator.getObjectSize(secureDataset)/1073741824);
    }
}
