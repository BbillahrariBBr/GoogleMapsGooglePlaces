package test.com.potherdabai.googlemapsgoogleplaces;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    //first confirm play services is install in user phone and check the crack version

    private static final String TAG = "MainActivity";
    private  static final int ERROR_DIALOG_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isServicesOk()){
            init();
        }
    }

    //method for checking a version
    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOk: Checking google services Version");

        int available = GoogleApiAvailability
                .getInstance()
                .isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOk: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured  and we can resolve it
            Log.d(TAG, "isServicesOk: an error Occured But we can fix it");

            Dialog dialog = GoogleApiAvailability
                    .getInstance()
                    .getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);

            dialog.show();

        }
        else {
            Toast.makeText(this,"You Cant Map Request",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void init(){
        Button btnMap = (Button)findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }
}
