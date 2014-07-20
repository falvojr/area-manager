package cleber.dias.areamanager.app;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import cleber.dias.areamanager.db.DatabaseHelper;
import cleber.dias.areamanager.db.Reserva;

import com.j256.ormlite.dao.Dao;

@EActivity(R.layout.activity_reserva)
public class ReservaActivity extends ActionBarActivity {

	@OrmLiteDao(helper = DatabaseHelper.class, model = Reserva.class)
	Dao<Reserva, Long> reservaDao;

	@ViewById
	TextView lblData;
	@ViewById
	EditText txtCPF;
	@ViewById
	EditText txtNome;
	@ViewById
	EditText txtEndereco;
	@ViewById
	EditText txtTelefone;
	@ViewById
	RadioGroup rdoGrpFormaPagamento;
	@ViewById
	RadioGroup rdoGrpStatusPagamento;

	@AfterViews
	void afterViews() {

	}
}
