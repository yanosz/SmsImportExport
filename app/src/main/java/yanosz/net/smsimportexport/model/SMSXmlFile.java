package yanosz.net.smsimportexport.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan on 3/10/15.
 */
@Root(name="smses")
public class SMSXmlFile {

    @Attribute(required = false)
    public int count;

    @ElementList(inline = true)
    public List<SMS> smses;

    public String toString(){
        String s = "{";
        if(smses != null){
            for (SMS sms : smses){
                s += sms.toString();
            }
        }
        return s+"}";
    }
}
