import java.util.ArrayList;
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
	Map<String, value> symbol_table;
	ArrayList<ArrayList<Integer>> value = new ArrayList<ArrayList<Integer>>();
	Map<String, value> unknown_labels;
	int decl(String, int);
	int lab(string);
	int subr(int, string);
	int printi(int);
	int jmp(string);
	int jmpc(string);
	int cmpe();
	int cmplt();
	int cmpgt();
	int pushi(int);
	int popm(int);
	int popa(int);
	int popv(string);
	int peek(string, int);
	int poke(int, string);
	int swp();
	int add();
	int sub();
	int mul();
	int div();
	int parse(String);
	List<String> find_tokens(String);
	boolean is_alpha(String);
	// destination from slide 3 is passed in
	int compile(ArrayList<byte>);
}

int parse(String statement) {
	// Find operation and operands in current statement
	String[] tokens = find_tokens(statement); //(return statement.split( );)
	for(String str: tokens){
		System.out.println(str + "," );
	}
	String operation = tokens[0];
	switch(operation) {
		case("add"): 
			add(parts); break;
		case("pushi"):
			int value = tokens[1];
			pushi(value);
			break;
		case("decl"):
			if (tokens[2] == "int") {decl(tokens[1], INT);}
			//if (tokens[2] == "short") {decl(tokens[1], SHORT);}
			//if (tokens[2] == "float") {decl(tokens[1], FLOAT);}
			break;
		case("lab"):
			lab(tokens[1]);
			break;}
	}
	//cmpe, cmplt, cmpgt, swp, add, sub, mul, div (tokens.size() == 1)
	//lab, printi, jmp, jmpc, pushi, pushvi, popm, popv (tokens.size() == 2)
	//decl, subr, peek, poke (tokens.size() == 3)
	}
}

List<String> find_tokens(String);

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
}

int lab(string) {
	
}

int subr(int, string);
int printi(int);
int jmp(string);
int jmpc(string);
int cmpe();
int cmplt();
int cmpgt();

int pushi(int value){
	Integer[] bytes = Arrays.copyof(value, value.length);
	mem.add(PUSHI);
	mem.add(bytes[0]);
	mem.add(bytes[1]);
	mem.add(bytes[2]);
	mem.add(bytes[3]);
	pc += 5;

}

int popm(int);
int popa(int);
int popv(string);
int peek(string, int);
int poke(int, string);
int swp();
int add();
int sub();
int mul();
int div();




