package com.isosystem.smarthouse.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import com.isosystem.smarthouse.Notifications.Notifications;
import com.isosystem.smarthouse.Utils.EvaluatorResult;
import com.isosystem.smarthouse.Utils.MathematicalFormulaEvaluator;

public class FormulaCheckDialog extends DialogFragment {

	String formula = "";
	String fractionDigits = "";

	public FormulaCheckDialog(String str, String d) {
		super();

		this.formula = str;
		this.fractionDigits = d;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);

		final EditText input = new EditText(this.getActivity());
		input.setKeyListener(DigitsKeyListener.getInstance("0123456789."));

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(
				"������� ���������� �������� ����� ������� � ������� �������� X:")
				.setView(input)
				.setPositiveButton("���������",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								MathematicalFormulaEvaluator eval = new MathematicalFormulaEvaluator(
										formula, input.getText().toString(),
										fractionDigits, true);
								EvaluatorResult result = eval.eval();

								if (result.isCorrect == false) {
									Notifications.showTooltip(getActivity(),
											result.errorMessage);
								} else {
									Notifications
											.showTooltip(
													getActivity(),
													"���������: "
															+ result.numericRoundedResult);
								}
							}
						})
				.setNegativeButton("������",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								FormulaCheckDialog.this.dismiss();
							}
						});
		return builder.create();
	}
}