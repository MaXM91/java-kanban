package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    protected Node<Task> head;
    protected Node<Task> tail;
    private final HashMap<Integer, Node<Task>> linkedHashMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (linkedHashMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
        linkedHashMap.put(task.getId(), tail);
    }

    private void linkLast(Task task) {
        final Node<Task> newNode = new Node<>(tail, task, null);
        tail = newNode;

        if (head == null) {
            head = newNode;
        } else {
            tail.prev.next = newNode;
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> history = new ArrayList<>();
        Node<Task> node = head;

        while (node != null) {
            history.add(node.data);
            node = node.next;
        }
        return history;
    }

    @Override
    public void remove(int id) {
        removeNode(linkedHashMap.remove(id));
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }
        }
    }

    static class Node<Task> {
        public Task data;
        public Node<Task> next;
        public Node<Task> prev;

        Node(Node<Task> prev, Task data, Node<Task> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

}
