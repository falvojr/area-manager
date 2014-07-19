package cleber.dias.areamanager.app;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import cleber.dias.areamanager.ext.TextViewEx;

@EViewGroup(R.layout.textview_alert_dialog)
public class TextViewAlertDialogLayout extends LinearLayout {

	@ViewById
	protected TextViewEx txtAlertDialog;

	public TextViewAlertDialogLayout(Context context) {
		super(context);
	}

	public TextViewAlertDialogLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setText(String mensagem) {
		this.txtAlertDialog.setText(mensagem, true);
	}
}
