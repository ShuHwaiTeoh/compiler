import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class Main{
   public static void main(String[ ] args) {
	   File filename = new File(args[0]);//add path
	   ArrayList<String> source = new ArrayList<String>();
	   String statement;
	   BufferedReader reader;
	   
	   try{
		   reader = new BufferedReader(new FileReader(filename));
		   String line;
	   while((line = reader.readLine())!= null) {
		   source.add(line);
		   }
	   }
	   catch(IOException e){e.printStackTrace();}
	   Bytecode bc = Bytecode(source);
	   
	   ArrayList<Byte> destination = new ArrayList<Byte>();
	   bc.compile(destination);
   }
}
