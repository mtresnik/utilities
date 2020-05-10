package com.resnik.util.files.csv;

import com.resnik.util.logger.Log;
import org.junit.Test;

import java.io.IOException;

public class TestCSV {

    public static final String TAG = TestCSV.class.getSimpleName();


    @Test
    public void testCSV(){
        String[] header = new String[]{"Name", "Date", "Age", "Alive"};
        CSV csv = new CSV(header);
        csv.addLine(new String[]{"Mike", "05/09/2020", "23", "true"});
        csv.addLine(new String[]{"James", "05/11/2020", "30", "true"});
        csv.addLine(new String[]{"Eddy", "02/11/1700", "300", "false"});
        Log.d(TAG, csv);
        Log.d(TAG, csv.getColTypes());
        Log.d(TAG, csv.getColumn(1));
        Log.d(TAG, csv.getColumn("Name"));
        try {
            csv.save("src/res/csv/test.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
