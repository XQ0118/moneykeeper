package cn.edu.hznu.moneykeeper.Util;

import cn.edu.hznu.moneykeeper.R;

public class InsertImage {

    private int icon_id;



    public static int insertIcon(int id){
        int a = R.mipmap.type_eat;
        int b = R.mipmap.type_calendar;
        int c = R.mipmap.type_3c;
        int d = R.mipmap.type_candy;
        int e = R.mipmap.type_pill;
        int f = R.mipmap.type_movie;
        int g = R.mipmap.type_wallet;
        int h = R.mipmap.type_unexpected_income;
        int i = R.mipmap.type_clothes;

        int img = a;
        switch (id){
            case 0:
                img = a;
                break;
            case 1:
                img = b;
                break;
            case 2:
                img = c;
                break;
            case 3:
                img = d;
                break;
            case 4:
                img = e;
                break;
            case 5:
                img = f;
                break;
            case 6:
                img = g;
                break;
            case 7:
                img = h;
                break;
            case 8:
                img = i;
                break;
        }
        return img;
    }

}
