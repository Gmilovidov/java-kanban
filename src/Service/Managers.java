package Service;

public final class Managers {

   private Managers(){}

    public static TaskManager getDefault() {
        return new inMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
       return new InMemoryHistoryManager();
    }
}
