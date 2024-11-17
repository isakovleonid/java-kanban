import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class InMemoryHistoryManager implements HistoryManager{
    private static final int MAX_HISTORY_CNT = 10;
    Queue<Task> viewHistory;

    public InMemoryHistoryManager() {
        this.viewHistory = new LinkedList<>();;
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(viewHistory);
    }

    @Override
    public void add(Task task) {
        if (viewHistory.size() >= MAX_HISTORY_CNT) {
            viewHistory.remove();
        }

        viewHistory.add(task);
    }
}
