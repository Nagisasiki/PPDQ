package Methods.EPDS;

import Class.EPDS.ExternalNode;
import Class.EPDS.InnerNode;
import Class.EPDS.SecureDataset;
import Class.EPDS.SecureDistribution;

import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;

public class ExernalIndex {

    public static ExternalNode buildTree(SecureDataset secureDataset){
        ArrayList<ExternalNode> externalNodes = new ArrayList<>();
        for (SecureDistribution secureDistribution : secureDataset.getSecureDistributions()){
            ExternalNode externalNode = new ExternalNode(secureDistribution);
            externalNodes.add(externalNode);
        }
        while (externalNodes.size() > 1){

            ArrayList<ExternalNode> temp = new ArrayList<>();


            for (int i = 0; i < (externalNodes.size()-1) ; i += 2) {
                ExternalNode left = externalNodes.get(i);
                ExternalNode right = externalNodes.get(i+1);
                ExternalNode merged = new ExternalNode(left, right);
                temp.add(merged);
            }

            if (externalNodes.size() % 2 == 1){
                temp.add(externalNodes.get(externalNodes.size()-1));
            }

            externalNodes = temp;

        }

        if (externalNodes.size() == 0){
            return null;
        }else {
            return externalNodes.get(0);
        }

    }


    public static void getCandidateDatasets(ExternalNode externalNode, SecureDistribution secureDistribution , PriorityQueueWithCapacity priorityQueueWithCapacity,int num){
        if(externalNode!= null){
            if (priorityQueueWithCapacity.getPriorityQueue().size()<num){
                if (externalNode.getFlag()==0 ){
                    getCandidateDatasets(externalNode.getLeftChild(),secureDistribution,priorityQueueWithCapacity,num);
                    getCandidateDatasets(externalNode.getRightChild(),secureDistribution,priorityQueueWithCapacity,num);
                }else {
                    externalNode.setPriority(DensityVector.calculateInnerProduct(externalNode.getGlobeVector(),secureDistribution.getGlobeLocationVector()));
                    //System.out.println("sssssssssssssssssssssssssssssss");
                    priorityQueueWithCapacity.insert(externalNode);
                }
            }else {

                if (DensityVector.calculateInnerProduct(externalNode.getGlobeVector(),secureDistribution.getGlobeLocationVector())>= priorityQueueWithCapacity.getMinPriorityValue()){
                    if (externalNode.getFlag()==0){
                        getCandidateDatasets(externalNode.getLeftChild(),secureDistribution,priorityQueueWithCapacity,num);
                        getCandidateDatasets(externalNode.getRightChild(),secureDistribution,priorityQueueWithCapacity,num);
                    }else {
                        externalNode.setPriority(DensityVector.calculateInnerProduct(externalNode.getGlobeVector(),secureDistribution.getGlobeLocationVector()));
                        priorityQueueWithCapacity.insert(externalNode);
                    }
                }

            }


        }

    }
}
