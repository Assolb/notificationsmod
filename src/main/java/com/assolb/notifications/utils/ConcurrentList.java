package com.assolb.notifications.utils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentList<V> implements List<V> {

    private NavigableMap<Integer, V> map = new ConcurrentSkipListMap<>();

    @Override
    public int size()
    {
        return map.size();
    }

    @Override
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return map.values().contains(o);
    }

    @Override
    public Iterator<V> iterator()
    {
        return map.values().iterator();
    }

    @Override
    public Object[] toArray()
    {
        return map.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        return map.values().toArray(a);
    }

    @Override
    public V get(int index)
    {
        return map.get(index);
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return map.values().containsAll(c);
    }

    @Override
    public int indexOf(Object o)
    {
        for (Map.Entry<Integer, V> ent : map.entrySet()) {
            if (Objects.equals(ent.getValue(), o)) {
                return ent.getKey();
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o)
    {
        for (Map.Entry<Integer, V> ent : map.descendingMap().entrySet()) {
            if (Objects.equals(ent.getValue(), o)) {
                return ent.getKey();
            }
        }
        return -1;
    }

    @Override
    public ListIterator<V> listIterator(int index)
    {
        return new ListIterator<V>()
        {
            private int currIdx = 0;

            @Override
            public boolean hasNext()
            {
                return currIdx < map.size();
            }

            @Override
            public V next()
            {
                if (currIdx >= map.size()) {
                    throw new IllegalArgumentException(
                            "next() called at end of list");
                }
                return map.get(currIdx++);
            }

            @Override
            public boolean hasPrevious()
            {
                return currIdx > 0;
            }

            @Override
            public V previous()
            {
                if (currIdx <= 0)
                {
                    throw new IllegalArgumentException(
                            "previous() called at beginning of list");
                }
                return map.get(--currIdx);
            }

            @Override
            public int nextIndex()
            {
                return currIdx + 1;
            }

            @Override
            public int previousIndex()
            {
                return currIdx - 1;
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(V e)
            {
                // Might change size of map if currIdx == map.size(),
                // so need to synchronize
                synchronized (map)
                {
                    map.put(currIdx, e);
                }
            }

            @Override
            public void add(V e)
            {
                synchronized (map)
                {
                    // Insertion is not supported except at end of list
                    if (currIdx < map.size())
                    {
                        throw new UnsupportedOperationException();
                    }
                    map.put(currIdx++, e);
                }
            }
        };
    }

    @Override
    public ListIterator<V> listIterator()
    {
        return listIterator(0);
    }

    @Override
    public List<V> subList(int fromIndex, int toIndex)
    {
        return null;
    }

    @Override
    public boolean add(V e)
    {
        synchronized (map)
        {
            map.put(map.size(), e);
            return true;
        }
    }

    @Override
    public boolean addAll(Collection<? extends V> c)
    {
        synchronized (map) {
            for (V val : c) {
                add(val);
            }
            return true;
        }
    }

    @Override
    public V set(int index, V element)
    {
        synchronized (map) {
            if (index < 0 || index > map.size())
            {
                throw new IllegalArgumentException("Index out of range");
            }
            return map.put(index, element);
        }
    }

    @Override
    public void clear()
    {
        synchronized (map)
        {
            map.clear();
        }
    }

    @Override
    public synchronized void add(int index, V element)
    {
        synchronized (map)
        {
            if (index < map.size())
            {
                // Insertion is not supported except at end of list
                throw new UnsupportedOperationException();
            } else if (index < 0 || index > map.size())
            {
                throw new IllegalArgumentException("Index out of range");
            }
            // index == map.size()
            add(element);
        }
    }

    @Override
    public synchronized boolean addAll(
            int index, Collection<? extends V> c)
    {
        synchronized (map)
        {
            if (index < map.size())
            {
                // Insertion is not supported except at end of list
                throw new UnsupportedOperationException();
            } else if (index < 0 || index > map.size())
            {
                throw new IllegalArgumentException("Index out of range");
            }
            // index == map.size()
            for (V val : c)
            {
                add(val);
            }
            return true;
        }
    }

    @Override
    public boolean remove(Object o)
    {
        synchronized (map)
        {
            int removeId = -1;

            for(int i = 0; i < map.size(); i++)
            {
                if(map.get(i).equals(o))
                {
                    removeId = i;
                    break;
                }
            }
            if(removeId != -1) map.remove(removeId);
        }
        return true;
    }

    @Override
    public V remove(int index)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }
}