 package com.barclouds.views.adapter;

 import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.barclouds.R;

 public class IoMenuAdapter extends BaseAdapter {
     private Context context;
     private int[] i;
     private String[] names;

     public IoMenuAdapter(Context context, int[] i, String[] names) {
         this.context = context;
         this.i = i;
         this.names=names;
     }


     public int getCount() {
         // TODO Auto-generated method stub
         return i.length;
     }

     public Object getItem(int position) {
         // TODO Auto-generated method stub
         return null;
     }


     public long getItemId(int position) {
         // TODO Auto-generated method stub
         return 0;
     }

     class Holder {
         ImageView imageview;
         TextView textview;
     }


     public View getView(int position, View convertView, ViewGroup parent) {
         Holder holder = null;
         //复用了回收的view 只需要直接作内容填充的修改就好了
         if (convertView == null) {
             holder = new Holder();
             //动态载入一个布局界面
             convertView = LayoutInflater.from(context).inflate(
                     R.layout.iomenu_cell, null);
//             convertView.setBackgroundColor(Color.BLACK);//设置每个GirdView的背景颜色
             holder.imageview = (ImageView) convertView
                     .findViewById(R.id.imageview_menu_cell);
             holder.textview =(TextView)convertView
                     .findViewById(R.id.textview_menu_name);
             convertView.setTag(holder);
         } else {
             //没有供复用的view 按一般的做法新建view
             holder = (Holder) convertView.getTag();

         }
         holder.imageview.setBackgroundResource(i[position]);
         holder.textview.setText(names[position]);

         return convertView;
     }

 }
