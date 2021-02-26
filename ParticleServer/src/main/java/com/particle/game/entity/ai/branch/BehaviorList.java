package com.particle.game.entity.ai.branch;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class BehaviorList<T> implements List<T> {

    private List<T> data;

    public BehaviorList(List<T> data) {
        if (data == null) {
            throw new RuntimeException("Data is null");
        }

        this.data = data;
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.data.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.data.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.data.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return this.data.toArray(a);
    }

    @Override
    public boolean add(T t) {
        throw new RuntimeException("Unmodified");
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("Unmodified");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.data.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new RuntimeException("Unmodified");
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new RuntimeException("Unmodified");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new RuntimeException("Unmodified");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new RuntimeException("Unmodified");
    }

    @Override
    public void clear() {
        throw new RuntimeException("Unmodified");
    }

    @Override
    public T get(int index) {
        return this.data.get(index);
    }

    @Override
    public T set(int index, T element) {
        throw new RuntimeException("Unmodified");
    }

    @Override
    public void add(int index, T element) {
        throw new RuntimeException("Unmodified");
    }

    @Override
    public T remove(int index) {
        throw new RuntimeException("Unmodified");
    }

    @Override
    public int indexOf(Object o) {
        return this.data.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.data.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return this.data.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return this.data.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return this.subList(fromIndex, toIndex);
    }
}
