public class Managers {
    private static HistoryManager defaultHistory = new InMemoryHistoryManager();
    private static TaskManager defaultTaskManager = new InMemoryTaskManager();

    public static TaskManager getDefault(){
        return defaultTaskManager;
    }

    public static HistoryManager getDefaultHistory(){
        return defaultHistory;
    }
}
