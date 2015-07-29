package com.jude.know.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.jude.utils.JUtils;

/**
 * Created by Mr.Jude on 2015/7/29.
 */
public class PopupWindowsUtils {
    public interface PopupListener{
        void onListenerPop(ListPopupWindow listp);
        void onListItemClickBack(ListPopupWindow popwindow, View parent, int position);
    }

    public static ListPopupWindow createListPopupWindows(Context ctx, final ListAdapter adapter, final PopupListener listener){
        final ListPopupWindow listPopupWindow = new ListPopupWindow(ctx);
        listPopupWindow.setModal(true);
        listener.onListenerPop(listPopupWindow);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parents, View view,
                                    int position, long id) {
                listener.onListItemClickBack(listPopupWindow,view,position);
            }
        });
        return listPopupWindow;
    }

    public static ListPopupWindow createTextListPopupWindows(final Context ctx, final String[] list, final PopupListener listener){
        return createListPopupWindows(ctx, new BaseAdapter() {
            @Override
            public int getCount() {
                return list.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                TextView tv = new TextView(parent.getContext());
                tv.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, JUtils.dip2px(48)));
                tv.setBackgroundColor(Color.WHITE);
                tv.setGravity(Gravity.CENTER);
                tv.setText(list[position]);
                return tv;
            }
        }, listener);
    }
}
