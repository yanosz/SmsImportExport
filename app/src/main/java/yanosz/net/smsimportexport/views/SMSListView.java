package yanosz.net.smsimportexport.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import yanosz.net.smsimportexport.model.SMS;

/**
 * Created by jan on 3/10/15.
 */
public class SMSListView extends ListView {
    private List<SMS> smses = new ArrayList<SMS>();
    private ListAdapter adapter;

    public SMSListView(Context context) {
        super(context);
        init();
    }

    public SMSListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SMSListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setModel(List<SMS> smses){
        this.smses = smses;
       invalidateViews();
    }

    private void init(){
        adapter =  new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return smses.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Recycle view if given
                SMS sms = smses.get(position);
                TextView view;
                if(convertView != null){
                    view = (TextView) convertView;
                } else {
                    view = new TextView(getContext());
                    view.setTextColor(Color.BLACK);
                }
                Calendar cal = GregorianCalendar.getInstance();
                cal.setTimeInMillis(sms.date);
                DateFormat sf = DateFormat.getDateTimeInstance();
                view.setText(sms.address + " - " + sf.format(cal.getTime()) + "\n" + sms.body);

                return view;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
        setAdapter(adapter);
    }

}
