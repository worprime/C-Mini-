package homework1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class treeNode {
	private Token value;
	private List<treeNode> Child=new ArrayList<treeNode>();
	private int index=0;
	
    treeNode(Token value){
    	this.value=value;
    }
    
     int getChildSize() {
    	return Child.size();
    }

    public void addTree(treeNode _child) {
    	if (_child!=null) {
    		this.Child.add(index, _child);
    		index++;
    	}
    }
    
    public void addTree(treeNode child,int _index) {
    	if (child!=null) {
    		if (Child.size()<=_index) {
    			for (int i=Child.size();i<_index;i++) {
    				Child.add(null);
    			}
    			index=_index;
    		}else {
    			this.Child.remove(_index);
    		}
    		this.Child.add(_index, child);
    	}
    }

    public treeNode getChild(int _index) {
    	return Child.get(_index);
    }
    
    public Token getToken() {
    	return value;
    }

    public String toString() {
    	return value.getToken();
    }
}
