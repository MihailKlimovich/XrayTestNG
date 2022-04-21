package dates;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Dates {

    public String todayDate1;
    public String todayDate2;

    public String generateTodayDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        Date d = c.getTime();
        todayDate1 = format.format(d);
        return todayDate1;
    }

    public String generateTodayDate_plus(Integer Month, Integer Day){
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, + Month);
        c.add(Calendar.DAY_OF_WEEK, + Day);
        String Res = format.format(c.getTime());
        return Res;
    }



    public String generateTodayDate2(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        Date d = c.getTime();
        todayDate2 = format.format(d);
        return todayDate2;
    }



    public String generateTodayDate2_minus(Integer Month, Integer Day){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, - Month);
        c.add(Calendar.DAY_OF_WEEK, - Day);
        String Res = format.format(c.getTime());
        return Res;
    }

    public String generateTodayDate2_plus(Integer Month, Integer Day){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, + Month);
        c.add(Calendar.DAY_OF_WEEK, + Day);
        String Res = format.format(c.getTime());
        return Res;
    }

    public String generateDate_minus(Integer Month, Integer Day){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, - Month);
        c.add(Calendar.DAY_OF_WEEK, - Day);
        String Res = format.format(c.getTime());
        return Res;
    }

    public String generateDate_plus(Integer Month, Integer Day){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, + Month);
        c.add(Calendar.DAY_OF_WEEK, + Day);
        String Res = format.format(c.getTime());
        return Res;
    }

    public String generateTodayDate3_plus(Integer Month, Integer Day){
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, + Month);
        c.add(Calendar.DAY_OF_WEEK, + Day);
        String Res = format.format(c.getTime());
        return Res;
    }

    public String generateDate_plus4(Integer Month, Integer Day){
        SimpleDateFormat format = new SimpleDateFormat("M/dd/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, + Month);
        c.add(Calendar.DAY_OF_WEEK, + Day);
        String Res = format.format(c.getTime());
        return Res;
    }

    public String generateDate_plus5(Integer Month, Integer Day){
        SimpleDateFormat format = new SimpleDateFormat("ddMMYYYY");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, + Month);
        c.add(Calendar.DAY_OF_WEEK, + Day);
        String Res = format.format(c.getTime());
        return Res;
    }

    public String generateDate_plus6(Integer Month, Integer Day){
        SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, + Month);
        c.add(Calendar.DAY_OF_WEEK, + Day);
        String Res = format.format(c.getTime());
        return Res;
    }

    public String generateDateForPreviewSchedule_ddMMMyyyy(Integer Month, Integer Day){
        SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy", Locale.US);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, + Month);
        c.add(Calendar.DAY_OF_WEEK, + Day);
        String Res = format.format(c.getTime());
        System.out.println("Generated Date: " + Res);
        return  Res;
    }

    public String generateDate_yyyyMMdd(Integer Month, Integer Day){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, + Month);
        c.add(Calendar.DAY_OF_WEEK, + Day);
        Date d = c.getTime();
        String Res = format.format(d);
        System.out.println("Generated Date: " + Res);
        return Res;
    }

    public String generateDate_yyyyMMdd_withStaticDay(Integer Month, Integer Day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, + Month);
        c.set(Calendar.DAY_OF_MONTH, Day);

        Date d = c.getTime();
        String Res = format.format(d);
        return Res;
    }



}
