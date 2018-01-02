package com.gil.wood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gil.wood.androidExtraRes.AutoResizeTextView;

import static com.gil.wood.nagar.main.GreedyResults;

public class Results extends AppCompatActivity {


    private AutoResizeTextView testResultsTextView;
    private String resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        testResultsTextView = (AutoResizeTextView) findViewById(R.id.testat);
        resultString = displayResultText();
        testResultsTextView.setText(resultString);
    }





    public String displayResultText (){
        double[] AvailableWoodList = (double[]) getIntent().getSerializableExtra("invArray");
        double[] PiecesList = (double[]) getIntent().getSerializableExtra("demArray");
        boolean isGreedyEfficiency = (boolean) getIntent().getSerializableExtra("isGreedyEfficiency");
        return GreedyResults (AvailableWoodList, PiecesList, isGreedyEfficiency);

    }
}
