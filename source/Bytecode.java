import java.util.ArrayList;
import java.util.*;

public class Bytecode {
	public static final int HALT = 0;
	public static final int JMP = 36;
	public static final int JMPC = 40;
	public static final int PUSHI = 70;
	public static final int PUSHVI = 74;
	public static final int POPM = 76;
	public static final int POPA = 77;
	public static final int POPV = 80;
	public static final int PEEKI = 86;
	public static final int POKEI = 90;
	public static final int SWP = 94;
	public static final int ADD = 100;
	public static final int SUB = 104;
	public static final int MUL = 108;
	public static final int DIV = 112;
	public static final int CMPE = 132;
	public static final int CMPLT = 136;
	public static final int CMPGT = 140;
	public static final int PRINTI = 146;
	public static final int LABEL = -1;
	public static final int SHORT = 0;
	public static final int INT = 1;
	public static final int FLOAT = 2;
	public static final int RET = 48;
	//runtime stack pointer
	int code;
	// current line being compiled
	int sc = 0;
	// program counter
	int pc = -1;
	// number of local variables in a function
	int fo = -1;
	// lines
	ArrayList<String> source = new ArrayList<String>();
	// opcodes
	public ArrayList<Integer> mem = new ArrayList<Integer>();
	int[] value = {0,0};
	Map<String, int[]> symbol_table = new HashMap< String,int[]>();
	//ArrayList<ArrayList<Integer>> value = new ArrayList<ArrayList<Integer>>();
	Map<String, ArrayList<Integer>> unknown_labels = new HashMap< String,ArrayList<Integer>>();
	
	public Bytecode(ArrayList<String> s) {
		source = s;
	}
	
	int decl(String var, int type){
		String var_name = var;
		int[] v = {fo++, type};
		symbol_table.put(var_name, v);
		System.out.println("this_is_decl"+var_name+v[0]);
		// reserve a space in the stack for the variable by generating the following code
		pushi(0);
		return 0;
	}
	
	int lab(String lable) {
		if (unknown_labels.get(lable) == null) {
			
			int[] v = {pc+1, LABEL};
			String key = lable;
			symbol_table.put(key, v);
		}
		else {
			ArrayList<Integer> mem_place_to_change = unknown_labels.get(lable);
			int put_in_mem_value = pc+1;//// we add it without reason??
			for(int k :  mem_place_to_change) {
				byte[] result = new byte[4];
				System.out.println("###########################################");
				System.out.println("this_is_save_in_lab  "+mem_place_to_change);
				System.out.println("this_is_value_in_lab  "+put_in_mem_value);
				result[0] = (byte) (put_in_mem_value >> 24);
				result[1] = (byte) (put_in_mem_value >> 16);
				result[2] = (byte) (put_in_mem_value >> 8);
				result[3] = (byte) (put_in_mem_value /*>> 0*/);
				System.out.println("result b "+mem.get(k+1));
				System.out.println("result b "+mem.get(k+2));
				System.out.println("result b "+mem.get(k+3));
				System.out.println("result b "+mem.get(k+4));
				mem.set(k+1, (int)result[3]);
				mem.set(k+2, (int)result[2]);
				mem.set(k+3, (int)result[1]);
				mem.set(k+4, (int)result[0]);
				System.out.println("result a "+mem.get(k+1)+"  "+(int)result[3]);
				System.out.println("result a "+mem.get(k+2)+"  "+(int)result[2]);
				System.out.println("result a "+mem.get(k+3)+"  "+(int)result[1]);
				System.out.println("result a "+mem.get(k+4)+"  "+(int)result[0]);
				System.out.println("###########################################");
			}
		}
		return 0;
	}
	
	int subr(int argNum, String flabel) {
		String k = flabel;
		int[] v = {++fo, argNum};
		symbol_table.put(k, v);
		//System.out.println("subr: here");
		return 0;
	}
	
	int ret() {
		pushi(0);
		mem.add(POPA);
		mem.add(RET);
		pc = pc+2;
		return 0;
	}
	
	int printi(int literal) {
		System.out.println(literal);
		pushi(literal);
		mem.add(PRINTI);
		pc += 1;
		return 0;
	}
	int jmp(String label) {
		int[] p;
		pc++;
		System.out.println("jmp: "+label+" "+pc);
		p = symbol_table.get(label);
		if (p != null) {
			pushi(p[0]);
			mem.add(JMP);
//			pc++;
		}
		else {
			String label_name = label;
			if (unknown_labels.get(label_name)!=null) {
				unknown_labels.get(label_name).add(pc);
				unknown_labels.put(label_name, unknown_labels.get(label_name));
				pushi(0);
				mem.add(JMP);
			}
			else {
				ArrayList<Integer> arrlist = new ArrayList<Integer>();
				arrlist.add(pc);
				unknown_labels.put(label_name, arrlist);
				pushi(0);
				mem.add(JMP);
			}
			
//			pc++;
		}
		return 0;
	}
	int jmpc(String label) {
		int[] p;
		pc++;
		System.out.println("jmpc: "+label+" "+pc);
		p = symbol_table.get(label);
		if (p != null) {
			pushi(p[0]);
			mem.add(JMPC);
//			pc++;
		}
		else {
			String label_name = label;
			if (unknown_labels.get(label_name)!=null) {
				unknown_labels.get(label_name).add(pc);
				unknown_labels.put(label_name, unknown_labels.get(label_name));
				pushi(0);
				mem.add(JMPC);
			}
			else {
				ArrayList<Integer> arrlist = new ArrayList<Integer>();
				arrlist.add(pc);
				unknown_labels.put(label_name, arrlist);
				pushi(0);
				mem.add(JMPC);
			}
//			pc++;
		}
		return 0;
	}
	int cmpe() {
		mem.add(CMPE);
		pc++;
		return 0;
	}
	int cmplt() {
		mem.add(CMPLT);
		pc++;
		return 0;
	}
	int cmpgt() {
		mem.add(CMPGT);
		pc++;
		return 0;
	}
	
	int pushi(int v){
		byte[] result = new byte[4];
		
		result[0] = (byte) (v >> 24);
		result[1] = (byte) (v >> 16);
		result[2] = (byte) (v >> 8);
		result[3] = (byte) (v /*>> 0*/);
//		System.out.println(v+":"+result[0]+","+result[1]+","+result[2]+","+result[3]);
		mem.add(PUSHI);
		mem.add((int)result[3]);
		mem.add((int)result[2]);
		mem.add((int)result[1]);
		mem.add((int)result[0]);
		pc += 5;
		return 0;
	}
	
	int printv(String var) {
		int[] p;
		p = symbol_table.get(var);
		pushv(var);
		mem.add(PRINTI);
		pc++;
//		pushi(p[0]);
		return 0;
	}
	
	int pushv(String var) {
		int[] p;
		p = symbol_table.get(var);
		pushi(p[0]);
		mem.add(PUSHVI);
		pc++;
		return 0;
	}
	
	int popm(int val) {
		pushi(val);
		mem.add(POPM);
		pc++;
		return 0;
	}
	int popv(String label) {
		int[] p;
		System.out.println(label);
		p = symbol_table.get(label);
		pushi(p[0]);
		mem.add(POPV);
		pc++;
		return 0;
	}
	int peek(String var, int val) {
		int[] p;
		p = symbol_table.get(var);
		pushi(p[0]);
		pushi(val);
		mem.add(PEEKI);
		pc++;
		return 0;
	}
	int poke(String var, int val) {
		int[] p;
		System.out.println(var);
		p = symbol_table.get(var);
		System.out.println(p[0]);
		System.out.println(val);
		pushi(p[0]);
		pushi(val);
		mem.add(POKEI);
		pc++;
		return 0;	
	}
	int swp() {
		mem.add(SWP);
		pc++;
		return 0;
	}
	int add() {
		mem.add(ADD);
		pc++;
		return 0;
	}
	int sub() {
		mem.add(SUB);
		pc++;
		return 0;
	}
	int mul() {
		mem.add(MUL);
		pc++;
		return 0;
	}
	int div() {
		mem.add(DIV);
		pc++;
		return 0;
	}
	
	void parse(String statement) {
		// Find operation and operands in current statement
		String[] tokens = find_tokens(statement); //(return statement.split( );)
//		for(String str: tokens){
//			System.out.println(str + "," );
//		}
		String operation = tokens[0];
		System.out.println("token[0]:  "+tokens[0]+"    pc:  "+pc);
		switch(operation) {
			case("decl"):
				decl(tokens[1], INT);
				break;
			case("lab"):
				lab(tokens[1]);
				break;
			case("subr"):
				subr(Integer.parseInt(tokens[1]), tokens[2]);
				break;
			case("ret"):
				ret();
//				System.out.println("subr"+k);
				break;			
			case("printi"):
				printi(Integer.parseInt(tokens[1]));
				break;
			case("printv"):
				printv(tokens[1]);
				break;		
			case("jmp"):
				jmp(tokens[1]);
				break;	
			case("jmpc"):
				jmpc(tokens[1]);
				break;	
			case("cmpe"):
				cmpe();
				break;	
			case("cmplt"):
				cmplt();
				break;	
			case("cmpgt"):
				cmpgt();
				break;	
			case("pushi"):
				int v = Integer.parseInt(tokens[1]);
				pushi(v);
				break;		
			case("pushv"):
				pushv(tokens[1]);
				break;
			case("popm"):
				popm(Integer.parseInt(tokens[1]));
				break;
			case("popv"):
				popv(tokens[1]);
				break;
			case("peek"):
				peek(tokens[1], Integer.parseInt(tokens[2]));
				break;
			case("poke"):
				poke(tokens[2], Integer.parseInt(tokens[1]));
				break;
			case("swp"): 
				swp();
				break;
			case("add"): 
				add();
				break;
			case("sub"): 
				sub();
				break;
			case("mul"): 
				mul();
				break;
			case("div"): 
				div();
				break;
		}
	}
	
	String[] find_tokens(String statement){
		String[] tokens = statement.split(" ");
		return tokens;
	}
	
	int compile() {
		int i = 0;
		pushi(16);
		pushi(17);
		pushi(1);
		mem.add(44);
		mem.add(0);
		pc=pc+2;
//		System.out.println(source);
		for (String str : source){
			sc = i + 1;
//			System.out.println("grep:"+str+"\n");
//			System.out.println("checkif:"+!str.isEmpty()+"\n");
			if (!str.isEmpty() && str.contains("//")==false) {
//				System.out.println("check:"+str+"\n");
				parse(str);
			}
		}
		return 0;
	}
	

}




