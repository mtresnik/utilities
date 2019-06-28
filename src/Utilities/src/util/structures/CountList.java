package util.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Function;
import javafx.util.Pair;
import util.math.functions.Function2;

public class CountList<T> implements List<CountObject<T>> {

    private List<CountObject<T>> elements;
    private Pair<Boolean, Comparator<CountObject>> maintainOrderPair;
    private Function2<CountObject<T>, CountObject<T>, Boolean> equalsMethod;

    public CountList(List<CountObject<T>> elements, Pair<Boolean, Comparator<CountObject>> maintainOrderPair, Function2<CountObject<T>, CountObject<T>, Boolean> equalsMethod) {
        this.elements = elements;
        this.maintainOrderPair = maintainOrderPair;
        this.equalsMethod = equalsMethod;
        this.maintainOrder();
    }
    
    public CountList(List<CountObject<T>> elements) {
        this.elements = elements;
        this.init();
    }

    public CountList(CountObject<T>... elemArray) {
        this.elements = new ArrayList(Arrays.asList(elemArray));
        this.init();
    }

    public CountList(T firstElem, T... remElem) {
        this.elements = new ArrayList();
        this.init();
        List<T> subList = new ArrayList(Arrays.asList(remElem)) {
            {
                this.add(firstElem);
            }
        };
        for (int i = 0; i < subList.size(); i++) {
            T curr = subList.get(i);
            CountObject<T> currCount = new CountObject(curr);
            this.add(currCount);
        }
    }

    private void init() {
        this.equalsMethod = (CountObject<T> t1, CountObject<T> t2) -> (t1 == t2) || (t1 != null && t2 != null && t1.equals(t2));
        this.maintainOrderPair = new Pair<Boolean, Comparator<CountObject>>(true,
                CountObject.AMOUNT_COMPARATOR
        );
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return elements.contains(o);
    }

    @Override
    public Iterator<CountObject<T>> iterator() {
        return elements.iterator();
    }

    @Override
    public Object[] toArray() {
        return elements.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return elements.toArray(ts);
    }

    @Override
    public boolean add(CountObject<T> e) {
        for (int i = 0; i < this.elements.size(); i++) {
            CountObject<T> curr = this.elements.get(i);
            if (this.equalsMethod.apply(curr, e)) {
                CountObject<T> replacement = CountObject.addIntersect(curr, e);
                this.elements.remove(i);
                this.elements.add(i, replacement);
                this.maintainOrder();
                return true;
            }
        }
        boolean success = this.elements.add(e);
        this.maintainOrder();
        return success;
    }

    public boolean addElement(T t1) {
        return this.add(new CountObject<T>(t1));
    }

    public boolean addElement(T t1, int n) {
        return this.add(new CountObject<T>(t1, n));
    }

    public boolean addCountObject(T t) {
        CountObject<T> countObject = new CountObject(t);
        return this.add(countObject);
    }

    public boolean addUnion(CountObject<T> e) {
        for (int i = 0; i < this.elements.size(); i++) {
            CountObject<T> curr = this.elements.get(i);
            if (this.equalsMethod.apply(curr, e)) {
                CountObject<T> replacement = CountObject.addUnion(curr, e);
                this.elements.remove(i);
                this.elements.add(i, replacement);
                this.maintainOrder();
                return true;
            }
        }
        boolean success = this.elements.add(e);
        this.maintainOrder();
        return success;
    }

    @Override
    public boolean remove(Object o) {
        return this.elements.remove(o);
    }

    public boolean remove(CountObject<T> e) {
        for (int i = 0; i < this.elements.size(); i++) {
            CountObject<T> curr = this.elements.get(i);
            if (this.equalsMethod.apply(curr, e)) {
                CountObject<T> replacement = CountObject.subIntersect(curr, e);
                this.elements.remove(i);
                this.elements.add(i, replacement);
                this.maintainOrder();
                return true;
            }
        }
        boolean success = this.elements.remove(e);
        this.maintainOrder();
        return success;
    }

    public boolean removeUnion(CountObject<T> e) {
        for (int i = 0; i < this.elements.size(); i++) {
            CountObject<T> curr = this.elements.get(i);
            if (this.equalsMethod.apply(curr, e)) {
                CountObject<T> replacement = CountObject.subUnion(curr, e);
                this.elements.remove(i);
                this.elements.add(i, replacement);
                this.maintainOrder();
                return true;
            }
        }
        boolean success = this.elements.remove(e);
        this.maintainOrder();
        return success;
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        Iterator<?> iterator = clctn.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            if (this.contains(o) == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends CountObject<T>> clctn) {
        Iterator<? extends CountObject<T>> iterator = clctn.iterator();
        boolean ret = true;
        while (iterator.hasNext()) {
            CountObject<T> pair = iterator.next();
            boolean result = this.add(pair);
            ret &= result;
        }
        this.maintainOrder();
        return ret;
    }

    @Override
    public boolean addAll(int i, Collection<? extends CountObject<T>> clctn) {
        return this.addAll(clctn);
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        Iterator<?> iterator = clctn.iterator();
        boolean ret = true;
        while (iterator.hasNext()) {
            Object curr = iterator.next();
            boolean result = this.remove(curr);
            ret &= result;
        }
        this.maintainOrder();
        return ret;
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        List<CountObject<T>> toRemove = new ArrayList();
        for (int i = 0; i < this.elements.size(); i++) {
            CountObject<T> curr = this.elements.get(i);
            if (clctn.contains(curr) == false) {
                toRemove.add(curr);
            }
        }
        return this.elements.removeAll(toRemove);
    }

    @Override
    public void clear() {
        this.elements.clear();
    }

    @Override
    public CountObject<T> get(int i) {
        return this.elements.get(i);
    }

    @Override
    public CountObject<T> set(int i, CountObject<T> e) {
        this.remove(e);
        return this.elements.set(i, e);
    }

    @Override
    public void add(int i, CountObject<T> e) {
        this.add(e);
        int j = this.indexOf(e);
        CountObject<T> rem = this.remove(j);
        this.elements.add(i, rem);
        this.maintainOrder();
    }

    @Override
    public CountObject<T> remove(int i) {
        return this.elements.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < this.elements.size(); i++) {
            CountObject<T> curr = this.elements.get(i);
            if (Objects.equals(o, curr)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = this.elements.size() - 1; i >= 0; i--) {
            CountObject<T> curr = this.elements.get(i);
            if (Objects.equals(o, curr)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<CountObject<T>> listIterator() {
        return this.elements.listIterator();
    }

    @Override
    public ListIterator<CountObject<T>> listIterator(int i) {
        return this.elements.listIterator(i);
    }

    @Override
    public List<CountObject<T>> subList(int i, int i1) {
        return this.elements.subList(i, i1);
    }

    @Override
    public String toString() {
        return "CountList{" + "elements=" + elements + '}';
    }

    public int getAmountNegative() {
        int ret = 0;
        for (int i = 0; i < this.size(); i++) {
            CountObject<T> curr = this.elements.get(i);
            if (curr.getAmount() < 0) {
                ret += curr.getAmount();
            }
        }
        return ret;
    }

    public int getAmount() {
        int ret = 0;
        for (int i = 0; i < this.size(); i++) {
            CountObject<T> curr = this.elements.get(i);
            if (curr.getAmount() > 0) {
                ret += curr.getAmount();
            }
        }
        return ret;
    }

    public <R> List<R> applyBulkFunction(Function<CountObject<T>, R> function) {
        List<R> retList = new ArrayList();
        for (int i = 0; i < this.elements.size(); i++) {
            CountObject<T> curr = this.elements.get(i);
            R result = function.apply(curr);
            retList.add(result);
        }
        return retList;
    }

    public <N extends Number> N applyBulkFunctionNumerical(Function<CountObject<T>, N> function, Function2<N, N, N> numerical) {
        List<N> numList = applyBulkFunction(function);
        N prev = numList.get(0);
        for (int i = 0; i < numList.size(); i++) {
            N curr = numList.get(i);
            prev = numerical.apply(prev, curr);
        }
        return (N) prev;
    }

    public void sortAmount() {
        this.elements.sort(CountObject.AMOUNT_COMPARATOR);
    }

    public void disableAutoSort() {
        Comparator<CountObject> comp = this.maintainOrderPair.getValue();
        this.maintainOrderPair = new Pair(false, comp);
    }

    public void enableAutoSort() {
        Comparator<CountObject> comp = this.maintainOrderPair.getValue();
        this.maintainOrderPair = new Pair(true, comp);
        this.maintainOrder();
    }

    public Pair<Boolean, Comparator<CountObject>> getMaintainOrderPair() {
        return maintainOrderPair;
    }

    public void setMaintainOrderPair(Pair<Boolean, Comparator<CountObject>> maintainOrderPair) {
        this.maintainOrderPair = maintainOrderPair;
    }

    private void maintainOrder() {
        if (this.maintainOrderPair == null || this.maintainOrderPair.getKey() == false || this.maintainOrderPair.getValue() == null) {
            return;
        }
        this.sort(this.maintainOrderPair.getValue());
    }
    
    public void sort(){
        // Find all similarities
        this.maintainOrder();
    }

    public CountList<T> setEqualsMethod(Function2<CountObject<T>, CountObject<T>, Boolean> equalsMethod) {
        CountList<T> retList = new CountList(this.elements, this.maintainOrderPair, equalsMethod);
        return retList;
    }
    
    
    @Override
    public CountList<T> clone(){
        List<CountObject<T>> newElements = new ArrayList();
        for(CountObject<T> currCountObject : this.elements){
            CountObject<T> clonedObj = currCountObject.clone();
            newElements.add(clonedObj);
        }
        return new CountList<T>(newElements);
        
        
    }

    
    
}
