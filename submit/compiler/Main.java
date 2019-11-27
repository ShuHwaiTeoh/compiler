import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.*;

public class Main{
   public static void main(String[ ] args) {
	   File filename = new File(args[0]);//add path
	   ArrayList<String> source = new ArrayList<String>();
	   ArrayList<Byte> destination = new ArrayList<Byte>();
	   //String statement;
	   BufferedReader reader;
	   byte[] data = {0};
	   int idx = 0;
	   
	   try{
		   reader = new BufferedReader(new FileReader(filename));
		   String line;
	   while((line = reader.readLine())!= null) {
//		   System.out.print(line);
		   source.add(line);
		   }
	   }
	   catch(IOException e){e.printStackTrace();}
	   Bytecode bc = new Bytecode(source);
	   
	   //ArrayList<Byte> destination = new ArrayList<Byte>();
	  // bc.compile(destination);
	   bc.compile();
	   System.out.println("end_compile" );
	   for (int i=0;i<bc.mem.size();i++) {
		   //System.out.println(bc.mem);
		   byte[] b = ByteBuffer.allocate(4).putInt(bc.mem.get(i)).array();
		   destination.add(b[3]);
		   //System.out.println(b[3]);
		   //System.out.print("check");
		   //data[idx++] = b[3];
		   //System.out.println(data[idx-1]);
		   //data[idx++] = (byte) (bc.mem.get(i));
	   }
	   System.out.println("finish_data_adding" );
	   try {
           OutputStream out = new FileOutputStream("output.smp"); 
           for (byte d: destination) out.write(d);
           //System.out.println("Successfully byte inserted"); 
           out.close(); 
       } 
       catch (Exception e) { 
           System.out.println("Exception: " + e); 
       } 
   }
}
