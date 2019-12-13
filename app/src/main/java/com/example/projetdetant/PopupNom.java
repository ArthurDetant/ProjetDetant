package com.example.projetdetant;


import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;



public class PopupNom extends Dialog {

    EditText EditTextnomJ;
    Button validButtonName;


    public PopupNom(Activity activity){ //Récupération du layout de la popup(my_popup_name)
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.my_popup_name);

        EditTextnomJ = findViewById(R.id.EditTextnomJ);
        validButtonName = findViewById(R.id.validName);
    }

    public Button getValidButtonName(){
        return validButtonName;
    }

    public String getName(){
        return EditTextnomJ.getText().toString();
    }

    public void build(){
        show();
    }
}