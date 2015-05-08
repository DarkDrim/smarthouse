package com.isosystem.smarthouse.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import com.isosystem.smarthouse.Notifications.Notifications;
import com.isosystem.smarthouse.Utils.BooleanFormulaEvaluator;
import com.isosystem.smarthouse.Utils.EvaluatorResult;
import com.isosystem.smarthouse.Utils.MathematicalFormulaEvaluator;

public class ValidationFormulaCheckDialog extends DialogFragment {

	String outgoingFormula = "";
	String validationFormula = "";

	public ValidationFormulaCheckDialog(String formula1, String formula2) {
		super();

		this.outgoingFormula = formula1;
		this.validationFormula = formula2;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);

		final EditText input = new EditText(this.getActivity());
		input.setKeyListener(DigitsKeyListener.getInstance("0123456789."));

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(
				"Укажите формулу обработки, формулу валидации и ввидете x:")
				.setView(input)
				.setPositiveButton("Проверить",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								MathematicalFormulaEvaluator eval = new MathematicalFormulaEvaluator(
										outgoingFormula, input.getText()
												.toString(), "0", true);

								EvaluatorResult result = eval.eval();

								if (result.isCorrect == false) {
									Notifications.showTooltip(getActivity(),
											result.errorMessage);
								} else {
									BooleanFormulaEvaluator boolEval = new BooleanFormulaEvaluator(
											validationFormula,
											result.numericRoundedResult);
									EvaluatorResult boolResult = boolEval
											.eval();

									if (boolResult.isCorrect == false) {
										Notifications.showTooltip(
												getActivity(),
												boolResult.errorMessage);
									} else {
										if (boolResult.booleanResult == true) {
											Notifications.showTooltip(
													getActivity(),
													"Значение прошло валидацию");
										} else {
											Notifications
													.showTooltip(getActivity(),
															"Значение не прошло валидацию");
										}
									}
								}
							}
						})
				.setNegativeButton("Отмена",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								ValidationFormulaCheckDialog.this.dismiss();
							}
						});
		return builder.create();
	}
}