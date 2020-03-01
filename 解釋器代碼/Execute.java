package homework1;
import homework1.Token;
import java.util.List;
import homework1.Lexical;
import homework1.filterResource;

public class Execute {
	
	
	public static void main(String[] args) {
		filterResource filt =new filterResource();
		String text=filt.filter("C:\\Users\\junow\\eclipse-workspace\\homework1\\src\\test.txt");
		List<Token> list=(new Lexical(text)).scanner();
		grammar gram_ana=new grammar(list);
		treeNode tree=gram_ana.parser();
		new semantics(tree);
	}
}
