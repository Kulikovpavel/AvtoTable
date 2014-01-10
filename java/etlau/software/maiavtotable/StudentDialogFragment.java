package etlau.software.maiavtotable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;

public class StudentDialogFragment extends DialogFragment {

    static StudentDialogFragment newInstance(Student student) {
        if (student == null) {
            student = new Student();
        }

        StudentDialogFragment f = new StudentDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("_id", student.id);
        args.putString(MaiAvtoDB.NAME, student.name);
        args.putString(MaiAvtoDB.GROUP, student.group);
        args.putInt(MaiAvtoDB.KPP, student.kpp);
        args.putInt(MaiAvtoDB.EXAMTYPE, student.examtype);
        args.putString(MaiAvtoDB.DATE, student.__getExamDate());
        args.putString(MaiAvtoDB.COMMENTS, student.comment);

        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.edit_dialog, null);

        int checkedIndexKPP = getArguments().getInt(MaiAvtoDB.KPP);
        if (checkedIndexKPP == 0) {
            RadioButton button = (RadioButton)dialogView.findViewById(R.id.radioButtonMKPP);
            button.setChecked(true);
        }
        else {
            RadioButton button = (RadioButton)dialogView.findViewById(R.id.radioButtonAKPP);
            button.setChecked(true);
        }

        int checkedIndexExam = getArguments().getInt(MaiAvtoDB.EXAMTYPE);
        if (checkedIndexExam == 0) {
            RadioButton button = (RadioButton)dialogView.findViewById(R.id.radioButtonTheory);
            button.setChecked(true);
        }
        else if (checkedIndexExam == 1) {
            RadioButton button = (RadioButton)dialogView.findViewById(R.id.radioButtonPractice);
            button.setChecked(true);
        }
        else {
            RadioButton button = (RadioButton)dialogView.findViewById(R.id.radioButtonCity);
            button.setChecked(true);
        }

        TextView nameView = (TextView) dialogView.findViewById(R.id.editTextName);
        nameView.setText(getArguments().getString(MaiAvtoDB.NAME));

        TextView groupView = (TextView) dialogView.findViewById(R.id.editTextGroup);
        groupView.setText(getArguments().getString(MaiAvtoDB.GROUP));

        TextView commentView = (TextView) dialogView.findViewById(R.id.editComment);
        commentView.setText(getArguments().getString(MaiAvtoDB.COMMENTS));

        builder.setView(dialogView);

        DatePicker dateView = (DatePicker) dialogView.findViewById(R.id.datePicker);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Student.dateFormat.parse(getArguments().getString(MaiAvtoDB.DATE)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateView.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        builder.setView(dialogView);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Student student = new Student();

                student.id = getArguments().getLong("_id");

                RadioGroup radioGroupKPP = (RadioGroup) dialogView.findViewById(R.id.radioKPP);
                int checkedKppID = radioGroupKPP.getCheckedRadioButtonId();
                View checkedRadioKpp = dialogView.findViewById(checkedKppID);
                student.kpp = radioGroupKPP.indexOfChild(checkedRadioKpp);

                RadioGroup radioGroupExam = (RadioGroup) dialogView.findViewById(R.id.radioExamtype);
                int checkedExamtypeID = radioGroupExam.getCheckedRadioButtonId();
                View checkedExamtype = dialogView.findViewById(checkedExamtypeID);
                student.examtype = radioGroupExam.indexOfChild(checkedExamtype);

                TextView nameView = (TextView) dialogView.findViewById(R.id.editTextName);
                student.name = nameView.getText().toString();

                TextView groupView = (TextView) dialogView.findViewById(R.id.editTextGroup);
                student.group = groupView.getText().toString();

                TextView commentView = (TextView) dialogView.findViewById(R.id.editComment);
                student.comment = commentView.getText().toString();

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                student.examdate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                MainActivity callingActivity = (MainActivity) getActivity();
                callingActivity.datasource.createStudent(student);
                callingActivity.renew_list();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        // Create the AlertDialog
        return builder.create();
    }
}