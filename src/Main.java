public class Main {
    public static void main(String[] args) {
        int timeSlice = 2;
        MasterCore masterCore = new MasterCore(timeSlice);
        masterCore.loadProcesses();
        masterCore.startExecution();
    }
}