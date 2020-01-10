package com.coldhammer.teg.passwordkeeper.Serializer;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class GObjectSerializer extends ArrayList<HashMap<String, String>> {
    private String text;
    public GObjectSerializer(String text)
    {
        this.text = text;
        createList();
    }

    public GObjectSerializer() {}


    private void createList()
    {
        String[] lines = text.split(";");

        for(String line:lines)
        {
            add(createHashMap(line));
        }
    }

    public String getString(int index, String key)
    {
        String value = get(index).get(key);
        if(value == null) value = "";
        return value;
    }

    public void add(GObject object)
    {
        add(object.keyValueToSerialize());
    }

    public void update(int index, HashMap<String, String> dataSet)
    {
        set(index, dataSet);
    }

    @Override
    public String toString()
    {
      if(size() == 0) return "";
      StringBuffer buffer = new StringBuffer();
      for(HashMap<String, String> keyPairs : this)
      {
          buffer = keySetToStringBuffer(keyPairs, buffer);
          buffer.append(";");
      }
      buffer.deleteCharAt(buffer.length()-1);
      return buffer.toString();
    }

    public static StringBuffer keySetToStringBuffer(HashMap<String, String> keySet, StringBuffer stringBuffer)
    {
        if(keySet.size()==0) return new StringBuffer();
        StringBuffer buffer = stringBuffer;
        if(buffer == null) buffer = new StringBuffer();
        for(String key: keySet.keySet())
        {
            buffer.append(String.format("%s:%s", getEscapedString(key, 0), getEscapedString(keySet.get(key), 0)));
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.length()-1);
        return buffer;
    }
    public static HashMap<String, String> createHashMap(String data)
    {
        String[] keyValuePairs = data.split(",");
        HashMap<String, String> hashMap = new HashMap<>();
        for(String keyValuePair : keyValuePairs)
        {
            String[] seperatedPair = keyValuePair.split(":");
            if(seperatedPair.length != 2) continue;
            String key = getEscapedString(seperatedPair[0], 1);
            String value = getEscapedString(seperatedPair[1] , 1);
            hashMap.put(key, value);
        }
        return hashMap;
    }
    private static String[][] escapingCharacters = {{",", ";", ":"},{"&$44", "&$59", "&$58"}};
    private static String getEscapedString(String string, int reverse)
    {
        //TODO: Escape these String
        String text = string;
        for(int i = 0; i < escapingCharacters[0].length; i++)
        {
            text = text.replace(escapingCharacters[0 + reverse][i], escapingCharacters[1 - reverse][i]);
        }
        return text;
    }
}
