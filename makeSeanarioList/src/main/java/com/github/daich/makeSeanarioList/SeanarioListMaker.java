package com.github.daich.makeSeanarioList;

import com.github.daich.makeSeanarioList.util.MyArrayUtil;
import com.github.daich.makeSeanarioList.util.file.finder.MyExcelFinder;
import com.github.daich.makeSeanarioList.util.file.reader.MyExcelReader;
import com.github.daich.makeSeanarioList.util.file.writer.MyFileWriter;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author USER
 */
public class SeanarioListMaker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // java引数チェック
            assertArgs(args);
            // java引数1；シナリオリストの出力先指定
            final String puttingListTxtPath = args[0]; // TODO: 後で決める
            // java引数2；読み取るシナリオが置かれている場所
            final String seanariosPath = args[1]; // "C:\\netbeans\\projects\\fg-bookie\\designDoc\\01_DB設計\\entity"

            // 指定したPathに存在するブック情報をMap形式で取得
            // Mapのキー：ブック名
            // Mapのバリュー：シート名のリスト
            Map<String, List<String>> excelBooks = getSeanarioList(seanariosPath);

            // シナリオリストをファイルに書き出す
            MyFileWriter.write(puttingListTxtPath,
                    // Map情報を元にシナリオリストのテキスト内容を構築する
                    buildSeanarioListTxt(excelBooks));

        } // 何らかのエラーが発生した場合
        catch (Exception e) {
            e.printStackTrace();
            // システム結果コード：0（異常終了）を設定する
            System.exit(0);
        }
        // システム結果コード：1（正常終了）を設定する
        System.exit(1);
    }

    /**
     * java引数チェック
     *
     * @param args
     */
    private static void assertArgs(String[] args) {
        int expect_argsAmount = 2;
        // 引数の数チェック
        if (args.length != expect_argsAmount) {
            throw new RuntimeException("java引数の数が想定外。[想定] " + expect_argsAmount + "[実際] " + args.length);
        }
        // 引数の各要素の空チェック
        if (MyArrayUtil.isAnyoneEmpty(args)) {
            throw new RuntimeException("java引数のいずれかが空。\n" + MyArrayUtil.toString(args));
        }
    }

    /**
     * エクセルブックのマップ情報を元にシナリオリストのテキスト内容を構築する
     *
     * @param excelBooks
     * @return シナリオリストのテキスト内容
     */
    private static String buildSeanarioListTxt(Map<String, List<String>> excelBooks) {
        StringBuilder sBuilder = new StringBuilder();
        int listIndex = 1;

        // ブック単位に繰り返し
        for (String bookName : excelBooks.keySet()) {
//            System.out.println("FileWrite for bookName -> " + bookName);
            sBuilder.append("[Book] ").append(bookName).append("\n");
            // シート単位に繰り返し
            for (String sheetName : excelBooks.get(bookName)) {
//                System.out.println("FileWrite for sheetName -> " + bookName);
                sBuilder.append("    ").append(listIndex++).append("   ").append(sheetName).append("\n");
            }
            sBuilder.append("\n");
        }
        return sBuilder.toString();
    }

    /**
     *
     * @param seanariosPath
     * @return
     */
    private static Map<String, List<String>> getSeanarioList(String seanariosPath) {
        // 取得したエクセルリストを格納するMap
        // Mapのキー：ブック名
        // Mapのバリュー：シート名のリスト
        Map<String, List<String>> excelBooks = new HashMap<>();
        // 指定されたPathに存在するブックたちを取得
        List<File> files = MyExcelFinder.find(seanariosPath);

        // ブック単位に繰り返す
        files.forEach((file) -> {
//            System.out.println("file Name : " + file.getName());
            // ファイル名×シート名リストでMapに格納する
            excelBooks.put(file.getName(), MyExcelReader.getSheetNames(file));
        });

        return excelBooks;
    }

}
