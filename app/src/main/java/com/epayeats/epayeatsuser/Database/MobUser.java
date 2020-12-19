package com.epayeats.epayeatsuser.Database;


import com.epayeats.epayeatsuser.Database.dbflow.MyDatabse;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MyDatabse.class)
public class MobUser extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    String menuID;

    @Column
    String menuName;

    @Column
    String image1;

    @Column
    String menuMainCatagoreyName;

    @Column
    String menuMainCatagoreyID;

    @Column
    String menuSubCatagoreyName;

    @Column
    String menuSubCatagoreyID;

    @Column
    String menuLocalAdminID;

    @Column
    String menuDescription;

    @Column
    String menuOfferPrice;

    @Column
    String menuSellingPrice;

    @Column
    String menuActualPrice;

    @Column
    String menuOpenTime;

    @Column
    String menuCloseTime;

    @Column
    String menuOnorOff;

    @Column
    String menuUnit;

    @Column
    String menuApprovel;

    @Column
    String restName;

    @Column
    String restID;

    @Column
    int qty;

    public void insertData ( String menuID, String menuName, String image1, String menuMainCatagoreyName, String menuMainCatagoreyID, String menuSubCatagoreyName, String menuSubCatagoreyID, String menuLocalAdminID, String menuDescription, String menuOfferPrice, String menuSellingPrice, String menuActualPrice, String menuOpenTime, String menuCloseTime, String menuOnorOff, String menuUnit, String menuApprovel, String restName, String restID, int qty) {

        this.menuID = menuID;
        this.menuName = menuName;
        this.image1 = image1;
        this.menuMainCatagoreyName = menuMainCatagoreyName;
        this.menuMainCatagoreyID = menuMainCatagoreyID;
        this.menuSubCatagoreyName = menuSubCatagoreyName;
        this.menuSubCatagoreyID = menuSubCatagoreyID;
        this.menuLocalAdminID = menuLocalAdminID;
        this.menuDescription = menuDescription;
        this.menuOfferPrice = menuOfferPrice;
        this.menuSellingPrice = menuSellingPrice;
        this.menuActualPrice = menuActualPrice;
        this.menuOpenTime = menuOpenTime;
        this.menuCloseTime = menuCloseTime;
        this.menuOnorOff = menuOnorOff;
        this.menuUnit = menuUnit;
        this.menuApprovel = menuApprovel;
        this.restName = restName;
        this.restID = restID;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getMenuMainCatagoreyName() {
        return menuMainCatagoreyName;
    }

    public void setMenuMainCatagoreyName(String menuMainCatagoreyName) {
        this.menuMainCatagoreyName = menuMainCatagoreyName;
    }

    public String getMenuMainCatagoreyID() {
        return menuMainCatagoreyID;
    }

    public void setMenuMainCatagoreyID(String menuMainCatagoreyID) {
        this.menuMainCatagoreyID = menuMainCatagoreyID;
    }

    public String getMenuSubCatagoreyName() {
        return menuSubCatagoreyName;
    }

    public void setMenuSubCatagoreyName(String menuSubCatagoreyName) {
        this.menuSubCatagoreyName = menuSubCatagoreyName;
    }

    public String getMenuSubCatagoreyID() {
        return menuSubCatagoreyID;
    }

    public void setMenuSubCatagoreyID(String menuSubCatagoreyID) {
        this.menuSubCatagoreyID = menuSubCatagoreyID;
    }

    public String getMenuLocalAdminID() {
        return menuLocalAdminID;
    }

    public void setMenuLocalAdminID(String menuLocalAdminID) {
        this.menuLocalAdminID = menuLocalAdminID;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public String getMenuOfferPrice() {
        return menuOfferPrice;
    }

    public void setMenuOfferPrice(String menuOfferPrice) {
        this.menuOfferPrice = menuOfferPrice;
    }

    public String getMenuSellingPrice() {
        return menuSellingPrice;
    }

    public void setMenuSellingPrice(String menuSellingPrice) {
        this.menuSellingPrice = menuSellingPrice;
    }

    public String getMenuActualPrice() {
        return menuActualPrice;
    }

    public void setMenuActualPrice(String menuActualPrice) {
        this.menuActualPrice = menuActualPrice;
    }

    public String getMenuOpenTime() {
        return menuOpenTime;
    }

    public void setMenuOpenTime(String menuOpenTime) {
        this.menuOpenTime = menuOpenTime;
    }

    public String getMenuCloseTime() {
        return menuCloseTime;
    }

    public void setMenuCloseTime(String menuCloseTime) {
        this.menuCloseTime = menuCloseTime;
    }

    public String getMenuOnorOff() {
        return menuOnorOff;
    }

    public void setMenuOnorOff(String menuOnorOff) {
        this.menuOnorOff = menuOnorOff;
    }

    public String getMenuUnit() {
        return menuUnit;
    }

    public void setMenuUnit(String menuUnit) {
        this.menuUnit = menuUnit;
    }

    public String getMenuApprovel() {
        return menuApprovel;
    }

    public void setMenuApprovel(String menuApprovel) {
        this.menuApprovel = menuApprovel;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getRestID() {
        return restID;
    }

    public void setRestID(String restID) {
        this.restID = restID;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
