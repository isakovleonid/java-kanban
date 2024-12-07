package ru.isakovleonid.practicum.taskmanager.historymanager;

import ru.isakovleonid.practicum.taskmanager.taskmanager.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    class LinkedTaskList {
        private Node<Task> first;
        private Node<Task> last;

        public Node<Task> linkLast(Task newLastTask) {
            Node<Task> newNode = new Node<>(null, newLastTask, null);

            Node<Task> prevLast = this.last;
            this.last = newNode;
            if (prevLast == null) {
                this.first = newNode;
            } else {
                newNode.prev = prevLast;
                prevLast.next = newNode;
            }

            return newNode;
        }

        public List<Task> getLinkedTasks() {
            Node<Task> currNode = this.first;
            List<Task> nodeList = new ArrayList<>();

            while (currNode != null) {
                nodeList.add(currNode.element);
                currNode = currNode.next;
            }

            return nodeList;
        }
    }

    LinkedTaskList linkedTaskList;
    Map<Integer, Node<Task>> viewHistory;

    public InMemoryHistoryManager() {
        this.linkedTaskList = new LinkedTaskList();
        this.viewHistory = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {

        return linkedTaskList.getLinkedTasks();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            int taskId = task.getId();
            remove(taskId);
            Node<Task> newNode = linkedTaskList.linkLast(task);
            viewHistory.put(taskId, newNode);
        }
    }

    @Override
    public void remove(int id) {
        if (viewHistory.containsKey(id)) {
            Node<Task> nodeForRemoving = viewHistory.get(id);

            removeNode(nodeForRemoving);
            viewHistory.remove(nodeForRemoving);
        }
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            Node<Task> prev = node.prev;
            Node<Task> next = node.next;

            if (prev != null)
                prev.next = next;
            else
                linkedTaskList.first = next;

            if (next != null)
                next.prev = prev;
            else
                linkedTaskList.last = prev;
        }
    }
}
