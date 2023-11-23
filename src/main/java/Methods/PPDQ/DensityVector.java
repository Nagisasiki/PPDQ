package Methods.PPDQ;

public class DensityVector {

    private double[] densityVector;

    public void setDensityVector(double[] densityVector) {
        this.densityVector = densityVector;
    }

    public DensityVector(float density, float resolution){
        this.densityVector = DensityVector.genVector(density, resolution);
    }

    public double[] getDensityVector() {
        return densityVector;
    }

    public static double[] genVector(float density, float resolution){
        int number = (int) (density/resolution);
        int length = (int)(1/resolution);
        double[] densityVector = new double[length*2];

        for (int i=0; i<number; i++){
            densityVector[2*i] = 1.0;
        }

        return densityVector;
    }

    public static void printVector(double[] densityVector){
        for (int i=0; i<densityVector.length; i++){
            System.out.print(densityVector[i] + ", ");;
        }
        System.out.println();
    }

    public static double calculateInnerProduct(double[] vector1, double[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vector dimensions do not match.");
        }

        double result = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            result += vector1[i] * vector2[i];
        }

        return result;
    }

    public static double calculateInnerProduct(DensityVector vector1, DensityVector vector2) {
        double[] v1 = vector1.getDensityVector();
        double[] v2 = vector2.getDensityVector();
        if (v1.length != v2.length) {
            throw new IllegalArgumentException("Vector dimensions do not match.");
        }

        double result = 0.0;
        for (int i = 0; i < v1.length; i++) {
            result += v1[i] * v2[i];
        }

        return result;
    }


}
