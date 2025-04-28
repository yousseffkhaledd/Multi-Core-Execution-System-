import java.util.*;
public class SlaveCore extends Thread {
    private int coreId;
    private SharedMemory sharedMemory;
    private ReadyQueue readyQueue;
    private int timeQuantum; // Quantum for round-robin scheduling

    public SlaveCore(int coreId, SharedMemory sharedMemory, ReadyQueue readyQueue, int timeQuantum) {
        this.coreId = coreId;
        this.sharedMemory = sharedMemory;
        this.readyQueue = readyQueue;
        this.timeQuantum = timeQuantum;
    }

    @Override
    public void run() {
        while (!readyQueue.isEmpty()) {
            Process process = readyQueue.getNextProcess();
            if (process == null) continue;

            System.out.println("Core " + coreId + " is executing Process " + process.getId());

            int executedInstructions = 0;

            while (process.hasMoreInstructions() && executedInstructions < timeQuantum) {
                String instruction = process.getInstructions()[process.getProgramCounter()];
                System.out.println("Core " + coreId + " executing: " + instruction);
                executeInstruction(instruction);
                process.incrementProgramCounter();
                executedInstructions++;
            }

            // Requeue the process if it is not finished
            if (process.hasMoreInstructions()) {
                System.out.println("Core " + coreId + " is requeuing Process " + process.getId());
                readyQueue.addProcess(process);
            } else {
                System.out.println("Process " + process.getId() + " completed on Core " + coreId);
            }
        }
    }

    private void executeInstruction(String instruction) {
        try {
            String[] parts;
            if (instruction.contains("print")) {
                String variable = instruction.split(" ")[1];
                Double value;
                synchronized (sharedMemory) {
                    value = sharedMemory.getMemory().get(variable);
                }
                System.out.println("Core " + coreId + " [Print]: " + variable + " = " + (value != null ? value : "undefined"));
            } else if (instruction.contains("=")) {
                parts = instruction.split("=");
                String variable = parts[0].trim();
                String expression = parts[1].trim();

                double result = evaluateExpression(expression, sharedMemory.getMemory());
                sharedMemory.updateMemory(variable, result);
            }
        } catch (Exception e) {
            System.err.println("Error executing instruction \"" + instruction + "\": " + e.getMessage());
        }
    }

    private double evaluateExpression(String expression, Map<String, Double> memory) {
        String[] operands;
        synchronized (memory) {
            if (expression.contains("+")) {
                operands = expression.split("\\+");
                return getValue(operands[0], memory) + getValue(operands[1], memory);
            } else if (expression.contains("-")) {
                operands = expression.split("-");
                return getValue(operands[0], memory) - getValue(operands[1], memory);
            } else if (expression.contains("*")) {
                operands = expression.split("\\*");
                return getValue(operands[0], memory) * getValue(operands[1], memory);
            } else if (expression.contains("/")) {
                operands = expression.split("/");
                double divisor = getValue(operands[1], memory);
                if (divisor == 0) throw new ArithmeticException("Division by zero");
                return getValue(operands[0], memory) / divisor;
            } else if (expression.matches("\\d+")) {
                return Double.parseDouble(expression);
            }
        }
        throw new IllegalArgumentException("Invalid expression: " + expression);
    }

    private double getValue(String operand, Map<String, Double> memory) {
        if (operand.matches("\\d+")) {
            return Double.parseDouble(operand);
        }
        return memory.getOrDefault(operand.trim(), 0.0);
    }
}