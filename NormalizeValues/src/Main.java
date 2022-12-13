import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;  
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main  {  
	
	public static void main(String[] args)   { 
		
		Scanner keyboard = new Scanner(System.in);
		String line = ""; 
		String[] data = null;
		String first = "";
		ArrayList<ArrayList<Double> > theList = new ArrayList<ArrayList<Double> >();
		ArrayList<String> classList = new ArrayList<>();
		boolean flag = true;
		
		System.out.print("Write the path for the csv file: ");
		String path = keyboard.nextLine();
		keyboard.close();
		
		//Read from csv
		try   {  
			BufferedReader br = new BufferedReader(new FileReader(path));  
			while ((line = br.readLine()) != null)  { 
				if(flag) {
					first = line;
					flag = false;
				}else {
					data = line.split(",");
					classList.add(data[data.length-1]);
					
					for(int i =0;i<data.length-1;i++) {
						theList.add(new ArrayList<Double>());
						theList.get(i).add(Double.parseDouble(data[i]));
					}
				}
			}  
			br.close();
		}   
		catch (IOException e)   {  
			e.printStackTrace();  
		}  
		
		//Calculate min-max and normalize value
		BigDecimal temp;
		for(int i=0;i<theList.size()/theList.get(0).size();i++) {
			Double max = Collections.max(theList.get(i));
			Double min = Collections.min(theList.get(i));
			for(int y=0;y<theList.get(i).size();y++) {
				temp = normalize(theList.get(i).get(y),max,min);
				theList.get(i).set(y, temp.doubleValue());
			}
		}
		
		//Save to csv file
		try (PrintWriter writer = new PrintWriter("normalized-file.csv")) {
		      StringBuilder sb = new StringBuilder();
		      sb.append(first);
		      sb.append('\n');
		      
		      for(int y=0;y<theList.get(0).size();y++) {
		    	  for(int i=0;i<theList.size()/theList.get(0).size();i++) {
		    		  sb.append(theList.get(i).get(y));
		    		  sb.append(',');
		    	  }
		    	  sb.append(classList.get(y));
		    	  sb.append('\n');
		      }
		      writer.write(sb.toString());
		      System.out.println("The normilized file is in the program's folder");

		} catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());
		}
	}  
	
	//Normalize the value according to the given mix and max
	public static BigDecimal normalize (double data, double max, double min) {
		BigDecimal a,b,c;
		a = BigDecimal.valueOf(data);
		b = BigDecimal.valueOf(max);
		c = BigDecimal.valueOf(min);
		
		return a.subtract(c).divide(b.subtract(c), 6, RoundingMode.HALF_UP);
	}
}  