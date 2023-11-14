package service;

import java.io.File;

public final class Managers {

   private Managers(){}

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8077/");
    }

    public static HistoryManager getDefaultHistory() {
       return new InMemoryHistoryManager();
    }
}
