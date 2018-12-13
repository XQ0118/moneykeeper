package cn.edu.hznu.moneykeeper;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by noisa on 2016/3/15.
 * Avatar的实体类
 */
public class Avatar implements Parcelable{
    private String aId;//无
    private String aName;//英雄的中文名
    private String aIconPath;//头像的路径
    private String detailFileName;//英雄详情的Json文件路径


    public String getDetailFileName() {
        return detailFileName;
    }

    public String getaIconPath() {
        return aIconPath;
    }

    public void setaIconPath(String aIconPath) {
        this.aIconPath = aIconPath;
    }

    public Avatar(){

    }

    public Avatar(String aId, String aName) {
        this.aId = aId;
        this.aName = aName;
    }

    public Avatar(String id, String name, String aIconPath) {
        this(id, name);
        this.aIconPath = aIconPath;
    }

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    /**本类数据的序列化，传送到Fragment的GridView布局里面
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.aId);
        dest.writeString(this.aName);
        dest.writeString(this.aIconPath);
    }

    protected Avatar(Parcel in) {
        this.aId = in.readString();
        this.aName = in.readString();
        this.aIconPath = in.readString();
    }

    public static final Creator<Avatar> CREATOR = new Creator<Avatar>() {
        @Override
        public Avatar createFromParcel(Parcel source) {
            return new Avatar(source);
        }

        @Override
        public Avatar[] newArray(int size) {
            return new Avatar[size];
        }
    };

    // {"data":"[{"name":"","icon":"","detail_file_name":""}, {...}, {...},{...}]"} 英雄列表的Json格式设计
    public static List<Avatar> createList(String json) {
        try {
            JSONObject rootJson = new JSONObject(json);

            if (rootJson.has("data")) {
                JSONArray avatarStr = rootJson.getJSONArray("data");
                if (avatarStr != null) {
                    List<Avatar> avatarlist = new ArrayList<Avatar>();

                    for (int i = 0; i < avatarStr.length(); i++) {
                        JSONObject curJson = (JSONObject)avatarStr.get(i);
                        Avatar avatar = new Avatar();
                        avatar.aIconPath = Utils.getSafeJsonField(curJson, "icon");
                        avatar.aName = Utils.getSafeJsonField(curJson, "name");
                        avatar.detailFileName = Utils.getSafeJsonField(curJson, "detail_file_name");
                        avatarlist.add(avatar);
                    }

                    return avatarlist;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}