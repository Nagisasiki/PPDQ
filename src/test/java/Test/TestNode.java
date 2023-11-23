package Test;

import Class.EPDS.ExternalNode;
import Class.EPDS.InnerNode;
import Class.EPDS.SecureDataset;
import Class.EPDS.SecureDistribution;
import Methods.EPDS.InnerIndex;

public class TestNode {

    public static void main(String[] args) {

        /*double[] locationVector1 = {0,1,0,0,0,0,0,0};
        double[] densityVector1 = {0,0,1,0,0,0,0,0};
        double[] locationVector2 = {0,0,0,0,1,0,0,0};
        double[] densityVector2 = {0,0,0,1,0,0,0,0};

        InnerNode node1 = new InnerNode(locationVector1,densityVector1,3);
        InnerNode node2 = new InnerNode(locationVector2,densityVector2,3);
        InnerNode fnode = new InnerNode(node1,node2);
        node1.printNode();
        node2.printNode();
        fnode.printNode();*/

        String path1 = "E:\\lpy\\datasets\\simpletest\\Distribution_1.txt"; // 指定实际的路径
        SecureDistribution secureDistribution1 = new SecureDistribution(1,0.01f,3,20,path1);
        String path2 = "E:\\lpy\\datasets\\simpletest\\Distribution_2.txt"; // 指定实际的路径
        SecureDistribution secureDistribution2 = new SecureDistribution(2,0.01f,3,20,path2);

        //secureDistribution1.printMap();
        //secureDistribution2.printMap();

        secureDistribution1.setInnerIndex();

        //InnerIndex.traversalTree(secureDistribution1.getInnerIndex());


        //System.out.println(SecureDistribution.calculateSimilarity(secureDistribution1,secureDistribution2));

        //System.out.println(SecureDistribution.similarityWithInnerIndex(secureDistribution1,secureDistribution2));

        ExternalNode externalNode1 = new ExternalNode(secureDistribution1);
        ExternalNode externalNode2 = new ExternalNode(secureDistribution2);
        ExternalNode externalNode3 = new ExternalNode(externalNode1,externalNode2);
        externalNode1.printNode();
        externalNode2.printNode();
        externalNode3.printNode();

        String path3 = "E:\\lpy\\datasets\\simpletest"; // 指定实际的路径
        String name = "simpletest";

        SecureDataset secureDataset = new SecureDataset(name,path3,0.01f,20,3);
        secureDataset.constructExternalIndex();
        secureDataset.printExternalIndex();
    }
}
