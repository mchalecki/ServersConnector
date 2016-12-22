package tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    /**
     * Retrns ip which is in string.
     */
    public static String getIp(String message){
        String pattern = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(message);
        if(m.find())
            return m.group(1);
        else return null;
    }
}
