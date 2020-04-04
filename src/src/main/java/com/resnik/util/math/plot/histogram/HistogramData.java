package com.resnik.util.math.plot.histogram;

import com.resnik.util.logger.Log;
import com.resnik.util.objects.structures.CountList;
import com.resnik.util.objects.structures.CountObject;

import java.util.*;

public class HistogramData<T> extends CountList<T> {

    public static final String TAG = HistogramData.class.getSimpleName();

    public HistogramData(){
        this.setMaintainOrderPair(null);
    }

    public static <T> HistogramData<T> toHistogramData(CountList<T> countList){
        HistogramData<T> ret = new HistogramData<>();
        ret.setMaintainOrderPair(null);
        countList.forEach(tCountObject -> ret.add(tCountObject));
        Log.v(TAG,"ret:" + ret);
        return ret;
    }

    public HistogramData<T> yoke(HistogramData<T> other){
        other.forEach((tCountObject -> {this.add(tCountObject);}));
        return this;
    }

    public static HistogramData<Double> fromArray(double[] inputArray, double min, double max, double binSize){
        int n = (int)((max - min)/binSize);
        List<Double> bins = new ArrayList<>();
        CountList<Double> retCount = new CountList<>();
        retCount.setMaintainOrderPair(null);
        for(double curr = min + binSize/2; curr <= max; curr+= binSize){
            bins.add(curr);
            retCount.addElement(curr, 0);
        }
        for(double curr : inputArray){
            for(double bin : bins){
                if(Math.abs(curr - bin) <= binSize){
                    retCount.addElement(bin);
                }
            }
        }
        Log.v(TAG,retCount);
        return HistogramData.toHistogramData(retCount);
    }

    public static <T> void ensureBounds(HistogramData<T> one, HistogramData<T> other){
        for(CountObject<T> c1 : one){
            if(!other.containsElement(c1.getElement())){
                other.addElement(c1.getElement(), 0);
            }
        }
        for(CountObject<T> c2 : other){
            if(!one.containsElement(c2.getElement())){
                one.addElement(c2.getElement(), 0);
            }
        }
    }


}
