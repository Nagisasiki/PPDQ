package Class.EPDS;

import Methods.EPDS.LocationVector;

public class ExternalNode {
    private ExternalNode leftChild;
    private ExternalNode rightChild;


    private int flag;

    private double[] globeVector;

    double priority;

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public ExternalNode(SecureDistribution secureDistribution){
        this.flag = secureDistribution.getId();
        this.rightChild = null;
        this.leftChild = null;
        this.globeVector = secureDistribution.getGlobeLocationVector();
    }

    public ExternalNode(ExternalNode left, ExternalNode right){
        this.flag = 0;
        this.rightChild = right;
        this.leftChild = left;
        this.globeVector = LocationVector.innerNodeORFunction(left.globeVector,right.globeVector);
    }

    public void printNode(){
        System.out.println("ID: " + this.flag);
        System.out.println("Œª÷√œÚ¡ø£∫");
        for (int i=0; i<this.globeVector.length;i++){
            System.out.print(this.globeVector[i] + ", ");
        }
        System.out.println();

    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public ExternalNode getLeftChild() {
        return leftChild;
    }

    public ExternalNode getRightChild() {
        return rightChild;
    }

    public double[] getGlobeVector() {
        return globeVector;
    }

    public void setGlobeVector(double[] globeVector) {
        this.globeVector = globeVector;
    }
}
