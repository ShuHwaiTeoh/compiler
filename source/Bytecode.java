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
	//bytecode to  write
	//int code;
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
	Map<String, int[]> symbol_table;
	//ArrayList<ArrayList<Integer>> value = new ArrayList<ArrayList<Integer>>();
	Map<String, Integer> unknown_labels;
	
	public Bytecode(ArrayList<String> s) {
		source = s;
	}
	
	int decl(String var, int type){
		String var_name = "main" + "_" + var;
		int[] v = {++fo, type};
		symbol_table.put(var_name, v);
		// reserve a space in the stack for the variable by generating the following code
		pushi(0);
		return 0;
	}
	
	int lab(String lable) {
		int[] v = {++fo, LABEL};
		if (unknown_labels.get(lable) == null) {
			String key = "main" + "_" + lable;
			symbol_table.put(key, v);
		}
		else {
			symbol_table.put(lable, v);
		}
		return 0;
	}
	
	int subr(int argNum, String flabel) {
		String k = "main" + "_" + flabel;
		int[] v = {++fo, argNum};
		symbol_table.put(k, v);
		return 0;
	}
	
	int ret() {
		pushi(0);
		mem.add(POPA);
		mem.add(HALT);
		pc = pc+2;
		return 0;
	}
	
	int printi(int literal) {
		pushi(0);
		mem.add(PRINTI);
		pc += 1;
		return 0;
	}
	int jmp(String label) {
		int[] p;
		p = symbol_table.get(label);
		if (p != null) {
			pushi(p[0]);
			mem.add(JMP);
			pc++;
		}
		else {
			String label_name = "main" + "_" + label;
			unknown_labels.put(label_name, 0);
		}
		return 0;
	}
	int jmpc(String label) {
		int[] p;
		p = symbol_table.get(label);
		if (p != null) {
			pushi(p[0]);
			mem.add(JMP);
			pc++;
		}
		else {
			String label_name = "main" + "_" + label;
			unknown_labels.put(label_name, 0);
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
		mem.add(PUSHI);
		mem.add((int)result[0]);
		mem.add((int)result[1]);
		mem.add((int)result[2]);
		mem.add((int)result[3]);
		pc += 5;
		return 0;
	}
	
	int printv(String var) {
		int[] p;
		p = symbol_table.get(var);
		pushi(p[0]);
		pushvi(var);
		//mem.add(PRINTV);
		return 0;
	}
	
	int pushvi(String var) {
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
		p = symbol_table.get(var);
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
		switch(operation) {
			case("decl"):
				if (tokens[2] == "int") decl(tokens[1], INT);
				break;
			case("lab"):
				lab(tokens[1]);
				break;
			case("subr"):
				subr(Integer.parseInt(tokens[1]), "main");
				break;
			case("ret"):
				ret();
				break;			
			case("printi"):
				printi(0);
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
			case("pushvi"):
				pushvi(tokens[1]);
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
				poke(tokens[1], Integer.parseInt(tokens[2]));
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
		for (String str : source){
			sc = i + 1;
			parse(str);
			// Check that source code ends with RET
			// save mem into des (destination in slide 3 -> compile result saved in .smp file)
			//des = (byte)mem;
		}
		return 0;
	}
	

}




