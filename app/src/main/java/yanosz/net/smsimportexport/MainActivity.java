package yanosz.net.smsimportexport;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yanosz.net.smsimportexport.model.SMS;
import yanosz.net.smsimportexport.model.SMSXmlFile;
import yanosz.net.smsimportexport.util.FileUtil;
import yanosz.net.smsimportexport.views.SMSListView;


public class MainActivity extends Activity {

    private List<SMS> smses;

    public enum State{IDLE,PARSED_EXPORT,PARSED_IMPORT,ERROR};

    private State currentState = State.IDLE;
    private Button importButton;
    private Button exportButton;
    private SMSListView parsedSMSes;
    private TextView headerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        importButton = (Button)findViewById(R.id.importButton);
        exportButton = (Button)findViewById(R.id.exportButton);
        parsedSMSes = (SMSListView)findViewById(R.id.smsListView);
        headerInfo = (TextView) findViewById(R.id.headerInfo);

        // Listeners
        importButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(currentState == State.IDLE){
                    onSMSReadFromFile();
                } else {
                    onPerformImport();
                }
                applyViewState();
            }
        });
        exportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(currentState == State.IDLE)
                       onSMSReadFromDB();
                else if(currentState == State.PARSED_EXPORT){
                    onSMSPerformExport();
                }
                applyViewState();
            }
        });
        applyViewState();

    }

    private void applyViewState() {
        if(currentState == State.IDLE){
            importButton.setEnabled(true);
            exportButton.setEnabled(true);
        } else if(currentState == State.PARSED_EXPORT){
            importButton.setEnabled(false);
            exportButton.setEnabled(true);
        } else if (currentState == State.PARSED_IMPORT){
            importButton.setEnabled(true);
            exportButton.setEnabled(false);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onSMSReadFromDB(){
        headerInfo.setText("Found SMS as listed. Press Perform Export to proceed");
        exportButton.setText("Perform Export");
        parsedSMSes.setModel(new SMS.DbDAO().find());

    }

    public void onSMSPerformExport(){
    }

    public void onPerformImport(){
        int i = 0;
        if (smses != null){
            for(SMS s : smses){
                s.create(getApplicationContext());
                i++;
            }

        }
        headerInfo.setText("Import Performed - Processed " +i + " message(s)");
        smses.clear();
        parsedSMSes.setModel(smses);
        importButton.setText("Import");
        currentState = State.IDLE;
    }

    public void onFileSelected(Uri uri){
        // Read XML for IMPORT
        Serializer serializer = new Persister();
        try {

            SMSXmlFile smsXmlFile = serializer.read(SMSXmlFile.class, getContentResolver().openInputStream(uri));
            Log.d("SMSes", smsXmlFile.toString());
            smses = smsXmlFile.smses;
            parsedSMSes.setModel(smsXmlFile.smses);
            headerInfo.setText("Parsed File. Press 'Perform Import' to proceed.");
            importButton.setText("Perform Import");
            currentState = State.PARSED_IMPORT;
        } catch (Exception e){
            throw new RuntimeException(e);
            //Toast.makeText(getApplicationContext(),"Error reading / parsing XML-File: " + e.getMessage(),Toast.LENGTH_LONG);
        }
    }


    public void onSMSReadFromFile(){
       try {
           Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
           intent.setType("*/*");
           Intent.createChooser(intent, "Select a File to Import");
           startActivityForResult(intent, FILE_SELECT_CODE);

           Log.d("File:","Selecting file");

       } catch (ActivityNotFoundException e){
           Toast.makeText(getApplicationContext(),"Please install a File Manager.", Toast.LENGTH_SHORT);
       }


    }
    private static final int FILE_SELECT_CODE = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("File Uri: ",uri.toString());
                    // Get the path
                    String path = FileUtil.getPath(this, uri);
                    //Log.d("File Path: ", uri.toString());
                   onFileSelected(uri);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
