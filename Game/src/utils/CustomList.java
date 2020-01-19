package utils;

import java.util.Iterator;
public class CustomList<T> implements Iterable<Node> {
    private Node first;
    private Node last;
    public CustomList() {
        first = last = null;
    }
    public boolean isEmpty(){
        return first == null;
    }
    public void push(T data) {
        Node tempo = new Node();
        tempo.setData(data);
        tempo.setNext(null);
        if (first == null) {
            tempo.setPrev(null);
            first = last = tempo;
        } else {
            tempo.setPrev(last);
            last.setNext(tempo);
            last = tempo;
        }
    }
    @Override
    public Iterator<Node> iterator() {
        return new ListIterator(first);
    }
}