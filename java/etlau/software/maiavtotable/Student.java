package etlau.software.maiavtotable;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Student {
    public long id;
    public String comment = "";
    public String group = "";
    public Integer kpp = 0;
    public Integer examtype = 0;
    public Calendar examdate = Calendar.getInstance();

    public String name= "";
    private final String[] EXAMTYPE = {"Теория", "Площадка", "Город"};
    private final String[] KPPTYPE = {"МКПП", "АКПП"};
    public final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return name + " " + group + " " + KPPTYPE[kpp] +  " " + EXAMTYPE[examtype] + " " + __getExamDate() + " " + comment;
    }

    public void __setExamdate(String examdate) {
        try {
            this.examdate.setTime(dateFormat.parse(examdate));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            this.examdate = Calendar.getInstance();
        }

    }
    public String  __getExamDate() {
        return dateFormat.format(examdate.getTime());
    }

}