package Methods.EPDS;

import Class.DQ.Dataset;
import Class.EPDS.ExternalNode;
import Class.EPDS.SecureDataset;
import Class.EPDS.SecureDistribution;
import Methods.DQ.SearchWithDistribution;

import java.util.*;

public class SimilarityCalculate {
    //无精度损失，明文方案
    public static void similarityCalculateOnPlaintext(Dataset dataset, int k, int querynumber){
        for (int i=1; i<=querynumber;i++){
            SearchWithDistribution.topkSearch(dataset,i,k);
        }
    }



    //向量化后搜索方案，无安全内积计算
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

            /*System.out.println("查询数据集Id：" + query.getId());
            //System.out.println("相似度：");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("数据集 ID: " + id +  "   " + "相似度: " + similar );
                }
                if (num>k)
                    break;
            }*/
        }

        System.out.println("基础方案平均搜索时间：" + time/querynumber);
        System.out.println("相似度计算时间：" + simTime/querynumber);
        System.out.println("其余时间" + (time/querynumber - simTime/querynumber));

    }

    public static SecureDistribution GenQuery(int id,float resolution, int numberOfHashFunctions, int dimensional, String filePath){
        String absolutePath = filePath + "\\Distribution_" + id + ".txt";
        SecureDistribution query = new SecureDistribution(id,resolution,numberOfHashFunctions,dimensional,absolutePath);
        return query;
    }

    //排名筛选，m为放大系数
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
            //System.out.println("队列长度：" + priorityQueue.size());
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

            /*System.out.println("查询数据集Id：" + query.getId());
            //System.out.println("相似度：");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("数据集 ID: " + id +  "   " + "相似度: " + similar );
                }
                if (num>k)
                    break;
            }*/
        }
        System.out.println("Rank搜搜平均搜索时间：" + time/querynumber);

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

            /*System.out.println("查询数据集Id：" + query.getId());
            //System.out.println("相似度：");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("数据集 ID: " + id +  "   " + "相似度: " + similar );
                }

                if (num>k)
                    break;
            }*/
        }

        System.out.println("内部树形结构平均搜索时间：" + time/querynumber);
        System.out.println("相似度计算时间：" + simTime/querynumber);
        System.out.println("其余时间" + (time/querynumber - simTime/querynumber));

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
            //优先权队列存储排名靠前的数据集

            PriorityQueueWithCapacity priorityQueueWithCapacity = new PriorityQueueWithCapacity<>(candidateNumber);

            stime=System.currentTimeMillis();
            ExernalIndex.getCandidateDatasets(secureDataset.getExternalIndex(),query,priorityQueueWithCapacity,candidateNumber);
            //priorityQueueWithCapacity.printQueue();
            //Map<SecureDistribution,Double> candidate = candidateTable.getCandidate();
            //System.out.println("队列长度" + priorityQueueWithCapacity.getPriorityQueue().size());
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

            /*System.out.println("查询数据集Id：" + query.getId());
            //System.out.println("相似度：");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("数据集 ID: " + id +  "   " + "相似度: " + similar );
                }
                if (num>k)
                    break;
            }*/
        }
        System.out.println("外部树形结构平均搜索时间：" + time/querynumber);
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
            //优先权队列存储排名靠前的数据集

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

            System.out.println("查询数据集Id：" + query.getId());
            //System.out.println("相似度：");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("数据集 ID: " + id +  "   " + "相似度: " + similar );
                }
                if (num>k)
                    break;
            }
        }
        System.out.println("两层索引结构平均搜索时间：" + time/querynumber);
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
            //优先权队列存储排名靠前的数据集

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

            /*System.out.println("查询数据集Id：" + query.getId());
            //System.out.println("相似度：");
            int num=0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != i){
                    System.out.println("数据集 ID: " + id +  "   " + "相似度: " + similar );
                }
                if (num>k)
                    break;
            }*/
        }
        System.out.println("两层索引结构平均搜索时间：" + time/querynumber);
        System.out.println("rank时间" + rankTime/querynumber);
        System.out.println("相似度计算时间" + simTime/querynumber);
        System.out.println("其余时间" + (time/querynumber - rankTime/querynumber - simTime/querynumber));
    }

    public static double correctness (List<Map.Entry<Integer, Double>> sortedSimilarityList1, List<Map.Entry<Integer, Double>> sortedSimilarityList2,int k){
        double correctness;
        List<Double> list1 = SimilarityCalculate.extractNonZeroDoubles(sortedSimilarityList1,k);
        List<Double> list2 = SimilarityCalculate.extractNonZeroDoubles(sortedSimilarityList2,k);

        if (list1.isEmpty() || list2.isEmpty()) {
            return 1.0; // 如果任一列表为空，相似度为0
        }

        /*for (int i=0;i<k;i++){
            System.out.println(list1.get(i));
        }

        for (int j=0;j<k;j++){
            System.out.println(list2.get(j));
        }*/

        List<Double> intersection = new ArrayList<>(list2);
        intersection.retainAll(list1); // 交集



        int commonElements = intersection.size();
        int totalElementsInList2 = list2.size();




        // 计算相似度比例
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
            //优先权队列存储排名靠前的数据集

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


            /*System.out.println("查询数据集Id：" + query.getId());
            System.out.println("相似度：");
            int num = 0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != queryId) {
                    System.out.println("数据集 ID: " + id + "   " + "相似度: " + similar);
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


            /*System.out.println("查询数据集Id：" + query.getId());
            //System.out.println("相似度：");
            int num = 0;
            for (Map.Entry<Integer, Double> entry : sortedSimilarityList) {
                num++;
                int id = entry.getKey();
                double similar = entry.getValue();
                if (id != queryId) {
                    System.out.println("数据集 ID: " + id + "   " + "相似度: " + similar);
                }

                if (num > k)
                    break;
            }*/
        }
        return sortedSimilarityList;

    }

    //k:topk搜索，m预排名系数，queryNumber重复次数
    public static void calculationCorrectness(SecureDataset secureDataset, int k, float m,int queryNumber,List<Integer> queryIds){
        double similarity = 0.0;
        double temp;
        for (int i=1; i<=queryNumber;i++){
            int queryId = queryIds.get(i-1);
            //SimilarityCalculate.oneRankSearchWithDoubleTree(secureDataset,k,m,i);

            //SimilarityCalculate.oneSimilarityCalculateWithInnerTree(secureDataset,k,i);

            temp= SimilarityCalculate.correctness(SimilarityCalculate.oneSimilarityCalculateWithInnerTree(secureDataset,k,queryId),SimilarityCalculate.oneRankSearchWithDoubleTree(secureDataset,k,m,queryId),k);
            //System.out.println("第" + i + "次相似度正确率" + temp);
            similarity += temp;
        }

        System.out.println(similarity);

        similarity = similarity/queryNumber;


        System.out.println("数据集："+secureDataset.getName());

        System.out.println("Top:" + k + " 查询");

        System.out.println("预排名系数："+ m/secureDataset.getLength());

        System.out.println("查询次数：" + queryNumber );

        System.out.println("正确率："+ similarity);
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
        System.out.println("计数值：" + count);
        double similarity = (double) count/k;
        /*if (similarity!=1.0){
            for (Map.Entry<Integer,Double> entry1 : sortedSimilarityList1){
                System.out.println("数据集：" + entry1.getKey());
                System.out.println("相似度：" + entry1.getValue());
            }
            System.out.println();
            for (Map.Entry<Integer,Double> entry2 : sortedSimilarityList2){
                System.out.println("数据集：" + entry2.getKey());
                System.out.println("相似度：" + entry2.getValue());
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
            return 1.0; // 如果任一列表为空，相似度为1
        }

        *//*for (int i=0;i<k;i++){
            System.out.println(list1.get(i));
        }

        for (int j=0;j<k;j++){
            System.out.println(list2.get(j));
        }*//*

        List<Integer> intersection = new ArrayList<>(list2);
        intersection.retainAll(list1); // 交集



        int commonElements = intersection.size();
        int totalElementsInList2 = list1.size();




        // 计算相似度比例
        double similarity = (double) commonElements / totalElementsInList2;
        //System.out.println("相似度"+similarity);

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
                System.out.println("第" + i +"次相似度:" + temp);

            similarity += temp;
        }

        System.out.println(similarity);

        similarity = similarity/queryNumber;


        System.out.println("数据集："+secureDataset.getName());

        System.out.println("Top:" + k + " 查询");

        System.out.println("查询次数：" + queryNumber );

        System.out.println("正确率："+ similarity);
        System.out.println();

    }





}
