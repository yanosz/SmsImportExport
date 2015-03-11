package yanosz.net.smsimportexport.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import java.net.URISyntaxException;

/**
 * Created by jan on 3/10/15.
 */
public class FileUtil {
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                Toast.makeText(context,"Error Reading file: +" + e.getMessage(),Toast.LENGTH_LONG);
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}
