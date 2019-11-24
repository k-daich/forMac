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
public class ArrayUtil {

    /**
     * 配列の空判定
     *  ※いずれかの要素が空ならtrue
     *  ※配列自体がnullならtrue
     * @param array
     * @return 
     */
    public static boolean isAnyoneEmpty(String[] array) {
        // 配列がnullならtrue
        if(array == null) return true;
        // 配列の要素のいずれかが空文字ならtrue
        for (String value : array) {
            if (StringUtil.isEmpty(value)) return true;
        }
        return false;
    }
}
