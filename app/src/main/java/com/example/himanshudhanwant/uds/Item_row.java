package com.example.himanshudhanwant.uds;

import android.content.ClipData;

/**
 * Created by Himanshu Dhanwant on 19-Nov-17.
 */

public class Item_row {
    private String item1;
    private int id, cost1,quant;

    public Item_row(int id,String s1,int i1,int quant){
        this.id=id;
        item1=s1;
        cost1=i1;
        this.quant=quant;
    }
    public int getId(){ return id;}
    public String getItem1(){
        return item1;
    }
    public int getCost1() {
        return cost1;
    }
    public int getQuant() {
        return quant;
    }
}
