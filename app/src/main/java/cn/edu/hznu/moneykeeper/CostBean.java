package cn.edu.hznu.moneykeeper;

import android.widget.ImageView;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

import androidx.annotation.NonNull;


public class CostBean extends LitePalSupport implements Serializable {

    public String costTitle;
    public String costDate;
    public String costMoney;
    public String costNote;
    public int costId;
    public String costDateinfo;
    public int costImg;
    public int colorType;

    public void setColorType(int colorType) {
        this.colorType = colorType;
    }

    public int getColorType() {
        return colorType;
    }

    public int getCostImg() {
        return costImg;
    }

    public void setCostImg(int costImg) {
        this.costImg = costImg;
    }

    public String getCostDateinfo() {
        return costDateinfo;
    }

    public void setCostDateinfo(String costDateinfo) {
        this.costDateinfo = costDateinfo;
    }

    public String getCostNote() {
        return costNote;
    }

    public void setCostNote(String costNote) { this.costNote = costNote; }

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
