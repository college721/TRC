package com.example.lukey.trc.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.lukey.trc.Model.Order;
import com.example.lukey.trc.Model.Product;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukey on 02/02/2018.
 */

public class Database extends SQLiteAssetHelper{
    private static final String DB_NAME="TRC_DBv2.db";
    private static final int DB_VER=1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts()
    {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect={"ProductName","ProductId","Quantity","Price","QtyInWarehouse"};
        String sqlTable="OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("QtyInWarehouse"))
                ));
            }while(c.moveToNext());

        }
        return result;

    }


    public void addToCart(Order order)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,QtyInWarehouse) VALUES ('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getQtyInWarehouse());

        db.execSQL(query);




    }

    public void updateQty(Product product, Order order)
    {
        product.getWarehouseQty();
        int sum = (Integer.parseInt(product.getWarehouseQty()));
        int sum1 = (Integer.parseInt(order.getQtyInWarehouse()));
        int sum2 = (sum) - (sum1);
        String total = Integer.toString(sum2);
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(QtyInWarehouse) VALUES ('%s');",
                total);

        db.execSQL(query);


    }

    public void cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);

    }


}
