/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.xservlets;

/**
 *
 * @author edward
 */
public class Notice {
  protected String type;
  private String content;
  public static String WARNING_TYPE="Warning";
  
  public Notice (String type, String content){
      this.type=type;
      this.content=content;
  }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
  
}
