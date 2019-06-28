package util.structures;

import java.util.Comparator;
import java.util.Objects;

public class CountObject<T> {

    private T identifiableElement;
    private int amount;
    public final int upper_bound, lower_bound;

    public static Comparator<CountObject> AMOUNT_COMPARATOR;

    static {
        // Comparators are functional interfaces (t, t1) -> int
        AMOUNT_COMPARATOR = (CountObject t, CountObject t1) -> ( Integer.compare(t.getAmount(), t1.getAmount()));
    }

    public CountObject(T element) {
        this(element, 0, Integer.MAX_VALUE);
    }

    public CountObject(T element, int amount) {
        this(element);
        this.setAmount(amount);
    }

    public CountObject(T element, int lower_bound, int upper_bound) {
        this.identifiableElement = element;
        this.amount = 1;
        if (upper_bound < 1 || upper_bound < lower_bound) {
            throw new IllegalArgumentException("Upper bound must be strictly greater than the lower bound.");
        }
        this.upper_bound = upper_bound;
        this.lower_bound = lower_bound;
    }

    public static <T> CountObject<T> addIntersect(CountObject<T> p1, CountObject<T> p2) {
        if (Objects.equals(p1.identifiableElement, p2.identifiableElement) == false) {
            return null;
        }
        final int lower_bound = Math.max(p1.lower_bound, p2.lower_bound);
        final int upper_bound = Math.min(p1.upper_bound, p2.upper_bound);
        int amount = p1.getAmount() + p2.getAmount();
        CountObject<T> retPair = new CountObject(p1.identifiableElement, lower_bound, upper_bound);
        retPair.setAmount(amount);
        return retPair;
    }

    public static <T> CountObject<T> addUnion(CountObject<T> p1, CountObject<T> p2) {
        if (Objects.equals(p1.identifiableElement, p2.identifiableElement) == false) {
            return null;
        }
        final int lower_bound = Math.min(p1.lower_bound, p2.lower_bound);
        final int upper_bound = Math.max(p1.upper_bound, p2.upper_bound);
        int amount = p1.getAmount() + p2.getAmount();
        CountObject<T> retPair = new CountObject(p1.identifiableElement, lower_bound, upper_bound);
        retPair.setAmount(amount);
        return retPair;
    }

    public static <T> CountObject<T> subIntersect(CountObject<T> p1, CountObject<T> p2) {
        if (Objects.equals(p1.identifiableElement, p2.identifiableElement) == false) {
            return null;
        }
        final int lower_bound = Math.max(p1.lower_bound, p2.lower_bound);
        final int upper_bound = Math.min(p1.upper_bound, p2.upper_bound);
        int amount = p1.getAmount() - p2.getAmount();
        CountObject<T> retPair = new CountObject(p1.identifiableElement, lower_bound, upper_bound);
        retPair.setAmount(amount);
        return retPair;
    }

    public static <T> CountObject<T> subUnion(CountObject<T> p1, CountObject<T> p2) {
        if (Objects.equals(p1.identifiableElement, p2.identifiableElement) == false) {
            return null;
        }
        final int lower_bound = Math.min(p1.lower_bound, p2.lower_bound);
        final int upper_bound = Math.max(p1.upper_bound, p2.upper_bound);
        int amount = p1.getAmount() - p2.getAmount();
        CountObject<T> retPair = new CountObject(p1.identifiableElement, lower_bound, upper_bound);
        retPair.setAmount(amount);
        return retPair;
    }

    public boolean add(int value) {
        this.amount += value;
        return this.checkBounds();
    }

    public boolean subtract(int value) {
        this.amount -= value;
        return this.checkBounds();
    }

    public boolean increment() {
        return this.add(1);
    }

    public boolean decrement() {
        return this.subtract(1);
    }

    private boolean checkBounds() {
        boolean ret = true;
        if (this.amount > upper_bound) {
            this.amount = upper_bound;
            ret &= false;
        }
        if (this.amount < lower_bound) {
            this.amount = lower_bound;
            ret &= false;
        }
        return ret;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        this.checkBounds();
    }

    public T getElement() {
        return identifiableElement;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public String toString() {
        return identifiableElement + ":" + this.amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CountObject<?> other = (CountObject<?>) obj;
        if (!Objects.equals(this.identifiableElement, other.identifiableElement)) {
            return false;
        }
        return true;
    }
    
    @Override
    public CountObject<T> clone(){
        T newElem = this.getElement();
        return new CountObject(newElem, this.getAmount());
    }

}
