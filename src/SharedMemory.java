import java.util.Map;

public class SharedMemory {
    private Map<String, Double> memory;

    public SharedMemory(Map<String, Double> memory) {
        this.memory = memory;
    }

    // Synchronized getter
    public synchronized Map<String, Double> getMemory() {
        return memory;
    }

    // Synchronized update method
    public synchronized void updateMemory(String key, Double value) {
        memory.put(key, value);
        System.out.println("Updated Shared Memory: " + memory);
    }

    // Print memory state (synchronized to avoid inconsistency during print)
    public synchronized void printMemoryState() {
        System.out.println("Final Shared Memory State: " + memory);
    }
}