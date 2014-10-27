package cleber.dias.areamanager.app;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Predicate;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import cleber.dias.areamanager.app.db.DatabaseHelper;
import cleber.dias.areamanager.app.model.Reserva;
import cleber.dias.areamanager.app.model.enuns.StatusPagamentoEnum;

import com.j256.ormlite.dao.Dao;

@EActivity(R.layout.activity_relatorio)
public class RelatorioActivity extends ActionBarActivity {

	private static final String TAG = RelatorioActivity.class.getSimpleName();

	@OrmLiteDao(helper = DatabaseHelper.class, model = Reserva.class)
	Dao<Reserva, Long> reservaDao;

	@ViewById
	Spinner cboMeses, cboAnos;

	@ViewById
	TextView lblTituloAno, lblTituloMes, lblMesQtdReservas, lblMesValorRecebido, lblMesValorReceber, lblMesValorTotal, lblAnoQtdReservas, lblAnoValorRecebido, lblAnoValorReceber, lblAnoValorTotal;

	private List<Reserva> reservas, reservasMes, reservasAno;

	@AfterViews
	protected void afterViews() {
		try {
			this.lblTituloAno.setTypeface(this.lblTituloAno.getTypeface(), Typeface.BOLD);
			this.lblTituloMes.setTypeface(this.lblTituloMes.getTypeface(), Typeface.BOLD);

			final Calendar calendar = Calendar.getInstance();

			this.reservas = this.reservaDao.queryForAll();

			// Carrega todos os meses possíveis.
			ArrayAdapter<CharSequence> adapterMeses = ArrayAdapter.createFromResource(this, R.array.array_meses, android.R.layout.simple_spinner_item);
			adapterMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			this.cboMeses.setAdapter(adapterMeses);

			// Adicona o ano corrente as opções de filtro
			Set<Integer> anos = new LinkedHashSet<Integer>();
			calendar.setTime(new Date());
			anos.add(calendar.get(Calendar.YEAR));

			// Seta o mês corrente
			int indiceMes = calendar.get(Calendar.MONTH);
			this.cboMeses.setSelection(indiceMes);

			// Carrega os anos de acordo com os registros salvos no banco de dados.
			for (Reserva reserva : this.reservas) {
				calendar.setTime(new Date(reserva.getData()));
				anos.add(calendar.get(Calendar.YEAR));
			}
			ArrayAdapter<Integer> adapterAnos = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, new ArrayList<Integer>(anos));
			adapterAnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			this.cboAnos.setAdapter(adapterAnos);

			// Seta o ano corrente
			calendar.setTime(new Date());
			int ano = calendar.get(Calendar.YEAR);
			this.cboAnos.setSelection(adapterAnos.getPosition(ano));

			// Atribui o listener de seleção de mês
			this.cboMeses.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adapter, View view, final int position, long id) {
					RelatorioActivity.this.filtrarReservasPorMes(position);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			// Atribui o listener de seleção de ano
			this.cboAnos.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(final AdapterView<?> adapter, View view, final int position, long id) {
					final Integer anoSelecionado = (Integer) adapter.getItemAtPosition(position);
					RelatorioActivity.this.filtrarReservasPorAno(anoSelecionado);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

		} catch (SQLException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private void filtrarReservasPorMes(final int indiceMes) {
		final Calendar calendar = Calendar.getInstance();
		RelatorioActivity.this.reservasMes = ListUtils.select(RelatorioActivity.this.reservas, new Predicate<Reserva>() {
			@Override
			public boolean evaluate(Reserva reserva) {
				calendar.setTime(new Date(reserva.getData()));
				return calendar.get(Calendar.MONTH) == indiceMes;
			}
		});

		this.atualizarLabelsMes();
	}

	@UiThread
	void atualizarLabelsMes() {
		this.lblMesQtdReservas.setText(this.getString(R.string.label_quantidade_de_reservas, this.reservasMes.size()));
		double valorRecebido = 0, valorReceber = 0, valorTotal = 0;
		for (Reserva reservaMes : this.reservasMes) {
			if (reservaMes.getStatusPagamento() == StatusPagamentoEnum.PAGO.getId()) {
				valorRecebido += reservaMes.getValor();
			} else {
				valorReceber += reservaMes.getValor();
			}
			valorTotal += reservaMes.getValor();
		}
		this.lblMesValorRecebido.setText(this.getString(R.string.label_valor_recebido, NumberFormat.getCurrencyInstance().format(valorRecebido)));
		this.lblMesValorReceber.setText(this.getString(R.string.label_valor_a_receber, NumberFormat.getCurrencyInstance().format(valorReceber)));
		this.lblMesValorTotal.setText(this.getString(R.string.label_valor_total, NumberFormat.getCurrencyInstance().format(valorTotal)));
	}

	private void filtrarReservasPorAno(final Integer ano) {
		final Calendar calendar = Calendar.getInstance();
		RelatorioActivity.this.reservasAno = ListUtils.select(RelatorioActivity.this.reservas, new Predicate<Reserva>() {
			@Override
			public boolean evaluate(Reserva reserva) {
				calendar.setTime(new Date(reserva.getData()));
				return calendar.get(Calendar.YEAR) == ano;
			}
		});

		this.atualizarLabelsAno();
	}

	@UiThread
	void atualizarLabelsAno() {
		this.lblAnoQtdReservas.setText(this.getString(R.string.label_quantidade_de_reservas, this.reservasAno.size()));
		double valorRecebido = 0, valorReceber = 0, valorTotal = 0;
		for (Reserva reservaMes : this.reservasAno) {
			if (reservaMes.getStatusPagamento() == StatusPagamentoEnum.PAGO.getId()) {
				valorRecebido += reservaMes.getValor();
			} else {
				valorReceber += reservaMes.getValor();
			}
			valorTotal += reservaMes.getValor();
		}
		this.lblAnoValorRecebido.setText(this.getString(R.string.label_valor_recebido, NumberFormat.getCurrencyInstance().format(valorRecebido)));
		this.lblAnoValorReceber.setText(this.getString(R.string.label_valor_a_receber, NumberFormat.getCurrencyInstance().format(valorReceber)));
		this.lblAnoValorTotal.setText(this.getString(R.string.label_valor_total, NumberFormat.getCurrencyInstance().format(valorTotal)));
	}
}
