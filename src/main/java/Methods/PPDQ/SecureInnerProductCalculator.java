package Methods.PPDQ;

import Jama.Matrix;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 安全内积计算器
 *
 * @author Another
 * @date 2023/07/10
 */
public class SecureInnerProductCalculator {
    private Matrix[] keyMatrix;//作为密钥的两个随机可逆矩阵
    private int[] bitVector;//用于分裂向量的随机位向量

    public SecureInnerProductCalculator(int dimensions) {
        //生成两个dimensions * dimensions维的随机可逆矩阵
        keyMatrix = new Matrix[2];
        keyMatrix[0] = generateInvertibleMatrix(dimensions);
        keyMatrix[1] = generateInvertibleMatrix(dimensions);

        //生成dimensions维的随机位向量
        bitVector = new int[dimensions];
        for (int i = 0; i < dimensions; i++) {
            bitVector[i] = ThreadLocalRandom.current().nextInt(2);//每一位生成0或1的随机值
        }
    }

    /**
     * 生成可逆矩阵
     *
     * @param dimensions 维度数
     * @return {@link Matrix} 可逆矩阵
     */
    private Matrix generateInvertibleMatrix(int dimensions) {
        Matrix matrix = null;
        boolean isMatrixInvertible = false;

        while (!isMatrixInvertible) {
            double[][] randomData = new double[dimensions][dimensions];
            for (int i = 0; i < dimensions; i++) {
                for (int j = 0; j < dimensions; j++) {
                    randomData[i][j] = ThreadLocalRandom.current().nextDouble(0.1, 0.9); //每一位生成一个0.1到0.9之间的随机浮点数
                }
            }
            matrix = new Matrix(randomData);
            isMatrixInvertible = isMatrixInvertible(matrix);
        }

        return matrix;
    }

    /**
     * 矩阵是否可逆
     *
     * @param matrix 矩阵
     * @return boolean 若为可逆矩阵，返回true，否则返回false
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
        //double[] values = vector.getValues();//取出向量

        //检查向量维度是否正确
        if (values.length != keyMatrix[0].getColumnDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        //准备分裂向量的存储
        double[] value1 = new double[values.length];
        double[] value2 = new double[values.length];

        if (search) {
            //根据位向量分裂检索向量
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
            //根据位向量分裂非检索向量，分裂规则正好和检索向量的规则相反
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

        //将两个加密向量合并为一个新向量
        double[] encryptedValues = new double[value1.length + value2.length];
        for (int i = 0; i < value1.length; i++) {
            encryptedValues[i] = value1[i];
            encryptedValues[value2.length + i] = value2[i];
        }

        return encryptedValues;
        //vector.setValues(encryptedValues);
    }*/

    public double[] encryptVector(double[] values, boolean search) {
        // 检查向量维度是否正确
        int dimensions = keyMatrix[0].getColumnDimension();

        if (values.length != dimensions) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        // 准备分裂向量的存储
        double[] value1 = new double[dimensions];
        double[] value2 = new double[dimensions];

        if (search) {
            // 根据位向量分裂检索向量
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
            // 根据位向量分裂非检索向量，分裂规则正好和检索向量的规则相反
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

        // 将两个加密向量合并为一个新向量
        int encryptedLength = value1.length + value2.length;
        double[] encryptedValues = new double[encryptedLength];
        System.arraycopy(value1, 0, encryptedValues, 0, value1.length);
        System.arraycopy(value2, 0, encryptedValues, value1.length, value2.length);

        return encryptedValues;
    }


    /**
     * 使用转置矩阵加密
     *
     * @param vector        明文向量
     * @param idOfKeyMatrix 密钥矩阵的编号
     * @return {@link double[]} 密文向量
     */
    /*public double[] encryptWithTranspose(double[] vector, int idOfKeyMatrix) {
        //检查向量维度是否正确
        if (vector.length != keyMatrix[idOfKeyMatrix].getColumnDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        //使用转置矩阵乘以向量
        Matrix vectorMatrix = new Matrix(vector, vector.length);//将向量转为列矩阵
        Matrix transposeMatrix = keyMatrix[idOfKeyMatrix].transpose();//获取转置矩阵
        Matrix resultMatrix = transposeMatrix.times(vectorMatrix);

        //取列向量作为加密向量返回
        return resultMatrix.getColumnPackedCopy();
    }*/

    public double[] encryptWithTranspose(double[] vector, int idOfKeyMatrix) {
        // 检查向量维度是否正确
        if (vector.length != keyMatrix[idOfKeyMatrix].getColumnDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        // 使用转置矩阵乘以向量
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

        // 返回加密向量
        return result;
    }


    /**
     * 使用逆矩阵加密
     *
     * @param vector        明文向量
     * @param idOfKeyMatrix 密钥矩阵的编号
     * @return {@link double[]} 密文向量
     */
    /*public double[] encryptWithInverse(double[] vector, int idOfKeyMatrix) {
        //检查向量维度是否正确
        if (vector.length != keyMatrix[idOfKeyMatrix].getRowDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        //使用逆矩阵乘以向量
        Matrix vectorMatrix = new Matrix(vector, vector.length);//将向量转为列矩阵
        Matrix inverseMatrix = keyMatrix[idOfKeyMatrix].inverse();//获取逆矩阵
        Matrix resultMatrix = inverseMatrix.times(vectorMatrix);

        //取列向量作为加密向量返回
        return resultMatrix.getColumnPackedCopy();
    }*/

    public double[] encryptWithInverse(double[] vector, int idOfKeyMatrix) {
        // 检查向量维度是否正确
        if (vector.length != keyMatrix[idOfKeyMatrix].getRowDimension()) {
            throw new IllegalArgumentException("Vector dimension does not match keyMatrix dimension.");
        }

        // 使用逆矩阵乘以向量
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

        // 返回加密向量
        return result;
    }
}
