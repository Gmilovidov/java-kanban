package service;

import model.Task;
import model.TaskType;


import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    protected Map<Integer, TaskType.Node> dataNode = new HashMap<>();
    protected CustomLinkedList customLinkedMap = new CustomLinkedList();
    protected TaskType.Node head;
    protected TaskType.Node tail;

    public void removeNode( TaskType.Node node) {
            if (node.prev != null) {
                node.prev.next = node.next;
                if (node.next == null) {
                    tail = node.prev;
                } else {
                    node.next.prev = node.prev;
                }
            } else {
                head = node.next;
                if (head == null) {
                    tail = null;
                } else {
                    head.prev = null;
                }
            }
    }

    @Override
    public void add(Task task) {
        if (this.dataNode != null) {
            int id = task.getId();
            remove(id);
            customLinkedMap.linkLast(task);
            dataNode.put(id, tail);
        }
    }

    @Override
    public void remove(int id) {
        if (dataNode.containsKey(id)) {
            TaskType.Node node = dataNode.remove(id);
            if (node == null) {
                return;
            }
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        TaskType.Node node = head;
        while (node != null) {
            tasks.add(0, node.task);
            node = node.next;
        }
        return tasks;
    }

    public class CustomLinkedList {

        public void linkLast(Task task) {
            TaskType.Node node = new TaskType.Node(head, task, null);
            if (head == null) {
                head = node;
            } else {
                tail.next = node;
            }
            node.prev = tail;
            tail = node;
        }
    }

}
