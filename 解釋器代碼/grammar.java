package homework1;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;
import java.util.Stack;
public class grammar {
	private int index=0;
	private Token token;
	private List<Token> tokenList;
	private treeNode tree=new treeNode(null);
	private treeNode sent_temp=null;
	private DefaultMutableTreeNode node = null;
	private DefaultMutableTreeNode fTree_node = new DefaultMutableTreeNode("����");
	JFrame f = new JFrame("JTree");
	
	grammar(List<Token> _tokenList){
		this.tokenList=_tokenList;
	}
	
	private void takeToken() {
		if (index<tokenList.size()) {
			token=tokenList.get(index);
			index++;
		}else {
			System.out.println("�Z������ʧ��");
			System.exit(0);
		}
	}
	//����
	treeNode parser() {
			while (index<tokenList.size()) {
				tree.addTree(run());
				fTree_node.add(node);
				System.out.println();
			}
			JTree Jtree = new JTree(fTree_node);
			f.add(Jtree);
			f.setSize(300, 300);
		    f.setVisible(true);
		    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    Jtree.addTreeSelectionListener(new TreeSelectionListener() {
		        @Override
		        public void valueChanged(TreeSelectionEvent e) {
		            DefaultMutableTreeNode node = (DefaultMutableTreeNode) Jtree
		                    .getLastSelectedPathComponent();
		            if (node == null)
		                return;
		            Object object = node.getUserObject();
		        }});
		    System.out.println("�Z�������ɹ�");
			return tree;
		}
	
	//����ֵ
	private treeNode first_assign() {
		DefaultMutableTreeNode temp_node =null;
		treeNode temp=null;
		takeToken();
		if (token.getSyn()==20) {
			temp=new treeNode(token);
			System.out.print("=");
			temp_node=new DefaultMutableTreeNode(new treeNode(token));
			treeNode temp1=rightValue();
			temp.addTree(temp1,1);
			if (temp1!=null) {
				temp_node.add(node);
			}
		}else {
			index--;
		}
		node = temp_node;
		return temp;
	}
	//���Mֵ�]��
	private treeNode array_value_closure() {
		treeNode temp=null;
		DefaultMutableTreeNode temp_node = null;
		treeNode temp_treeNode =null;
		DefaultMutableTreeNode temp_node2 = null;
		if ((temp_treeNode=array_value())!=null) {
			temp_node2=node;
		}else {
			temp_treeNode=stmt();
			temp_node2=node;
		}
		takeToken();
		if (token.getSyn()==25) {
			temp=new treeNode(token);
			temp_node=new DefaultMutableTreeNode(temp);
			System.out.print(",");
			temp.addTree(temp_treeNode);
			temp_node.add(temp_node2);
			if ((temp_treeNode=array_value())!=null) {
					temp_node2=node;
			}else {
				temp_treeNode=array_value_closure();
				temp_node2=node;
			}
			temp.addTree(temp_treeNode);
			temp_node.add(temp_node2);
		}else {
			index--;
			temp=temp_treeNode;
			temp_node=temp_node2;
		}
		node=temp_node;
		return temp;
	}
	
	//���Mֵ
	private treeNode array_value() {
		treeNode temp=null;
		DefaultMutableTreeNode temp_node = null;
		takeToken();
		if (token.getSyn()==35) {
			System.out.print("{");
			temp= array_value_closure();
			temp_node=node;
			takeToken();
			if (token.getSyn()==36) {
				System.out.print("}");
			}else {
				System.out.println("���Mȱ��}");
				System.exit(1);
			}
		}else {
			index--;
		}
		node=temp_node;
		return temp;
	}
	
	//��ֵ
	private treeNode rightValue() {
		treeNode temp=null;
		DefaultMutableTreeNode temp_node = null;
		takeToken();
		if (token.getSyn()==35) {
			index--;
			temp=array_value();
			temp_node=node;
		}else {
				if(token.getSyn()==11){
					index--;
					temp=read_stat();
					temp_node=node;
				}else {
					index--;
					temp=stmt();
					temp_node=node;
				}
		}
		node =temp_node;
		return temp;
	}
		
	
	//����
	private treeNode value() {
		DefaultMutableTreeNode temp_node = null;
		treeNode temp=null;
		takeToken();
		if (token.getSyn()==99||token.getSyn()==98||token.getSyn()==97) {
			System.out.print(token.getToken());
			temp=new treeNode(token);
			temp_node=new DefaultMutableTreeNode(new treeNode(token));
		}
		if(token.getSyn()==11) {
			System.out.print(token.getToken());
			index--;
			temp=read_stat();
			temp_node=new DefaultMutableTreeNode(new treeNode(token));
		}
		node =temp_node;
		return temp;
	}
	
	//��ʽ
	private treeNode factor() {
		DefaultMutableTreeNode temp_node = null;
		treeNode temp=null;
		takeToken();
		switch(token.getSyn()) {
			case 23:
				System.out.print(token.getToken());
				temp=stmt();
				temp_node=node;
				takeToken();
				if (token.getSyn()==24) {
					System.out.print(token.getToken());
				}else {
					System.out.println("��ʽȱ��)");
					System.exit(1);
				}
				break;
			case 97:
			case 98:
			case 99:
				index--;
				temp=value();
				if (temp!=null) {
					temp_node=node;
				}
				break;
			case 100:
				index--;
				temp=Variable();
				temp_node=node;
				break;
			default:
				index--;
				break;
		}
		node =temp_node;
		return temp;
	}
	//��ʽ�ݹ�
	private treeNode factor_rec() {
		DefaultMutableTreeNode temp_node = null;
		treeNode temp=null;
		takeToken();
		switch(token.getSyn()) {
		case 14:
		case 15:
		case 32:
			System.out.print(token.getToken());
			treeNode temp1=new treeNode(token);
			DefaultMutableTreeNode temp_node1=
					new DefaultMutableTreeNode(new treeNode(token));
			//��һ�����㔵
			if(sent_temp!=null) {
				temp1.addTree(sent_temp);
				temp_node1.add(node);
				sent_temp=null;
			}
			//��һ�����㔵
			temp1.addTree(factor());
			temp_node1.add(node);
			sent_temp=temp1;
			node=temp_node1;
			//��һ���˳�����
			temp=factor_rec();
			sent_temp=null;
			if(temp==null) {
				temp=temp1;
				temp_node=temp_node1;
			}
			else {
				temp_node=node;
			}
			break;
		default:
			index--;
			break;
		}
		node =temp_node;
		return temp;
	}
	//����
	private treeNode divisor() {
		DefaultMutableTreeNode temp_node =null;
		treeNode temp=null;
		treeNode temp1=factor();
		DefaultMutableTreeNode temp_node1=node;
		sent_temp=temp1;
		temp=factor_rec();
		sent_temp=null;
		temp_node=node;
		if (temp==null) {
			temp=temp1;
			temp_node=temp_node1;
		}
		node =temp_node;
		return temp;
	}
	//����
	private treeNode run() {
		DefaultMutableTreeNode temp_node = null;
		treeNode temp=new treeNode(null);
		takeToken();
		switch(token.getSyn()) {
		case 1:
		case 2:
		case 3:
			index--;
			temp=declare();
			temp_node=node;
			break;
		case 4:
			index--;
			temp=for_stat();
			temp_node=node;
			break;
		case 5:
			index--;
			temp=if_stat();
			temp_node=node;
			break;
		case 6:
			index--;
			temp=while_stat();
			temp_node=node;
			break;
		case 10:
			index--;
			temp=print_stat();
			temp_node=node;
			break;
		case 35:
			System.out.print(token.getToken());
			temp=new treeNode(token);
			temp_node = new DefaultMutableTreeNode(temp);
			temp.addTree(run());
			if (temp.getChild(0)!=null) {
				temp_node.add(node);
			}
			takeToken();
			if (token.getSyn()==36) {
				temp.addTree(new treeNode(token));
				temp_node.add(new DefaultMutableTreeNode(new treeNode(token)));
				System.out.print(token.getToken());
			}else {
				System.out.println("ȱ��}");
				System.exit(1);
			}
			break;
		case 100:
			index--;
			temp=setVarFunc();
			if (temp!=null) {
				temp_node=node;
			}
			break;
		default:
			System.out.println("�Z�������e�`");
			System.exit(1);
			break;
		}
		node =temp_node;
		return temp;
	} 
	//���xֵ
	private treeNode declare_assign() {
		treeNode temp=null;
		DefaultMutableTreeNode temp_node = null;
		treeNode temp_Variable =Variable();
		DefaultMutableTreeNode temp_node1 = node;
		treeNode temp_assign=first_assign();
		if (temp_assign!=null) {
			temp=temp_assign;
			temp_node=node;
			temp.addTree(temp_Variable,0);
			if (temp_Variable!=null) {
				temp_node.add(temp_node1);
			}
		}
		else {
			temp=temp_Variable;
			temp_node = temp_node1;
		}
		node =temp_node;
		return temp;
	}
	//��
	private treeNode declare() { 
		takeToken();
		DefaultMutableTreeNode temp_node = new DefaultMutableTreeNode(new treeNode(token));
		treeNode temp=new treeNode(token);
		treeNode temp_treeNode=null;
		System.out.print(token.getToken());
		temp_treeNode=declare_assign();
		temp.addTree(temp_treeNode);
		if (temp_treeNode!=null) {
			temp_node.add(node);
			}
		takeToken();
		while(token.getSyn()==25) {
			temp_treeNode=declare_assign();
			temp.addTree(temp_treeNode);
			if (temp_treeNode!=null) {
				temp_node.add(node);
				}
			takeToken();
		}
		if (token.getSyn()==22) {
			System.out.print(token.getToken());
		}else {
			System.out.println("��ȱ��;");
			System.exit(1);
		}
		node =temp_node;
		return temp;
	}
	
	//if�Z��
	private treeNode if_stat() {
		takeToken();
		DefaultMutableTreeNode temp_node = new DefaultMutableTreeNode(new treeNode(token));
		treeNode temp=new treeNode(token);
		treeNode temp_treeNode=null;
		System.out.print(token.getToken());
		takeToken();
		if (token.getSyn()==23) {
			System.out.print(token.getToken());
			temp_treeNode=stmt();
			temp.addTree(temp_treeNode);
			if (temp_treeNode!=null) {
				temp_node.add(node);
			}
			takeToken();
			if (token.getSyn()==24) {
				System.out.println(token.getToken());
				temp_treeNode=complex_func_stat();
				temp.addTree(temp_treeNode);
				if (temp_treeNode!=null) {
					temp_node.add(node);
				}
				if (index<tokenList.size()) {
					temp_treeNode=else_stat();
					temp.addTree(temp_treeNode);
					if (temp_treeNode!=null) {
						temp_node.add(node);
					}
				}
			}else {
				System.out.println("if��ȱ��)");
				System.exit(1);
			}
		}else {
			System.out.println("if��ȱ��(");
			System.exit(1);
		}
		node =temp_node;
		return temp;
	}
	//�߼����ʽ
		private treeNode logical_expr(){
			DefaultMutableTreeNode temp_node = null;
			treeNode temp=null;
			treeNode temp_treeNode=null;
			
			takeToken();
			switch(token.getSyn()) {
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
				case 38:
					System.out.print(token.getToken());
					treeNode temp1=new treeNode(token);
					DefaultMutableTreeNode temp_node1=
							new DefaultMutableTreeNode(new treeNode(token));
					if(sent_temp!=null) {
						temp1.addTree(sent_temp);
						temp_node1.add(node);
						sent_temp=null;
					}
					
					temp_treeNode=divisor();
					temp_node=node;
					sent_temp=temp_treeNode;
					node=temp_node;
					temp=item();
					if (temp==null) {
						temp=temp_treeNode;
					}else {
						temp_node=node;
					}
					temp1.addTree(temp);
					temp_node1.add(temp_node);
					
					sent_temp=temp1;
					node=temp_node1;
					temp=logical_expr();
					temp_node=node;
					sent_temp=null;
					if (temp==null) {
						temp=temp1;
						temp_node=temp_node1;
					}
					break;
				default:
					index--;
					break;
			}
			node =temp_node;
			return temp;
		}
	
	//ѭ�h������
	private treeNode complex_func_stat() {
		DefaultMutableTreeNode temp_node = null;
		treeNode temp=null;
		treeNode temp_treenode=null;
		takeToken();
		if (token.getSyn()==35) {
			temp=new treeNode(token);
			temp_node=new DefaultMutableTreeNode(temp);
			System.out.println(token.getToken());
			temp_treenode=function();
			temp.addTree(temp_treenode);
			if (temp.getChild(0)!=null) {
				temp_node.add(node);
				}
			takeToken();
			while(token.getSyn()!=36) {
				index--;
				temp_treenode=function();
				temp.addTree(temp_treenode);
				if (temp.getChild(0)!=null) {
					temp_node.add(node);
				}
				takeToken();
			}
			if (token.getSyn()==36) {
				temp.addTree(new treeNode(token));
				temp_node.add(new DefaultMutableTreeNode(new treeNode(token)));
				System.out.println(token.getToken());
			}else {
				System.out.println("ѭ�h������ȱ��}");
				System.exit(1);
			}
		}else {
			index--;
			temp=function();
			if (temp!=null) {
				temp_node=node;
				}
		}
		node =temp_node;
		return temp;
	}
	
	
	//for�Z��
	private treeNode for_stat() {
		takeToken();
		treeNode temp=new treeNode(token);
		DefaultMutableTreeNode temp_node =new DefaultMutableTreeNode(temp);
		treeNode temp_treeNode=null;
		System.out.print(token.getToken());
		takeToken();
		if (token.getSyn()==23) {
			System.out.print(token.getToken());
			takeToken();
			switch(token.getSyn()) {
			case 1:
			case 2:
			case 3:
				index--;
				temp_treeNode=declare();
				temp.addTree(temp_treeNode);
				temp_node.add(node);
				break;
			case 100:
				index--;
				temp_treeNode=setVarFunc();
				temp.addTree(temp_treeNode);
				temp_node.add(node);
				break;
			}
			temp_treeNode=stmt();
			temp.addTree(temp_treeNode);
			if (temp_treeNode!=null) {
				temp_node.add(node);
			}
			takeToken();
			if (token.getSyn()==22) {
				System.out.print(token.getToken());
				temp_treeNode=declare_assign();
				temp.addTree(temp_treeNode);
				if (temp_treeNode!=null) {
					temp_node.add(node);
				}
				takeToken();
				if (token.getSyn()==24) {
					System.out.print(token.getToken());
					temp_treeNode=complex_func_stat();
					temp.addTree(temp_treeNode);
					if (temp_treeNode!=null) {
						temp_node.add(node);
						}
					}else {
						System.out.println("forȱ��)");
						System.exit(1);
					}
				}else {
					System.out.println("forȱ��;");
					System.exit(1);
				}
		}else {
			System.out.println("forȱ��(");
			System.exit(1);
		}
		node =temp_node;
		return temp;
	}
	
	//while�Z��
	private treeNode while_stat() {
		takeToken();
		DefaultMutableTreeNode temp_node = new DefaultMutableTreeNode(new treeNode(token));
		treeNode temp=new treeNode(token);
		System.out.print(token.getToken());
		takeToken();
		if (token.getSyn()==23) {
			System.out.print(token.getToken());
			temp.addTree(stmt());
			if (node!=null) {
				temp_node.add(node);
			}
			takeToken();
			if (token.getSyn()==24) {
				System.out.println(token.getToken());
				temp.addTree(complex_func_stat());
				if (node!=null) {
					temp_node.add(node);
				}
			}else {
				System.out.println("while��ȱ��)");
				System.exit(1);
			}
		}else {
			System.out.println("while��ȱ��(");
			System.exit(1);
		}
		node =temp_node;
		return temp;
	}
	
	//break
	private treeNode break_stat() {
		takeToken();
		DefaultMutableTreeNode temp_node = new DefaultMutableTreeNode(new treeNode(token));
		treeNode temp=new treeNode(token);
		System.out.print(token.getToken());
		takeToken();
		if (token.getSyn()==22) {
			System.out.print(token.getToken());
		}else {
			System.out.println("breakȱ��;");
			System.exit(1);
		}
		node =temp_node;
		return temp;
	}
	//continue
	private treeNode continue_stat() {
		takeToken();
		DefaultMutableTreeNode temp_node = new DefaultMutableTreeNode(new treeNode(token));
		treeNode temp=new treeNode(token);
		System.out.print(token.getToken());
		takeToken();
		if (token.getSyn()==22) {
			System.out.print(token.getToken());
		}else {
			System.out.println("continueȱ��;");
			System.exit(1);
		}
		node =temp_node;
		return temp;
	}
	//else�Z��
	private treeNode else_stat() {
		DefaultMutableTreeNode temp_node = null;
		treeNode temp=null;
		takeToken();
		if (token.getSyn()==9) {
			System.out.print(token.getToken());
			temp=complex_func_stat();
			temp_node=node;
		}else {
		index--;}
		node =temp_node;
		return temp;
	}
	//����
	private treeNode Variable() {
		takeToken();
		DefaultMutableTreeNode temp_node = new DefaultMutableTreeNode(new treeNode(token));
		treeNode temp=new treeNode(token);
		treeNode temp_treeNode=null;
		System.out.print(token.getToken());
		takeToken();
		if (token.getSyn()==33) {
			while(token.getSyn()==33) {
				System.out.print(token.getToken());
				temp_treeNode=stmt();
				temp.addTree(temp_treeNode);
				if (temp_treeNode!=null) {
						temp_node.add(node);
					}
				takeToken();
				if (token.getSyn()==34) {
					System.out.print(token.getToken());
					takeToken();
				}else {
					System.out.println("ȱ��]");
					System.exit(1);
				}
			}
			index--;
		}else {
			index--;
		}
		node =temp_node;
		return temp;
	}
	//��
	private treeNode item() {
		DefaultMutableTreeNode temp_node = null;
		treeNode temp=null;
		treeNode temp_treeNode=null;
		takeToken();
		switch(token.getSyn()) {
		case 12:
		case 13:
			System.out.print(token.getToken());
			treeNode temp1=new treeNode(token);
			DefaultMutableTreeNode temp_node1=
					new DefaultMutableTreeNode(new treeNode(token));
			if(sent_temp!=null) {
				temp1.addTree(sent_temp);
				temp_node1.add(node);
				sent_temp=null;
			}
			temp_treeNode=divisor();
			temp1.addTree(temp_treeNode);
			if (temp_treeNode!=null) {
				temp_node1.add(node);
			}
			sent_temp=temp1;
			node=temp_node1;
			temp=item();
			sent_temp=null;
			if (temp==null) {
				temp=temp1;
				temp_node=temp_node1;
			}else {
				temp_node=node;
			}
			break;
		
		default:
			index--;
			break;
		}
		node =temp_node;
		return temp;
	}
	//���ʽ
	private treeNode stmt() {
		treeNode temp=divisor();
		DefaultMutableTreeNode temp_node = node;
		sent_temp=temp;
		treeNode temp1=item();
		DefaultMutableTreeNode temp_node1 = node;
		if (temp1!=null) {
			temp=temp1;
			temp_node=temp_node1;
		}
		sent_temp=temp;
		node=temp_node;
		temp1=logical_expr();
		temp_node1 = node;
		if (temp1!=null) {
			temp=temp1;
			temp_node=temp_node1;
		}
		sent_temp=temp;
		node=temp_node;
		temp1=AND_OR_logical_expr();
		temp_node1 = node;
		if (temp1!=null) {
			temp=temp1;
			temp_node=temp_node1;
		}
		sent_temp=null;
		node =temp_node;
		return temp;
	}
	//����c�߼����ʽ
	private treeNode AND_OR_logical_expr() {
		DefaultMutableTreeNode temp_node = null;
		treeNode temp=null;
		takeToken();
		switch(token.getSyn()) {
			case 28:
			case 29:
			case 30:
			case 31:
				System.out.print(token.getToken());
				temp=new treeNode(token);
				temp_node=new DefaultMutableTreeNode(new treeNode(token));
				if (sent_temp!=null) {
					temp.addTree(sent_temp);
					temp_node.add(node);
					sent_temp=null;
				}
				treeNode temp_treenode=stmt();
				temp.addTree(temp_treenode);
				if (temp_treenode!=null) {
					temp_node.add(node);
				}
				break;
			default:
				index--;
				break;
		}
		node =temp_node;
		return temp;
	}
	//�Z���
	private treeNode function() {
		DefaultMutableTreeNode temp_node = null;
		treeNode temp=null;
		takeToken();
		System.out.println();
		switch(token.getSyn()) {
		case 1:
		case 2:
		case 3:
			index--;
			temp=declare();
			temp_node=node;
			break;
		case 4:
			index--;
			temp=for_stat();
			temp_node=(node);
			break;
		case 5:
			index--;
			temp=if_stat();
			temp_node=node;
			break;
		case 6:
			index--;
			temp=while_stat();
			temp_node=node;
			break;
		case 10:
			index--;
			temp=print_stat();
			temp_node=node;
			break;
		case 7:
			index--;
			temp=break_stat();
			temp_node=node;
			break;
		case 8:
			index--;
			temp=continue_stat();
			temp_node=node;
			break;
		case 100:
			index--;
			temp=setVarFunc();
			temp_node=node;
			break;
		default:
			index--;
			break;
		}
		node =temp_node;
		return temp;
	}
	//��ֵ����
	private treeNode setVarFunc() {
		DefaultMutableTreeNode temp_node =null;
		treeNode temp=null;
		treeNode temp_Variable=Variable();
		DefaultMutableTreeNode temp_node1=node;
		temp=assign();
		if (temp!=null) {
			temp_node=node;
			temp.addTree(temp_Variable,0);
			temp_node.add(temp_node1);
		}
		else {
			temp=temp_Variable;
			temp_node=temp_node1;
		}
		node =temp_node;
		return temp;
	}
	// ��ֵ����
	private treeNode assign() {
		DefaultMutableTreeNode temp_node = null;
		treeNode temp=null;
		takeToken();
		if (token.getSyn()==20) {
			System.out.print(token.getToken());
			temp=new treeNode(token);
			temp_node=new DefaultMutableTreeNode(new treeNode(token));
			temp.addTree(rightValue(),1);
			if (temp.getChild(1)!=null) {
				temp_node.add(node);
			}
			takeToken();
			if (token.getSyn()==22) {
				System.out.print(token.getToken());
			}else {
				System.out.println("��ֵ��������ȱ��;");
				System.exit(1);
				}
		}else {
			System.out.println("�Z�������e�`");
			System.exit(1);	
		}
		node =temp_node;
		return temp;
	}

	//print�Z��
	private treeNode print_stat() {
		takeToken();
		DefaultMutableTreeNode temp_node = new DefaultMutableTreeNode(new treeNode(token));
		treeNode temp=new treeNode(token);
		treeNode temp_treeNode=null;
		System.out.print(token.getToken());
		takeToken();
		if (token.getSyn()==23) {
			System.out.print(token.getToken());
			temp_treeNode=stmt();
			temp.addTree(temp_treeNode);
			if (temp_treeNode!=null) {
				temp_node.add(node);
				}
			takeToken();
			if (token.getSyn()==24) {
				System.out.print(token.getToken());
				takeToken();
				if (token.getSyn()==22) {
					System.out.print(token.getToken());
				}
				else {
					System.out.println("printȱ��;");
					System.exit(1);
				}
			}else {
				System.out.println("printȱ��)");
				System.exit(1);
			}
		}else {
			System.out.println("printȱ��(");
			System.exit(1);
		}
		node =temp_node;
		return temp;
	}
	//read�Z��
	private treeNode read_stat() {
		takeToken();
		DefaultMutableTreeNode temp_node = new DefaultMutableTreeNode(new treeNode(token));
		treeNode temp=new treeNode(token);
		System.out.print(token.getToken());
		takeToken();
		if (token.getSyn()==23) {
			System.out.print(token.getToken());
			takeToken();
			if (token.getSyn()==24) {
				System.out.print(token.getToken());
			}else {
				System.out.println("readȱ��)");
				System.exit(1);
			}
		}else {
			System.out.println("readȱ��(");
			System.exit(1);
		}
		node =temp_node;
		return temp;
	}
	
}