package homework1;
import homework1.Token;

import java.util.ArrayList;
import java.util.List;
public class Lexical {
	private List<Token> tokenList= new ArrayList();
	private int index=0;
	private char [] array;
	private int row=0;
	Lexical(String text){
		this.array=text.toCharArray();
	}
	
	private String[] reserveWord = {
			"char", "int","real",
			"for", "if","while",
			"break", "continue",
			 "else", 
			"print","read"
	};
	private String[] operatorOrDelimiter = {
			"+","-", "*", "/", "<", "<=", ">", ">=", "=", "==",
			 ";", "(", ")", ",", "\"", "\'", "&",
			"&&", "|", "||", "%", "[", "]", "{",
			"}", ".","<>"};
	
	private int searchReserveWord(String word) {
		for (int i=0;i<this.reserveWord.length;i++) {
			if (this.reserveWord[i].equals(word)){
				return i+1;
			}
		}
		return -1;
	}
	
	private int searchOp(String op) {
		for (int i=0;i<this.operatorOrDelimiter.length;i++) {
			if (this.operatorOrDelimiter[i].equals(op)){
				return i+1;
			}
		}
		return -1;
	}
	
	private boolean isLetter(char letter) {
		if (letter >= 'a'&&letter <= 'z' || letter >= 'A'&&letter <= 'Z') {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isDigit(char digit) {
		if (digit>='0'&&digit<='9') {
			return true;
		}
		else {
			return false;
		}
	}
	
	private void skipLine() {
		while(array[index]!='\n') {
			index++;
			if (index==array.length) {
				break;
			}
		}
		row=row+1;
	}
	
	List<Token> scanner() {
		while(index<array.length){
			//------------去除空格------------
			if (array[index] == ' ') {
				index++;
				continue;
			}
			
			//-----------------标識符和保留字-----------------------
			if (isLetter(array[index])) {
				String temp=""+array[index];
				int flag=0;
				int err_flag=0;
				if (isLetter(array[index])) {
					flag=1;
				}
				index++;
				while(array[index]=='_'||isLetter(array[index])||isDigit(array[index])){
					if (isLetter(array[index])) {
						flag=1;
					}
					temp = temp+""+array[index];
					index++;
				}
				if (flag==0) {
					System.out.println(temp+"标識符錯誤"+" "+row);
					err_flag=1;
					skipLine();
				}
				if (array[index-1]=='_'){
					System.out.println("_結尾錯誤"+" "+row);
					err_flag=1;
					skipLine();
				}
				if (err_flag==0) {
					int syn=searchReserveWord(temp);
					if (syn!=-1) {
						tokenList.add(new Token(temp,syn,row));
						System.out.println('<'+temp+','+syn+','+row+'>');
					}else {
						tokenList.add(new Token(temp,100,row));
						System.out.println('<'+temp+','+100+','+row+'>');
					}

				}
				continue;
			}
			//------------------數字------------------------
			if (isDigit(array[index])) {
				String temp=""+array[index];
				index++;
				int point_flag=0;
				int err_flag=0;
				while (array[index]=='.'||isDigit(array[index])) {
					if (array[index]=='.'&&point_flag==0&&isDigit(array[index-1])&&isDigit(array[index+1])) {
						point_flag=1;
						temp=temp+""+array[index];
						index++;
						continue;
						}
					if (isDigit(array[index])) {
						temp=temp+""+array[index];
						index++;
						continue;
					}
					System.out.println("多出小數点或小數点后非數字"+" "+row);
					err_flag=1;
					skipLine();
					break;
				}
				if (array[index]=='_'||isLetter(array[index])){
					System.out.println("錯誤字符串"+" "+row);
					err_flag=1;
					skipLine();
				}
				if (err_flag==0) {
					if (point_flag==1) {
						tokenList.add(new Token(temp,98,row));
						System.out.println('<'+temp+','+98+','+row+'>');
					}
					else {
						tokenList.add(new Token(temp,99,row));
						System.out.println('<'+temp+','+99+','+row+'>');
					}
				}
				continue;}
			//字符串
			if (array[index]=='\'') {
				int err_flag=0;
				index++;
				String temp="\'";
				while (array[index]!='\'') {
					if (index<array.length) {
						temp=temp+""+array[index];
						index++;
					}else {
						err_flag=1;
						break;
					}
				}
				if (err_flag==0) {
					index++;
					temp=temp+'\'';
					tokenList.add(new Token(temp,97,row));
					System.out.println("<"+temp+","+97+','+row+">");
					continue;
				}else {
					System.out.println("\'錯誤"+" "+row);
					skipLine();
					continue;
				}
			}
			if (array[index]=='\"') {
				int err_flag=0;
				index++;
				String temp="";
				while (array[index]!='\"') {
					if (index<array.length) {
						temp=temp+""+array[index];
						index++;
					}else {
						err_flag=1;
						break;
					}
				}
				if (err_flag==0) {
					index++;
					tokenList.add(new Token(temp,97,row));
					System.out.println("<"+temp+","+97+','+row+">");
					continue;
				}else {
					System.out.println("\"錯誤"+" "+row);
					skipLine();
					continue;
				}
			}
			
			//------------------單个---------------------
			if (array[index] == '/' || array[index] == ';' || array[index] == '(' || 
					array[index] == ')' || array[index] == ','  || 
					array[index] == '%' || array[index] == '['||
					array[index] == ']' || array[index] == '{' || array[index] == '}' ) {
				int syn=this.reserveWord.length+searchOp(""+array[index]);
				tokenList.add(new Token(""+array[index]+'\0',syn,row));
				System.out.println('<'+""+array[index]+'\0'+','+syn+','+row+'>');
				index++;
				continue;
			}
			//------------------两个-------------------------
			if (array[index]=='\\') {
					System.out.println("\\錯誤"+" "+row);
					skipLine();
					continue;
				}
			
			if (array[index]=='*') {
				index++;
				String temp="";
				if (array[index]== '*') {
					System.out.println("**錯誤"+" "+row);
					skipLine();
					continue;
				}else {
					int syn=this.reserveWord.length+searchOp("*");
					tokenList.add(new Token("*",syn,row));
					System.out.println("<*,"+syn+','+row+">");
					continue;
				}
			}
			
			if (array[index]=='-') {
				index++;
				String temp="";
				if (array[index]== '-') {
					System.out.println("--錯誤"+" "+row);
					skipLine();
					continue;
				}else {
					int syn=this.reserveWord.length+searchOp("-");
					tokenList.add(new Token("-",syn,row));
					System.out.println("<-,"+syn+','+row+">");
					continue;
				}
			}
			
			//------------------两个-------------------------
			if (array[index]=='<') {
				index++;
				String temp="";
				if (array[index]== '=') {
					temp="<=";
					index++;
				}
				else if (array[index]== '>') {
					temp="<>";
					index++;
				}
				else {
					temp="<";
				}
				int syn=this.reserveWord.length+searchOp(temp);
				tokenList.add(new Token(temp,syn,row));
				System.out.println('<'+temp+','+syn+','+row+'>');
				continue;
			}
			
			if (array[index]=='>') {
				index++;
				String temp="";
				if (array[index]== '=') {
					temp=">=";
					index++;
				}else {
					temp=">";
				}
				int syn=this.reserveWord.length+searchOp(temp);
				tokenList.add(new Token(temp,syn,row));
				System.out.println('<'+temp+','+syn+','+row+'>');
				continue;
			}
			
			if (array[index]=='=') {
				index++;
				String temp="";
				if (array[index]== '=') {
					temp="==";
					index++;
				}
				else {
					temp="=";
				}
				int syn=this.reserveWord.length+searchOp(temp);
				tokenList.add(new Token(temp,syn,row));
				System.out.println('<'+temp+','+syn+','+row+'>');
				continue;
			}
			
			if (array[index]=='+') {
				index++;
				String temp="";
				if (array[index]== '+') {
					System.out.println("++錯誤"+" "+row);
					skipLine();
					continue;
				}
				else {
					temp="+";
				}
				int syn=this.reserveWord.length+searchOp(temp);
				tokenList.add(new Token(temp,syn,row));
				System.out.println('<'+temp+','+syn+','+row+'>');
				continue;
				
			}
			
			if (array[index]=='&') {
				index++;
				String temp="";
				if (array[index]== '&') {
					temp="&&";
					index++;
				}
				else {
					temp="&";
				}
				int syn=this.reserveWord.length+searchOp(temp);
				tokenList.add(new Token(temp,syn,row));
				System.out.println('<'+temp+','+syn+','+row+'>');
				continue;
			}
			
			if (array[index]=='|') {
				index++;
				String temp="";
				if (array[index]== '|') {
					temp="||";
					index++;
				}
				else {
					temp="|";
				}
				int syn=this.reserveWord.length+searchOp(temp);
				tokenList.add(new Token(temp,syn,row));
				System.out.println('<'+temp+','+syn+','+row+'>');
				continue;
			}
			if (array[index]=='\n') {
				index++;
				row++;
				continue;
			}
			//--------------------錯誤符號----------------------
			System.out.println(array[index]+"錯誤符號"+" "+row);
			
			while(array[index]!='\n'&& index<array.length) {
				index++;
			}
	}
		System.out.println("詞法分析完成");
		return tokenList;
	}
}
