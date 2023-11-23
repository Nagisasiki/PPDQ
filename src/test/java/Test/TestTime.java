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
            System.out.println("���������");
            System.out.println("0���˳�����");
            System.out.println("1���Ƚϲ�ͬkֵ����µ�ƽ����ѯʱ��");
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    System.out.println("�������ѯ���ݼ�����");
                    String dataset = scanner.nextLine();

                    System.out.println("���������ݼ�����");
                    int number = Integer.parseInt(scanner.nextLine());

                    String path = null;
                    if (dataset.equals("public")){
                        path = "E:\\lpy\\datasets\\public\\distributions"; // ָ��ʵ�ʵ�·��
                    } else if (dataset.equals("identifiable")) {
                        path = "E:\\lpy\\datasets\\identifiable\\distributions"; // ָ��ʵ�ʵ�·��
                    }else if (dataset.equals("trackable")){
                        path = "E:\\lpy\\datasets\\trackable\\distributions"; // ָ��ʵ�ʵ�·��
                    }else if (dataset.equals("train")){
                        path = "E:\\lpy\\datasets\\forecasting\\train\\distributions"; // ָ��ʵ�ʵ�·��
                    }else {
                        System.out.println("�����ڸ����ݼ�");
                        break;
                    }
                    String datasetName = "public"; // ָ�����ݼ���

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

                        System.out.println("top-k����, k=" + k + ",  " + "������������ʱ��" + time1/n + "ms" + ",  " + "������������ʱ��" + time2/n + "ms") ;
                    }


                    break;
                case "2":
                    System.out.println("ִ�йرղ���");
                    break;
                case "3":
                    System.out.println("ִ�б������");
                    break;
                case "0":
                    System.out.println("�˳�ʵ��");
                    isRunning = false; // �����˳�ѭ��������
                    break;
                default:
                    System.out.println("�޷�ʶ�������");
                    break;
            }
        }

        scanner.close();

    }

}
