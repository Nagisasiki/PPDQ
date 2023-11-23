package Methods.PPDQ;

import Jama.Matrix;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ��ȫ�ڻ�������
 *
 * @author Another
 * @date 2023/07/10
 */
public class SecureInnerProductCalculator {
    private Matrix[] keyMatrix;//��Ϊ��Կ����������������
    private int[] bitVector;//���ڷ������������λ����

    public SecureInnerProductCalculator(int dimensions) {
        //��������dimensions * dimensionsά������������
        keyMatrix = new Matrix[2];
        keyMatrix[0] = generateInvertibleMatrix(dimensions);
        keyMatrix[1] = generateInvertibleMatrix(dimensions);

        //����dimensionsά�����λ����
        bitVector = new int[dimensions];
        for (int i = 0; i < dimensions; i++) {
            bitVector[i] = ThreadLocalRandom.current().nextInt(2);//ÿһλ����0��1�����ֵ
        }
    }

    /**
     * ���ɿ������
     *
     * @param dimensions ά����
     * @return {@link Matrix} �������
     */
    private Matrix generateInvertibleMatrix(int dimensions) {
        Matrix matrix = null;
        boolean isMatrixInvertible = false;

        while (!isMatrixInvertible) {
            double[][] randomData = new double[dimensions][dimensions];
            for (int i = 0; i < dimensions; i++) {
                for (int j = 0; j < dimensions; j++) {
                    randomData[i][j] = ThreadLocalRandom.current().nextDouble(0.1, 0.9); //ÿһλ����һ��0.1��0.9֮������������
                }
            }
            matrix = new Matrix(randomData);
            isMatrixInvertible = isMatrixInvertible(matrix);
        }

        return matrix;
    }

    /**
     * �����Ƿ����
     *
     * @param matrix ����
     * @return boolean ��Ϊ������󣬷���true�����򷵻�false
     */
    private boolean isMatrixInvertible(Matrix matrix) {
        try {
            matrix.inverse();
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /*public double[] encryptVector(double[] values, boolean search) {//
        //double[] values = vector.getValues();//ȡ������

        //�������ά���Ƿ���ȷ
        if (values.length != keyMatrix[0].getColumnDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        //׼�����������Ĵ洢
        double[] value1 = new double[values.length];
        double[] value2 = new double[values.length];

        if (search) {
            //����λ�������Ѽ�������
            for (int i = 0; i < values.length; i++) {
                if (bitVector[i] == 0) {
                    value1[i] = values[i];
                    value2[i] = values[i];
                } else {
                    if (Double.compare(values[i], 0) != 0) {
                        value1[i] = ThreadLocalRandom.current().nextDouble(0, values[i]);
                    } else {
                        value1[i] = ThreadLocalRandom.current().nextDouble(0.1, 0.9);
                    }
                    value2[i] = values[i] - value1[i];
                }
            }
            value1 = encryptWithTranspose(value1, 0);
            value2 = encryptWithTranspose(value2, 1);
        } else {
            //����λ�������ѷǼ������������ѹ������úͼ��������Ĺ����෴
            for (int i = 0; i < values.length; i++) {
                if (bitVector[i] == 1) {
                    value1[i] = values[i];
                    value2[i] = values[i];
                } else {
                    if (Double.compare(values[i], 0) != 0) {
                        value1[i] = ThreadLocalRandom.current().nextDouble(0, values[i]);
                    } else {
                        value1[i] = ThreadLocalRandom.current().nextDouble(0.1, 0.9);
                    }
                    value2[i] = values[i] - value1[i];
                }
            }
            value1 = encryptWithInverse(value1, 0);
            value2 = encryptWithInverse(value2, 1);
        }

        //���������������ϲ�Ϊһ��������
        double[] encryptedValues = new double[value1.length + value2.length];
        for (int i = 0; i < value1.length; i++) {
            encryptedValues[i] = value1[i];
            encryptedValues[value2.length + i] = value2[i];
        }

        return encryptedValues;
        //vector.setValues(encryptedValues);
    }*/

    public double[] encryptVector(double[] values, boolean search) {
        // �������ά���Ƿ���ȷ
        int dimensions = keyMatrix[0].getColumnDimension();

        if (values.length != dimensions) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        // ׼�����������Ĵ洢
        double[] value1 = new double[dimensions];
        double[] value2 = new double[dimensions];

        if (search) {
            // ����λ�������Ѽ�������
            for (int i = 0; i < dimensions; i++) {
                if (bitVector[i] == 0) {
                    value1[i] = values[i];
                    value2[i] = values[i];
                } else {
                    if (Double.compare(values[i], 0) != 0) {
                        value1[i] = ThreadLocalRandom.current().nextDouble(0, values[i]);
                    } else {
                        value1[i] = ThreadLocalRandom.current().nextDouble(0.1, 0.9);
                    }
                    value2[i] = values[i] - value1[i];
                }
            }
            value1 = encryptWithTranspose(value1, 0);
            value2 = encryptWithTranspose(value2, 1);
        } else {
            // ����λ�������ѷǼ������������ѹ������úͼ��������Ĺ����෴
            for (int i = 0; i < dimensions; i++) {
                if (bitVector[i] == 1) {
                    value1[i] = values[i];
                    value2[i] = values[i];
                } else {
                    if (Double.compare(values[i], 0) != 0) {
                        value1[i] = ThreadLocalRandom.current().nextDouble(0, values[i]);
                    } else {
                        value1[i] = ThreadLocalRandom.current().nextDouble(0.1, 0.9);
                    }
                    value2[i] = values[i] - value1[i];
                }
            }
            value1 = encryptWithInverse(value1, 0);
            value2 = encryptWithInverse(value2, 1);
        }

        // ���������������ϲ�Ϊһ��������
        int encryptedLength = value1.length + value2.length;
        double[] encryptedValues = new double[encryptedLength];
        System.arraycopy(value1, 0, encryptedValues, 0, value1.length);
        System.arraycopy(value2, 0, encryptedValues, value1.length, value2.length);

        return encryptedValues;
    }


    /**
     * ʹ��ת�þ������
     *
     * @param vector        ��������
     * @param idOfKeyMatrix ��Կ����ı��
     * @return {@link double[]} ��������
     */
    /*public double[] encryptWithTranspose(double[] vector, int idOfKeyMatrix) {
        //�������ά���Ƿ���ȷ
        if (vector.length != keyMatrix[idOfKeyMatrix].getColumnDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        //ʹ��ת�þ����������
        Matrix vectorMatrix = new Matrix(vector, vector.length);//������תΪ�о���
        Matrix transposeMatrix = keyMatrix[idOfKeyMatrix].transpose();//��ȡת�þ���
        Matrix resultMatrix = transposeMatrix.times(vectorMatrix);

        //ȡ��������Ϊ������������
        return resultMatrix.getColumnPackedCopy();
    }*/

    public double[] encryptWithTranspose(double[] vector, int idOfKeyMatrix) {
        // �������ά���Ƿ���ȷ
        if (vector.length != keyMatrix[idOfKeyMatrix].getColumnDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        // ʹ��ת�þ����������
        int vectorLength = vector.length;
        double[] result = new double[vectorLength];

        double[][] transposeMatrix = keyMatrix[idOfKeyMatrix].getArrayCopy();
        for (int i = 0; i < vectorLength; i++) {
            double sum = 0.0;
            for (int j = 0; j < vectorLength; j++) {
                sum += transposeMatrix[j][i] * vector[j];
            }
            result[i] = sum;
        }

        // ���ؼ�������
        return result;
    }


    /**
     * ʹ����������
     *
     * @param vector        ��������
     * @param idOfKeyMatrix ��Կ����ı��
     * @return {@link double[]} ��������
     */
    /*public double[] encryptWithInverse(double[] vector, int idOfKeyMatrix) {
        //�������ά���Ƿ���ȷ
        if (vector.length != keyMatrix[idOfKeyMatrix].getRowDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        //ʹ��������������
        Matrix vectorMatrix = new Matrix(vector, vector.length);//������תΪ�о���
        Matrix inverseMatrix = keyMatrix[idOfKeyMatrix].inverse();//��ȡ�����
        Matrix resultMatrix = inverseMatrix.times(vectorMatrix);

        //ȡ��������Ϊ������������
        return resultMatrix.getColumnPackedCopy();
    }*/

    public double[] encryptWithInverse(double[] vector, int idOfKeyMatrix) {
        // �������ά���Ƿ���ȷ
        if (vector.length != keyMatrix[idOfKeyMatrix].getRowDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        // ʹ��������������
        int vectorLength = vector.length;
        double[] result = new double[vectorLength];

        double[][] inverseMatrix = keyMatrix[idOfKeyMatrix].inverse().getArrayCopy();
        for (int i = 0; i < vectorLength; i++) {
            double sum = 0.0;
            for (int j = 0; j < vectorLength; j++) {
                sum += inverseMatrix[i][j] * vector[j];
            }
            result[i] = sum;
        }

        // ���ؼ�������
        return result;
    }
}
