package cn.edu.hznu.moneykeeper.Util;

import cn.edu.hznu.moneykeeper.R;

public class InsertImage {

    //expend and income icon
    public static int insertIcon(int id){
        //支出
        int a = R.mipmap.type_eat;
        int b = R.mipmap.type_calendar;
        int c = R.mipmap.type_3c;
        int d = R.mipmap.type_candy;
        int e = R.mipmap.type_pill;
        int f = R.mipmap.type_movie;
        int g = R.mipmap.type_wallet;
        int h = R.mipmap.type_unexpected_income;
        int i = R.mipmap.type_clothes;

        //收入
        int j = R.mipmap.type_pluralism;
        int k = R.mipmap.type_handling_fee;
        int l = R.mipmap.type_unexpected_income;
        int m = R.mipmap.type_salary;

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
            case 100:
                img = j;
                break;
            case 101:
                img = k;
                break;
            case 102:
                img = l;
                break;
            case 103:
                img = m;
                break;
        }
        return img;
    }

    // months icons
    public static int insertMonthsIcon(int id){
        //支出
        int a = R.mipmap.month_1;
        int b = R.mipmap.month_2;
        int c = R.mipmap.month_3;
        int d = R.mipmap.type_candy;
        int e = R.mipmap.type_pill;
        int f = R.mipmap.type_movie;
        int g = R.mipmap.type_wallet;
        int h = R.mipmap.type_unexpected_income;
        int i = R.mipmap.type_clothes;
        int j = R.mipmap.type_pluralism;
        int k = R.mipmap.month_11;
        int l = R.mipmap.month_12;


        int img = a;
        switch (id){
            case 0:
                img = a; //1
                break;
            case 1:
                img = b;  //2
                break;
            case 2:
                img = c;  //3
                break;
            case 3:
                img = d; //4
                break;
            case 4:
                img = e;//5
                break;
            case 5:
                img = f;//6
                break;
            case 6:
                img = g;//7
                break;
            case 7:
                img = h;//8
                break;
            case 8:
                img = i;//9
                break;
            case 9:
                img = j;//10
                break;
            case 10:
                img = k;//11
                break;
            case 11:
                img = l;//12
                break;

        }
        return img;
    }

    //默认第一个分类字符
    public static String insertIconTitle(int id) {
        //支出
        String a = "餐饮";
        //收入
        String b = "薪资";

        String title = a;
        switch (id){
            case 0:
                title = a;
                break;
            case 100:
                title = b;
                break;
        }
        return title;
    }
}
