package com.gil.wood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.util.ArrayList;


import static com.gil.wood.R.string.calculate;

public class MainActivity extends AppCompatActivity {

    public static final int INV_SIZE = 4;
    public static final int DEM_SIZE = 4;



    // UI references.
    private EditText[] invQuantView = new EditText[INV_SIZE];
    private EditText[] invLengthView = new EditText[INV_SIZE];
    private EditText[] demQuantView = new EditText[DEM_SIZE];
    private EditText[] demLengthView = new EditText[DEM_SIZE];
    private Button calculateButton;
    private Switch simpleSwitch;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculateButton = (Button) findViewById(R.id.buttonCalculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick();
            }
        });
        assignViews(invQuantView, invLengthView, demQuantView, demLengthView);
        simpleSwitch = (Switch) findViewById(R.id.greedy);
    }

    private void assignViews(EditText[] invQuanViewsId, EditText[] invLenViewsId, EditText[] demQuanViewsId, EditText[] demLenViewsId) {

        invQuanViewsId[0] = (EditText) findViewById(R.id.invQuan1EditTextView);
        invQuanViewsId[1] = (EditText) findViewById(R.id.invQuan2EditTextView);
        invQuanViewsId[2] = (EditText) findViewById(R.id.invQuan3EditTextView);
        invQuanViewsId[3] = (EditText) findViewById(R.id.invQuan4EditTextView);

        invLenViewsId[0] = (EditText) findViewById(R.id.invLength1EditTextView);
        invLenViewsId[1] = (EditText) findViewById(R.id.invLength2EditTextView);
        invLenViewsId[2] = (EditText) findViewById(R.id.invLength3EditTextView);
        invLenViewsId[3] = (EditText) findViewById(R.id.invLength4EditTextView);

        demQuanViewsId[0] = (EditText) findViewById(R.id.demQuan1EditTextView);
        demQuanViewsId[1] = (EditText) findViewById(R.id.demQuan2EditTextView);
        demQuanViewsId[2] = (EditText) findViewById(R.id.demQuan3EditTextView);
        demQuanViewsId[3] = (EditText) findViewById(R.id.demQuan4EditTextView);

        demLenViewsId[0] = (EditText) findViewById(R.id.demLength1EditTextView);
        demLenViewsId[1] = (EditText) findViewById(R.id.demLength2EditTextView);
        demLenViewsId[2] = (EditText) findViewById(R.id.demLength3EditTextView);
        demLenViewsId[3] = (EditText) findViewById(R.id.demLength4EditTextView);
    }

    private void buttonClick() {
        Intent intent = new Intent(getApplicationContext(), Results.class);
        intent.putExtra("invArray", getWoodLengthsArray(invQuantView, invLengthView));
        intent.putExtra("demArray", getWoodLengthsArray(demQuantView, demLengthView));
        intent.putExtra("isGreedyEfficiency", simpleSwitch.isChecked());
        startActivity(intent);
    }


    /**
     * converts the input information of the UI`s to a primitive wood-pieces length array
     */
    private double[] getWoodLengthsArray(EditText[] QuantView, EditText[] LengthView) {
        ArrayList<Double> invArray = new ArrayList<>();

        for (int i = 0; i < QuantView.length; i++) {
            for (int j = 0; ((!QuantView[i].getText().toString().isEmpty()) && (j < Integer.parseInt(QuantView[i].getText().toString()))); j++) {
                if (!LengthView[i].getText().toString().isEmpty() && ((Double.parseDouble(LengthView[i].getText().toString())) > 0))
                    invArray.add(new Double(Double.parseDouble(LengthView[i].getText().toString())));
            }
        }

        double[] simpleInvArray = new double[invArray.size()];
        for (int i = 0; i < simpleInvArray.length; i++) {
            simpleInvArray[i] = invArray.get(i);
        }
        return simpleInvArray;
    }


}
