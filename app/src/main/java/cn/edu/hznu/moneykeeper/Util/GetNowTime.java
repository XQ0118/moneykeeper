package cn.edu.hznu.moneykeeper.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

//获取详细时间戳
public class GetNowTime {

    public static String getInfoTime(){
        String time_str = null;
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        time_str = df.format(d);// new Date()为获取当前系统时间
        return time_str;
    }

}
