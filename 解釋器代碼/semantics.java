package homework1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class semantics {
	private HashMap<String,Symbol> table=new HashMap<String,Symbol>();
	private treeNode temp_node=null;
	private treeNode treeRoot=null;
	private List<HashMap> table_array=new ArrayList<HashMap>();
	private Symbol temp_symbol=null;
	semantics(treeNode _tree){
		treeRoot=_tree;
		run();
	}
	private void printTable() {
		 System.out.println("--------------------------符號表---------------------------");
		 for (int i=0;i<table_array.size();i++) {
				HashMap<String,Symbol> temp=table_array.get(i);
				Set<String> keys=temp.keySet();
				 for(String key:keys){
					 Symbol value=temp.get(key);
					 System.out.println("Lexeme:"+value.getLexme()+"      tokenClass:"+value.getTokenClass()+"      type:"+value.getType()+"      value:"+value.getValue()+"     array:"+value.getArray());
				 }
		}
		 System.out.println("-----------------------------------------------------------");
	}
	//运行
	void run() {
		table_array.add(table);
		for (int i=0;i<treeRoot.getChildSize();i++) {
			temp_node=treeRoot.getChild(i);
			analysis();
			}
		System.out.println("語義分析完成");
	}
	//查找有該变量的符號表位置
	private int searchVariableTableIndex(String Lexeme) 
	{
		for (int i=table_array.size()-1;i>-1;i--) {
			HashMap<String,Symbol> temp=table_array.get(i);
			if (temp.containsKey(Lexeme)!=false) {
				return i;
			}
		}
		return -1;
	}
	
	//分析
	private void analysis() {
		switch(temp_node.getToken().getSyn()) {
		//聲明
		case 1:
		case 2:
		case 3:
			declare();
			break;
		//for
		case 4:
			for_stat();
			break;
		//if
		case 5:
			if_stat();
			break;
		//while
		case 6:
			while_stat();
			break;
		//print
		case 10:
			print_stat();
			break;
		//scan
		case 11:
			read_stat();
			break;
		//{
		case 35:
			treeNode temp=temp_node;
			table=new HashMap<String,Symbol>();
			table_array.add(table);
			for (int i=0;i<temp.getChildSize();i++) {
				temp_node=temp.getChild(i);
				analysis();
			}
			break;
		//}
		case 36:
			table_array.remove(table_array.size()-1);
			table=table_array.get(table_array.size()-1);
			break;
		//賦值
		case 20:
			assign();
			break;
		default:
			System.out.println("語義分析錯誤");
			System.exit(1);
			break;
		}  
	}
	//數組賦值
	private void array_assign() {
		if (searchVariableTableIndex(temp_node.getChild(0).toString())==-1) {
			System.out.println("未聲明变量"+temp_node.getChild(0).toString());
			System.exit(1);
		}else {
			int tableIndex=searchVariableTableIndex(temp_node.getChild(0).toString());
			HashMap<String,Symbol> temp_table=table_array.get(tableIndex);
			List<Integer> array_dimension=new ArrayList<Integer>();
			Symbol var=temp_table.get(temp_node.getChild(0).toString());
			treeNode temp_root=temp_node;
			treeNode temp_treeNode= temp_root.getChild(0);
			
			int temp=1;
			for (int j=0;j<temp_treeNode.getChildSize();j++) {
				temp_node=temp_treeNode.getChild(j);
				stmt();
				temp=temp*Integer.parseInt(temp_symbol.getLexme());
				
				array_dimension.add(Integer.parseInt(temp_symbol.getLexme()));
			}
			temp_node= temp_root.getChild(1);
			//是否多數組賦值
			if (temp_node.getToken().getSyn()==25) {
				List<Object> temp_list=new ArrayList<Object>();
				Stack<treeNode> temp_stack=new Stack<treeNode>();
				treeNode loop_temp_treeNode=null;
				temp_stack.push(temp_node);
				
				while(!temp_stack.isEmpty()) {
					loop_temp_treeNode=temp_stack.pop();
					if (loop_temp_treeNode.getToken().getSyn()==25) {
						temp_stack.push(loop_temp_treeNode.getChild(1));
						temp_stack.push(loop_temp_treeNode.getChild(0));
						continue;
					}else {
						switch(var.getType()) {
						case "int":
							try {
								
									temp_list.add(Integer.parseInt(loop_temp_treeNode.toString()));
								}
							catch(NumberFormatException e) {
								System.out.print(e);
								System.out.print("數組賦值类型錯誤");
								System.exit(1);
							}
							break;
						case "char":
						
								temp_list.add(loop_temp_treeNode.toString());
							break;
						case "float":
							try {
								temp_list.add(Float.parseFloat(loop_temp_treeNode.toString()));
							}catch(NumberFormatException e) {
								System.out.print(e);
								System.out.print("數組賦值类型錯誤");
								System.exit(1);
							}
							break;
						}
					}
				}

				if (var.getArraySize()!=temp_list.size()) {
					System.out.print("數組賦值个數錯誤");
					System.exit(1);
				}else {
					var.setArray(temp_list);
					System.out.print(var.getLexme()+"="+var.getArray());
					System.out.println();
					temp_table.put(var.getLexme(),var);
					table_array.remove(tableIndex);
					table_array.add(tableIndex, temp_table);
				}
				
			}else {
				
				stmt();
				switch(var.getType()) {
				case "int":
					try {
						var.setArray(Integer.parseInt(temp_symbol.getLexme()), array_dimension);
					}catch(Exception e){
						System.out.print(e);
						System.out.print("數組賦值类型錯誤");
						System.exit(1);
					}
					break;
				case "char":
					var.setArray(temp_symbol.getLexme(), array_dimension);
					break;
				case "float":
					try {
					var.setArray(Float.parseFloat(temp_symbol.getLexme()), array_dimension);
					}catch(Exception e){
						System.out.print(e);
						System.out.print("數組賦值类型錯誤");
						System.exit(1);
					}
					break;
				}
				System.out.print(var.getLexme()+"="+var.getArray());
				System.out.println();
				temp_table.put(var.getLexme(),var);
				table_array.remove(tableIndex);
				table_array.add(tableIndex, temp_table);
				}
			}
		}
	
	
	
	// 聲明語句
	private void declare() {
		treeNode temp_root=temp_node;
		String type=temp_root.toString();
		for (int i=0;i<temp_root.getChildSize();i++) {
			treeNode temp=temp_root.getChild(i);
			if (searchVariableTableIndex(temp.toString())!=-1) {
				System.out.println("已聲明变量");
				System.exit(1);
			}else {
				//变量有初值
				if (temp.getToken().getSyn()==20) {
					Symbol temp_var=new Symbol(temp.getChild(0).toString(),"id");
					temp_var.setType(type);
					//变量不是數組
					if (temp.getChild(0).getChildSize()==0) {
						temp_node=temp;
						table.put(temp.getChild(0).toString(),temp_var);
						table_array.remove(table_array.size()-1);
						table_array.add(table);
						System.out.println("------------------");
						System.out.println("聲明变量"+temp.getChild(0).toString());
						assign();
					//变量是數組	
					}else {
						treeNode temp_treeNode=temp.getChild(0);
						for (int j=0;j<temp_treeNode.getChildSize();j++) {
							temp_node=temp_treeNode.getChild(j);
							stmt();
							System.out.println(temp_symbol.getLexme());
							temp_var.setArrayDimension(Integer.parseInt(temp_symbol.getLexme()));
						}
						temp_var.initArray();
						temp_node=temp;
						table.put(temp.getChild(0).toString(),temp_var);
						table_array.remove(table_array.size()-1);
						table_array.add(table);
						System.out.println("------------------");
						System.out.println("聲明变量"+temp.getChild(0).toString());
						array_assign();
						printTable();
					}
				//变量沒有初值
				}else {
					Symbol temp_var=new Symbol(temp.toString(),"id");
					temp_var.setType(type);
					if (temp.getChildSize()==0) {
						table.put(temp.toString(),temp_var);
						System.out.println("------------------");
						System.out.println("聲明变量"+temp.toString());
						printTable();
					}else {
						treeNode temp_treeNode=temp;
						for (int j=0;j<temp_treeNode.getChildSize();j++) {
							temp_node=temp_treeNode.getChild(j);
							stmt();
							temp_var.setArrayDimension(Integer.parseInt(temp_symbol.getLexme()));
						}
						temp_var.initArray();
						table.put(temp_var.getLexme(),temp_var);
						table_array.remove(table_array.size()-1);
						table_array.add(table);
						System.out.println("------------------");
						System.out.println("聲明变量"+temp.toString());
						printTable();
					}
				}
			}
		}
	}
	//变量賦值
	private void var_assign() {
		int tableIndex=searchVariableTableIndex(temp_node.getChild(0).toString());
		HashMap<String,Symbol> temp_table=table_array.get(tableIndex);
		Symbol var=temp_table.get(temp_node.getChild(0).toString());
		temp_node=temp_node.getChild(1);
		stmt();
		if (var.getType().contains("real")&&temp_symbol.getTokenClass().contains("int")) {
			try {
				Float.parseFloat(temp_symbol.getLexme());
				var.setValue(temp_symbol.getLexme());
				System.out.println();
				temp_table.put(var.getLexme(),var);
				table_array.remove(tableIndex);
				table_array.add(tableIndex, temp_table);
				printTable();
			}catch(NumberFormatException e) {}
		}else if (var.getType().contains(temp_symbol.getTokenClass())) {
			switch(var.getType()) {
			case "char":
				var.setValue(temp_symbol.getLexme());
				table_array.remove(tableIndex);
				table_array.add(tableIndex, temp_table);
				System.out.print(temp_symbol.getLexme());
				System.out.println();
				printTable();
				break;
			case "int":
				try {
					Integer.parseInt(temp_symbol.getLexme());
					var.setValue(temp_symbol.getLexme());;
					System.out.println();
					temp_table.put(var.getLexme(),var);
					table_array.remove(tableIndex);
					table_array.add(tableIndex, temp_table);
					printTable();
				}catch(NumberFormatException e) {
					Long temp_long=Long.parseLong(temp_symbol.getLexme());
					int temp_int=temp_long.intValue();
					var.setValue(String.valueOf(temp_int));
					System.out.println();
					temp_table.put(var.getLexme(),var);
					table_array.remove(tableIndex);
					table_array.add(tableIndex, temp_table);
					printTable();
				}
				break;
			case "real":
				var.setValue(temp_symbol.getLexme());
				temp_table.put(var.getLexme(),var);
				table_array.remove(tableIndex);
				table_array.add(tableIndex, temp_table);
				System.out.println();
				printTable();
				break;
			}
			
		}else {
			System.out.println("变量"+var.getLexme()+"與賦值类型不一致");
			System.exit(1);
		}
	}
	//賦值
	private void assign() {
		if (searchVariableTableIndex(temp_node.getChild(0).toString())==-1) {
			System.out.println("未聲明变量"+temp_node.getChild(0).toString());
			System.exit(1);
		}else {
			int tableIndex=searchVariableTableIndex(temp_node.getChild(0).toString());
			HashMap<String,Symbol> temp_table=table_array.get(tableIndex);
			Symbol var=temp_table.get(temp_node.getChild(0).toString());
			if (var.isArray()) {
				array_assign();
			}else {
				var_assign();
			}
		}
	}
	//計算
	private void calculate() {
		treeNode temp=temp_node;
		Symbol symbol1=null;
		Symbol symbol2=null;
		if (temp.getChildSize()==2) {
			temp_node=temp.getChild(0);
			stmt();
			symbol1=temp_symbol;
			temp_node=temp.getChild(1);
			stmt();
			symbol2=temp_symbol;
		}else {
			symbol1=new Symbol("0","int");
			temp_node=temp.getChild(0);
			stmt();
			symbol2=temp_symbol;
		}
		switch (temp.getToken().getSyn()) {
			case 12:
				try{
					int value_1=Integer.parseInt(symbol1.getLexme());
					int value_2=Integer.parseInt(symbol2.getLexme());
					int value=(value_1+value_2);
					temp_symbol=new Symbol(String.valueOf(value),"int");
				}catch(NumberFormatException e) {
				try{
					float value_1=Float.parseFloat(symbol1.getLexme());
					float value_2=Float.parseFloat(symbol2.getLexme());
					temp_symbol=new Symbol(String.valueOf(value_1+value_2),"real");
				}catch(NumberFormatException e1) {
					temp_symbol=new Symbol(temp.getChild(0).toString()+temp_symbol.getLexme(),"char");
				}
				}
				break;
			case 13:
				try{
					int value_1=Integer.parseInt(symbol1.getLexme());
					int value_2=Integer.parseInt(symbol2.getLexme());
					int value=(value_1-value_2);
					temp_symbol=new Symbol(String.valueOf(value),"int");
				}catch(NumberFormatException e) {
				try{
					
					float value_1=Float.parseFloat(symbol1.getLexme());
					float value_2=Float.parseFloat(symbol2.getLexme());
					temp_symbol=new Symbol(String.valueOf(value_1-value_2),"real");
				}catch(NumberFormatException e1) {
					temp_symbol=new Symbol(temp.getChild(0).toString().replaceAll(temp_symbol.getLexme(), ""),"char");
				}
				}
				break;
			case 14:
				try{
					
					int value_1=Integer.parseInt(symbol1.getLexme());
					int value_2=Integer.parseInt(symbol2.getLexme());
					int value=(value_1*value_2);
					temp_symbol=new Symbol(String.valueOf(value),"int");
				}catch(NumberFormatException e) {
				try{
					
					float value_1=Float.parseFloat(symbol1.getLexme());
					float value_2=Float.parseFloat(symbol2.getLexme());
					temp_symbol=new Symbol(String.valueOf(value_1*value_2),"real");
				}catch(NumberFormatException e1) {
					System.out.println(e1);
					System.exit(1);
				}
				}
				break;
			case 15:
				try{
					int value_1=Integer.parseInt(symbol1.getLexme());
					int value_2=Integer.parseInt(symbol2.getLexme());
					if (value_2==0) {
						System.out.println("除數為0錯誤");
						System.exit(1);
					}
					int value=value_1/value_2;
					temp_symbol=new Symbol(String.valueOf(value),"int");
				}catch(NumberFormatException e) {
				try{
					
					float value_1=Float.parseFloat(symbol1.getLexme());
					float value_2=Float.parseFloat(symbol2.getLexme());
					if (value_2==0) {
						System.out.println("除數為0錯誤");
						System.exit(1);
					}
					temp_symbol=new Symbol(String.valueOf(value_1/value_2),"real");
				}catch(NumberFormatException e1) {
					System.out.println(e1);
					System.exit(1);
					}
				}
				break;
			case 32:
				try{
					int value_1=Integer.parseInt(symbol1.getLexme());
					int value_2=Integer.parseInt(symbol2.getLexme());
					if (value_2==0) {
						System.out.println("除數為0錯誤");
						System.exit(1);
					}
					int value=(value_1%value_2);
					temp_symbol=new Symbol(String.valueOf(value),"int");
				}catch(NumberFormatException e) {
				try{
					
					float value_1=Float.parseFloat(symbol1.getLexme());
					float value_2=Float.parseFloat(symbol2.getLexme());
					if (value_2==0) {
						System.out.println("除數為0錯誤");
						System.exit(1);
					}
					temp_symbol=new Symbol(String.valueOf(value_1%value_2),"real");
				}catch(NumberFormatException e1) {
					System.out.println(e1);
					System.exit(1);
					}
				}
				break;
		}
		}
	//逻輯运算
	private void logical_expr() {
		treeNode temp=temp_node;
		temp_node=temp.getChild(0);
		stmt();
		Symbol symbol1=temp_symbol;
		temp_node=temp.getChild(1);
		stmt();
		Symbol symbol2=temp_symbol;
		try{
			Float.parseFloat(symbol1.getLexme());
			Float.parseFloat(symbol2.getLexme());
		}catch(NumberFormatException e1) {
			System.out.println("邏輯表达式出錯");
			System.exit(1);
			
		}
		float value_1=Float.parseFloat(symbol1.getLexme());
		float value_2=Float.parseFloat(symbol2.getLexme());
		int temp_bool=0;
		switch (temp.getToken().getSyn()) {
			case 16:
					if (value_1<value_2) {
						temp_bool=1;
					}
					temp_symbol=new Symbol(String.valueOf(temp_bool),"real");
				break;
			case 17:
					if (value_1<=value_2) {
						temp_bool=1;
					}
					temp_symbol=new Symbol(String.valueOf(temp_bool),"int");
				break;
			case 18:
				if (value_1>value_2) {
					temp_bool=1;
				}
				temp_symbol=new Symbol(String.valueOf(temp_bool),"int");
				break;
			case 19:
				if (value_1>=value_2) {
					temp_bool=1;
				}
				temp_symbol=new Symbol(String.valueOf(temp_bool),"int");
				break;
			case 21:
				if (value_1==value_2) {
					temp_bool=1;
				}
				temp_symbol=new Symbol(String.valueOf(temp_bool),"int");
				break;
			case 29:
				if (value_1!=0&&value_2!=0) {
					temp_bool=1;
				}
				temp_symbol=new Symbol(String.valueOf(temp_bool),"int");
				break;
			case 31:
				if (value_1==1||value_2==1) {
					temp_bool=1;
				}
				temp_symbol=new Symbol(String.valueOf(temp_bool),"int");
				break;
			case 38:
				if (value_1!=value_2) {
					temp_bool=1;
				}
				temp_symbol=new Symbol(String.valueOf(temp_bool),"int");
				break;
		}
	}
	//表达式
	private void stmt() {
		switch (temp_node.getToken().getSyn()) {
		case 11:
			read_stat();
			break;
		case 12:
		case 13:
		case 14:
		case 15:
		case 32:
			calculate();
			break;
		case 16:
		case 17:
		case 18:
		case 19:
		case 21:
		case 28:
		case 29:
		case 30:
		case 31:
		case 38:
			logical_expr();
			break;
		case 97:
			temp_symbol=new Symbol(temp_node.toString(),"char");
			break;
		case 98:
			temp_symbol=new Symbol(temp_node.toString(),"real");
			break;
		case 99:
			temp_symbol=new Symbol(temp_node.toString(),"int");
			break;
		case 100:
			if (searchVariableTableIndex(temp_node.toString())==-1) {
				System.out.println("未聲明变量"+temp_node.toString());
				System.exit(1);
			}else {
				int tableIndex=searchVariableTableIndex(temp_node.toString());
				HashMap<String,Symbol> temp_table=table_array.get(tableIndex);
				Symbol var=temp_table.get(temp_node.toString());
				if (var.isArray()) {
					List<Integer> array_dimension=new ArrayList<Integer>();
					treeNode temp=temp_node;
					for (int j=0;j<temp.getChildSize();j++) {
						temp_node=temp.getChild(j);
						stmt();
						array_dimension.add(Integer.parseInt(temp_symbol.getLexme()));
					}
					temp_symbol=new Symbol(var.getArray(array_dimension),var.getType());
				}else {
					temp_symbol=new Symbol(var.getValue(),var.getType());
				}
			}
			break;
		}
		
	}
	//print
	private void print_stat() {
		temp_node=temp_node.getChild(0);
		//System.out.println("------------------");
		stmt();
		System.out.println(temp_symbol.getLexme());
		//printTable();
	}
	//read
	private void read_stat() {
		System.out.print("輸入:");
		Scanner sc = new Scanner(System.in);
		String temp=sc.next();
		try {
			Integer.parseInt(temp);
			temp_symbol=new Symbol(temp,"int");
		}catch(NumberFormatException e) {
		try {
			Float.parseFloat(temp);
			temp_symbol=new Symbol(temp,"real");
		}catch(NumberFormatException b){
			temp_symbol=new Symbol(temp,"char");
		}
		}
	}
	//for循環
	private void for_stat() {
		treeNode temp=temp_node;
		temp_node=temp.getChild(0);
		switch(temp_node.getToken().getSyn()) {
		case 1:
		case 2:
		case 3:
			declare();
			break;
		case 20:
			assign();
			break;
		}
		temp_node=temp.getChild(1);
		stmt();
		while(Integer.parseInt(temp_symbol.getLexme())!=0) {
			temp_node=temp.getChild(1);
			stmt();
			if(Integer.parseInt(temp_symbol.getLexme())==0) {
				break;
			}
			temp_node=temp.getChild(3);
			loop_stat();
			if (temp_symbol.getLexme().contains("break")) {
				break;
			}
			temp_node=temp.getChild(2);
			assign();
		}
	}
	//if語句
	private void if_stat() {
		treeNode temp=temp_node;
		temp_node=temp.getChild(0);
		stmt();
		if (Integer.parseInt(temp_symbol.getLexme())!=0) {
			temp_node=temp.getChild(1);
			analysis();
		}else if(temp.getChildSize()>2){
			temp_node=temp.getChild(2);
			analysis();
		}
	}
	private void while_stat() {
		treeNode temp=temp_node;
		temp_node=temp.getChild(0);
		stmt();
		while (Integer.parseInt(temp_symbol.getLexme())!=0) {
			temp_node=temp.getChild(1);
			loop_stat();
			if (temp_symbol.getLexme().contains("break")) {
				break;
			}
			temp_node=temp.getChild(0);
			stmt();
		}
	}
	//循環語句块
	private void loop_stat() {
		treeNode temp=temp_node;
		switch(temp.getToken().getSyn()) {
		//聲明
		case 1:
		case 2:
		case 3:
			declare();
			break;
		//for
		case 4:
			for_stat();
			break;
		//if
		case 5:
				treeNode temp_treenode=temp;
				temp_node=temp_treenode.getChild(0);
				stmt();
				if (Integer.parseInt(temp_symbol.getLexme())!=0) {
					temp_node=temp_treenode.getChild(1);
					loop_stat();
				}else if(temp_treenode.getChildSize()>2){
					temp_node=temp_treenode.getChild(2);
					loop_stat();
				}
				break;
			//while
			case 6:
				while_stat();
				break;
			case 7:
				temp_symbol=new Symbol(temp.toString(),"keyword");
				break;
			case 8:
				temp_symbol=new Symbol(temp.toString(),"keyword");
				break;
			//print
			case 10:
				print_stat();
				break;
			//scan
			case 11:
				read_stat();
				break;
			//{
			case 35:
				treeNode temp_treenode1=temp;
				table=new HashMap<String,Symbol>();
				table_array.add(table);
				for (int j=0;j<temp_treenode1.getChildSize();j++) {
					temp_node=temp_treenode1.getChild(j);
					loop_stat();
					if (temp_symbol.getLexme().contains("break")||temp_symbol.getLexme().contains("continue")) {
						break;
					}
					}
				break;
			//}
			case 36:
				table_array.remove(table_array.size()-1);
				table=table_array.get(table_array.size()-1);
				break;
			//賦值
			case 20:
				assign();
				break;
			default:
				System.out.println("語義分析錯誤");
				System.exit(1);
				break;
			}  
		}
}
	
