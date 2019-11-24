package com.github.daich.makeSeanarioList.util.file.finder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class MyExcelFinder {

    /**
     * Invelidate Constructor
     */
    private MyExcelFinder() {
        // none
    }

    /**
     *
     * @param baseDir
     * @return
     */
    public static List<File> find(String baseDir) {
        List<File> excelFileList = new ArrayList<>();
        File dir = new File(baseDir);
        File[] files = dir.listFiles();

        for (File file : files) {
            System.out.println("find FileName :" + file.getName());
            // ファイルであるか、かつエクセルファイルであるか
            if (file.isFile()
                    && isExcel(file)) {
                System.out.println("〇 it is Excel File :" + file.getName());
                // エクセルファイルであればファイル一覧に追加する
                excelFileList.add(file);
            }
            else {
                System.out.println("× it is not Excel File :" + file.getName());
            }
        }
        return excelFileList;
    }

    /**
     * if ExcelFile Extension is excel , return true.
     *
     * @param file
     * @return isExcelFileExtension
     */
    public static boolean isExcel(File file) {
        String fileNameLowerCase = file.getName().toLowerCase();
        return fileNameLowerCase.endsWith(EXTENSION.XLS.getName())
                || fileNameLowerCase.endsWith(EXTENSION.XLSX.getName())
                || fileNameLowerCase.endsWith(EXTENSION.XLSM.getName());
    }

    /**
     * ファイルの拡張子を列挙した
     */
    private enum EXTENSION {
        XLS("xls"),
        XLSX("xlsx"),
        XLSM("xlsm");

        private final String name;

        EXTENSION(String name) {
            this.name = name;
        }
        /**
         * return name
         * @return name
         */
        public String getName() {
            return this.name;
        }
    }

}
