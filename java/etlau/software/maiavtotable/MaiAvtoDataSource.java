package etlau.software.maiavtotable;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



public class MaiAvtoDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MaiAvtoDB dbHelper;
    private String[] allColumns = { "_id", MaiAvtoDB.NAME, MaiAvtoDB.GROUP, MaiAvtoDB.KPP,
            MaiAvtoDB.EXAMTYPE, MaiAvtoDB.DATE, MaiAvtoDB.COMMENTS };


    static String join(Collection<?> s, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iter = s.iterator();
        while (iter.hasNext()) {
            builder.append(iter.next());
            if (!iter.hasNext()) {
                break;
            }
            builder.append(delimiter);
        }
        return builder.toString();
    }

    public MaiAvtoDataSource(Context context) {
        dbHelper = new MaiAvtoDB(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Student createStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(MaiAvtoDB.NAME, student.name);
        values.put(MaiAvtoDB.GROUP, student.group);
        values.put(MaiAvtoDB.KPP, student.kpp);
        values.put(MaiAvtoDB.EXAMTYPE, student.examtype);
        values.put(MaiAvtoDB.DATE, student.__getExamDate());
        values.put(MaiAvtoDB.COMMENTS, student.comment);
        long insertId;
        if (student.id == 0) {  // insert new or update existing
            insertId = database.insert(MaiAvtoDB.TABLE_NAME, null, values);
        }
        else {
            database.update(MaiAvtoDB.TABLE_NAME, values, "_id" + " = " + student.id, null);
            insertId = student.id;
        }

        Cursor cursor = database.query(MaiAvtoDB.TABLE_NAME,
                allColumns, "_id" + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Student newStudent = cursorToStudent(cursor);
        cursor.close();
        return newStudent;
    }

    public void deleteStudent(Student student) {
        long id = student.id;
        System.out.println("Student deleted with id: " + id);
        database.delete(MaiAvtoDB.TABLE_NAME, "_id"
                + " = " + id, null);
    }

    public List<Student> getAllStudents(String filter) {
        List<Student> students = new ArrayList<Student>();

        String[] orderList = {MaiAvtoDB.KPP + " DESC", MaiAvtoDB.EXAMTYPE, MaiAvtoDB.NAME};
        String orderString = join(Arrays.asList(orderList), ", ");

        Cursor cursor = database.query(MaiAvtoDB.TABLE_NAME,
                allColumns, filter, null, null, null, orderString);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Student student = cursorToStudent(cursor);
            students.add(student);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return students;
    }

    private Student cursorToStudent(Cursor cursor) {
        Student student = new Student();
        student.id = cursor.getLong(0);
        student.name = cursor.getString(1);
        student.group = cursor.getString(2);
        student.kpp = cursor.getInt(3);
        student.examtype = cursor.getInt(4);
        student.__setExamdate(cursor.getString(5));
        student.comment = cursor.getString(6);
        return student;
    }
}