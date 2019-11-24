package com.github.daich.makeSeanarioList.util;

/**
 *
 * @author USER
 */
public class MyArrayUtil {

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
            if (MyStringUtil.isEmpty(value)) return true;
        }
        return false;
    }

    /**
     * 配列の中身が一覧化されるイイ感じの文字列を返す
     * @param array
     * @return 
     */
    public static String toString(String[] array) {
        int array_index = 1;
        StringBuilder sBuider = new StringBuilder();
        for(String ele : array) {
            sBuider.append("[").append(array_index++).append("] ").append(ele).append("\n");
        }
        return sBuider.toString();
    }
}
