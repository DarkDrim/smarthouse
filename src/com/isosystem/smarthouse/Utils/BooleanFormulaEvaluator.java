package com.isosystem.smarthouse.Utils;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import android.text.TextUtils;

public class BooleanFormulaEvaluator {

	// �������� �������
	String mFormula;
	// �������� ����������
	String mVariable;

	public BooleanFormulaEvaluator(String formula, String var) {

		this.mFormula = formula;
		this.mVariable = var;
	}

	public EvaluatorResult eval() {

		EvaluatorResult result = new EvaluatorResult();
		Evaluator evaluator = new Evaluator();
	
		String replacedFormula = mFormula;
		
		// ���� ���� ������ ���������� true	
		if (TextUtils.isEmpty(replacedFormula.trim())) {
			result.booleanResult = true;
			return result;
		}

		// �������� �������� ����������
		try {
			evaluator.putVariable("x", mVariable);
		} catch (Exception e) {
			result.isCorrect = false;
			result.errorMessage = "������ ��� ������� �������� �������� ���������� � �������. ��������� � ���������� �������� ���������� x";
			e.printStackTrace();
			return result;
		}

		// ������ 'x' � '�' �� #{x}
		try {
			replacedFormula = replacedFormula.replace('X', 'x');
			replacedFormula = replacedFormula.replace("x", "#{x}");
		} catch (Exception e) {
			result.isCorrect = false;
			result.errorMessage = "������ ��� ������� �������� ���������� � �������. ��������� � ������������ ��������� �������";
			e.printStackTrace();
			return result;
		}

		String evalResult = "";

		// ��������� ��������. ����������� ���������� replacedFormula
		try {
			evalResult = evaluator.evaluate(replacedFormula);
		} catch (EvaluationException e) {
			result.isCorrect = false;
			result.errorMessage = "������ ��� ���������� �������. ��������� � ������������ ��������� �������";
			e.printStackTrace();
			return result;
		}
		
		if (evalResult == "Infinity") {
			result.isCorrect = false;
			result.errorMessage = "��� ������ � ���������� ������� �� ����, �������������� �������";
			return result;
		}
		
		if (evalResult.equals("1.0")) {
			result.booleanResult = true;
		} else {
			result.booleanResult = false;
		}

		return result;
	}

}
