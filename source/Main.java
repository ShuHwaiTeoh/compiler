import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Main{
   public static void main(String[ ] args) {
	   File filename = new File(args[0]);//add path
	   ArrayList<String> source = new ArrayList<String>();
	   //String statement;
	   BufferedReader reader;
	   byte[] data = {0};
	   int idx = 0;
	   
	   try{
		   reader = new BufferedReader(new FileReader(filename));
		   String line;
	   while((line = reader.readLine())!= null) {
		   source.add(line);
		   }
	   }
	   catch(IOException e){e.printStackTrace();}
	   Bytecode bc = new Bytecode(source);
	   
	   //ArrayList<Byte> destination = new ArrayList<Byte>();
	  // bc.compile(destination);
	   bc.compile();
	   for (int i=0;i<bc.mem.size();i++) {
		   data[idx++] = (byte) bc.mem.indexOf(i);
	   }

	   try {
           OutputStream out = new FileOutputStream("output.smp"); 
           out.write(data); 
           System.out.println("Successfully byte inserted"); 
           out.close(); 
       } 
       catch (Exception e) { 
           System.out.println("Exception: " + e); 
       } 
   }
}
