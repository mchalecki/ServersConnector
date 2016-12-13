/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSONFunctionality;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.json.JSONObject;

public class SendJSON {
    
    private int type;
    private int ID;
    private JSONObject content;
    JSONObject obj = new JSONObject();
    
   public void setType(int _type)
   {
       this.type = _type;
   }
   public void setID(int _ID)
   {
       this.ID = _ID;
   }
   
   public void createConetent(String title, String text)
   {
       JSONObject temp = new JSONObject();
       temp.put("title", title);
       temp.put("text", text);
       content = temp;
   }
   public JSONObject createJSON()
   {
        obj.put("type", type);
        obj.put("id", ID);
        return obj;
   }
   
   public void send(JSONObject jsonObject, Socket socket) throws IOException
   {    
   String strJson = jsonObject.toString();    
   OutputStream out = socket.getOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(out);
        o.writeObject(strJson);
    out.flush();
   }
}
