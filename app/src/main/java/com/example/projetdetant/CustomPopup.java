package com.example.projetdetant;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;


public class CustomPopup extends Dialog {


    private Button validerbutton;
    private EditText editTextpuissance;
    private EditText editTextvie;
    private EditText editTextmaxmonstre;


    public CustomPopup(Activity activity){
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.my_popup_template);
        validerbutton = findViewById(R.id.buttonvalider);
        editTextpuissance = findViewById(R.id.editTextpuissance);
        editTextvie = findViewById(R.id.editTextvie);
        editTextmaxmonstre = findViewById(R.id.editTextmaxmonstre);

    }
    public String getVie(){ return editTextvie.getText().toString();}
    public String getPuissance(){ return editTextpuissance.getText().toString();}
    public String getPuissanceMaxMonstre(){ return editTextmaxmonstre.getText().toString();}

    public Button getValiderbutton(){
        return validerbutton;
    }


    public void build(){
        show();
    }
}
