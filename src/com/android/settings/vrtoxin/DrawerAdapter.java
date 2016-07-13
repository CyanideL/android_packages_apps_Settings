package com.android.settings.vrtoxin.drawer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.internal.util.vrtoxin.FontHelper;
import com.android.internal.util.vrtoxin.NavDrawerColorHelper;

import com.android.settings.R;
import com.android.settings.vrtoxin.drawer.DrawerItems;

public class DrawerAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<DrawerItems> navDrawerItems;
	private TextView txtTitle;
	
	public DrawerAdapter(Context context, ArrayList<DrawerItems> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item,null);
        }
         
        final ColorStateList iconColor = NavDrawerColorHelper.getIconColorList(context);
        final ColorStateList textColor = NavDrawerColorHelper.getTextColorList(context);
        final int mDrawerFont = Settings.System.getInt(context.getContentResolver(),
                Settings.System.DRAWER_FONT_STYLE, 20);

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        txtTitle = (TextView) convertView.findViewById(R.id.title);
        
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        imgIcon.setImageTintList(iconColor);
        txtTitle.setText(navDrawerItems.get(position).getTitle());
        txtTitle.setTextColor(textColor);
        getFontStyle(mDrawerFont);

        return convertView;
	}

    public void getFontStyle(int font) {
        switch (font) {
            case FontHelper.FONT_NORMAL:
            default:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
                break;
            case FontHelper.FONT_ITALIC:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif", Typeface.ITALIC));
                break;
            case FontHelper.FONT_BOLD:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
                break;
            case FontHelper.FONT_BOLD_ITALIC:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif", Typeface.BOLD_ITALIC));
                break;
            case FontHelper.FONT_LIGHT:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                break;
            case FontHelper.FONT_LIGHT_ITALIC:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-light", Typeface.ITALIC));
                break;
            case FontHelper.FONT_THIN:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                break;
            case FontHelper.FONT_THIN_ITALIC:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-thin", Typeface.ITALIC));
                break;
            case FontHelper.FONT_CONDENSED:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                break;
            case FontHelper.FONT_CONDENSED_ITALIC:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-condensed", Typeface.ITALIC));
                break;
            case FontHelper.FONT_CONDENSED_LIGHT:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-condensed-light", Typeface.NORMAL));
                break;
            case FontHelper.FONT_CONDENSED_LIGHT_ITALIC:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-condensed-light", Typeface.ITALIC));
                break;
            case FontHelper.FONT_CONDENSED_BOLD:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));
                break;
            case FontHelper.FONT_CONDENSED_BOLD_ITALIC:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD_ITALIC));
                break;
            case FontHelper.FONT_MEDIUM:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                break;
            case FontHelper.FONT_MEDIUM_ITALIC:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-medium", Typeface.ITALIC));
                break;
            case FontHelper.FONT_BLACK:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-black", Typeface.NORMAL));
                break;
            case FontHelper.FONT_BLACK_ITALIC:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("sans-serif-black", Typeface.ITALIC));
                break;
            case FontHelper.FONT_DANCINGSCRIPT:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("cursive", Typeface.NORMAL));
                break;
            case FontHelper.FONT_DANCINGSCRIPT_BOLD:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("cursive", Typeface.BOLD));
                break;
            case FontHelper.FONT_COMINGSOON:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                break;
            case FontHelper.FONT_NOTOSERIF:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("serif", Typeface.NORMAL));
                break;
            case FontHelper.FONT_NOTOSERIF_ITALIC:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("serif", Typeface.ITALIC));
                break;
            case FontHelper.FONT_NOTOSERIF_BOLD:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("serif", Typeface.BOLD));
                break;
            case FontHelper.FONT_NOTOSERIF_BOLD_ITALIC:
                if (txtTitle != null) txtTitle.setTypeface(Typeface.create("serif", Typeface.BOLD_ITALIC));
                break;
        }
    }
}
