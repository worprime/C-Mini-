package homework1;
import java.io.*;

public class filterResource {
	
	public String filter(String file) {
		String text =new String("");
		BufferedReader br = null;
		String line;
		StringBuffer sb = new StringBuffer();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
			while((line = br.readLine()) != null) {
			    sb.append(line+'\n');
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		char [] array = (new String(sb)).toString().toCharArray();
        int index=0;
		while (index < array.length) {
			if (array[index] == '/' && array[index+1] == '/') {
				index = index + 2;
				while (array[index]!='\n') {
					index++;
				}
				index++;
				continue;
			}
			if (array[index]=='/' && array[index+1]=='*') {
				index = index + 2;
				while (array[index]!='*' || array[index+1]!='/') {
					index++;
					if (index+1>array.length) {
						System.out.println("õ]”–’“µΩ*/");
						System.exit(1);
					}
				}
				index=index+2;
				continue;
			}
			if (array[index]!='\t'&&array[index]!='\r') {
				text=text+""+array[index];
				index++;
				continue;
			}
			index++;
		}
		return text;
	}
}
