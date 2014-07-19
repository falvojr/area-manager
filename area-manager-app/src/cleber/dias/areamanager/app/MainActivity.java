package cleber.dias.areamanager.app;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import cleber.dias.areamanager.ext.TextViewEx;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main)
public class MainActivity extends ActionBarActivity {

	@ViewById(R.id.txtDescricaoApp)
	protected TextViewEx txtDescricao;

	@AfterViews
	void afterViews() {
		// Justifica o texto de descrição do aplicativo:
		this.txtDescricao.setText(this.getString(R.string.label_descricao_app), true);
	}

	@OptionsItem(R.id.action_sobre)
	void actionSettings() {
		// Infla o layout da view com a mensagem do Alert:
		TextViewAlertDialogLayout dialoglayout = TextViewAlertDialogLayout_.build(this);
		dialoglayout.setText(this.getString(R.string.label_descricao_sobre));

		// Cria e apresenta o Alert:
		new AlertDialog.Builder(this).setTitle(this.getString(R.string.label_sobre)).setView(dialoglayout).setIcon(android.R.drawable.ic_dialog_info)
		.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create().show();
	}
}
