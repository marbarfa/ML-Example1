package com.ml.android.melitraining.database.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "BOOKMARKS")
public class Bookmark {

    public static final String ITEM_END_DATE = "item_end_date";
    public static final String CREATED_AT = "created_at";

    @DatabaseField(generatedId = true)
    Long id = null;
    @DatabaseField(unique = true, canBeNull = false)
    String itemId = null;
    @DatabaseField(dataType = DataType.DATE_STRING, format = "dd/MM/yyyy")
    Date itemEndDate = null;
    @DatabaseField(dataType = DataType.DATE_STRING, format = "dd/MM/yyyy")
    Date createdAt = new Date();
    @DatabaseField(dataType = DataType.DATE_STRING, format = "dd/MM/yyyy")
    Date updatedAt = new Date();
    @DatabaseField
    boolean checked = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Date getItemEndDate() {
        return itemEndDate;
    }

    public void setItemEndDate(Date itemEndDate) {
        this.itemEndDate = itemEndDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}