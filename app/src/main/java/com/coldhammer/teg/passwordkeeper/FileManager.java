package com.coldhammer.teg.passwordkeeper;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.coldhammer.teg.passwordkeeper.CustomAdaptor.CustomAdaptor;
import com.coldhammer.teg.passwordkeeper.CustomAdaptor.CustomAdaptorItem;
import com.coldhammer.teg.passwordkeeper.Serializer.GObjectSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Coldhammer on 12/31/2019.
 */

public class FileManager {
    public static final String FILE_NAME = "password.txt";
    //TODO: I have to refactor the converter
    Context context;
    PasswordItem[] passwordItems;

    public FileManager(Context context)
    {
        this.context = context;
    }

    public PasswordItem[] getPasswordItems()
    {
        return passwordItems;
    }

    public GObjectSerializer read() {
        try {
            File file = new File(context.getApplicationContext().getFilesDir(), FILE_NAME);
            if(file.exists()==false) return new GObjectSerializer();
            FileInputStream fileInputStream= context.openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while ((lines = bufferedReader.readLine())!=null) {
                if(lines == "") continue;
                return new GObjectSerializer(lines);
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new GObjectSerializer();
    }

    public void write(GObjectSerializer objectSerializer) {
        try {
            File file = new File(context.getApplicationContext().getFilesDir(), FILE_NAME);
            if(file.exists()!=false) file.createNewFile();
            FileOutputStream fileOutputStream = context.openFileOutput(FILE_NAME,MODE_PRIVATE);
            fileOutputStream.write(objectSerializer.toString().getBytes("UTF-8"));
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
