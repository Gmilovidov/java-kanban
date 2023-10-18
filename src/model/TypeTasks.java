package model;

public enum TypeTasks {
    TASK,
    EPIC,
    SUBTASK;

    public static class Node  {

        public Node prev;
        public Task task;
        public Node next;


        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }
}
