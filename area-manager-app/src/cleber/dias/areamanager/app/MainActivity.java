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
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import cleber.dias.areamanager.ext.TextViewEx;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends ActionBarActivity {

	@ViewById(R.id.lblDescricaoApp)
	protected TextViewEx txtDescricao;

	@AfterViews
	void afterViews() {
		// Justifica o texto de descrição do aplicativo:
		this.txtDescricao.setText(this.getString(R.string.label_descricao_app), true);
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
