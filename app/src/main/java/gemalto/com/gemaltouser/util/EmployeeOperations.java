package gemalto.com.gemaltouser.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuramv on 9/8/2018.
 */

public class EmployeeOperations {
    public static final String LOGTAG = "EMP_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            DbHelper.COLUMN_ID,
            DbHelper.COLUMN_FIRST_NAME,
            DbHelper.COLUMN_LAST_NAME,
            DbHelper.COLUMN_GENDER,
            DbHelper.COLUMN_EMAIL,
            DbHelper.COLUMN_DOB

    };

    public EmployeeOperations(Context context){
        dbhandler = new DbHelper(context);
    }

    public void open(){
        Log.i(LOGTAG,"Database Opened");
        database = dbhandler.getWritableDatabase();


    }
    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();

    }
    public Employee addEmployee(Employee Employee){
        ContentValues values  = new ContentValues();
        values.put(DbHelper.COLUMN_ID,Employee.getEmpId());
        values.put(DbHelper.COLUMN_FIRST_NAME,Employee.getFirstname());
        values.put(DbHelper.COLUMN_LAST_NAME,Employee.getLastname());
        values.put(DbHelper.COLUMN_GENDER, Employee.getGender());
        values.put(DbHelper.COLUMN_EMAIL, Employee.getEmail());
        values.put(DbHelper.COLUMN_DOB, Employee.getDob());
        long insertid = database.insert(DbHelper.TABLE_EMPLOYEES,null,values);
        Employee.setEmpId(insertid);
        return Employee;

    }

    // Getting single Employee
    public Employee getEmployee(long id) {

        Cursor cursor = database.query(DbHelper.TABLE_EMPLOYEES,allColumns,DbHelper.COLUMN_ID + "=?",new String[]{String.valueOf(id)},null,null, null, null);
        if (cursor != null )
            cursor.moveToFirst();

        Employee e = new Employee(Long.parseLong(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
        // return Employee
        return e;
    }

    public ArrayList<Employee> getAllEmployees() {

        Cursor cursor = database.query(DbHelper.TABLE_EMPLOYEES,allColumns,null,null,null, null, null);

        ArrayList<Employee> employees = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Employee employee = new Employee();
                employee.setEmpId(cursor.getLong(cursor.getColumnIndex(DbHelper.COLUMN_ID)));
                employee.setFirstname(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_FIRST_NAME)));
                employee.setLastname(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_LAST_NAME)));
                employee.setGender(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_GENDER)));
                employee.setEmail(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_EMAIL)));
                employee.setDob(cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_DOB)));
                employees.add(employee);
            }
        }
        // return All Employees
        return employees;
    }




    // Updating Employee
    public int updateEmployee(Employee employee) {

        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_FIRST_NAME, employee.getFirstname());
        values.put(DbHelper.COLUMN_LAST_NAME, employee.getLastname());
        values.put(DbHelper.COLUMN_GENDER, employee.getGender());
        values.put(DbHelper.COLUMN_EMAIL, employee.getEmail());
        values.put(DbHelper.COLUMN_DOB, employee.getDob());

        // updating row
        return database.update(DbHelper.TABLE_EMPLOYEES, values,
                DbHelper.COLUMN_ID + "=?",new String[] { String.valueOf(employee.getEmpId())});
    }

    // Deleting Employee
    public void removeEmployee(Employee employee) {

        database.delete(DbHelper.TABLE_EMPLOYEES, DbHelper.COLUMN_ID + "=" + employee.getEmpId(), null);
    }
}
