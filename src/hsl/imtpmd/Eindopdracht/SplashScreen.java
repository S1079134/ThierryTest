package hsl.imtpmd.Eindopdracht;

import hsl.imtpmd.Eindopdracht.R;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class SplashScreen extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		Thread splashscreen = new Thread() {
			public void run() {
				try {
					sleep(1900);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
					finish();
				}
			}
		};
		splashscreen.start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
