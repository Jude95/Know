package com.jude.know.util;

/**
 * Created by Mr.Jude on 2015/4/29.
 */
public class RecentDateFormater implements TimeTransform.DateFormater{
    @Override
    public String format(TimeTransform date, long delta) {
        if (delta>0){
            if (delta / TimeTransform.SECOND < 1){
                return delta +"秒前";
            }else if (delta / TimeTransform.HOUR < 1){
                return delta / TimeTransform.SECOND+"分钟前";
            }else if (delta / TimeTransform.DAY < 2 && new TimeTransform().getDay() == date.getDay()){
                return delta / TimeTransform.HOUR+"小时前";
            }else if (delta / TimeTransform.DAY < 3 && new TimeTransform().getDay() == new TimeTransform(date.getTimestamp()+TimeTransform.DAY).getDay()){
                return "昨天"+date.toString("HH:mm");
            }else if (delta / TimeTransform.DAY < 4 && new TimeTransform().getDay() == new TimeTransform(date.getTimestamp()+TimeTransform.DAY*2).getDay()){
                return "前天"+date.toString("HH:mm");
            }else{
                return date.toString("yyyy/MM/dd  hh:mm");
            }
        }else{
            delta = -delta;
            if (delta / TimeTransform.SECOND < 1){
                return delta +"秒后";
            }else if (delta / TimeTransform.HOUR < 1){
                return delta / TimeTransform.SECOND+"分钟后";
            }else if (delta / TimeTransform.DAY > -2 && new TimeTransform().getDay() == date.getDay()){
                return delta / TimeTransform.HOUR+"小时后";
            }else if (delta / TimeTransform.DAY > -3 && new TimeTransform().getDay() == new TimeTransform(date.getTimestamp()-TimeTransform.DAY).getDay()){
                return "明天"+date.toString("HH:mm");
            }else if (delta / TimeTransform.DAY > -4 && new TimeTransform().getDay() == new TimeTransform(date.getTimestamp()-TimeTransform.DAY*2).getDay()){
                return "后天"+date.toString("HH:mm");
            }else{
                return date.toString("yyyy/MM/dd  hh:mm");
            }
        }
    }
}
