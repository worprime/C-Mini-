package homework1;

public class Token {
	private int syn;
	private String token;
	private int row=0;
	Token(String _token,int _syn,int _row) {
		this.token = _token;
		this.syn = _syn;
		this.row = _row;
	}
	Token(String _token,int _syn) {
		this.token = _token;
		this.syn = _syn;
	}
	int getSyn() {
		return syn;
	}
	
	String getToken() {
		return token;
	}
	
	int getRow() {
		return row;
	}
}
