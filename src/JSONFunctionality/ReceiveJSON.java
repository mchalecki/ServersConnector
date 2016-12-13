/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSONFunctionality;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import org.json.JSONObject;
import org.json.JSONValue;

public class ReceiveJSON {
    
    private int type;
    private int ID;
    private JSONObject content;
    
    public void setContent()
    {
        if(type == 1)
        {
            String jsonContent = "{\"title\":,\"text\":}";
            content = new JSONObject(jsonContent);
        }
        if(type ==0)
        {
            String jsonContent = null;
            content = new JSONObject(jsonContent);
        }
    }
    
   public int getType()
   {
       return type;
   }
   public int getID()
   {
       return ID;
   }
  
   
   public JSONObject receive(String json, Socket socket)throws IOException
   {
       JSONObject obj = (JSONObject) JSONValue.parse(json);
       InputStream in = socket.getInputStream();
            ObjectInputStream i = new ObjectInputStream(in);
            
        type = obj.get("type").toString();
        ID = obj.get("ID").toInt();
        
        return obj;
   }
}
