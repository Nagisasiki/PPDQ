package Test;

import Class.PPDQ.*;
import Methods.PPDQ.SecureInnerProductCalculator;
import Methods.PPDQ.SecureSearchWithSimilarIndex;
import Methods.PPDQ.SecureTree;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestTime {
    public TestTime() throws NoSuchAlgorithmException, InvalidKeyException {
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, InterruptedException {


        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("请输入命令：");
            System.out.println("0：退出试验");
            System.out.println("1：比较不同k值情况下得平均查询时间");
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    System.out.println("请输入查询数据集名称");
                    String dataset = scanner.nextLine();

                    System.out.println("请输入数据集数量");
                    int number = Integer.parseInt(scanner.nextLine());

                    String path = null;
                    if (dataset.equals("public")){
                        path = "E:\\lpy\\datasets\\public\\distributions"; // 指定实际的路径
                    } else if (dataset.equals("identifiable")) {
                        path = "E:\\lpy\\datasets\\identifiable\\distributions"; // 指定实际的路径
                    }else if (dataset.equals("trackable")){
                        path = "E:\\lpy\\datasets\\trackable\\distributions"; // 指定实际的路径
                    }else if (dataset.equals("train")){
                        path = "E:\\lpy\\datasets\\forecasting\\train\\distributions"; // 指定实际的路径
                    }else {
                        System.out.println("不存在该数据集");
                        break;
                    }
                    String datasetName = "public"; // 指定数据集名

                    SecureInnerProductCalculator secureInnerProductCalculator = new SecureInnerProductCalculator(200);

                    SecureDataset secureDataset = new SecureDataset(datasetName, path, secureInnerProductCalculator,number);
                    ArrayList<SecureSimilarIndex> secureSimilarIndices = secureDataset.getIndices();

                    ArrayList<SecureIndexNode> secureIndexNodes = SecureTree.GenIndexNodes(secureDataset);
                    SecureIndexNode secureRoot = SecureTree.buildTree(secureIndexNodes);


                    for(int k = 10; k<=15; k++){

                        long stime;
                        long etime;
                        long time1 = 0;
                        long time2 = 0;
                        int n=0;
                        for(int i=1; i<number; i = i + 10){
                            n++;
                            String abspath = path + "\\Distribution_" + i + ".txt";
                            SecureDistriMap secureDistriMap = new SecureDistriMap(i,abspath);
                            SecureGenQuery secureGenQuery = new SecureGenQuery(secureDistriMap,3, secureInnerProductCalculator);


                            stime = System.currentTimeMillis();
                            SecureSearchWithSimilarIndex.topKsearchWithIndex(secureSimilarIndices,secureGenQuery,k);
                            etime = System.currentTimeMillis();
                            time1 += (etime - stime);

                            stime = System.currentTimeMillis();
                            SecureTree.topKSearchWithTree(secureRoot,secureGenQuery,k);
                            etime = System.currentTimeMillis();

                            time2 += (etime - stime);

                        }

                        System.out.println("top-k搜索, k=" + k + ",  " + "无索引树搜索时间" + time1/n + "ms" + ",  " + "有索引树搜索时间" + time2/n + "ms") ;
                    }


                    break;
                case "2":
                    System.out.println("执行关闭操作");
                    break;
                case "3":
                    System.out.println("执行保存操作");
                    break;
                case "0":
                    System.out.println("退出实验");
                    isRunning = false; // 设置退出循环的条件
                    break;
                default:
                    System.out.println("无法识别的命令");
                    break;
            }
        }

        scanner.close();

    }

}
