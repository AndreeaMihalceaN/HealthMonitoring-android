package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * Created by Andreea on 21.11.2017.
 */

public class Day {

    private Long id;
    private Calendar date;

    public Day() {

    }


    public Day(Long id, Calendar date) {
        this.id = id;
        this.date = date;
    }

    public Day(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(df.parse(date));
        this.date = cal;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
