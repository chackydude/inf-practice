import java.util.*;

public class MyNavigableSet<T> extends AbstractSet<T> implements NavigableSet<T> {
    private ArrayList<T> data;
    private Comparator<T>  comparator;
    private SortedSet<T> sortedSet;

    // constructors
    public MyNavigableSet(Comparator<T> comparator) {
        this.comparator = comparator;
        this.data = new ArrayList<T>(100);
    }

    public MyNavigableSet(Comparator<T> comparator, ArrayList<T> list) {
        this.comparator = comparator;
        this.data = list;
    }

    // adds an element comparing according to the comparator
    public boolean add(T element) {
        if (data.size() == 0) {
            data.add(element);
            return true;
        } else {
            if (this.data.contains(element)) {
                return false;
            } else {
                for (int i = 0; i < this.data.size(); i++) {
                    if (this.comparator.compare(this.data.get(i), element) > 0) {
                        this.data.add(i, element);
                        return true;
                    }
                }
                this.data.add(this.data.size(), element);
                return true;
            }
        }
    }

    // first element which strictly less than arg, else null
    @Override
    public T lower(T element) {
        if (this.data.size() == 0) {
            return null;
        } else  if (this.comparator.compare(data.get(data.size()-1), element) < 0){
            return data.get(data.size()-1);
        } else {
            for (int i = 0; i < this.data.size(); i++) {
                if (this.comparator.compare(this.data.get(i), element) >= 0) {
                    if (i > 0) {
                        return this.data.get(i - 1);
                    } else return null;
                }
            }
            return null;
        }
    }

    // first element which not strictly less than arg, else null
    @Override
    public T floor(T element) {
        if (this.data.size() == 0) {
            return null;
        } else  if (this.comparator.compare(data.get(data.size()-1), element) <= 0){
            return data.get(data.size()-1);
        } else {
            for (int i = 0; i < this.data.size(); i++) {
                if (this.comparator.compare(this.data.get(i), element) > 0) {
                    if (i > 0) {
                        return this.data.get(i - 1);
                    } else return null;
                }
            }
            return null;
        }
    }

    // returns the closest element in the set, but which is greater than or equal to the arg, else null
    @Override
    public T ceiling(T element) {
        if (this.data.size() == 0) return null;
        if (this.data.indexOf(element) > 0) {
            return this.data.get(this.data.indexOf(element));
        } else {
            for (int i = 0; i < this.data.size(); i++) {
                if (this.comparator.compare(this.data.get(i), element) >= 0) {
                    return this.data.get(i);
                }
            }
            return null;
        }
    }

    // returns the closest element in the set, but strictly more than arg, else null
    @Override
    public T higher(T element) {
        if (this.data.size() == 0) {
            return null;
        } else {
            for (int i = 0; i < this.data.size(); i++) {
                if (this.comparator.compare(this.data.get(i), element) > 0) {
                    return this.data.get(i);
                }
            }
            return null;
        }
    }

    // deletes and returns min-element (first)
    @Override
    public T pollFirst() {
        if (this.data.size() == 0) {
            return null;
        } else {
            T first = this.data.get(0);
            this.data.remove(this.data.get(0));
            return first;
        }
    }

    // deletes and returns max-element (last)
    @Override
    public T pollLast() {
        if (this.data.size() == 0) {
            return null;
        } else {
            T last = this.data.get(this.data.size() - 1);
            this.data.remove(this.data.get(this.data.size() - 1));
            return last;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new NavIterator<>();
    }

    @Override
    public NavigableSet<T> descendingSet() {
        MyNavigableSet<T> result = new MyNavigableSet<>(this.comparator);
        for (int i = this.data.size()-1; i >= 0; i--) {
            result.add(this.data.get(i));
        }
        return result;
    }

    // NavIterator has descending-methods (descendingNext, descendingHasNext)
    @Override
    public Iterator<T> descendingIterator() {
        return new NavIterator<>();
    }

    // inclusive - true : add in the result-set, false - not
    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        // if x100500
        return null;
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        return null;
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public Comparator<T> comparator() {
        return this.comparator;
    }

    // returns certain range (set) of MyNavigableSet
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        SortedSet<T> result = new MyNavigableSet<T>(this.comparator);
        // if from == to
        if (this.comparator.compare(fromElement, toElement) == 0) {
            return result;
        } else {
            int start = this.data.indexOf(fromElement);
            int end = this.data.indexOf(toElement);
            for (int i = start; i < end; i++) {
                result.add(this.data.get(i));
            }
            return result;
        }
    }

    // returns range (set) to certain element of MyNavigableSet
    @Override
    public SortedSet<T> headSet(T toElement) {
        // if head bigger than every element in the set
        if (this.comparator.compare(toElement, this.data.get(this.data.size() - 1)) > 0) {
            return this;
        } else {
            SortedSet<T> result = new MyNavigableSet<T>(this.comparator);
            for (int i = 0; i < this.data.indexOf(toElement); i++) {
                result.add(this.data.get(i));
            }
            return result;
        }
    }

    // returns range (set) from certain element of MyNavigableSet
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // if tail less than every element in the set
        if (this.comparator.compare(fromElement, this.data.get(this.data.size() - 1)) <= 0) {
            return this;
        } else {
            SortedSet<T> result = new MyNavigableSet<T>(this.comparator);
            int end = this.data.size();
            for (int i = this.data.indexOf(fromElement); i < end; i++) {
                result.add(this.data.get(i));
            }
            return result;
        }
    }

    @Override
    public T first() {
        return this.data.get(0);
    }

    @Override
    public T last() {
        return this.data.get(this.data.size() - 1);
    }

    @Override
    public String toString() {
        return this.data.toString();
    }

    // iterator
    private class NavIterator<T> implements Iterator<T> {
        private int cursor;
        private int descendingCursor;

        public NavIterator() {
            this.cursor = 0;
            this.descendingCursor = data.size()-1;
        }

        @Override
        public boolean hasNext() {
            return this.cursor < data.size();
        }

        @Override
        public T next() {
            this.cursor++;
            return (T) data.get(this.cursor - 1);
        }

        public boolean descendingHasNext() {
            return this.descendingCursor > 0;
        }

        public T descendingNext() {
            this.descendingCursor--;
            return (T) data.get(this.descendingCursor + 1);
        }
    }
}

// TODO: NavigableSet, SortedSet, AbstractClass
// 0) написать NavigableSet
// 1) сделать Java Doc для Navigable Set
// 2) залить на ГитХаб

// TODO: add, lower, floor, celling, higher, pollFirst, pollLast, iterator, subset, headset, tailset, toString, hashCode, first, last, size