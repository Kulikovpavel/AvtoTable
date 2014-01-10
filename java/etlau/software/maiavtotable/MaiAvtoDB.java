package etlau.software.maiavtotable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MaiAvtoDB extends SQLiteOpenHelper {

    private static final int DB_VERSION = 8;
    private static final String DB_NAME = "maiavto";

    public static final String TABLE_NAME = "students";
    public static final String NAME = "name";
    public static final String GROUP = "studygroup";
    public static final String KPP = "kpp";
    public static final String EXAMTYPE = "examtype";
    public static final String DATE = "examdate";
    public static final String COMMENTS = "comments";
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( _id integer primary key autoincrement, "
            + NAME + " TEXT, " + GROUP + " TEXT, " + KPP + " INTEGER, "+ EXAMTYPE + " TEXT, " + DATE + " TEXT, " + COMMENTS + " TEXT);";
    private static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

    public MaiAvtoDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL(DROP_TABLE);
        //onCreate(sqLiteDatabase);
    }
}