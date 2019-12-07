#!/bin/bash

### util function Area
# echo(文字イエロー)
function yellow_echo() {
    echo -e "\e[1;33m$*\e[m";
}

# echo(文字レッド)
function red_echo() {
    echo -e "\e[1;31m$*\e[m";
}

# sed(背景マゼンタ)
# 引数1: 対象ファイル名
# 引数2: 色付け対象キーワード
function bg_magenta_sed() {
    sed "s/\($2\)/\o033[45m\1\o033[m/g" $1
}

### scenario function Area
# Basic認証を標準入力情報をもとに設定する
function set_basicAuthInfo() {
    # Basic認証情報の更新が必要か質問する
    yellow_echo -n "Basic認証情報を変更するなら'y'を入力ください。"
    read -p " > " INP_BASIC_SETUP_UMU

    # Yの場合
    if [ x$INP_BASIC_SETUP_UMU = 'xy' ]; then
        set_basicAuth
    else
        echo "[Notice] Basic認証情報の変更をスキップしました。"
    fi

}

# Basic認証を標準入力情報をもとに設定する
function set_basicAuth() {
    yellow_echo -n "試験対象のドメインを入力ください。プロトコルやポートは不要。 (例：google.com)"
    read -p " > " INP_DOMAIN
    yellow_echo -n "Basic認証のユーザ名を入力ください。"
    read -p " > " INP_USER_NAME
    yellow_echo -n "Basic認証のパスワードを入力ください。"
    read -p " > " INP_PSWD

    BASE64_ENC_STRING=`echo $INP_USER_NAME:INP_PSWD | base64`
    # echo $BASE64_ENC_STRING
    BASIC_AUTH_STRING="request_header_add Authorization \"Basic $BASE64_ENC_STRING\" test_site #replace by shell"
    
    sed -e "s/^request_header_add Authorization.*test_site #replace by shell/${BASIC_AUTH_STRING}/" /cygdrive/c/work/tools/Squid/etc/squid/squid.conf
}

# エクセルを読取り、セレニウムシナリオのリストを作成する
function create_excelList() {

    # シナリオExcelのリストを書き出すパスを取得(パス指定ファイルには相対パスを指定する)
    OUTPUT_LIST_TXT_PATH=`head -1 ./OUTPUT_LIST_TXT_PATH.txt | xargs -I{} cygpath -w \`pwd\`/{}`

    # シナリオExcelが置かれているパスを取得(パス指定ファイルには絶対パスを指定する)
    SEANARIO_EXCEL_PATH=`head -1 ./SEANARIO_EXCEL_PATH.txt`

    # java実行 エクセルのシート一覧を取得する
    cd ../../makeSeanarioList/target
    # echo "java -jar upSeanarioLit-jar-with-dependencies.jar $OUTPUT_LIST_TXT_PATH $SEANARIO_EXCEL_PATH"
    java -jar upSeanarioLit-jar-with-dependencies.jar $OUTPUT_LIST_TXT_PATH $SEANARIO_EXCEL_PATH

    JAVA_SYSTEM_EXIT=$?

    if [ $JAVA_SYSTEM_EXIT = 0 ]; then
        red_echo "シナリオExcelのリストアップ用jarの実行結果が異常です。有識者に連絡ください。"
        exit
    fi
}

# 指定されたNoのセレニウムを実行する
# 引数1: 指定されたNo（半角スペース区切り）
function exec_selenium() {
    # 繰り返し処理用にカンマ区切り形式をスペース形式に変換
    FORMED_SHEET_NO=`echo $1 | sed "s/,/ /g"`

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
}


### main Area
# shの位置を保存
SH_DIR=`pwd`

## ファンクション呼出 Basic情報の設定
set_basicAuthInfo

## ファンクション呼出 エクセルを読取り、シナリオリストを作成する
create_excelList

# shの位置に戻る
cd $SH_DIR

# javaで生成したエクセルのシート一覧をターミナルに表示
bg_magenta_sed ./temp/scenarioList.txt '^\[Book\].*'

# 実行するシートを選んでもらう
echo -n "実行したいシート名のNoを入力してください。"
yellow_echo "  ※,区切りで複数指定可"
read -p " > " INP_SHEET_NO

## ファンクション 選択されたシートを対象にセレニウムを実行
exec_selenium $INP_SHEET_NO

echo "[END]"


