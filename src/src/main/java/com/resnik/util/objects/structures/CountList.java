package com.resnik.util.objects.structures;

import java.util.*;
import java.util.function.Function;

import com.resnik.util.logger.Log;
import javafx.util.Pair;
import com.resnik.util.objects.FunctionUtils.Function2;

public class CountList<T> implements List<CountObject<T>> {

    public static final String TAG = CountList.class.getSimpleName();

    private List<CountObject<T>> elements;
    private Pair<Boolean, Comparator<CountObject>> maintainOrderPair;
    private Function2<CountObject<T>, CountObject<T>, Boolean> equalsMethod;

    public CountList(List<CountObject<T>> elements, Pair<Boolean, Comparator<CountObject>> maintainOrderPair, Function2<CountObject<T>, CountObject<T>, Boolean> equalsMethod) {
        this.elements = elements;
        this.maintainOrderPair = maintainOrderPair;
        this.equalsMethod = equalsMethod;
        this.maintainOrder();
    }

    public CountList(List<CountObject<T>> elements, boolean maintainOrder) {
        this.elements = elements;
        Log.v(TAG,"elements:" + elements);
        this.init(maintainOrder);
    }

    public CountList(List<CountObject<T>> elements) {
        this(elements, true);
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

    private void init(){
        this.init(true);
    }

    private void init(boolean maintainOrder) {
        this.equalsMethod = (CountObject<T> t1, CountObject<T> t2) -> (t1 == t2) || (t1 != null && t2 != null && t1.equals(t2));
        if(maintainOrder){
            this.maintainOrderPair = new Pair<Boolean, Comparator<CountObject>>(true,
                    CountObject.AMOUNT_COMPARATOR
            );
        }
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

    public boolean addAllElements(Iterable<T> allElements){
        boolean ret = true;
        Iterator<T> iterator = allElements.iterator();
        while(iterator.hasNext()){
            ret &= this.addElement(iterator.next());
        }
        return ret;

    }

    public boolean addAllElements(T[] allElements){
        boolean ret = true;
        for(T element : allElements){
            ret &= this.addElement(element);
        }
        return ret;
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


    public boolean containsElement(T element){
        for(CountObject<T> countObject : this){
            if(Objects.equals(countObject.getElement(), element)){
                return true;
            }
        }
        return false;
    }

    public int getCountOr(T element, int option){
        for(CountObject<T> countObject : this){
            if(Objects.equals(countObject.getElement(), element)){
                return countObject.getAmount();
            }
        }
        return option;
    }

    public int maxCount(){
        int max = 0;
        for(CountObject elem : this){
            max = Math.max(max, elem.getAmount());
        }
        return max;
    }

}
