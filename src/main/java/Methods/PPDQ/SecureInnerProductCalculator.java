package Methods.PPDQ;

import Jama.Matrix;

public class SecureInnerProductCalculator {
    private Matrix keyMatrix;

    public SecureInnerProductCalculator(int dimensions) {
        // 生成一个随机的dimensions x dimensions可逆矩阵作为密钥
        keyMatrix = generateInvertibleMatrix(dimensions);
    }

    private Matrix generateInvertibleMatrix(int dimensions) {
        Matrix matrix = null;
        boolean isMatrixInvertible = false;

        while (!isMatrixInvertible) {
            double[][] randomData = new double[dimensions][dimensions];
            for (int i = 0; i < dimensions; i++) {
                for (int j = 0; j < dimensions; j++) {
                    randomData[i][j] = Math.random(); // 随机生成 0-1 之间的数
                }
            }
            matrix = new Matrix(randomData);
            isMatrixInvertible = isMatrixInvertible(matrix);
        }

        return matrix;
    }

    private boolean isMatrixInvertible(Matrix matrix) {
        try {
            matrix.inverse();
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public double[] multiplyWithKeyMatrix(double[] vector) {
        if (vector.length != keyMatrix.getColumnDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        Matrix vectorMatrix = new Matrix(vector, 1);
        Matrix resultMatrix = vectorMatrix.times(keyMatrix);

        return resultMatrix.getRowPackedCopy();
    }

    public double[] multiplyInverseKeyMatrixWithVector(double[] vector) {
        if (vector.length != keyMatrix.getRowDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        Matrix vectorMatrix = new Matrix(vector, vector.length);
        Matrix inverseMatrix = keyMatrix.inverse();
        Matrix resultMatrix = inverseMatrix.times(vectorMatrix);

        return resultMatrix.getColumnPackedCopy();
    }


    public void printKeyMatrix() {
        int numRows = keyMatrix.getRowDimension();
        int numCols = keyMatrix.getColumnDimension();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                System.out.print(keyMatrix.get(i, j) + " ");
            }
            System.out.println();
        }
    }

}
