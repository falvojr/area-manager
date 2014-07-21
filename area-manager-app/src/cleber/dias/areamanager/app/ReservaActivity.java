package cleber.dias.areamanager.app;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import cleber.dias.areamanager.app.db.DatabaseHelper;
import cleber.dias.areamanager.app.model.Reserva;
import cleber.dias.areamanager.app.model.enuns.FormaPagamentoEnum;
import cleber.dias.areamanager.app.model.enuns.StatusPagamentoEnum;

import com.j256.ormlite.dao.Dao;

@EActivity(R.layout.activity_reserva)
public class ReservaActivity extends ActionBarActivity {

	@OrmLiteDao(helper = DatabaseHelper.class, model = Reserva.class)
	Dao<Reserva, Long> reservaDao;

	@ViewById
	TextView lblTituloReserva;
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

	private Reserva reserva;

	@SuppressLint("SimpleDateFormat")
	@AfterViews
	void afterViews() {
		long timeData = this.getIntent().getLongExtra(CalendarioActivity.EXTRA_KEY_DATA_RESERVA, 0L);

		this.lblData.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(timeData)));

		this.reserva = this.buscarReservaPorTime(timeData);
		if (this.reserva == null) {
			this.lblTituloReserva.setText(this.getString(R.string.label_incluir_reserva));

			this.reserva = new Reserva();
			this.reserva.setData(timeData);
		} else {
			this.lblTituloReserva.setText(this.getString(R.string.label_editar_reserva));

			this.txtCPF.setText(String.valueOf(this.reserva.getCpf()));
			this.txtNome.setText(this.reserva.getNome());
			this.txtEndereco.setText(this.reserva.getEndereco());
			this.txtTelefone.setText(this.reserva.getTelefone());

			if (FormaPagamentoEnum.CHEQUE.getId().equals(this.reserva.getFormaPagamento())) {
				this.rdoGrpFormaPagamento.check(R.id.rdoCheque);
			} else if (FormaPagamentoEnum.DEPOSITO.getId().equals(this.reserva.getFormaPagamento())) {
				this.rdoGrpFormaPagamento.check(R.id.rdoDeposito);
			} else if (FormaPagamentoEnum.DINHEIRO.getId().equals(this.reserva.getFormaPagamento())) {
				this.rdoGrpFormaPagamento.check(R.id.rdoDinheiro);
			}

			if (StatusPagamentoEnum.PAGO.getId() == this.reserva.getStatusPagamento()) {
				this.rdoGrpStatusPagamento.check(R.id.rdoPago);
			} else {
				this.rdoGrpStatusPagamento.check(R.id.rdoNaoPago);
			}
		}
	}

	@Click(R.id.btnSalvar)
	void actionSalvar() {
		this.txtCPF.setError(null);
		this.txtNome.setError(null);
		this.txtTelefone.setError(null);

		String cpf = this.txtCPF.getText().toString();
		String nome = this.txtNome.getText().toString();
		String telefone = this.txtTelefone.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(cpf)) {
			this.txtCPF.setError(this.getString(R.string.erro_campo_obrigatorio));
			focusView = this.txtCPF;
			cancel = true;
		}
		if (TextUtils.isEmpty(nome)) {
			this.txtNome.setError(this.getString(R.string.erro_campo_obrigatorio));
			focusView = this.txtNome;
			cancel = true;
		}
		if (TextUtils.isEmpty(telefone)) {
			this.txtTelefone.setError(this.getString(R.string.erro_campo_obrigatorio));
			focusView = this.txtTelefone;
			cancel = true;
		}
		if (cancel) {
			focusView.requestFocus();
		} else {
			try {
				this.reserva.setCpf(Long.parseLong(cpf));
				this.reserva.setNome(nome);
				this.reserva.setEndereco(this.txtEndereco.getText().toString());
				this.reserva.setTelefone(telefone);
				switch (this.rdoGrpFormaPagamento.getCheckedRadioButtonId()) {
				case R.id.rdoCheque:
					this.reserva.setFormaPagamento(FormaPagamentoEnum.CHEQUE.getId());
					break;
				case R.id.rdoDeposito:
					this.reserva.setFormaPagamento(FormaPagamentoEnum.DEPOSITO.getId());
					break;
				case R.id.rdoDinheiro:
					this.reserva.setFormaPagamento(FormaPagamentoEnum.DINHEIRO.getId());
					break;
				default:
					this.reserva.setFormaPagamento(null);
					break;
				}
				this.reserva.setStatusPagamento(this.rdoGrpStatusPagamento.getCheckedRadioButtonId() == R.id.rdoPago ? StatusPagamentoEnum.PAGO.getId() : StatusPagamentoEnum.NAO_PAGO.getId());
				if (this.reserva.getId() == 0L) {
					this.reservaDao.create(this.reserva);
				} else {
					this.reservaDao.update(this.reserva);
				}
				this.setResult(RESULT_OK);
				this.finish();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Click(R.id.btnCancelar)
	void actionCancelar() {
		OnClickListener listenerSim = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				ReservaActivity.this.setResult(RESULT_CANCELED);
				ReservaActivity.this.finish();
			}
		};
		new AlertDialog.Builder(this).setTitle(this.getString(R.string.label_confirmacao)).setMessage(this.getString(R.string.label_mensagem_cancelar))
		.setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, listenerSim).setNegativeButton(android.R.string.no, null)
		.show();
	}

	private Reserva buscarReservaPorTime(long time) {
		try {
			List<Reserva> reservasTime = this.reservaDao.queryForEq("data", time);
			return reservasTime.isEmpty() ? null : reservasTime.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
