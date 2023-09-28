package Service;

import Model.Task;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {


    protected Map<Integer, Node> dataNode = new HashMap<>();
    protected CustomLinkedList customLinkedMap = new CustomLinkedList();
    protected Node head;
    protected Node tail;

    public void removeNode(Node node) {
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
    public void add(@NotNull Task task) {
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
            Node node = dataNode.remove(id);
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
        Node node = head;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }


    public class CustomLinkedList {
        public void linkLast(Task task) {
            Node node = new Node(head, task, null);
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
