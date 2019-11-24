package com.github.daich.makeSeanarioList.util.file.writer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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
