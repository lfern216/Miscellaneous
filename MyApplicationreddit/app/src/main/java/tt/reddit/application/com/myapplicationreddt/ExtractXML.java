package tt.reddit.application.com.myapplicationreddt;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lazaro on 7/27/18.
 */

public class ExtractXML {

    private String tag;
    private String xml;

    public ExtractXML(String xml, String tag) {
        this.tag = tag;
        this.xml = xml;
    }

    public List<String> start(){

        List<String> result = new ArrayList<>();

        String[] splitter = xml.split(tag+"\"");

        for(int i=1;i<splitter.length;i++){
            String temp = splitter[i];
            int index = temp.indexOf("\"");
            temp = temp.substring(0,index);
            Log.e("ExtractXML","final res: "+ temp);
            result.add(temp);
        }

        return result;
    }
}
