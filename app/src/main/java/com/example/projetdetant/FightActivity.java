package com.example.projetdetant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class FightActivity extends AppCompatActivity {
    String puissanceJ;
    String PVJ ;
    String puissanceADV;
    String res;

    private Button Button_Attaque;
    private Button Button_Fuite;

    private TextView textView_Puissanceadv;
    private TextView textView_Puissance;
    private TextView textView_PV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        Button_Attaque = findViewById(R.id.Button_Attaque);
        Button_Fuite = findViewById(R.id.Button_Fuite);
        textView_Puissanceadv = findViewById(R.id.textView_Puissanceadv);
        textView_Puissance = findViewById(R.id.textView_Puissance);
        textView_PV = findViewById(R.id.textView_PV);

        Intent data = getIntent();
        puissanceADV = data.getStringExtra("PuissanceMonstre");
        puissanceJ = data.getStringExtra("PuissanceJoueur");
        PVJ = data.getStringExtra("PVJoueur");

        textView_Puissanceadv.setText(puissanceADV);
        textView_Puissance.setText(puissanceJ);
        textView_PV.setText(PVJ);

        Button_Attaque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res = fight();
                gestionNombres(res);
                Intent intent = new Intent(FightActivity.this,MainActivity.class);
                intent.putExtra("PuissanceMonstre", puissanceADV);
                intent.putExtra("PuissanceJoueur", puissanceJ);
                intent.putExtra("PVJoueur", PVJ);

                intent.putExtra("resultatfight", res);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        Button_Fuite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FightActivity.this,MainActivity.class);
                setResult(Activity.RESULT_CANCELED,intent);
                finish();
            }
        });


    }
    public String fight(){
        double resultat;
        double rand1 = Math.random();
        double rand2 = Math.random();
        resultat= Integer.parseInt(puissanceJ) * rand1 - Integer.parseInt(puissanceADV) * rand2;
        if (resultat>=0){
            return "victoire";
        }
        else{
            return "defaite";
        }
    }
    public void gestionNombres(String res){

        if (res.equals("victoire")){
            int puissanceJ_tps =Integer.parseInt(puissanceJ);
            puissanceJ_tps +=10;
            System.out.println(puissanceJ_tps);
            puissanceJ= Integer.toString(puissanceJ_tps);
        }
        if (res.equals("defaite")){
            int pv_tps =Integer.parseInt(PVJ);
            pv_tps-=3;
            PVJ =Integer.toString(pv_tps);
        }

    }


}
