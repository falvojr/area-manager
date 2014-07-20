package cleber.dias.areamanager.app;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import cleber.dias.areamanager.db.DatabaseHelper;
import cleber.dias.areamanager.db.Reserva;
import cleber.dias.areamanager.db.StatusPagamentoEnum;
import cleber.dias.areamanager.ext.TextViewEx;

import com.j256.ormlite.dao.Dao;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends ActionBarActivity {

	@ViewById(R.id.lblDescricaoApp)
	protected TextViewEx txtDescricao;

	@OrmLiteDao(helper = DatabaseHelper.class, model = Reserva.class)
	Dao<Reserva, Long> reservaDao;

	@AfterViews
	void afterViews() {
		// Justifica o texto de descrição do aplicativo:
		this.txtDescricao.setText(this.getString(R.string.label_descricao_app), true);

		for (int i = 1; i <= 10; i++) {
			Reserva r = new Reserva();
			r.setCpf(36289058851L);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, +i);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			r.setData(c.getTime());
			r.setStatusPagamento((i%2) == 0 ? StatusPagamentoEnum.PAGO : StatusPagamentoEnum.NAO_PAGO);
			r.setTelefone("+55 016 99721-8281");
			try {
				this.reservaDao.create(r);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Click(R.id.btnEntrar)
	void actionEntrar() {
		CalendarioActivity_.intent(this).start();
	}

	@SuppressLint("InflateParams")
	@OptionsItem(R.id.action_sobre)
	void actionSettings() {
		// Infla o layout da view com a mensagem do Alert:
		View layout = this.getLayoutInflater().inflate(R.layout.fragment_mensagem_dialog, null);
		((TextViewEx) layout.findViewById(R.id.txtAlertDialog)).setText(this.getString(R.string.label_descricao_sobre), true);

		// Cria e apresenta o Alert:
		new AlertDialog.Builder(this).setTitle(this.getString(R.string.label_sobre)).setView(layout).setIcon(android.R.drawable.ic_dialog_info)
		.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create().show();
	}
}
