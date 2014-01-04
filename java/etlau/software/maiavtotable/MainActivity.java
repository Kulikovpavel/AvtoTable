package etlau.software.maiavtotable;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends ListActivity  {
    protected MaiAvtoDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new MaiAvtoDataSource(this);
        datasource.open();

        renew_list();

        ListView listView = this.getListView();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parentView, View childView, int position, long id) {
                confirmDeletion((Student) parentView.getItemAtPosition(position));
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parentView, View childView, int position, long id) {
                onEditClick((Student) parentView.getItemAtPosition(position));
            }
        });
    }

    private void confirmDeletion(final Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы уверены, что хотите удалить запись?")
                .setTitle("Подтвердите удаление");


        builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                datasource.deleteStudent(student);
                renew_list();
            }
        });
        builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getFilterString() {  // get filter for sqlite query
        String filter = "";  // StringBuilder will be better, but this is small task
        filter += MaiAvtoDB.KPP + " IN (";
        String comma = "";
        CheckBox mkpp = (CheckBox) findViewById(R.id.mkpp);
        if (mkpp.isChecked()) {
            comma = ", ";
            filter += "0";
        }
        CheckBox akpp = (CheckBox) findViewById(R.id.akpp);
        if (akpp.isChecked()) filter += comma + "1";
        filter += ") AND ";
        filter += MaiAvtoDB.EXAMTYPE + " IN (";

        comma = "";
        CheckBox theory = (CheckBox) findViewById(R.id.theory);
        if (theory.isChecked()) {
            comma = ", ";
            filter += "0";
        }
        CheckBox trainingGround = (CheckBox) findViewById(R.id.trainingGround);
        if (trainingGround.isChecked()) {
            filter += comma + "1";
            comma = ", ";
        }
        CheckBox city = (CheckBox) findViewById(R.id.city);
        if (city.isChecked()) filter += comma +"2";
        filter += ")";
        return filter;
    }

    protected void renew_list() {  // renew list after change
        String filter = getFilterString();
        List<Student> values = datasource.getAllStudents(filter);
        ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }


    public void onEditClick(Student student) {
        DialogFragment dialog = StudentDialogFragment.newInstance(student);
        dialog.show(getFragmentManager(), "NoticeDialogFragment");
    }

    public void onInsertClick(View v) {
        DialogFragment dialog = StudentDialogFragment.newInstance(null);
        dialog.show(getFragmentManager(), "NoticeDialogFragment");
    }

    public void onCheckBoxClick(View v) {
        renew_list();
    }
}
