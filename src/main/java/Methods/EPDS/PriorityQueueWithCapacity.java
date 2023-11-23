package Methods.EPDS;

import Class.EPDS.ExternalNode;
import Class.EPDS.SecureDistribution;

import java.util.PriorityQueue;


public class PriorityQueueWithCapacity<E extends ExternalNode> {
    private final int capacity;
    private final PriorityQueue<E> priorityQueue;

    public PriorityQueueWithCapacity(int capacity) {
        this.capacity = capacity;
        this.priorityQueue = new PriorityQueue<>(capacity, (a, b) -> Double.compare(a.getPriority(), b.getPriority()));
    }

    public PriorityQueue<E> getPriorityQueue() {
        return priorityQueue;
    }

    public void insert(E element) {
        if (priorityQueue.size() < capacity) {
            priorityQueue.offer(element);
        } else {
            // ���������������ɾ�����ȼ���͵�Ԫ��
            E lowestPriority = priorityQueue.poll();
            //System.out.println("�Ƴ����ȼ���͵�Ԫ��: " + lowestPriority);
            priorityQueue.offer(element);
        }
    }

    public double getMinPriorityValue() {
        if (priorityQueue.isEmpty()) {
            throw new IllegalStateException("����Ϊ�գ��޷���ȡ��С���ȼ�ֵ");
        }
        return priorityQueue.peek().getPriority();
    }

    public void printQueue() {
        System.out.println("�����е�Ԫ�أ�");
        while (!priorityQueue.isEmpty()) {
            E element = priorityQueue.poll();
            System.out.println("���ݼ�ID��" + element.getFlag());
            System.out.println("���ȼ���" + element.getPriority());
        }
    }


}
