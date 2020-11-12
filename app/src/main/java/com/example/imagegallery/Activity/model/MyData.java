package com.example.imagegallery.Activity.model;

import com.example.model.MyImage;
import com.example.model.MyResult;
import com.example.my_db.DatabaseHelper;
import com.example.utility.Utils;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.queriable.StringQuery;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(name = "MyData", database = DatabaseHelper.class)
public class MyData extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column
    Integer myDataId;
    @Column
    String id;
    @Column
    String description;

    public MyData() {
    }

    public static void updateData(MyResult result, MyImage myImage) {
        if(result!=null)
        {
            try {
                SQLite.update(MyData.class).set(MyData_Table.description.eq(result.getDescription())).where(
                        MyData_Table.id.is(result.getId())
                ).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } if(myImage!=null)
        {
            try {
                SQLite.update(MyData.class).set(MyData_Table.description.eq(myImage.getDescription())).where(
                        MyData_Table.id.is(myImage.getId())
                ).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public Integer getMyDataId() {
        return myDataId;
    }

    public void setMyDataId(int myDataId) {
        this.myDataId = myDataId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdNew(){
        this.id = id;
    }

    public void getIdNew(){
        this.id =id;
    }
    public void testmethod(){
        this.id = id;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public static void saveData(MyResult result, MyImage image) {
        if(result!=null)
        {
            MyData data=new MyData();
            data.setId(result.getId());
            data.setDescription(result.getDescription());
            data.save();
        } if(image!=null)
        {
            MyData data=new MyData();
            data.setId(image.getId());
            data.setDescription(image.getDescription());
            data.save();
        }
    }

    public static MyData getData(MyResult result, MyImage myImage) {
        String query="";
        if(result!=null)
        {
             query="select * from MyData where MyData.Id='"+result.getId()+"'";
        }
        else if(myImage!=null)
        {
             query="select * from MyData where MyData.Id='"+myImage.getId()+"'";
        }
        if(!Utils.checkString(query))
        {
            try {
                StringQuery<MyData>stringQuery=new StringQuery<>(MyData.class,query);
                return stringQuery.querySingle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
