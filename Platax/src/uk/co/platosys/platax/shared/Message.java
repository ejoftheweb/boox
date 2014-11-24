package uk.co.platosys.platax.shared;

public class Message implements java.io.Serializable{
private String text;
	public Message() {
		
	}
    public Message(String text){
    	this.text=text;
    }
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
