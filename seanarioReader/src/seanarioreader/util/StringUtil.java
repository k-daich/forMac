/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seanarioreader.util;

/**
 *
 * @author USER
 */
public class StringUtil {

    /**
     * Stringの空判定
     * @param value
     * @return 
     */
    public static boolean isEmpty(String value) {
        return value == null || value.equals(value);
    }
}
