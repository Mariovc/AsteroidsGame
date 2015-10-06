package com.mvc.velascom_u2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Author: Mario Velasco Casquero
 * Date: 27/07/2015
 * Email: m3ario@gmail.com
 */
public class Tab2 extends Fragment {

    private static final int EMPTY_OPERATOR = 0;
    private static final int CLEAR_OPERATOR = 1;
    private static final int EQUAL_OPERATOR = 2;
    private static final int PLUS_OPERATOR = 3;
    private static final int MINUS_OPERATOR = 4;
    private static final int MULTI_OPERATOR = 5;
    private static final int DIV_OPERATOR = 6;


    private TextView resultTV;

    private int number1;
    private int operator = EMPTY_OPERATOR;
    private boolean putNewNumber = true;
    private boolean cleared = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);
        resultTV = (TextView) view.findViewById(R.id.resultTV);
        view.findViewById(R.id.number0Button).setOnClickListener(new onClickNumber(0));
        view.findViewById(R.id.number1Button).setOnClickListener(new onClickNumber(1));
        view.findViewById(R.id.number2Button).setOnClickListener(new onClickNumber(2));
        view.findViewById(R.id.number3Button).setOnClickListener(new onClickNumber(3));
        view.findViewById(R.id.number4Button).setOnClickListener(new onClickNumber(4));
        view.findViewById(R.id.number5Button).setOnClickListener(new onClickNumber(5));
        view.findViewById(R.id.number6Button).setOnClickListener(new onClickNumber(6));
        view.findViewById(R.id.number7Button).setOnClickListener(new onClickNumber(7));
        view.findViewById(R.id.number8Button).setOnClickListener(new onClickNumber(8));
        view.findViewById(R.id.number9Button).setOnClickListener(new onClickNumber(9));
        view.findViewById(R.id.operatorCButton).setOnClickListener(new onClickOperator(CLEAR_OPERATOR));
        view.findViewById(R.id.operatorEqualButton).setOnClickListener(new onClickOperator(EQUAL_OPERATOR));
        view.findViewById(R.id.operatorPlusButton).setOnClickListener(new onClickOperator(PLUS_OPERATOR));
        view.findViewById(R.id.operatorMinusButton).setOnClickListener(new onClickOperator(MINUS_OPERATOR));
        view.findViewById(R.id.operatorMultiButton).setOnClickListener(new onClickOperator(MULTI_OPERATOR));
        view.findViewById(R.id.operatorDivButton).setOnClickListener(new onClickOperator(DIV_OPERATOR));
        return view;
    }


    public void addNumber (int number) {
        int finalNumber;
        if (putNewNumber) {
            finalNumber = number;
            putNewNumber = false;
        } else {
            finalNumber = getScreenNumber();
            finalNumber = finalNumber * 10 + number;
        }
        setScreenNumber(finalNumber);
    }

    public void onClickOperator(int operator) {
        int screenNumber = getScreenNumber();
        if (cleared) {
            cleared = false;
            number1 = screenNumber;
            putNewNumber = true;
        }
        if (putNewNumber || this.operator == EQUAL_OPERATOR) {
            number1 = screenNumber;
            this.operator = operator;
            putNewNumber = true;
        }
    }

    private void onClickEqual() {
        int screenNumber = getScreenNumber();
        if (!cleared) {
            number1 = operate(number1, screenNumber, operator);
            setScreenNumber(number1);
            operator = EQUAL_OPERATOR;
        }
    }

    private int operate(int number1, int number2, int operator) {
        int finalNumber;
        switch (operator) {
            case PLUS_OPERATOR:
                finalNumber = number1 + number2;
                break;
            case MINUS_OPERATOR:
                finalNumber = number1 - number2;
                break;
            case MULTI_OPERATOR:
                finalNumber = number1 * number2;
                break;
            case DIV_OPERATOR:
                finalNumber = number1 / number2;
                break;
            default:
                finalNumber = number1;
                break;
        }
        return finalNumber;
    }

    private int getScreenNumber() {
        return Integer.parseInt(resultTV.getText().toString());
    }

    private void setScreenNumber(int number) {
        resultTV.setText(String.valueOf(number));
    }

    private void clearScreen() {
        number1 = 0;
        operator = EMPTY_OPERATOR;
        resultTV.setText("0");
        putNewNumber = true;
        cleared = true;
    }


    private class onClickOperator implements View.OnClickListener {

        private int operator;

        public onClickOperator(int operator) {
            this.operator = operator;
        }

        @Override
        public void onClick(View v) {
            switch (operator) {
                case EQUAL_OPERATOR:
                    onClickEqual();
                    break;
                case CLEAR_OPERATOR:
                    clearScreen();
                    break;
                case PLUS_OPERATOR:
                case MINUS_OPERATOR:
                case MULTI_OPERATOR:
                case DIV_OPERATOR:
                    onClickOperator(operator);
                    break;
            }
        }
    }

    private class onClickNumber implements View.OnClickListener {

        private int number;

        public onClickNumber(int number) {
            this.number = number;
        }

        @Override
        public void onClick(View v) {
            addNumber(number);
        }
    }
}