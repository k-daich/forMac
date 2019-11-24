/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seanarioreader.util.file.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author USER
 */
public class MyFileWriter {

    /**
     * Invalidate Construcotr
     */
    private MyFileWriter() {}

    /**
     * 指定の出力先に指定の内容を出力する
     *
     * @param fileName
     * @param content
     */
    public static void write(String fileName, String content) {
        try (PrintWriter writer = 
                new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8")))) {
            writer.write(content);
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException("failed to create version.txt. dest : " + fileName);
        }
    }

}
