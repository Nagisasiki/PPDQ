package Methods.EPDS;


import Class.EPDS.InnerNode;
import Class.EPDS.SecureDistribution;
import Class.PPDQ.SecureGenQuery;
import Class.PPDQ.SecureIndexNode;
import Methods.PPDQ.DensityVector;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Map;

public class InnerIndex {

    /*public static ArrayList<InnerNode> GenInnerNodes(Map<LocationVector, DensityVector> distributions){
        ArrayList<InnerNode> InnerNodes = new ArrayList<>();
        for (Map.Entry<LocationVector, DensityVector> entry : distributions.entrySet()){
            InnerNode innerNode = new InnerNode(entry.getKey().getLocationVector(), entry.getValue().getDensityVector());
            InnerNodes.add(innerNode);
        }
        return InnerNodes;
    }*/


    public static InnerNode buildTree(Map<LocationVector, DensityVector> map,int numberOfHashFunctions){
        ArrayList<InnerNode> innerNodes = new ArrayList<>();
        for (Map.Entry<LocationVector, DensityVector> entry : map.entrySet()){
            InnerNode innerNode = new InnerNode(entry.getKey().getLocationVector(), entry.getValue().getDensityVector(),numberOfHashFunctions);
            innerNodes.add(innerNode);
        }
        while (innerNodes.size() > 1){

            ArrayList<InnerNode> temp = new ArrayList<>();


            for (int i = 0; i < (innerNodes.size()-1) ; i += 2) {
                InnerNode left = innerNodes.get(i);
                InnerNode right = innerNodes.get(i+1);
                InnerNode merged = new InnerNode(left, right);
                temp.add(merged);
            }

            if (innerNodes.size() % 2 == 1){
                temp.add(innerNodes.get(innerNodes.size()-1));
            }

            innerNodes = temp;

        }

        if (innerNodes.size() == 0){
            return null;
        }else {
            return innerNodes.get(0);
        }

    }


    public static void traversalTree(InnerNode root){
        if (root != null){
            if (root.getFlag() == 1){
                root.printNode();
            }else {
                root.printNode();
            }
            traversalTree(root.getLeftChild());
            traversalTree(root.getRightChild());
        }
    }


    /*public static double[] findDensityVector(InnerNode root,double[] locationVector, int numberOfHashFunctions,float tolerance){

        if (root != null){
            if (root.getFlag() == 1 || (LocationVector.calculateInnerProduct(root.getLocationVector(),locationVector) - numberOfHashFunctions)<tolerance){

                findDensityVector(root.getLeftChild(),locationVector,numberOfHashFunctions,tolerance);
                findDensityVector(root.getRightChild(),locationVector,numberOfHashFunctions,tolerance);
            }else if (root.getFlag() == 0 || (LocationVector.calculateInnerProduct(root.getLocationVector(),locationVector) - numberOfHashFunctions)<tolerance){

                return root.getDensityVector();
            }else {

                return null;
            }
        }
        return null;
    }*/

    public static double[] findDensityVector(InnerNode root, double[] locationVector, int numberOfHashFunctions, float tolerance) {
        if (root != null) {
            //System.out.println("sssssssssssssssssssssssssssss");

            double innerProduct = LocationVector.calculateInnerProduct(root.getLocationVector(), locationVector);


            if (root.getFlag() == 0 && Math.abs(innerProduct - numberOfHashFunctions) < tolerance) {
                // 如果是叶子节点并且满足条件，返回密度向量
                return root.getDensityVector();
            } else if (root.getFlag() == 1 && Math.abs(innerProduct - numberOfHashFunctions) < tolerance){

                // 如果是内部节点，遍历左右子树
                double[] leftResult = findDensityVector(root.getLeftChild(), locationVector, numberOfHashFunctions, tolerance);

                double[] rightResult = findDensityVector(root.getRightChild(), locationVector, numberOfHashFunctions, tolerance);


                // 返回非空的结果
                if (leftResult != null) {
                    return leftResult;
                } else {
                    return rightResult;
                }
            }
        }

        return null;
    }



}
