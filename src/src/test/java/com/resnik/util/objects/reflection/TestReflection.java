package com.resnik.util.objects.reflection;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.Supplier;

public class TestReflection {


    public static class TempClass{

        private double value;

        public TempClass(){
            this(0.0);
        }

        public TempClass(double val){
            this.value = val;
        }

        @Override
        public String toString() {
            return Double.toString(value);
        }
    }

    @Test
    public void testSupplier(){
        Supplier<TempClass> supplier = TempClass::new;
        TempClass t1 = supplier.get();
        System.out.println(t1);

        Function<Double, TempClass> tempClassDouble = TempClass::new;
        TempClass t2 = tempClassDouble.apply(5.0);
        System.out.println(t2);
    }


}
