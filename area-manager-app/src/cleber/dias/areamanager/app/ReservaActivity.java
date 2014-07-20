package cleber.dias.areamanager.app;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;

import android.support.v7.app.ActionBarActivity;
import cleber.dias.areamanager.db.DatabaseHelper;
import cleber.dias.areamanager.db.Reserva;

import com.j256.ormlite.dao.Dao;

@EActivity(R.layout.activity_reserva)
public class ReservaActivity extends ActionBarActivity {

	@OrmLiteDao(helper = DatabaseHelper.class, model = Reserva.class)
	Dao<Reserva, Long> reservaDao;

}
