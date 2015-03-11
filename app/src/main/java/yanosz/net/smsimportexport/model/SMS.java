package yanosz.net.smsimportexport.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jan on 3/10/15.
 */

import yanosz.net.smsimportexport.util.SmsWriteOpUtil;

import static android.provider.Telephony.*;

@Root(name="sms")
public class SMS {

    private static HashMap<Integer,Uri> uriMap = new HashMap<Integer,Uri>();
    static {
        uriMap.put(Sms.MESSAGE_TYPE_INBOX, Sms.Inbox.CONTENT_URI);
        uriMap.put(Sms.MESSAGE_TYPE_SENT, Sms.Sent.CONTENT_URI);
        uriMap.put(Sms.MESSAGE_TYPE_DRAFT, Sms.Draft.CONTENT_URI);
        uriMap.put(Sms.MESSAGE_TYPE_OUTBOX, Sms.Outbox.CONTENT_URI);
        uriMap.put(Sms.MESSAGE_TYPE_QUEUED, Sms.Outbox.CONTENT_URI);
        uriMap.put(Sms.MESSAGE_TYPE_FAILED, Sms.Outbox.CONTENT_URI);
        uriMap.put(Sms.MESSAGE_TYPE_ALL, Sms.CONTENT_URI);


    }


    //protocol="0" address="017666236914" date="1423059762704" type="2" subject="null" body="Ich komm vermutlich 5 mins zu spät" toa="null" sc_toa="null" service_center="null" read="1" status="0" locked="0"

    @Attribute(required=false)
    public Integer protocol;

    @Attribute
    public String address;

    @Attribute(required=false)
    public Long date;

    @Attribute(required=false)
    public Integer type;

    @Attribute(required=false)
    public String body;

    @Attribute(required=false)
    public Integer read;

    @Attribute(required=false)
    public Integer status;

    @Attribute(required=false)
    public Integer locked;

    @Attribute(required=false)
    public String subject;

    @Attribute(required=false, name = "service_center")
    public String serviceCenter;



    @Attribute(required=false)
    public String toa;

    @Attribute(required=false)
    public String sc_toa;

    public void create(Context c) {
        ContentValues values = new ContentValues();
        values.put("address", address);
        values.put("body", body);
        values.put("date",date);
        values.put("protocol",protocol);
        values.put("read",read);
        values.put("status",status);
        values.put("type", type);
        values.put("subject",subject);
        values.put("service_center",serviceCenter);
        values.put("locked",locked);

        Uri uri = uriMap.get(type);
        if(uri == null){
            uri = uriMap.get(0); // Defaullt: "All"-Box
        }
        SmsWriteOpUtil.setWriteEnabled(c,true);
        Uri insert = c.getContentResolver().insert(uri, values);
        Log.i("Created", "Uri is " + uri.toString());
        SmsWriteOpUtil.setWriteEnabled(c,false);
    }

    public static class DbDAO{
        public List<SMS> find(){ // Get all SMSes
            return new ArrayList<SMS>();
        }
    }

    @Override
    public String toString() {
        return "SMS{" +
                "protocol=" + protocol +
                ", address='" + address + '\'' +
                ", date=" + date +
                ", type=" + type +
                ", body='" + body + '\'' +
                ", read=" + read +
                ", status=" + status +
                ", locked=" + locked +
                '}';
    }
}
