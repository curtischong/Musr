package com.example.daniel.museapp.utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.IOException;
public class Save
{
    private String filename;
    private PrintWriter writer;

    public Save(String filename) {
        try {
            this.filename = filename;
            writer = new PrintWriter(filename, "UTF-8");
        } catch (IOException e) {
            Log.w("Save", "Well, couldn't save: " + filename);
        }
    }

    public void write(String line)  {
        writer.println(line);

    }

    public void close() {
        writer.close();
    }

}