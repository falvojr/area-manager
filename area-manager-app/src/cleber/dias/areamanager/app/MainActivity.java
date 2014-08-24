package cleber.dias.areamanager.app;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import cleber.dias.areamanager.ext.TextViewEx;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends ActionBarActivity {

	public static final String EXTRA_EXIT = "EXIT";

	@ViewById
	protected TextViewEx lblDescricaoApp;

	@ViewById
	protected TextView lblVersao;

	@AfterViews
	void afterViews() {
		if (this.getIntent().getBooleanExtra(EXTRA_EXIT, false)) {
			this.finish();
		}
		// Justifica o texto de descrição do aplicativo:
		this.lblDescricaoApp.setText(this.getString(R.string.label_descricao_app), true);

		try {
			String versao = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
			this.lblVersao.setText(String.format(super.getString(R.string.label_versao_app), versao));
		} catch (NameNotFoundException e) { }
	}

	@Click(R.id.btnEntrar)
	void actionEntrar() {
		CalendarioActivity_.intent(this).start();
	}

	@Click(R.id.btnSair)
	void actionSair() {
		Intent intent = MainActivity_.intent(this).get();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(EXTRA_EXIT, true);
		this.startActivity(intent);
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

	@Override
	protected void onDestroy() {
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}
}
