import java.util.LinkedList;
import java.util.Queue;

public class ReadyQueue {
    private Queue<Process> queue;

    public ReadyQueue() {
        this.queue = new LinkedList<>();
    }

    public synchronized void addProcess(Process process) {
        queue.offer(process);
    }

    public synchronized Process getNextProcess() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}