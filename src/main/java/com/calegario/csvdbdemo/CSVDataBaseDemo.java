package com.calegario.csvdbdemo;

import com.calegario.csvdb.CSVDBManager;
import java.util.ArrayList;
import java.util.List;

public class CSVDataBaseDemo
{
    public static void main( String[] args )
    {
        String[] header = new String[]{"index", "fruit", "name"};
        CSVDBManager manager = new CSVDBManager("data.csv", header);

        String[] firstRow = new String[]{"1", "Banana", "Roberto"};
        String[] secondRow = new String[]{"2", "Apple", "Carlos"};
        String[] thirdRow = new String[]{"3", "Pinneaple", "Heman"};
        List<String[]> newData = new ArrayList<String[]>();
        newData.add(firstRow);
        newData.add(secondRow);
        newData.add(thirdRow);

        manager.newDB(newData);

        System.out.println("Test data visualization...");
        manager.printDB();

        System.out.println("Test data adding...");
        manager.addRow(new String[]{"4", "Orange", "Tom"});
        manager.printDB();

        System.out.println("Test data removing...");
        manager.removeRow("Apple", "fruit");
        manager.printDB();
    }
}
