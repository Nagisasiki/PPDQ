package Class.EPDS;

import Methods.EPDS.DensityVector;
import Methods.EPDS.LocationVector;

import java.util.Map;

public class InnerNode {

    private InnerNode leftChild;
    private InnerNode rightChild;

    private double[] locationVector;
    private double[] densityVector;

    private int flag;

    private int numberOfHashFunctions;

    public InnerNode(LocationVector locationVector, DensityVector densityVector){
        this.leftChild=null;
        this.rightChild=null;
        this.flag = 0;
        this.locationVector = locationVector.getLocationVector();
        this.densityVector = densityVector.getDensityVector();
    }

    public InnerNode(double[] locationVector, double[] densityVector, int numberOfHashFunctions){
        this.leftChild=null;
        this.rightChild=null;
        this.flag = 0;
        this.locationVector = locationVector;
        this.densityVector = densityVector;
        this.numberOfHashFunctions = numberOfHashFunctions;
    }


    public InnerNode(InnerNode left, InnerNode right){
        this.flag = 1;
        this.leftChild = left;
        this.rightChild = right;

        this.locationVector = LocationVector.innerNodeORFunction(left.locationVector,right.locationVector);
        this.densityVector = null;
        this.numberOfHashFunctions = left.numberOfHashFunctions;
    }

    public void printNode(){
        if (this.flag==0){
            System.out.println("位置向量：");
            for (int i=0; i<this.locationVector.length;i++){
                System.out.print(this.locationVector[i] + ", ");
            }
            System.out.println();
            System.out.println("密度向量：");
            for (int i=0; i<this.densityVector.length;i++){
                System.out.print(this.densityVector[i] + ", ");
            }
            System.out.println();
        }else {
            System.out.println("位置向量：");
            for (int i=0; i<this.locationVector.length;i++){
                System.out.print(this.locationVector[i] + ", ");
            }
            System.out.println();
        }

    }

    public InnerNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(InnerNode leftChild) {
        this.leftChild = leftChild;
    }

    public InnerNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(InnerNode rightChild) {
        this.rightChild = rightChild;
    }

    public double[] getLocationVector() {
        return locationVector;
    }

    public void setLocationVector(double[] locationVector) {
        this.locationVector = locationVector;
    }

    public double[] getDensityVector() {
        return densityVector;
    }

    public void setDensityVector(double[] densityVector) {
        this.densityVector = densityVector;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getNumberOfHashFunctions() {
        return numberOfHashFunctions;
    }

    public void setNumberOfHashFunctions(int numberOfHashFunctions) {
        this.numberOfHashFunctions = numberOfHashFunctions;
    }
}
