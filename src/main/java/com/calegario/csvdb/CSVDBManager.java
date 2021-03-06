package com.calegario.csvdb;

import com.opencsv.*;
import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.List;
import java.nio.file.Paths;
import java.nio.charset.*;

public class CSVDBManager{
    private String csvPath;
    private String[] header;
    private char sep;
    private Charset encoding;

    public CSVDBManager(String csvPath, String[] header) {
        this(csvPath, header, ',', StandardCharsets.UTF_8);
    }

    public CSVDBManager(String csvPath, String[] header, char sep) {
        this(csvPath, header, sep, StandardCharsets.UTF_8);
    }

    public CSVDBManager(String csvPath, String[] header, char sep,
                        Charset encoding)
    {
        this.csvPath = csvPath;
        this.header = header;
        this.sep = sep;
        this.encoding = encoding;
    }

    public void addRow(String[] row) throws FileNotFoundException, IOException {
        List<String[]> data = getDB();
        data.remove(0);
        data.add(row);
        newDB(data);
    }

    public void removeRow(String toFindValue, int column)
        throws FileNotFoundException, IOException
    {
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
        } else {
            throw new ArrayIndexOutOfBoundsException(
                "No '" + toFindValue + "' found"
            );
        }
    }

    public void removeRow(String toFindValue, String columnName)
        throws FileNotFoundException, IOException
    {
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

    public List<String[]> getDB() throws FileNotFoundException, IOException{
        /**
         * Returns a table with the data of the CSV file
        **/
        List<String[]> data = new ArrayList<String[]>();
        CSVParser parser = new CSVParserBuilder().withSeparator(sep).build();
        CSVReader reader =
            new CSVReaderBuilder(new FileReader(csvPath))
                .withCSVParser(parser).build();
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            data.add(nextLine);
        }
        return data;
    }

    public void newDB(List<String[]> data)
        throws FileNotFoundException, IOException
    {
        Writer writer = Files.newBufferedWriter(Paths.get(csvPath),
                                                encoding);
        CSVWriter csvWriter = new CSVWriter(writer,
                                            sep,
                                            CSVWriter.NO_QUOTE_CHARACTER,
                                            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                                            CSVWriter.DEFAULT_LINE_END);
        csvWriter.writeNext(header);
        csvWriter.writeAll(data);
        csvWriter.flush();
        csvWriter.close();
    }

    public void printDB() throws FileNotFoundException, IOException {
        List<String[]> savedData = getDB();
        for (int i = 0; i < savedData.size(); i++) {
          System.out.println(savedData.get(i)[0] + "; " +
                             savedData.get(i)[1] + "; " +
                             savedData.get(i)[2]);
        }
    }

}
