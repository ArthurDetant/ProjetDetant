package com.example.projetdetant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class FightActivity extends AppCompatActivity {
    String puissanceJ;
    String PVJ ;
    String puissanceADV;
    String res;
    String idMonstre;
    String potion;
    String valeurPotion;
    String nomJoueur;

    private Button Button_Attaque;
    private Button Button_Fuite;

    private TextView textView_Puissanceadv;
    private TextView textView_Puissance;
    private TextView textView_PV;
    private TextView nomMonstre;
    private TextView TextViewnomJoueur;
    private ImageView Imagemonstre;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        Button_Attaque = findViewById(R.id.Button_Attaque);
        Button_Fuite = findViewById(R.id.Button_Fuite);
        textView_Puissanceadv = findViewById(R.id.textView_Puissanceadv);
        textView_Puissance = findViewById(R.id.textView_Puissance);
        textView_PV = findViewById(R.id.textView_PV);
        nomMonstre = findViewById(R.id.nomMonstre);
        TextViewnomJoueur = findViewById(R.id.TextViewnomJoueur);
        Imagemonstre = findViewById(R.id.Imagemonstre);


        Intent data = getIntent();
        idMonstre= data.getStringExtra("idMonstre");
        puissanceADV = data.getStringExtra("PuissanceMonstre");
        puissanceJ = data.getStringExtra("PuissanceJoueur");
        PVJ = data.getStringExtra("PVJoueur");
        potion = data.getStringExtra("Potion");
        valeurPotion = data.getStringExtra("Valeur");
        nomJoueur = data.getStringExtra("NomJoueur");

        TextViewnomJoueur.setText(nomJoueur);
        bonus();

        String monstreID = "monstre"+idMonstre;
        int resID = getResources().getIdentifier(monstreID , "drawable", getPackageName());
        Imagemonstre.setImageResource(resID);
        resID = getResources().getIdentifier(monstreID, "string", getPackageName());
        nomMonstre.setText(getString(resID));

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
            puissanceJ= Integer.toString(puissanceJ_tps);
        }
        if (res.equals("defaite")){
            int pv_tps =Integer.parseInt(PVJ);
            pv_tps-=3;
            PVJ =Integer.toString(pv_tps);
        }

    }
    public void bonus(){
        if (potion.equals("power")){
            Toast.makeText(FightActivity.this, "votre puissance est augmentée de: "+valeurPotion, Toast.LENGTH_SHORT).show();
        }else if (potion.equals("life")){
            Toast.makeText(FightActivity.this, "votre vie est augmentée de: "+valeurPotion, Toast.LENGTH_SHORT).show();
        }
    }



}
