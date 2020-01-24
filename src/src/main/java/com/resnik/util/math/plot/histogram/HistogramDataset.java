package com.resnik.util.math.plot.histogram;

import com.resnik.util.objects.structures.CountObject;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class HistogramDataset<T> extends LinkedHashMap<String, HistogramData<T>> implements Iterable<Map.Entry<String, HistogramData<T>>> {

    public static final Color[] ALL_COLORS = new Color[]{
            Color.RED, Color.ORANGE, Color.YELLOW, Color.PINK, Color.MAGENTA,
            Color.GREEN, Color.BLUE, Color.CYAN,
    };

    public static final Color[] WARM_COLORS = new Color[]{
            Color.RED, Color.ORANGE, Color.YELLOW, Color.PINK, Color.MAGENTA
    };

    public static final Color[] COLD_COLORS = new Color[]{
            Color.GREEN, Color.BLUE, Color.CYAN,
    };

    public static final Color RAND_COLOR() {
        return new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1.0f);
    }

    @Override
    public Iterator<Map.Entry<String, HistogramData<T>>> iterator() {
        return this.entrySet().iterator();
    }

    public void setColors(){
    }

    @Override
    public void forEach(Consumer<? super Map.Entry<String, HistogramData<T>>> consumer) {
        this.entrySet().forEach(consumer);
    }

    public void add(String str, HistogramData data){
        HistogramData toPut = data;
        if(this.containsKey(str)){
            toPut = this.get(str).yoke(data);
        }
        this.put(str, toPut);
    }

    public int numDatasets(){
        return this.entrySet().size();
    }

    @Override
    public int size(){
        List<T> uniqueSet = new ArrayList<>();
        for(Map.Entry<String, HistogramData<T>> entry : this){
            HistogramData<T> data = entry.getValue();
            for(int i = 0; i < data.size(); i++){
                CountObject<T> obj = data.get(i);
                if(!uniqueSet.contains(obj.getElement())){
                    uniqueSet.add(obj.getElement());
                }
            }
        }
        return uniqueSet.size();
    }

    public int maxCount(){
        int max = 0;
        for(Map.Entry<String, HistogramData<T>> entry : this){
            max = Math.max(max, entry.getValue().maxCount());
        }
        return max;
    }


}
