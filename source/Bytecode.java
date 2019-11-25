import java.util.ArrayList;

import com.sun.jdi.Value;

import java.util.*;
import javafx.util.Pair;

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
	ArrayList<Integer> mem = new ArrayList<Integer>();
	Pair <Integer, Integer> value = new Pair<Integer, Integer>(0,0);
	Map<String, Value> symbol_table;
	ArrayList<ArrayList<Integer>> value = new ArrayList<ArrayList<Integer>>();
	Map<String, value> unknown_labels;
	int decl(String, int);
	int lab(String);
	int subr(int, String);
	int ret();
	int printi(int);
	int printv(String);
	int jmp(String);
	int jmpc(String);
	int cmpe();
	int cmplt();
	int cmpgt();
	int pushi(int);
	int pushvi(String);
	int popm(int);
	int popa(int);
	int popv(String);
	int peek(String, int);
	int poke(int, String);
	int swp();
	int add();
	int sub();
	int mul();
	int div();
	int parse(String);
	String[] find_tokens(String);
	boolean is_alpha(String);
	// destination from slide 3 is passed in
	int compile(ArrayList<byte>);
}

int parse(String statement) {
	// Find operation and operands in current statement
	String[] tokens = find_tokens(statement); //(return statement.split( );)
	for(String str: tokens){
		System.out.println(str + "," );
		String operation = tokens[0];
		switch(operation) {
			case("decl"):
				if (tokens[2] == "int") decl(tokens[1], INT);
				//if (tokens[2] == "short") {decl(tokens[1], SHORT);}
				//if (tokens[2] == "float") {decl(tokens[1], FLOAT);}
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
				int value = Integer.parseInt(tokens[1]);
				pushi(value);
				break;		
			case("pushvi"):
				pushvi(tokens[1]);
				break;
			case("popm"):
				popm(Integer.parseInt(tokens[1]));
				break;
			case("popv"):
				pushvi(tokens[1]);
				break;
			case("peek"):
				peek(tokens[1], Integer.parseInt(tokens[2]));
				break;
			case("popv"):
				pushvi(tokens[1], Integer.parseInt(tokens[2]));
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
	//cmpe, cmplt, cmpgt, swp, add, sub, mul, div (tokens.size() == 1)
	//lab, printi, jmp, jmpc, pushi, pushvi, popm, popv (tokens.size() == 2)
	//decl, subr, peek, poke (tokens.size() == 3)
	}
}

String[] find_tokens(String statement){
	String[] tokens = statement.split(" ");
	return tokens;
}

boolean is_alpha(String);

int compile(ArrayList<byte> des) {
	int i = 0;
	for (String str : source){
		sc = i + 1;
		parse(str);
		// Check that source code ends with RET
		// save mem into des (destination in slide 3 -> compile result saved in .smp file)
		des = mem;
	}
}

int decl(String var, int type){
	String var_name = "main" + "_" + var;
	int[] value = {++fo, type};
	symbol_table.put(var_name, value);
	// reserve a space in the stack for the variable by generating the following code
	pushi(0);
	//if(type == "SHORT") pushs(0);
	//if(type == "FLOAT") pushf(0);
	return 0;
}

int lab(String lable) {
	String key = "main" + "_" + lable;
	int[] value = {++fo, LABEL};
	symbol_table.put(key, value);
	return 0;
}

int subr(int argNum, String flabel) {
	String key = "main" + "_" + flabel;
	int[] value = {++fo, argNum};
	symbol_table.put(key, value);
	return 0;
}

int ret() {
	pushi(0);
	popa(0);
	return 0;
}

int printi(int literal) {
	//Integer [] bytes = Arrays.copyof(literal, 4);
	mem.add(PRINTI);
//	mem.add(bytes[0]);
//	mem.add(bytes[1]);
//	mem.add(bytes[2]);
//	mem.add(bytes[3]);
	pc += 1;
}
int jmp(String label) {
	int p;
	p = symbol_table.get(label);
	pushi(p.getKey());
	mem.add(JMP);
	pc++;
}
int jmpc(String label) {
	int p;
	p = symbol_table.get(label);
	pushi(p.getKey());
	mem.add(JMPC);
	pc++;
}
int cmpe() {
	mem.add(CMPE);
	pc++;
}
int cmplt() {
	mem.add(CMPLT);
	pc++;
}
int cmpgt() {
	mem.add(CMPGT);
	pc++;
}

int pushi(int value){
	Integer[] bytes = Arrays.copyof(value, value.length);
	mem.add(PUSHI);
	mem.add(bytes[0]);
	mem.add(bytes[1]);
	mem.add(bytes[2]);
	mem.add(bytes[3]);
	pc += 5;
	return 0;
}

int printv(String var) {
	int p;
	p = symbol_table.get(var);
	pushi(p.getKey());
	pushv(p.getValue());
	//mem.add(PRINTV);
	return 0;
}

int pushvi(String var) {
	int p;
	p = symbol_table.get(var);
	pushi(p.getKey());
	mem.add(PUSHVI);
	pc++;
}

int popm(int val) {
	pushi(val);
	mem.add(POPM);
	pc++;
}
int popa(int);
int popv(String label) {
	int p;
	p = symbol_table.get(label);
	pushi(p.getKey());
	mem.add(POPV);
	pc++;
}
int peek(String var, int val) {
	int p;
	p = symbol_table.get(var);
	pushi(p.getKey());
	pushi(val);
	mem.add(PEEKI);
	pc++;
	return 0;
}
int poke(String var, int val) {
	int p;
	p = symbol_table.get(var);
	pushi(p.getKey());
	pushi(val);
	mem.add(POKEI);
	pc++;
	return 0;	
}
int swp() {
	mem.add(SWP);
	pc++;
}
int add() {
	mem.add(ADD);
	pc++;
}
int sub() {
	mem.add(SUB);
	pc++;
}
int mul() {
	mem.add(MUL);
	pc++;
}
int div() {
	mem.add(DIV);
	pc++;
}




