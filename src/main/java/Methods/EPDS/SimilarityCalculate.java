package Methods.EPDS;

import Class.DQ.Dataset;
import Class.EPDS.ExternalNode;
import Class.EPDS.SecureDataset;
import Class.EPDS.SecureDistribution;
import Methods.DQ.SearchWithDistribution;

import java.util.*;

public class SimilarityCalculate {
    //�޾�����ʧ�����ķ���
    public static void similarityCalculateOnPlaintext(Dataset dataset, int k, int querynumber){
        for (int i=1; i<=querynumber;i++){
            SearchWithDistribution.topkSearch(dataset,i,k);
        }
    }



    //�������������������ް�ȫ�ڻ�����
    public static void similarityCalculate(SecureDataset secureDataset, int k, int querynumber){
        float resolution = secureDataset.getResolution();
        int numberOfHashFunctions = secureDataset.getNumberOfHashFunctions();
        int dimensional = secureDataset.getDimensional();
        String path = secureDataset.getPath();
        long time=0;
        long stime;
        long etime;

        long simTime=0;
        long simTimeBegin;
        long simTimeEnd;


        for (int i=1; i<=querynumber; i++){

            Map<Integer, Double> similarMap = new HashMap<>();

            double similarity;
            SecureDistribution query = SimilarityCalculate.GenQuery(i,resolution,numberOfHashFunctions,dimensional,path);


            stime = System.currentTimeMillis();
            for (int j=1; j<=secureDataset.getLength(); j++){
                simTimeBegin = System.currentTimeMillis();
                similarity = SecureDistribution.calculateSimilarity(secureDataset.getDistribution(j),query);
                simTimeEnd = System.currentTimeMillis();
                simTime += simTimeEnd-simTimeBegin;
                similarMap.put(j,similarity);
            }
            List<Map.Entry<Integer, Double>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
            sortedSimilarityList.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
            etime = System.currentTimeMillis();

            time += (etime-stime);
            //System.out.println(time);

            /*System.out.println("��ѯ���ݼ�Id��" + query.getId());
            //System.out.println("���ƶȣ�");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("���ݼ� ID: " + id +  "   " + "���ƶ�: " + similar );
                }
                if (num>k)
                    break;
            }*/
        }

        System.out.println("��������ƽ������ʱ�䣺" + time/querynumber);
        System.out.println("���ƶȼ���ʱ�䣺" + simTime/querynumber);
        System.out.println("����ʱ��" + (time/querynumber - simTime/querynumber));

    }

    public static SecureDistribution GenQuery(int id,float resolution, int numberOfHashFunctions, int dimensional, String filePath){
        String absolutePath = filePath + "\\Distribution_" + id + ".txt";
        SecureDistribution query = new SecureDistribution(id,resolution,numberOfHashFunctions,dimensional,absolutePath);
        return query;
    }

    //����ɸѡ��mΪ�Ŵ�ϵ��
    public static void RankSearch(SecureDataset secureDataset, int k, float m,int querynumber){
        int candidateNumber = (int) (m*secureDataset.getLength());
        //ArrayList<SecureDistribution> secureDistributions = secureDataset.getSecureDistributions();
        float resolution = secureDataset.getResolution();
        int numberOfHashFunctions = secureDataset.getNumberOfHashFunctions();
        int dimensional = secureDataset.getDimensional();
        String path = secureDataset.getPath();
        long time=0;
        long stime;
        long etime;
        for (int i=1; i<=querynumber; i++){

            Map<Integer, Double> similarMap = new HashMap<>();

            double similarity;
            SecureDistribution query = SimilarityCalculate.GenQuery(i,resolution,numberOfHashFunctions,dimensional,path);

            //CandidateTable candidateTable = new CandidateTable(candidateNumber);
            //ArrayList<SecureDistribution> candidateDistributions = new ArrayList<>();

            PriorityQueue<SecureDistribution> priorityQueue = new PriorityQueue<>(
                    Comparator.comparingDouble(SecureDistribution::getPriority).reversed()
            );

            stime = System.currentTimeMillis();


            for (int j=1; j<=secureDataset.getLength(); j++){
                secureDataset.getDistribution(j).setPriority(LocationVector.calculateInnerProduct(secureDataset.getDistribution(j).getGlobeLocationVector(),query.getGlobeLocationVector()));
                priorityQueue.add(secureDataset.getDistribution(j));
            }


            //Map<SecureDistribution,Double> candidate = candidateTable.getCandidate();
            int n=0;
            //System.out.println("���г��ȣ�" + priorityQueue.size());
            while (!priorityQueue.isEmpty()) {
                SecureDistribution securedistribution = priorityQueue.poll();
                similarity = SecureDistribution.calculateSimilarity(securedistribution,query);
                similarMap.put(securedistribution.getId(),similarity);
                n++;
                if (n>candidateNumber)
                    break;
            }
            //int n=0;
            List<Map.Entry<Integer, Double>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
            sortedSimilarityList.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
            etime = System.currentTimeMillis();

            time += (etime-stime);

            /*System.out.println("��ѯ���ݼ�Id��" + query.getId());
            //System.out.println("���ƶȣ�");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("���ݼ� ID: " + id +  "   " + "���ƶ�: " + similar );
                }
                if (num>k)
                    break;
            }*/
        }
        System.out.println("Rank����ƽ������ʱ�䣺" + time/querynumber);

    }




    public static void similarityCalculateWithInnerTree(SecureDataset secureDataset, int k, int querynumber){
        float resolution = secureDataset.getResolution();
        int numberOfHashFunctions = secureDataset.getNumberOfHashFunctions();
        int dimensional = secureDataset.getDimensional();
        String path = secureDataset.getPath();
        long time=0;
        long stime;
        long etime;

        long simTime=0;
        long simTimeBegin;
        long simTimeEnd;


        for (int i=1; i<=querynumber; i++){

            Map<Integer, Double> similarMap = new HashMap<>();

            double similarity;
            SecureDistribution query = SimilarityCalculate.GenQuery(i,resolution,numberOfHashFunctions,dimensional,path);


            stime = System.currentTimeMillis();
            for (int j=1; j<=secureDataset.getLength(); j++){
                simTimeBegin = System.currentTimeMillis();
                similarity = SecureDistribution.similarityWithInnerIndex(secureDataset.getDistribution(j),query);
                simTimeEnd = System.currentTimeMillis();
                simTime += simTimeEnd - simTimeBegin;

                similarMap.put(j,similarity);
            }
            List<Map.Entry<Integer, Double>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
            sortedSimilarityList.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
            etime = System.currentTimeMillis();

            time += (etime-stime);

            /*System.out.println("��ѯ���ݼ�Id��" + query.getId());
            //System.out.println("���ƶȣ�");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("���ݼ� ID: " + id +  "   " + "���ƶ�: " + similar );
                }

                if (num>k)
                    break;
            }*/
        }

        System.out.println("�ڲ����νṹƽ������ʱ�䣺" + time/querynumber);
        System.out.println("���ƶȼ���ʱ�䣺" + simTime/querynumber);
        System.out.println("����ʱ��" + (time/querynumber - simTime/querynumber));

    }





    public static void RankSearchWithExternalTree(SecureDataset secureDataset, int k, float m,int querynumber){
        int candidateNumber = (int) (m*secureDataset.getLength());
        //ArrayList<SecureDistribution> secureDistributions = secureDataset.getSecureDistributions();
        float resolution = secureDataset.getResolution();
        int numberOfHashFunctions = secureDataset.getNumberOfHashFunctions();
        int dimensional = secureDataset.getDimensional();
        String path = secureDataset.getPath();
        long time=0;
        long stime;
        long etime;

        //secureDataset.constructInnerINdex();


        for (int i=1; i<=querynumber; i++){

            Map<Integer, Double> similarMap = new HashMap<>();

            double similarity;
            SecureDistribution query = SimilarityCalculate.GenQuery(i,resolution,numberOfHashFunctions,dimensional,path);

            //CandidateTable candidateTable = new CandidateTable(candidateNumber);
            //ArrayList<SecureDistribution> candidateDistributions = new ArrayList<>();
            //����Ȩ���д洢������ǰ�����ݼ�

            PriorityQueueWithCapacity priorityQueueWithCapacity = new PriorityQueueWithCapacity<>(candidateNumber);

            stime=System.currentTimeMillis();
            ExernalIndex.getCandidateDatasets(secureDataset.getExternalIndex(),query,priorityQueueWithCapacity,candidateNumber);
            //priorityQueueWithCapacity.printQueue();
            //Map<SecureDistribution,Double> candidate = candidateTable.getCandidate();
            //System.out.println("���г���" + priorityQueueWithCapacity.getPriorityQueue().size());
            int n=0;
            while (!priorityQueueWithCapacity.getPriorityQueue().isEmpty()) {
                ExternalNode externalNode = (ExternalNode) priorityQueueWithCapacity.getPriorityQueue().poll();

                SecureDistribution securedistribution = secureDataset.getDistribution(externalNode.getFlag());
                //SecureDistribution securedistribution = secureDataset.getDistribution(externalNode.getFlag());
                similarity = SecureDistribution.calculateSimilarity(securedistribution,query);
                similarMap.put(securedistribution.getId(),similarity);
                n++;
                if (n>candidateNumber)
                    break;


            }
            //int n=0;
            List<Map.Entry<Integer, Double>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
            sortedSimilarityList.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
            etime = System.currentTimeMillis();

            time += (etime-stime);

            /*System.out.println("��ѯ���ݼ�Id��" + query.getId());
            //System.out.println("���ƶȣ�");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("���ݼ� ID: " + id +  "   " + "���ƶ�: " + similar );
                }
                if (num>k)
                    break;
            }*/
        }
        System.out.println("�ⲿ���νṹƽ������ʱ�䣺" + time/querynumber);
    }


    public static void RankSearchWithDoubleTree(SecureDataset secureDataset, int k, float m,int querynumber){
        int candidateNumber = (int) (m*secureDataset.getLength());
        //ArrayList<SecureDistribution> secureDistributions = secureDataset.getSecureDistributions();
        float resolution = secureDataset.getResolution();
        int numberOfHashFunctions = secureDataset.getNumberOfHashFunctions();
        int dimensional = secureDataset.getDimensional();
        String path = secureDataset.getPath();
        long time=0;
        long stime;
        long etime;

        //secureDataset.constructInnerINdex();


        for (int i=1; i<=querynumber; i++){

            Map<Integer, Double> similarMap = new HashMap<>();

            double similarity;
            SecureDistribution query = SimilarityCalculate.GenQuery(i,resolution,numberOfHashFunctions,dimensional,path);

            //CandidateTable candidateTable = new CandidateTable(candidateNumber);
            //ArrayList<SecureDistribution> candidateDistributions = new ArrayList<>();
            //����Ȩ���д洢������ǰ�����ݼ�

            PriorityQueueWithCapacity priorityQueueWithCapacity = new PriorityQueueWithCapacity<>(candidateNumber);

            stime=System.currentTimeMillis();
            ExernalIndex.getCandidateDatasets(secureDataset.getExternalIndex(),query,priorityQueueWithCapacity,candidateNumber);
            /*PriorityQueue<SecureDistribution> priorityQueue = new PriorityQueue<>(
                    Comparator.comparingDouble(SecureDistribution::getPriority).reversed()
            );

            stime = System.currentTimeMillis();


            for (int j=1; j<=secureDataset.getLength(); j++){
                secureDataset.getDistribution(j).setPriority(LocationVector.calculateInnerProduct(secureDataset.getDistribution(j).getGlobeLocationVector(),query.getGlobeLocationVector()));
                priorityQueue.add(secureDataset.getDistribution(j));
            }*/
            //priorityQueueWithCapacity.printQueue();
            //Map<SecureDistribution,Double> candidate = candidateTable.getCandidate();
            int n=0;
            /*while (!priorityQueue.isEmpty()) {
                SecureDistribution securedistribution = priorityQueue.poll();
                similarity = SecureDistribution.similarityWithInnerIndex(securedistribution,query);
                similarMap.put(securedistribution.getId(),similarity);
                n++;
                if (n>candidateNumber)
                    break;
            }*/
            while (!priorityQueueWithCapacity.getPriorityQueue().isEmpty()) {
                ExternalNode externalNode = (ExternalNode) priorityQueueWithCapacity.getPriorityQueue().poll();

                SecureDistribution securedistribution = secureDataset.getDistribution(externalNode.getFlag());
                //SecureDistribution securedistribution = secureDataset.getDistribution(externalNode.getFlag());
                //similarity = SecureDistribution.calculateSimilarity(securedistribution,query);
                similarity = SecureDistribution.similarityWithInnerIndex(securedistribution,query);
                similarMap.put(securedistribution.getId(),similarity);
                n++;
                if (n>candidateNumber)
                    break;


            }
            //int n=0;
            List<Map.Entry<Integer, Double>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
            sortedSimilarityList.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
            etime = System.currentTimeMillis();

            time += (etime-stime);

            System.out.println("��ѯ���ݼ�Id��" + query.getId());
            //System.out.println("���ƶȣ�");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("���ݼ� ID: " + id +  "   " + "���ƶ�: " + similar );
                }
                if (num>k)
                    break;
            }
        }
        System.out.println("���������ṹƽ������ʱ�䣺" + time/querynumber);
    }


    public static void RankSearchWithRankAndTree(SecureDataset secureDataset, int k, float m,int querynumber){
        int candidateNumber = (int) (m*secureDataset.getLength());
        //ArrayList<SecureDistribution> secureDistributions = secureDataset.getSecureDistributions();
        float resolution = secureDataset.getResolution();
        int numberOfHashFunctions = secureDataset.getNumberOfHashFunctions();
        int dimensional = secureDataset.getDimensional();
        String path = secureDataset.getPath();
        long time=0;
        long stime;
        long etime;

        long rankTime=0;
        long rankTimeBegin;
        long rankTimeEnd;

        long simTime=0;
        long simTimeBegin;
        long simTimeEnd;

        //secureDataset.constructInnerINdex();


        for (int i=1; i<=querynumber; i++){

            Map<Integer, Double> similarMap = new HashMap<>();

            double similarity;
            SecureDistribution query = SimilarityCalculate.GenQuery(i,resolution,numberOfHashFunctions,dimensional,path);

            //CandidateTable candidateTable = new CandidateTable(candidateNumber);
            //ArrayList<SecureDistribution> candidateDistributions = new ArrayList<>();
            //����Ȩ���д洢������ǰ�����ݼ�

            /*PriorityQueueWithCapacity priorityQueueWithCapacity = new PriorityQueueWithCapacity<>(candidateNumber);

            stime=System.currentTimeMillis();
            ExernalIndex.getCandidateDatasets(secureDataset.getExternalIndex(),query,priorityQueueWithCapacity,candidateNumber);*/
            PriorityQueue<SecureDistribution> priorityQueue = new PriorityQueue<>(
                    Comparator.comparingDouble(SecureDistribution::getPriority).reversed()
            );

            stime = System.currentTimeMillis();



            rankTimeBegin = System.currentTimeMillis();
            for (int j=1; j<=secureDataset.getLength(); j++){
                secureDataset.getDistribution(j).setPriority(LocationVector.calculateInnerProduct(secureDataset.getDistribution(j).getGlobeLocationVector(),query.getGlobeLocationVector()));
                priorityQueue.add(secureDataset.getDistribution(j));
            }
            rankTimeEnd = System.currentTimeMillis();
            rankTime += rankTimeEnd - rankTimeBegin;
            //priorityQueueWithCapacity.printQueue();
            //Map<SecureDistribution,Double> candidate = candidateTable.getCandidate();
            int n=0;
            while (!priorityQueue.isEmpty()) {
                SecureDistribution securedistribution = priorityQueue.poll();
                simTimeBegin = System.currentTimeMillis();
                similarity = SecureDistribution.similarityWithInnerIndex(securedistribution,query);
                simTimeEnd = System.currentTimeMillis();
                simTime += simTimeEnd - simTimeBegin;
                similarMap.put(securedistribution.getId(),similarity);
                n++;
                if (n>candidateNumber)
                    break;
            }
            /*while (!priorityQueueWithCapacity.getPriorityQueue().isEmpty()) {
                ExternalNode externalNode = (ExternalNode) priorityQueueWithCapacity.getPriorityQueue().poll();

                SecureDistribution securedistribution = secureDataset.getDistribution(externalNode.getFlag());
                //SecureDistribution securedistribution = secureDataset.getDistribution(externalNode.getFlag());
                //similarity = SecureDistribution.calculateSimilarity(securedistribution,query);
                similarity = SecureDistribution.similarityWithInnerIndex(securedistribution,query);
                similarMap.put(securedistribution.getId(),similarity);
                n++;
                if (n>candidateNumber)
                    break;


            }*/
            //int n=0;
            List<Map.Entry<Integer, Double>> sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
            sortedSimilarityList.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
            etime = System.currentTimeMillis();

            time += (etime-stime);

            /*System.out.println("��ѯ���ݼ�Id��" + query.getId());
            //System.out.println("���ƶȣ�");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("���ݼ� ID: " + id +  "   " + "���ƶ�: " + similar );
                }
                if (num>k)
                    break;
            }*/
        }
        System.out.println("���������ṹƽ������ʱ�䣺" + time/querynumber);
        System.out.println("rankʱ��" + rankTime/querynumber);
        System.out.println("���ƶȼ���ʱ��" + simTime/querynumber);
        System.out.println("����ʱ��" + (time/querynumber - rankTime/querynumber - simTime/querynumber));
    }

    public static double correctness (List<Map.Entry<Integer, Double>> sortedSimilarityList1, List<Map.Entry<Integer, Double>> sortedSimilarityList2,int k){
        double correctness;
        List<Double> list1 = SimilarityCalculate.extractNonZeroDoubles(sortedSimilarityList1,k);
        List<Double> list2 = SimilarityCalculate.extractNonZeroDoubles(sortedSimilarityList2,k);

        if (list1.isEmpty() || list2.isEmpty()) {
            return 1.0; // �����һ�б�Ϊ�գ����ƶ�Ϊ0
        }

        /*for (int i=0;i<k;i++){
            System.out.println(list1.get(i));
        }

        for (int j=0;j<k;j++){
            System.out.println(list2.get(j));
        }*/

        List<Double> intersection = new ArrayList<>(list2);
        intersection.retainAll(list1); // ����



        int commonElements = intersection.size();
        int totalElementsInList2 = list2.size();




        // �������ƶȱ���
        double similarity = (double) commonElements / totalElementsInList2;

        return similarity;

    }

    public static List<Double> extractNonZeroDoubles(List<Map.Entry<Integer, Double>> sortedSimilarityList,int k) {
        List<Double> result = new ArrayList<>();
        int num=0;
        for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
            num++;
            Double similarity = entry.getValue();
            if (similarity != 0.0) {
                result.add(similarity);
            }
            if (num>=k)
                break;
        }

        return result;
    }


    public static List<Map.Entry<Integer, Double>> oneRankSearchWithDoubleTree(SecureDataset secureDataset, int k, float m,int queryId) {
        int candidateNumber = (int) (m);
        //ArrayList<SecureDistribution> secureDistributions = secureDataset.getSecureDistributions();
        float resolution = secureDataset.getResolution();
        int numberOfHashFunctions = secureDataset.getNumberOfHashFunctions();
        int dimensional = secureDataset.getDimensional();
        String path = secureDataset.getPath();

        //secureDataset.constructInnerINdex();


        List<Map.Entry<Integer, Double>> sortedSimilarityList = null;
        for (int i = 1; i <= 1; i++) {

            Map<Integer, Double> similarMap = new HashMap<>();

            double similarity;
            SecureDistribution query = SimilarityCalculate.GenQuery(queryId, resolution, numberOfHashFunctions, dimensional, path);

            //CandidateTable candidateTable = new CandidateTable(candidateNumber);
            //ArrayList<SecureDistribution> candidateDistributions = new ArrayList<>();
            //����Ȩ���д洢������ǰ�����ݼ�

            PriorityQueueWithCapacity priorityQueueWithCapacity = new PriorityQueueWithCapacity<>(candidateNumber);


            ExernalIndex.getCandidateDatasets(secureDataset.getExternalIndex(), query, priorityQueueWithCapacity, candidateNumber);
            //priorityQueueWithCapacity.printQueue();
            //Map<SecureDistribution,Double> candidate = candidateTable.getCandidate();
            //int n = 0;
            while (!priorityQueueWithCapacity.getPriorityQueue().isEmpty()) {
                ExternalNode externalNode = (ExternalNode) priorityQueueWithCapacity.getPriorityQueue().poll();

                SecureDistribution securedistribution = secureDataset.getDistribution(externalNode.getFlag());
                //SecureDistribution securedistribution = secureDataset.getDistribution(externalNode.getFlag());
                //similarity = SecureDistribution.calculateSimilarity(securedistribution,query);
                if(externalNode.getFlag()!=queryId){
                    similarity = SecureDistribution.similarityWithInnerIndex(securedistribution, query);
                    similarMap.put(securedistribution.getId(), similarity);
                }
                /*n++;
                if (n > candidateNumber)
                    break;*/


            }
            //int n=0;
            sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
            sortedSimilarityList.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));


            /*System.out.println("��ѯ���ݼ�Id��" + query.getId());
            System.out.println("���ƶȣ�");
            int num = 0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != queryId) {
                    System.out.println("���ݼ� ID: " + id + "   " + "���ƶ�: " + similar);
                }
                if (num > k)
                    break;
            }*/
        }
        return sortedSimilarityList;

    }

    public static List<Map.Entry<Integer, Double>> oneSimilarityCalculateWithInnerTree(SecureDataset secureDataset, int k, int queryId) {
        float resolution = secureDataset.getResolution();
        int numberOfHashFunctions = secureDataset.getNumberOfHashFunctions();
        int dimensional = secureDataset.getDimensional();
        String path = secureDataset.getPath();

        List<Map.Entry<Integer, Double>> sortedSimilarityList = null;
        for (int i = 1; i <= 1; i++) {

            Map<Integer, Double> similarMap = new HashMap<>();

            double similarity;
            SecureDistribution query = SimilarityCalculate.GenQuery(queryId, resolution, numberOfHashFunctions, dimensional, path);


            for (int j = 1; j <= secureDataset.getLength(); j++) {
                if(j!=queryId){
                    similarity = SecureDistribution.similarityWithInnerIndex(secureDataset.getDistribution(j), query);

                    similarMap.put(j, similarity);
                }

            }
            sortedSimilarityList = new ArrayList<>(similarMap.entrySet());
            sortedSimilarityList.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));


            /*System.out.println("��ѯ���ݼ�Id��" + query.getId());
            //System.out.println("���ƶȣ�");
            int num = 0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != queryId) {
                    System.out.println("���ݼ� ID: " + id + "   " + "���ƶ�: " + similar);
                }

                if (num > k)
                    break;
            }*/
        }
        return sortedSimilarityList;

    }

    //k:topk������mԤ����ϵ����queryNumber�ظ�����
    public static void calculationCorrectness(SecureDataset secureDataset, int k, float m,int queryNumber,List<Integer> queryIds){
        double similarity = 0.0;
        double temp;
        for (int i=1; i<=queryNumber;i++){
            int queryId = queryIds.get(i-1);
            //SimilarityCalculate.oneRankSearchWithDoubleTree(secureDataset,k,m,i);

            //SimilarityCalculate.oneSimilarityCalculateWithInnerTree(secureDataset,k,i);

            temp= SimilarityCalculate.correctness(SimilarityCalculate.oneSimilarityCalculateWithInnerTree(secureDataset,k,queryId),SimilarityCalculate.oneRankSearchWithDoubleTree(secureDataset,k,m,queryId),k);
            //System.out.println("��" + i + "�����ƶ���ȷ��" + temp);
            similarity += temp;
        }

        System.out.println(similarity);

        similarity = similarity/queryNumber;


        System.out.println("���ݼ���"+secureDataset.getName());

        System.out.println("Top:" + k + " ��ѯ");

        System.out.println("Ԥ����ϵ����"+ m/secureDataset.getLength());

        System.out.println("��ѯ������" + queryNumber );

        System.out.println("��ȷ�ʣ�"+ similarity);
        System.out.println();

    }

    public static int generateRandomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }


    public static List<Map.Entry<Integer, Double>> oneSimilarityCalculateOnPlaintext(Dataset dataset, int k, int queryId){

        return SearchWithDistribution.topkSearchReturn(dataset,queryId,k);

    }


    public static double vectorCorrectness (List<Map.Entry<Integer, Double>> sortedSimilarityList1, List<Map.Entry<Integer, Double>> sortedSimilarityList2,int k){
        int count =0;
        List<Map.Entry<Integer,Double>> list1 = SimilarityCalculate.extractNonZeroInts(sortedSimilarityList1,k);
        List<Map.Entry<Integer,Double>> list2 = SimilarityCalculate.extractNonZeroInts(sortedSimilarityList2,k);
        for (Map.Entry<Integer,Double> entry2 : list2){
            for (Map.Entry<Integer,Double> entry1 : list1){
                if (entry2.getKey().equals(entry1.getKey())){
                    count++;
                    break;
                }else if (entry2.getValue().equals(entry1.getValue())){
                    count++;
                    break;
                }
            }
        }
        System.out.println("����ֵ��" + count);
        double similarity = (double) count/k;
        /*if (similarity!=1.0){
            for (Map.Entry<Integer,Double> entry1 : sortedSimilarityList1){
                System.out.println("���ݼ���" + entry1.getKey());
                System.out.println("���ƶȣ�" + entry1.getValue());
            }
            System.out.println();
            for (Map.Entry<Integer,Double> entry2 : sortedSimilarityList2){
                System.out.println("���ݼ���" + entry2.getKey());
                System.out.println("���ƶȣ�" + entry2.getValue());
            }
            System.out.println();
        }*/
        /*double correctness;
        List<Integer> list1 = SimilarityCalculate.extractNonZeroInts(sortedSimilarityList1,k);
        List<Integer> list2 = SimilarityCalculate.extractNonZeroInts(sortedSimilarityList2,k);
        *//*for (int i=0;i<list1.size();i++){
            System.out.print(list1.get(i) + " ");
        }
        System.out.println();
        for (int i=0;i<list2.size();i++){
            System.out.print(list2.get(i) + " ");

        }
        System.out.println();*//*


        if (list1.isEmpty() || list2.isEmpty()) {
            return 1.0; // �����һ�б�Ϊ�գ����ƶ�Ϊ1
        }

        *//*for (int i=0;i<k;i++){
            System.out.println(list1.get(i));
        }

        for (int j=0;j<k;j++){
            System.out.println(list2.get(j));
        }*//*

        List<Integer> intersection = new ArrayList<>(list2);
        intersection.retainAll(list1); // ����



        int commonElements = intersection.size();
        int totalElementsInList2 = list1.size();




        // �������ƶȱ���
        double similarity = (double) commonElements / totalElementsInList2;
        //System.out.println("���ƶ�"+similarity);

        if (similarity != 1.0){
            for (int element : list1){
                System.out.print(element + " ");

            }
            System.out.println();
            for (int element : list2){
                System.out.print(element + " ");

            }
            System.out.println();
        }*/

        return similarity;

    }

    public static List<Map.Entry<Integer, Double>> extractNonZeroInts(List<Map.Entry<Integer, Double>> sortedSimilarityList,int k) {
        //List<Integer> result = new ArrayList<>();
        List<Map.Entry<Integer, Double>> list = new ArrayList<>();
        int num=0;
        for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
            num++;
            list.add(entry);
            /*int Id = entry.getKey();
            double similarity = entry.getValue();
            if (similarity != 0.0) {
                result.add(Id);
            }*/
            if (num>=k)
                break;
        }

        return list;
    }

    public static void calcuVectorCorrectness(Dataset dataset, SecureDataset secureDataset, int k,int queryNumber,List<Integer> queryIds){
        double similarity = 0.0;
        double temp;
        for (int i=1; i<=queryNumber;i++){
            int queryId = queryIds.get(i-1);
            //SimilarityCalculate.oneRankSearchWithDoubleTree(secureDataset,k,m,i);

            //SimilarityCalculate.oneSimilarityCalculateWithInnerTree(secureDataset,k,i);

            temp= SimilarityCalculate.vectorCorrectness(SimilarityCalculate.oneSimilarityCalculateOnPlaintext(dataset,k,queryId),SimilarityCalculate.oneSimilarityCalculateWithInnerTree(secureDataset,k,queryId),k);
            if (temp!=1)
                System.out.println("��" + i +"�����ƶ�:" + temp);

            similarity += temp;
        }

        System.out.println(similarity);

        similarity = similarity/queryNumber;


        System.out.println("���ݼ���"+secureDataset.getName());

        System.out.println("Top:" + k + " ��ѯ");

        System.out.println("��ѯ������" + queryNumber );

        System.out.println("��ȷ�ʣ�"+ similarity);
        System.out.println();

    }





}
