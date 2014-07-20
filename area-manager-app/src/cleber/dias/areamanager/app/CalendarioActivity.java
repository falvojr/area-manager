package cleber.dias.areamanager.app;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.OrmLiteDao;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import cleber.dias.areamanager.db.DatabaseHelper;
import cleber.dias.areamanager.db.Reserva;
import cleber.dias.areamanager.db.StatusPagamentoEnum;

import com.j256.ormlite.dao.Dao;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

@EActivity(R.layout.activity_calendario)
@OptionsMenu(R.menu.calendario)
public class CalendarioActivity extends ActionBarActivity {

	private static final int REQUEST_CODE_RESERVA = 1;

	@OptionsMenuItem(R.id.action_incluir)
	MenuItem menuItemIncluir;

	@OptionsMenuItem(R.id.action_editar)
	MenuItem menuItemEditar;

	@OptionsMenuItem(R.id.action_excluir)
	MenuItem menuItemExcluir;

	@OrmLiteDao(helper = DatabaseHelper.class, model = Reserva.class)
	Dao<Reserva, Long> reservaDao;

	private CaldroidFragment caldroidFragment;

	private Date dataSelecionada;

	@OptionsItem(R.id.action_incluir)
	void actionIncluir() {
		ReservaActivity_.intent(this).startForResult(REQUEST_CODE_RESERVA);
	}

	@OnActivityResult(REQUEST_CODE_RESERVA)
	void onResultReserva(int resultCode) {

	}

	@OptionsItem(R.id.action_editar)
	void actionEditar() {

	}

	@OptionsItem(R.id.action_excluir)
	void actionExcluir() {

	}

	@SuppressLint("InflateParams")
	@Click(R.id.btnLegendas)
	void actionLegendas() {
		// Infla o layout do fragmento com as legendas:
		View layout = this.getLayoutInflater().inflate(R.layout.fragment_legenda_dialog, null);

		// Cria e apresenta o Alert:
		new AlertDialog.Builder(this).setTitle(this.getString(R.string.label_legenda)).setView(layout).setIcon(android.R.drawable.ic_dialog_info)
		.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create().show();
	}

	private Reserva buscarReservaPorData(Date data) {
		try {
			List<Reserva> reservasDataUltimoClick = CalendarioActivity.this.reservaDao.queryForEq("data", data);
			return reservasDataUltimoClick.isEmpty() ? null : reservasDataUltimoClick.get(0);
		} catch (SQLException e) {
			return null;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.caldroidFragment = new CaldroidFragment();

		this.marcarReservasCalendario();

		if (savedInstanceState != null) {
			this.caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
		} else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			this.caldroidFragment.setArguments(args);

			this.getSupportFragmentManager().beginTransaction().replace(R.id.calendario, this.caldroidFragment).commit();
		}

		this.caldroidFragment.setCaldroidListener(new AMCaldroidListener());

		this.dataSelecionada = new Date();
		this.caldroidFragment.setBackgroundResourceForDate(R.color.areamanager_selecionada, this.dataSelecionada);
	}

	private void marcarReservasCalendario() {
		try {
			HashMap<Date, Integer> mapaReservas = new HashMap<Date, Integer>();

			List<Reserva> reservasPagas = this.reservaDao.queryForEq("statusPagamento", StatusPagamentoEnum.PAGO);
			for(Reserva reservaPaga : reservasPagas) {
				mapaReservas.put(reservaPaga.getData(), R.color.areamanager_paga);
			}

			List<Reserva> reservasNaoPagas = this.reservaDao.queryForEq("statusPagamento", StatusPagamentoEnum.NAO_PAGO);
			for(Reserva reservaNaoPaga : reservasNaoPagas) {
				mapaReservas.put(reservaNaoPaga.getData(), R.color.areamanager_nao_paga);
			}

			this.caldroidFragment.setBackgroundResourceForDates(mapaReservas);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (this.caldroidFragment != null) {
			this.caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}
	}

	private final class AMCaldroidListener extends CaldroidListener {

		@Override
		public void onSelectDate(Date date, View view) {
			this.selecionarData(date);
		}

		@Override
		public void onLongClickDate(Date date, View view) {
			this.selecionarData(date);
		}

		private void selecionarData(Date date) {
			// Reaplica o background da data selecionada no último clique, pois ela será atualizada neste método:
			Reserva reservaClickAnterior = CalendarioActivity.this.buscarReservaPorData(CalendarioActivity.this.dataSelecionada);
			if (reservaClickAnterior == null) {
				CalendarioActivity.this.caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, CalendarioActivity.this.dataSelecionada);
			} else {
				if (StatusPagamentoEnum.PAGO.equals(reservaClickAnterior.getStatusPagamento())) {
					CalendarioActivity.this.caldroidFragment.setBackgroundResourceForDate(R.color.areamanager_paga, reservaClickAnterior.getData());
				} else {
					CalendarioActivity.this.caldroidFragment.setBackgroundResourceForDate(R.color.areamanager_nao_paga, reservaClickAnterior.getData());
				}
			}

			// Configura a visibilidade dos itens da ActionBar:
			Boolean isDisponivel = CalendarioActivity.this.buscarReservaPorData(date) == null;
			CalendarioActivity.this.menuItemIncluir.setVisible(isDisponivel);
			CalendarioActivity.this.menuItemEditar.setVisible(!isDisponivel);
			CalendarioActivity.this.menuItemExcluir.setVisible(!isDisponivel);

			// Marca o item selecionado:
			CalendarioActivity.this.caldroidFragment.setBackgroundResourceForDate(R.color.areamanager_selecionada, date);
			CalendarioActivity.this.caldroidFragment.refreshView();

			// Armazena a data selecionada:
			CalendarioActivity.this.dataSelecionada = date;
		}

	}

}