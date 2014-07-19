package cleber.dias.areamanager.app;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import android.support.v7.app.ActionBarActivity;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends ActionBarActivity {

	@OptionsItem(R.id.action_settings)
    void actionSettings() {
      
    }
}
