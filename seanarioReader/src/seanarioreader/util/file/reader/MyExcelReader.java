package seanarioreader.util.file.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author USER
 */
public class MyExcelReader {

    /**
     * Invalidate Constructor
     */
    private MyExcelReader() {
        // none
    }

    /**
     * 1ブック内のリスト一覧を取得
     * @param file
     * @return シート名のリスト
     */
    public static List<String> getSheetNames(File file) {
        // poiを利用するのでWorkbookオブジェクトを取得する
        Workbook workbook = getWorkBook(file);

        // シート名一覧リスト(初期化)
        List<String> sheetList = new ArrayList<>();

        Iterator<Sheet> itr = workbook.iterator();

        // シートがあるだけ繰り返し
        while (itr.hasNext()) {
            // シート名を一覧に追加する
            sheetList.add(itr.next().getSheetName());
        }
        return sheetList;
    }

    /**
     * Workbookオブジェクトを取得する
     * @param file
     * @return workbook
     */
    private static List<String> getWorkBook(File file) {
        try {
            // java.io.Fileから
            return WorkbookFactory.create(new FileInputStream(file));
        } catch (IOException | EncryptedDocumentException ex) {
            throw new RuntimeException("Excelの読み込み失敗：" + file.getName() + "\n" + ex);
        }
    }
}
