package cn.fdongl.meme.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = format.parse("2017/2/4");
            Date d = Calendar.getInstance().getTime();
            System.out.println(d);
            date.setTime(date.getTime()+7*24*60*60*1000);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
