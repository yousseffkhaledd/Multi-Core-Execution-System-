public class Process {
    private int id;
    private String[] instructions;
    private PCB pcb;

    public Process(int id, String[] instructions, int memoryStart, int memoryEnd) {
        this.id = id;
        this.instructions = instructions;
        this.pcb = new PCB(id, memoryStart, memoryEnd);
    }

    public int getId() {
        return id;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public PCB getPCB() {
        return pcb;
    }

    public void incrementProgramCounter() {
        pcb.incrementProgramCounter();
    }

    public int getProgramCounter() {
        return pcb.getProgramCounter();
    }

    public boolean hasMoreInstructions() {
        return pcb.getProgramCounter() < instructions.length;
    }
}