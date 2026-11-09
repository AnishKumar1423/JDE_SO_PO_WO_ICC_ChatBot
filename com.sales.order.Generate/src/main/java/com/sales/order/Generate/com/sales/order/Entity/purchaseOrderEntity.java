package com.sales.order.Generate.com.sales.order.Entity;

import java.math.BigDecimal;

public class purchaseOrderEntity {

	//Purchase Order
    private String Branch_Plant1;
    private String Long_Address_Number_ALKY1;
    private String Long_Ship_To_ALKY1;
    private String Long_Buyer_Address_Number1;
    private String Item_Number1;
    private BigDecimal Quantity_Ordered1;
    private BigDecimal Unit_Cost1;
    private String Account_Number1;
    private String Select_Row1;
    private String P4310_Version1;

	public String getBranch_Plant1() {
		return Branch_Plant1;
	}

	public void setBranch_Plant1(String branch_Plant1) {
		Branch_Plant1 = branch_Plant1;
	}

	public String getLong_Address_Number_ALKY1() {
		return Long_Address_Number_ALKY1;
	}

	public void setLong_Address_Number_ALKY1(String long_Address_Number_ALKY1) {
		Long_Address_Number_ALKY1 = long_Address_Number_ALKY1;
	}

	public String getLong_Ship_To_ALKY1() {
		return Long_Ship_To_ALKY1;
	}

	public void setLong_Ship_To_ALKY1(String long_Ship_To_ALKY1) {
		Long_Ship_To_ALKY1 = long_Ship_To_ALKY1;
	}

	public String getLong_Buyer_Address_Number1() {
		return Long_Buyer_Address_Number1;
	}

	public void setLong_Buyer_Address_Number1(String long_Buyer_Address_Number1) {
		Long_Buyer_Address_Number1 = long_Buyer_Address_Number1;
	}

	public String getItem_Number1() {
		return Item_Number1;
	}

	public void setItem_Number1(String item_Number1) {
		Item_Number1 = item_Number1;
	}

	public BigDecimal getQuantity_Ordered1() {
		return Quantity_Ordered1;
	}

	public void setQuantity_Ordered1(BigDecimal quantity_Ordered1) {
		Quantity_Ordered1 = quantity_Ordered1;
	}

	public String getAccount_Number1() {
		return Account_Number1;
	}

	public void setAccount_Number1(String account_Number1) {
		Account_Number1 = account_Number1;
	}

	public BigDecimal getUnit_Cost1() {
		return Unit_Cost1;
	}

	public void setUnit_Price1(BigDecimal Unit_Cost1) {
		this.Unit_Cost1 = Unit_Cost1;
	}

	public String getSelect_Row1() {
		return Select_Row1;
	}

	public void setSelect_Row1(String select_Row1) {
		Select_Row1 = select_Row1;
	}

	public String getP4310_Version1() {
	    return P4310_Version1;
	}

	public void setP4310_Version1(String p4310_Version1) {
	    P4310_Version1 = p4310_Version1;
	}
}
