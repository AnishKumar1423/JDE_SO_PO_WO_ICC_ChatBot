
package com.sales.order.Generate.com.sales.order.Entity;

import java.math.BigDecimal;

public class salesOrderEntity {

	//Sales Order
    private String Business_Unit;
    private String Long_Address_Number;
    private String Long_Address_Number_Shipto;
    private BigDecimal Quantity_Ordered;
    private String Item_Number;
    private BigDecimal Unit_Price;
    private String Select_Row;

    public salesOrderEntity() {
    }

    public String getBusiness_Unit() {
        return Business_Unit;
    }

    public void setBusiness_Unit(String business_Unit) {
        this.Business_Unit = business_Unit;
    }

    public String getLong_Address_Number() {
        return Long_Address_Number;
    }

    public void setLong_Address_Number(String long_Address_Number) {
        this.Long_Address_Number = long_Address_Number;
    }

    public String getLong_Address_Number_Shipto() {
        return Long_Address_Number_Shipto;
    }

    public void setLong_Address_Number_Shipto(
            String long_Address_Number_Shipto) {
        this.Long_Address_Number_Shipto =
                long_Address_Number_Shipto;
    }

    public BigDecimal getQuantity_Ordered() {
        return Quantity_Ordered;
    }

    public void setQuantity_Ordered(BigDecimal quantity_Ordered) {
        this.Quantity_Ordered = quantity_Ordered;
    }

    public String getItem_Number() {
        return Item_Number;
    }

    public void setItem_Number(String item_Number) {
        this.Item_Number = item_Number;
    }

    public BigDecimal getUnit_Price() {
        return Unit_Price;
    }

    public void setUnit_Price(BigDecimal unit_Price) {
        this.Unit_Price = unit_Price;
    }

    public String getSelect_Row() {
        return Select_Row;
    }

    public void setSelect_Row(String select_Row) {
        this.Select_Row = select_Row;
    } 
    
}
