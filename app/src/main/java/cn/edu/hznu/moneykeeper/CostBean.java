package cn.edu.hznu.moneykeeper;

import org.litepal.crud.LitePalSupport;



public class CostBean extends LitePalSupport {

    public String costTitle;
    public String costDate;
    public String costMoney;
    public int costId;
    public String costDateinfo;

    public String getCostDateinfo() {
        return costDateinfo;
    }

    public void setCostDateinfo(String costDateinfo) {
        this.costDateinfo = costDateinfo;
    }



    public String getCostTitle() {
        return costTitle;
    }

    public void setCostTitle(String costTitle) {
        this.costTitle = costTitle;
    }

    public String getCostDate() {
        return costDate;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public String getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(String costMoney) {
        this.costMoney = costMoney;
    }

    public int getCostId() {
        return costId;
    }

    public void setCostId(int costId) {
        this.costId = costId;
    }


}
