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
            // 如果队列已满，则删除优先级最低的元素
            E lowestPriority = priorityQueue.poll();
            //System.out.println("移除优先级最低的元素: " + lowestPriority);
            priorityQueue.offer(element);
        }
    }

    public double getMinPriorityValue() {
        if (priorityQueue.isEmpty()) {
            throw new IllegalStateException("队列为空，无法获取最小优先级值");
        }
        return priorityQueue.peek().getPriority();
    }

    public void printQueue() {
        System.out.println("队列中的元素：");
        while (!priorityQueue.isEmpty()) {
            E element = priorityQueue.poll();
            System.out.println("数据集ID：" + element.getFlag());
            System.out.println("优先级：" + element.getPriority());
        }
    }


}
