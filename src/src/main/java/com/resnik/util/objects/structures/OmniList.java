package com.resnik.util.objects.structures;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class OmniList<T> implements Map<String, List<T>>, Iterable<Entry<String, List<T>>>, Cloneable {

    private List<Entry<String, List<T>>> entryList;
    private Class<T> clazzRep;

    public OmniList() {
        this.entryList = new ArrayList();
    }

    public OmniList(Map<String, List<T>> inputMap){
        this();
        this.putAll(inputMap);
    }

    public OmniList(List<Entry<String, List<T>>> inputList){
        this();
        this.putAll(inputList);
    }

    /**
     * Sorts each sublist based on the passed comparator.
     *
     * @param comp Sublist comparator.
     */
    public void sortSub(Comparator<T> comp) {
        for (Entry<String, List<T>> subEntry : entryList) {
            subEntry.getValue().sort(comp);
        }
    }

    public void sort(Comparator<String> comp) {
        Comparator<Entry<String, List<T>>> keyComp = (e1, e2) -> {
            return comp.compare(e1.getKey(), e2.getKey());
        };
        this.entryList.sort(keyComp);
    }

    @Override
    public int size() {
        int retInt = 0;
        for (Entry<String, List<T>> subEntry : entryList) {
            retInt += subEntry.getValue().size();
        }
        return retInt;
    }

    @Override
    public boolean isEmpty() {
        if (this.entryList.isEmpty()) {
            return true;
        }
        for (Entry<String, List<T>> subEntry : entryList) {
            if (subEntry.getValue().isEmpty() == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsKey(Object o) {
        if (o == null || (o instanceof String == false)) {
            return false;
        }
        String rep = (String) o;
        for (Entry<String, List<T>> subEntry : entryList) {
            if (subEntry.getKey().equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        if (clazzRep == null) {
            return false;
        }
        if (o == null) {
            try {
                T value = clazzRep.cast(o);
                return containsValueList(value);
            } catch (ClassCastException ex) {
                return false;
            }
        }
        if (o instanceof List) {
            List listRep = (List) o;
            if (listRep.isEmpty()) {
                return false;
            }
            Object firstElem = listRep.get(0);
            try {
                T firstValue = clazzRep.cast(firstElem);
                List<T> listRepGen = (List<T>) o;
                return containsList(listRepGen);
            } catch (ClassCastException ex) {
                return false;
            }
        }
        return false;
    }

    public boolean containsValueList(T value) {
        for (Entry<String, List<T>> subEntry : entryList) {
            if (subEntry.getValue().contains(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsList(List<T> listRep) {
        for (Entry<String, List<T>> subEntry : entryList) {
            boolean containsAll = true;
            for (T elem : listRep) {
                if (subEntry.getValue().contains(elem) == false) {
                    containsAll = false;
                    break;
                }
            }
            if (containsAll) {
                return true;
            }
        }
        for (T elem : listRep) {
            if (this.containsValueList(elem) == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<T> get(Object o) {
        if (o == null || (o instanceof String == false)) {
            return null;
        }
        for (Entry<String, List<T>> subEntry : entryList) {
            if (subEntry.getKey().equals(o)) {
                return subEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public List<T> put(String k, List<T> v) {
        if (k == null || v == null) {
            return null;
        }
        if (clazzRep == null) {
            for (T elem : v) {
                if (v != null) {
                    clazzRep = (Class<T>) v.getClass();
                    break;
                }
            }
        }
        if (clazzRep == null) {
            return null;
        }
        if (this.containsKey(k) == false) {
            this.entryList.add(new SimpleEntry<String, List<T>>(k, v));
            return v;
        }
        List<T> addTo = this.get(k);
        if (addTo == null) {
            throw new IllegalStateException();
        }
        for (T elem : v) {
            addTo.add(elem);
        }
        return v;
    }

    public List<T> put(Map.Entry<String, List<T>> entry){
        return this.put(entry.getKey(), entry.getValue());
    }

    public boolean add(String k, T v, T... rem) {
        if (k == null) {
            return false;
        }
        if (clazzRep == null) {
            if (v != null) {
                clazzRep = (Class<T>) v.getClass();
            } else {
                for (T elem : rem) {
                    if (elem != null) {
                        clazzRep = (Class<T>) elem.getClass();

                    }
                }
            }
        }
        if (clazzRep == null) {
            return false;
        }
        if (this.containsKey(k) == false) {
            List<T> newList = new ArrayList();
            newList.add(v);
            for (T elem : rem) {
                newList.add(elem);
            }
            return this.entryList.add(new SimpleEntry<String, List<T>>(k, newList));
        }
        List<T> addTo = this.get(k);
        if (addTo == null) {
            throw new IllegalStateException();
        }
        boolean retVal = addTo.add(v);
        for (T elem : rem) {
            retVal &= addTo.add(elem);
        }
        return retVal;
    }

    @Override
    public List<T> remove(Object o) {
        if (o == null || (o instanceof String == false)) {
            return null;
        }
        int remIndex = -1;
        int count = 0;
        for (Entry<String, List<T>> subEntry : entryList) {
            if (subEntry.getKey().equals(o)) {
                remIndex = count;
                break;
            }
            count++;
        }
        if (remIndex == -1) {
            return null;
        }
        return this.entryList.remove(remIndex).getValue();
    }

    public boolean removeKey(String key) {
        List<T> res = this.remove(key);
        return res != null;
    }

    public boolean removeAllOccurances(T value) {
        boolean success = false;
        List<String> keysToRemove = new ArrayList();
        for (Entry<String, List<T>> subEntry : entryList) {
            while (subEntry.getValue().contains(value)) {
                subEntry.getValue().remove(value);
                success = true;
            }
            if (subEntry.getValue().isEmpty()) {
                keysToRemove.add(subEntry.getKey());
            }
        }
        for (String toRem : keysToRemove) {
            this.removeKey(toRem);
        }
        return success;
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<T>> map) {
        map.forEach(OmniList.this::put);
    }

    public void putAll(List<Map.Entry<String, List<T>>> list){
        list.forEach(OmniList.this::put);
    }

    @Override
    public void clear() {
        this.entryList.clear();
    }

    @Override
    public Set<String> keySet() {
        Set<String> retSet = new LinkedHashSet();
        for (Entry<String, List<T>> subEntry : entryList) {
            retSet.add(subEntry.getKey());
        }
        return retSet;
    }

    @Override
    public Collection<List<T>> values() {
        List<List<T>> retList = new ArrayList();
        for (Entry<String, List<T>> subEntry : entryList) {
            retList.add(subEntry.getValue());
        }
        return retList;
    }

    @Override
    public Set<Entry<String, List<T>>> entrySet() {
        Set<Entry<String, List<T>>> retSet = new LinkedHashSet(this.entryList);
        return retSet;
    }

    @Override
    public Iterator<Entry<String, List<T>>> iterator() {
        return entryList.iterator();
    }

    @Override
    public String toString() {
        String retString = "{\n";
        for (Entry<String, List<T>> entry : this) {
            retString += entry.getKey() + " : " + entry.getValue().toString() + "\n";
        }
        retString += "}";
        return retString;
    }

    @Override
    protected OmniList clone() {
        return new OmniList<>(this.entryList);
    }
}
