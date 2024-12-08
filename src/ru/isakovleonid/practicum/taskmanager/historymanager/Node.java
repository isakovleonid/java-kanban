package ru.isakovleonid.practicum.taskmanager.historymanager;

public class Node<T> {
    T element;
    Node<T> prev;
    Node<T> next;

    public Node(Node<T> prev, T element, Node<T> next) {
        this.prev = prev;
        this.element = element;
        this.next = next;
    }
}
