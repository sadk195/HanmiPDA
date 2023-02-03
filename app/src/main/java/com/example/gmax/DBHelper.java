package com.example.gmax;

/* EM_KOREA 소스를 가져옴 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 UNIT_CONFIG, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        WEB_SERVICE_URL 문자열 컬럼으로 구성 된 테이블을 생성. */
        db.execSQL("CREATE TABLE UNIT_CONFIG (idx INTEGER PRIMARY KEY AUTOINCREMENT, WEB_SERVICE_URL TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String WEB_SERVICE_URL) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO UNIT_CONFIG VALUES(null, '" + WEB_SERVICE_URL + "')");
        db.close();
    }

    public void update(String WEB_SERVICE_URL,int idx) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE UNIT_CONFIG SET WEB_SERVICE_URL=" + WEB_SERVICE_URL + " WHERE idx ='" + idx + "';");
        db.close();
    }

    public void delete(String WEB_SERVICE_URL) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM UNIT_CONFIG WHERE WEB_SERVICE_URL='" + WEB_SERVICE_URL + "';");
        db.close();
    }

    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM UNIT_CONFIG", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " : "
                    + cursor.getString(1)
                    + "\n";
        }

        return result;
    }

}
