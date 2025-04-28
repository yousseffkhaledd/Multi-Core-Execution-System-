public class PCB {
    private int processId;
    private int programCounter;
    private int memoryStart;
    private int memoryEnd;

    public PCB(int processId, int memoryStart, int memoryEnd) {
        this.processId = processId;
        this.programCounter = 0; // Initially set to the first instruction
        this.memoryStart = memoryStart;
        this.memoryEnd = memoryEnd;
    }

    public int getProcessId() {
        return processId;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void incrementProgramCounter() {
        programCounter++;
    }

    public int getMemoryStart() {
        return memoryStart;
    }

    public int getMemoryEnd() {
        return memoryEnd;
    }
}