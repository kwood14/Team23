//package converter; // NetBeans IDE

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Converter. Converts CSV files to automatically organized output.
 * @author Phillip N, Emilio E, Kai W
 * @version 20.October.2018
 */
public class Converter
{

    /**
     * main. Entry point into the program.
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        
        NEWPEKGUI gui = new NEWPEKGUI();
        
        gui.setVisible(true);
        
        while(!gui.getReady()) {}
        
        File donorFile = gui.donorF, doneeFile = gui.doneeF;
        
        final String home = System.getProperty("user.home");
        final String folderLocation = home + File.separator + "Documents" + File.separator + "NEWPEK";
        final String fileName = donorFile.getName();
        final String fileLocation = folderLocation + File.separator + fileName;
        
        String[] attributeArr = new String[25];
        ArrayList<Donor> donorList = new ArrayList<>();
        
        String[] doneeAttributeArr = new String[18];
        ArrayList<Donee> doneeList = new ArrayList<>();
        
        System.out.println(fileLocation);
        
        File prevFile = new File("");
        
        try
        {
         
            try (Scanner inputStream = new Scanner(donorFile))
            {
                
                inputStream.useDelimiter("\\n");
                
                inputStream.next();
                
                // while data exists to be read
                while(inputStream.hasNext())
                {
                    
                    List<String> data = parseLine(inputStream.next());
                    
                    //System.out.println("Index: " + index + "\nData: " + data);
                    
                    for(int i = 0 ; i < attributeArr.length ; i++)
                    {
                        
                        attributeArr[i] = (data.get(i)).replace("\"","");
                        
                    }
                    
                    donorList.add(new Donor(attributeArr));
                    
                }
            }
           
            try (Scanner inputStream = new Scanner(doneeFile))
            {
                
                inputStream.useDelimiter("\\n");
                
                inputStream.next();
                
                // while data exists to be read
                while(inputStream.hasNext())
                {
                    
                    List<String> data = parseLine(inputStream.next());
                    
                    //System.out.println("Index: " + index + "\nData: " + data);
                    
                    for(int i = 0 ; i < doneeAttributeArr.length ; i++)
                    {
                        
                        doneeAttributeArr[i] = (data.get(i)).replace("\"","");
                        
                    }
                    
                    doneeList.add(new Donee(doneeAttributeArr));
                    
                }
            }
            
            String prevFileName = folderLocation + File.separator + donorList.get(0).getMonthYear(true) + "_" + donorList.get(donorList.size() - 1).getMonthYear(true) + ".txt";
            prevFile = new File(prevFileName);
            
        }
        catch (FileNotFoundException e)
        {
                    
            System.out.println("File not found.");
            //e.printStackTrace();
        
        }
        finally
        {
            
            txtMaker textFile = new txtMaker(donorList, doneeList, prevFile);
            
            if(!gui.checkPrevFile(prevFile)) {
                
                textFile.setStart(gui.start);
                
            }
            
            gui.changeLabel(textFile.saveTXT(folderLocation, donorList));
            
            while(!gui.getCharts()) {}
            
            textFile.sourcePieChart(donorList,folderLocation);
            textFile.wastePieChart(donorList,folderLocation);
        }
    
    }
    
    public String sort(ArrayList<Donor> donorList) {
        
        String sortedString = "";
        
        
        
        return sortedString;
        
    }
    
    
    /**
     * parseLine. Unedited source code taken from 
     * https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
     * 
     * @param inputLine, a row of elements from an CSV file.
     * @return result, a list composed of elements, where an element are the
     * group of characters within the '"' symbol.
     */
    public static List<String> parseLine(String inputLine) {

        final char DEFAULT_QUOTE = '"', DEFAULT_SEPARATOR = ',';
        List<String> result = new ArrayList<>();

        //if empty, return empty
        if (inputLine == null && inputLine.isEmpty()) {
            return result;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = inputLine.toCharArray();

        OUTER:
        for (char ch : chars) {
            if (inQuotes) {
                startCollectChar = true;
                if (ch == DEFAULT_QUOTE) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                switch (ch) {
                    case DEFAULT_QUOTE:
                        inQuotes = true;
                        //Fixed : allow "" in empty quote enclosed
                        if (chars[0] != '"' && DEFAULT_QUOTE == '\"') {
                            curVal.append('"');
                        }   //double quotes in column will hit this!
                        if (startCollectChar) {
                            curVal.append('"');
                        }   break;
                    case DEFAULT_SEPARATOR:
                        result.add(curVal.toString());
                        curVal = new StringBuffer();
                        startCollectChar = false;
                        break;
                //ignore LF characters
                    case '\r':
                        break;
                    case '\n':
                        //the end, break!
                        break OUTER;
                    default:
                        curVal.append(ch);
                        break;
                }
            }
        }

        result.add(curVal.toString());

        return result;
    }
    
}
