package homework1;

import java.util.ArrayList;
import java.util.List;


public class Symbol {
	private String type="";
	private String tokenClass;
	private String value="0";
	private String Lexeme;
	private List<Integer> array_dimension=new ArrayList<Integer>();
	private List<Object> array=new ArrayList<Object>();
	
	Symbol(String _Lexeme,String _tokenClass){
		this.Lexeme=_Lexeme;
		this.tokenClass=_tokenClass;
	}
	
	void setArrayDimension(int _Dimension) {
		this.array_dimension.add(_Dimension);
	}
	
	void setArray(List<Object> _array) {
		this.array=_array;
	}
	
	int getArrayDimensionSize() {
		return this.array_dimension.size();
	}
	
	List<Object> getArray() {
		if (array_dimension!=null) {
			return array;
		}
		return null;
	}
	
	int getArraySize() {
		int temp=1;
		for (int i=0;i<array_dimension.size();i++) {
			temp=temp*array_dimension.get(i);
		}
		return temp;
	}
	
	void initArray() {
		int temp=1;
		for (int i=0;i<array_dimension.size();i++) {
			temp=temp*array_dimension.get(i);
		}
		if (type.contains("float")) {
			for (int i=0;i<temp;i++) {
				array.add(0.0);
			}
			
		}else if (type.contains("char")){
			for (int i=0;i<temp;i++) {
				array.add("");
			}
		}else {
			for (int i=0;i<temp;i++) {
				array.add(0);
			}
		}
	}
	
	void setArray(int value,List<Integer> array_dimension) {
		if (type.contains("int")==false) {
			System.out.println("數組賦值與數組类型不符合");
			System.exit(1);
		}
		int index=0,i=0;
		while(i<array_dimension.size()) {
			int temp=array_dimension.get(i);
			if (array_dimension.get(i)<this.array_dimension.get(i)) {
				for (int k=i+1;k<this.array_dimension.size();k++) {
					temp=temp*this.array_dimension.get(k);
					}
				i++;
				index=index+temp;
			}else {
				System.out.println(Lexeme+"數組越界");
				System.exit(1);
			}
		}
		array.remove(index);
		array.add(index, value);
	}
	void setArray(String value,List<Integer> array_dimension) {
		if (type.contains("char")==false) {
			System.out.println("數組賦值與數組类型不符合");
			System.exit(1);
		}
		int index=0,i=0;
		while(i<array_dimension.size()) {
			int temp=array_dimension.get(i);
			if (array_dimension.get(i)<this.array_dimension.get(i)) {
				for (int k=i+1;k<this.array_dimension.size();k++) {
					temp=temp*this.array_dimension.get(k);
					}
				i++;
				index=index+temp;
			}else {
				System.out.println(Lexeme+"數組越界");
				System.exit(1);
			}
		}
		array.remove(index);
		array.add(index, value);
	}
	
	void setArray(float value,List<Integer> array_dimension){
		if (type.contains("real")==false) {
			System.out.println("數組賦值與數組类型不符合");
			System.exit(1);
		}
		int index=0,i=0;
		while(i<array_dimension.size()) {
			int temp=array_dimension.get(i);
			if (array_dimension.get(i)<this.array_dimension.get(i)) {
				for (int k=i+1;k<this.array_dimension.size();k++) {
					temp=temp*this.array_dimension.get(k);
					}
				i++;
				index=index+temp;
			}else {
				System.out.println(Lexeme+"數組越界");
				System.exit(1);
			}
		}
		array.remove(index);
		array.add(index, value);
	}
	
	String getArray(List<Integer> array_dimension) {
		int index=0,i=0;
		while(i<array_dimension.size()) {
			int temp=array_dimension.get(i);
			if(temp<0) {
				temp=this.array_dimension.get(i)+temp;
			}
			if (array_dimension.get(i)<this.array_dimension.get(i)) {
				for (int k=i+1;k<this.array_dimension.size();k++) {
					temp=temp*this.array_dimension.get(k);
					}
				i++;
				index=index+temp;
			}else {
				System.out.println(Lexeme+"數組越界");
				System.exit(1);
			}
		}
		return String.valueOf(array.get(index));
	}
	
	
	boolean isArray() {
		if (array_dimension.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
	
	
	String getLexme() {
		return Lexeme;
	}
	
	String getType() {
		return type;
	}
	String getTokenClass() {
		return tokenClass;
	}
	void setValue(String _value) {
		this.value=_value;
	}
	void setType(String _type) {
		this.type=_type;
	}
	String getValue() {
		return value;
	}
	
}
