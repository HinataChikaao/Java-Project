package test;


import org.apache.commons.collections4.iterators.ArrayIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Navigate implements Iterable {

    private List<String> names;
    private Iterator iterator;

    public Navigate() {
        names = new ArrayList<>();
        iterator = new ArrayIterator(names.toArray());
    }

    @Override
    public Iterator iterator() {
        return iterator;
    }

    public void setName(String name) {
        this.names.add(name);
    }
}
