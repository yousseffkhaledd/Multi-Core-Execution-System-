import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MasterCore {
    private ReadyQueue readyQueue;
    private SharedMemory sharedMemory;
    private int timeSlice;

    public MasterCore(int timeSlice) {
        this.readyQueue = new ReadyQueue();
        Map<String, Double> initialSharedMemory = new HashMap<>();
        this.sharedMemory = new SharedMemory(initialSharedMemory);
        this.timeSlice = timeSlice;
    }

    private String[] readInstructionsFromFile(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath)).toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{};
        }
    }

    public void loadProcesses() {
        String[] instructions1 = readInstructionsFromFile("input1.txt");
        String[] instructions2 = readInstructionsFromFile("input2.txt");

        Process process1 = new Process(1, instructions1, 0, 100);
        Process process2 = new Process(2, instructions2, 101, 200);

        readyQueue.addProcess(process1);
        readyQueue.addProcess(process2);
    }

    public void startExecution() {
        ArrayList<SlaveCore> cores = new ArrayList<>();
        cores.add(new SlaveCore(1, sharedMemory, readyQueue, timeSlice));
        cores.add(new SlaveCore(2, sharedMemory, readyQueue, timeSlice));

        for (SlaveCore core : cores) {
            core.start();
        }

        for (SlaveCore core : cores) {
            try {
                core.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        sharedMemory.printMemoryState();
    }
}