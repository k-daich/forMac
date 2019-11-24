#!/bin/bash

### function Area
# echo(背景マゼンタ)
function bg_magenta_echo() {
    echo -e "\e[45m$*\e[m";
}

# echo(文字イエロー)
function yellow_echo() {
    echo -e "\e[1;33m$*\e[m";
}

# echo(文字レッド)
function red_echo() {
    echo -e "\e[1;31m$*\e[m";
}

### main Area
# shの位置を保存
SH_DIR=`pwd`

# シナリオExcelのリストを書き出すパスを取得(パス指定ファイルには相対パスを指定する)
OUTPUT_LIST_TXT_PATH=`head -1 ./OUTPUT_LIST_TXT_PATH.txt | xargs -I{} cygpath -w \`pwd\`/{}`

# シナリオExcelが置かれているパスを取得(パス指定ファイルには絶対パスを指定する)
SEANARIO_EXCEL_PATH=`head -1 ./SEANARIO_EXCEL_PATH.txt`

# java実行 エクセルのシート一覧を取得する
cd ../../makeSeanarioList/target
echo "java -jar upSeanarioLit-jar-with-dependencies.jar $OUTPUT_LIST_TXT_PATH $SEANARIO_EXCEL_PATH"
java -jar upSeanarioLit-jar-with-dependencies.jar $OUTPUT_LIST_TXT_PATH $SEANARIO_EXCEL_PATH
JAVA_SYSTEM_EXIT=$?

if [ $JAVA_SYSTEM_EXIT = 0 ]; then
    red_echo "シナリオExcelのリストアップ用jarの実行結果が異常です。有識者に連絡ください。"
    exit
fi

# shの位置に戻る
cd $SH_DIR

# javaで生成したエクセルのシート一覧をターミナルに表示
cat ./temp/scenarioList.txt | while read line
do
    # grepを使ってBook行なのかSheet行なのか判定
    echo -e "$line" | grep "^\[Book\]" >/dev/null
    if [[ $? == 0 ]];
    # Bookの場合
    then
        bg_magenta_echo $line
    # Sheetの場合
    else
        echo "    "$line
    fi
done

# 実行するシートを選んでもらう
echo -n "実行したいシート名のNoを入力してください。"
yellow_echo "  ※,区切りで複数指定可"
read -p " > " INP_SHEET_NO

# 繰り返し処理用にカンマ区切り形式をスペース形式に変換
FORMED_SHEET_NO=`echo $INP_SHEET_NO | sed "s/,/ /g"`

# Book一覧を格納する連想配列
declare -A bookList_

while read line
do
    BOOK_NUM=`echo $line | sed "s/:.*//"`
    BOOK_NAME=`echo $line | sed "s/^.*\[Book\] *//"`
    bookList_["$BOOK_NUM"]="$BOOK_NAME"
    #declare -p bookList_
done < <(grep -n "\[Book\]" ./temp/scenarioList.txt)


# 指定されたシートごとに繰り返し
for i in $FORMED_SHEET_NO ; do
    EXE_SHEET_NAME=`grep -E "^    $i   " ./temp/scenarioList.txt | sed "s/^    $i   //"`
    EXE_SHEET_LINE_NUM=`grep -nE "^    $i   " ./temp/scenarioList.txt | sed "s/:.*//" | sed "s/ //g"`
    EXE_BOOK_NAME=""
    for ((i=$EXE_SHEET_LINE_NUM; 0<i; i--));
    do
        EXE_BOOK_NAME=${bookList_[$i]}
        #echo "[value] bookList_ : $EXE_BOOK_NAME"
        if [ -n "$EXE_BOOK_NAME" ];
        then
            echo "[value] EXE_BOOK_NAME=$EXE_BOOK_NAME"
            break;
        fi
    done

    # セレニウム実行
    echo "java -jar -cp Main $EXE_BOOK_NAME $EXE_SHEET_NAME"
done

echo "[END]"


