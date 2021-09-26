package com.nitnelav.metricas.halstead;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Vector;

public class Scanner {
    Vector<String> tokens = new Vector<>();
    Vector<String> variablesVector = new Vector<>();
    Vector<Double> numbersVector = new Vector<>();
    private String numbers = "123456789";
    private String alphabet = "abcedfghijklmnñopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String operators = "+-*/%<>=:,";
    private String space = " ";
    private String specialCharacters = "%f%d%s"; //formats
    private int ifAmount = 0;
    private int parenthesisAmount = 0;
    private int addAmount = 0; //+
    private int subAmount =0; //-
    private int divAmount = 0; // /
    private int mulAmount = 0; // -
    private int equalAmount = 0;// =
    private int moreAmount = 0; // >
    private int lessAmount = 0; // <
    private int comaAmount =0; // ,
    private int percentAmount = 0; // %
    private int doublePointAmount = 0; // :

    private String [][] ignore = {
            {"s0","specialCharacters","sf"},
            {"s0","space","sf"}
    };

    private String [][] parenthesisCase = {
            {"s0", "(", "sf"},
            {"s0", ")", "sf"},
    };

    private String [][] operatorsCase = {
            {"s0","operators","sf"}
    };

    private String [][] variableCase = {
            {"s0","alphabet","s1"},
            {"s1","alphabet","s1"},
            {"s1","numbers","s1"},
            {"s1","specialCharacters","sf"},
            {"s1","space","sf"},
            {"s1","operators","sf"},
            {"s1","(","sf"},
            {"s1",")","sf"}
    };

    private String [][] numbersCase = {
            {"s0","numbers","s1"},
            {"s1","numbers","s1"},
            {"s1","specialCharacters","sf"},
            {"s1","space","sf"},
            {"s1","operators","sf"},
            {"s1","(","sf"},
            {"s1",")","sf"},
    };

    private String [][] ifCase = {
            {"s0","i","s1"},
            {"s1","f","s2"},
            {"s2","space","sf"},
            {"s2","(","sf"},
    };


    public String startScanner(String code){

        int myPointer = 0;
        int previousPoint;
        String token;
        String [] cases = new String[]{
                "ifCase",
                "variableCase",
                "numbersCase",
                "parenthesisCase",
                "operatorsCase",
                "ignore"
        };

        while (myPointer < code.length())
        {
            int j = 0;
            int pointAux;
            while (j < cases.length)
            {
                pointAux = this.valueCase(code,myPointer,cases[j]);
                if(pointAux > 0){
                    previousPoint = myPointer;
                    myPointer = pointAux;
                    token = code.substring(previousPoint,myPointer);
                    tokens.add(token);
                    if(cases[j].equals("ifCase") || token.equals("if")){
                        this.ifAmount++;
                    }else if (cases[j].equals("parenthesisCase") || token.equals("(") || token.equals(")")){
                        this.parenthesisAmount++;
                    }else if(cases[j].equals("operatorsCase")){
                        if(token.equals("+")){
                            this.addAmount++;
                        }else if(token.equals("-")){
                            this.subAmount++;
                        }else if (token.equals("/")){
                            this.divAmount++;
                        }else if (token.equals("*")){
                            this.mulAmount++;
                        }else if (token.equals("=")){
                            this.equalAmount++;
                        }else if (token.equals(">")){
                            this.moreAmount++;
                        }else if (token.equals("<")){
                            this.lessAmount++;
                        }else if (token.equals(",")){
                            this.comaAmount++;
                        }else if(token.equals(":")){
                            this.doublePointAmount++;
                        }
                    }else if(cases[j].equals("variableCase") || cases[j].equals("numbersCase")){
                        if(!variablesVector.contains(token)){
                            this.variablesVector.add(token);
                            this.numbersVector.add((double) 1);
                        }else {
                            for (int i = 0; i<variablesVector.size();i++) {
                                if(variablesVector.get(i).equals(token)){
                                    Double val = this.numbersVector.get(i);
                                    val = val + 1;
                                    this.numbersVector.set(i,val);
                                }
                            }
                        }
                    }

                    break;
                }else {
                    j++;
                }
            }
            if(j == cases.length)
            {
                return null;
            }
        }
        return String.valueOf(this.numbersVector.get(0));
    }

    private int valueCase(String code,int pointer,String CaseType){

        int j = pointer;
        int indexState = 0;
        String currentState = "s0";
        String [][] currentCase = this.caseType(CaseType);
        boolean other = false;
        if (currentCase == null){
            return 0;
        }
        while (indexState < currentCase.length && j<code.length()){
            String stateCase = currentCase[indexState][0];
            if(currentState.equals("sf")){
                if(CaseType.equals("operatorsCase") || CaseType.equals("ignore") || CaseType.equals("parentesis")) {
                    return j;
                }
                return j-1;
            }else{
                if(currentState.equals(stateCase)) {
                    if (this.contains(String.valueOf(code.charAt(j)),currentCase[indexState][1])){
                        currentState = currentCase[indexState][2];
                        indexState = 0;
                        j++;
                    }else{
                        indexState++;
                    }
                }else{
                    indexState++;
                }
            }
        }

        if(currentState.equals("sf")){
            if(CaseType.equals("operatorsCase") || CaseType.equals("ignore") || CaseType.equals("parenthesis")) {
                return j;
            }
            return j-1;
        }

        return 0;
    }

    @Nullable
    private String[][] caseType(String typeCase){
        if (typeCase.equals("ifCase"))
            return this.ifCase;
        else if (typeCase.equals("numbersCase"))
            return this.numbersCase;
        else if (typeCase.equals("parenthesisCase"))
            return this.parenthesisCase;
        else if(typeCase.equals("variablesCase"))
            return this.variableCase;
        else if (typeCase.equals("operatorsCase"))
            return this.operatorsCase;
        else if (typeCase.equals("ignore"))
            return this.ignore;

        return null;
    }

    private boolean contains(String cad,String typeCase){
        if(typeCase.equals("alphabet")) {
            if (this.alphabet.contains(cad))
                return true;
        }else if(typeCase.equals("specialCharacters")) {
            if (this.specialCharacters.contains(cad))
                return true;
        }else if(typeCase.equals("operatorsCase")) {
            if (this.operators.contains(cad))
                return true;
        }else if(typeCase.equals("space")) {
            if (this.space.contains(cad))
                return true;
        }else if(typeCase.equals("(")) {
            if ("(".contains(cad))
                return true;
        }else if(typeCase.equals(")")) {
            if (")".contains(cad))
                return true;
        }else if(typeCase.equals("numbers")) {
            if (this.numbers.contains(cad))
                return true;
        }else if(typeCase.equals("i")) {
            if ("i".contains(cad))
                return true;
        }else if(typeCase.equals("f")) {
            if ("f".contains(cad))
                return true;
        }else if (typeCase.equals("e")) {
            if ("e".contains(cad))
                return true;
        }else if (typeCase.equals("l")) {
            if ("l".contains(cad))
                return true;
        }else if (typeCase.equals("s")) {
            if ("s".contains(cad))
                return true;
        }else if (typeCase.equals("e")) {
            if ("e".contains(cad))
                return true;
        }

        return false;
    }

    public int getiFs(){
        return this.ifAmount;
    }

    public int getParenthesis(){
        return this.parenthesisAmount;
    }

    public int getAdd(){
        return this.addAmount;
    }

    public int getSub(){
        return this.subAmount;
    }

    public int getDivision(){
        return this.divAmount;
    }

    public int getMul(){
        return this.mulAmount;
    }

    public int getEqual(){
        return this.equalAmount;
    }

    public int getMore(){
        return this.moreAmount;
    }

    public int getLess(){
        return this.lessAmount;
    }

    public int getComa(){
        return this.comaAmount;
    }

    public int getDoublePoint(){
        return this.doublePointAmount;
    }

    public Vector<Double> getNumbersVector() {
        return numbersVector;
    }

    public Vector<String> getVariablesVector() {
        return variablesVector;
    }

    @Override
    public String toString() {
        return "Scanner{" +
                "tokens=" + tokens +
                ", variablesVector=" + variablesVector +
                ", numbersVector=" + numbersVector +
                ", numbers='" + numbers + '\'' +
                ", alphabet='" + alphabet + '\'' +
                ", operators='" + operators + '\'' +
                ", space='" + space + '\'' +
                ", specialCharacters='" + specialCharacters + '\'' +
                ", ifAmount=" + ifAmount +
                ", parenthesisAmount=" + parenthesisAmount +
                ", addAmount=" + addAmount +
                ", subAmount=" + subAmount +
                ", divAmount=" + divAmount +
                ", mulAmount=" + mulAmount +
                ", equalAmount=" + equalAmount +
                ", moreAmount=" + moreAmount +
                ", lessAmount=" + lessAmount +
                ", comaAmount=" + comaAmount +
                ", percentAmount=" + percentAmount +
                ", doublePointAmount=" + doublePointAmount +
                ", ignore=" + Arrays.toString(ignore) +
                ", parenthesisCase=" + Arrays.toString(parenthesisCase) +
                ", operatorsCase=" + Arrays.toString(operatorsCase) +
                ", variableCase=" + Arrays.toString(variableCase) +
                ", numbersCase=" + Arrays.toString(numbersCase) +
                ", ifCase=" + Arrays.toString(ifCase) +
                '}';
    }
}

