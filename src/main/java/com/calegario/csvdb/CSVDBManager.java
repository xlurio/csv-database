package com.calegario.csvdb;

import com.opencsv.*;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import javax.swing.JOptionPane;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ClassCastException;
import java.lang.ArrayIndexOutOfBoundsException;

public class CSVDBManager{
    private String csvPath;
    private String[] header;

    public CSVDBManager(String csvPath, String[] header) {
        this.csvPath = csvPath;
        this.header = header;
    }

    public void addRow(String[] row) {
        List<String[]> data = getDB();
        data.remove(0);
        data.add(row);
        newDB(data);
    }

    public void removeRow(String toFindValue, int column) {
        List<String[]> data = getDB();
        data.remove(0);
        int removedCounter = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[column].equals(toFindValue)){
                data.remove(i);
                removedCounter++;
            }
        }
        if (removedCounter != 0) {
            newDB(data);
            JOptionPane.showMessageDialog(
                null, String.valueOf(removedCounter) + " row(s) removed!"
            );
        } else {
            JOptionPane.showMessageDialog(
                null, "No '" + toFindValue + "' found"
            );
        }
    }

    public void removeRow(String toFindValue, String columnName) {
        int column = getColumnIndex(columnName);
        removeRow(toFindValue, column);
    }

    public int getColumnIndex(String col) {
        for (int i = 0; i < header.length; i++){
            if (col == header[i]){
                return i;
            }
        }
        throw new ArrayIndexOutOfBoundsException("Column name not found!");
    }

    public List<String[]> getDB(){
        List<String[]> data = new ArrayList<String[]>();

        try {
            CSVReader reader = new CSVReader(new FileReader(csvPath));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                data.add(nextLine);
            }
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            data.add(new String[]{"Empty"});
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void newDB(List<String[]> data){
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(csvPath));
            CSVWriter csvWriter = new CSVWriter(writer);
            csvWriter.writeNext(header);
            csvWriter.writeAll(data);
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printDB(){
        List<String[]> savedData = getDB();
        for (int i = 0; i < savedData.size(); i++) {
          System.out.println(savedData.get(i)[0] + "; " +
                             savedData.get(i)[1] + "; " +
                             savedData.get(i)[2]);
        }
    }

    public String findValue(String columnsToLook, String valueToLook,
                            String columnToRetrive){
        /**
         * Retrieves the rows where the passed value to be looked
         for are present on the value of the passed column
        **/
        List<String[]> data = new ArrayList<String[]>();
        int indexToLook = getColumnIndex(columnsToLook);
        int indexToRetrieve = getColumnIndex(columnToRetrive);
        data = getDB();
        for (int i = 0; i < data.size(); i++) {
            String currValue = data.get(i)[indexToLook];
            if (currValue.contains(valueToLook)) {
                return data.get(i)[indexToRetrieve];
            }
        }
        return "";
    }

}
