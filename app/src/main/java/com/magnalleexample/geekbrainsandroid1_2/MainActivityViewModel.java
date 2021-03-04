package com.magnalleexample.geekbrainsandroid1_2;


import android.provider.CallLog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;


import com.magnalleexample.geekbrainsandroid1_2.databinding.ActivityMainBinding;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class MainActivityViewModel extends ViewModel {

    public MainActivityViewModel(){
        initFormatter();
    }
    public MainActivityViewModel(SavedStateHandle state){
        this.state = state;
        if(state.contains("inputString")){
            inputString.append((String) state.get("inputString"));
            history.append((String) state.get("history"));
            answer = state.get("answer");
            previousOperand = state.get("previousOperand");
            strType = state.get("strType");
            lastOperation = state.get("lastOperation");
        }
        initFormatter();
    }

    public void onSaveState(){
        state.set("inputString", inputString.toString());
        state.set("history", history.toString());
        state.set("answer", answer);
        state.set("previousOperand", previousOperand);
        state.set("strType", strType);
        state.set("lastOperation", lastOperation);
    }

    private SavedStateHandle state;

    public ActivityMainBinding binding;
    public  enum StrType{
        INPUT, ANSWER, ERROR, NEW_INPUT
    }
    public enum Operation{
        PLUS, MINUS, DIVIDE, MULTIPLY, SQR, SQRT, ONE_DIV;
        public Operation getOperation(){
            return this;
        }
    }
    private final StringBuilder inputString = new StringBuilder();
    private final StringBuilder history = new StringBuilder();
    private double answer = 0.0;
    private double previousOperand = 0.0;
    private StrType strType = StrType.INPUT;
    private Operation lastOperation = null;
    NumberFormat formatter;

    public void initFormatter(){
        formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(7);
        formatter.setMinimumFractionDigits(0);
    }

    public String getCurrentString(){
        switch(strType) {
            case INPUT:
            case NEW_INPUT:
                return inputString.toString();
            case ANSWER: return formatter.format(answer);
            case ERROR: return "ERROR";
        }
        return "0";
    }

    public void updateResult(){
        binding.viewResult.setText(getCurrentString());
        binding.viewHistory.setText(history.toString());
    }

    public void inputNumber(char symbol){
        if(strType != StrType.INPUT){
            setPreviousOperand();
            inputString.delete(0, inputString.length());
            strType = strType.INPUT;
        }
        inputString.append(symbol);
        updateResult();
    }
    public void calculate(boolean isUnary){
        double operand = 0.0;
        if(!isUnary) {
            operand = getCurrentInput();
            history.append(inputString.toString());
        }
        switch(lastOperation){
            case PLUS: answer = previousOperand + operand;
                break;
            case MINUS: answer = previousOperand - operand;
                break;
            case MULTIPLY:
                answer = previousOperand * operand;
                break;
            case DIVIDE:
                if(operand == 0)
                    strType = StrType.ERROR;
                else {
                    answer = previousOperand / operand;
                }
                break;
            case SQR: answer = previousOperand * previousOperand;
                break;
            case SQRT:
                if(previousOperand < 0)
                    strType = StrType.ERROR;
                else
                    answer = Math.sqrt(previousOperand);
                break;
            case ONE_DIV:
                if(previousOperand == 0)
                    strType = StrType.ERROR;
                else
                    answer = 1/previousOperand;
                break;
        }
        if(strType != StrType.ERROR) {
            history.append(" = " + formatter.format(answer));
            strType = StrType.ANSWER;
        }
    }

    private double getCurrentInput(){
        try {
            return formatter.parse(inputString.toString()).doubleValue();
        } catch (ParseException e) {
            strType = StrType.ERROR;
            return 0;
        }
    }

    private void writeHistory(Operation operation, double operand){
        switch(operation){
            case PLUS: history.append(" + ");
                break;
            case MINUS: history.append(" - ");
                break;
            case MULTIPLY: history.append(" * ");
                break;
            case DIVIDE: history.append(" / ");
                break;
            case ONE_DIV:
                history.append(" 1/("+  formatter.format(operand) + ")");
                break;
            case SQR:
                history.append(" SQR("+  formatter.format(operand) + ")");
                break;
            case SQRT:
                history.append(" SQRT("+  formatter.format(operand) + ")");
                break;
        }
    }

    public void binaryOperation(Operation operation){
        if(lastOperation != null)
            calculate(false);
        else {
            strType = StrType.NEW_INPUT;
            history.append(getCurrentInput());
        }
        setPreviousOperand();
        writeHistory(operation,0.0);
        lastOperation = operation;
        updateResult();
    }

    public void unaryOperation(Operation operation){
        if(lastOperation != null)
            calculate(false);
        setPreviousOperand();
        lastOperation = operation;
        writeHistory(operation, previousOperand);
        calculate(true);
        updateResult();
    }

    public void clearInput(){
        inputString.delete(0, inputString.length());
        answer = 0.0;
        updateResult();
    }

    public void clearAll(){
        inputString.delete(0, inputString.length());
        answer = 0.0;
        previousOperand = 0.0;
        updateResult();
    }

    public void equals(){
        if(lastOperation != null)
            calculate(false);
        lastOperation = null;
        setPreviousOperand();
        updateResult();
        history.delete(0, history.length());
    }

    private void setPreviousOperand(){
        switch(strType){
            case INPUT:
            case NEW_INPUT:
                previousOperand = getCurrentInput();
                break;
            case ANSWER: previousOperand = answer;
        }
    }
}
