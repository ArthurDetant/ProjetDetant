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
    String puissanceJ, PVJ, puissanceADV, res, idMonstre, potion, valeurPotion, nomJoueur;

    private Button Button_Attaque, Button_Fuite;

    private TextView textView_Puissanceadv, textView_Puissance, textView_PV, nomMonstre, TextViewnomJoueur;

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

        Intent data = getIntent();  // récupération des valeurs de l'Intent depuis le mainActivity
        idMonstre= data.getStringExtra("idMonstre");
        puissanceADV = data.getStringExtra("PuissanceMonstre");
        puissanceJ = data.getStringExtra("PuissanceJoueur");
        PVJ = data.getStringExtra("PVJoueur");
        potion = data.getStringExtra("Potion");
        valeurPotion = data.getStringExtra("Valeur");
        nomJoueur = data.getStringExtra("NomJoueur");

        TextViewnomJoueur.setText(nomJoueur);  //Affichage du nom du joueur
        bonus();  //Affichage des potions

        String monstreID = "monstre"+idMonstre; // mettre dans un String "monstreXX"
        int resID = getResources().getIdentifier(monstreID , "drawable", getPackageName()); // resID prend la bonne synthaxe pour changer l'image en fonction du monstre récupéré précédement
        Imagemonstre.setImageResource(resID);  // On remplace l'image par celle correspondant au monstre de la salle (images dans le drawable)
        resID = getResources().getIdentifier(monstreID, "string", getPackageName()); //resID prend la bonne synthaxe pour changer le nom en fonction du monstre récupéré précédement
        nomMonstre.setText(getString(resID));  // On remplace le nom par celui correspondant au monstre de la salle (nom dans le strings.xml)

        // On actualise les textView du layout
        textView_Puissanceadv.setText(puissanceADV);
        textView_Puissance.setText(puissanceJ);
        textView_PV.setText(PVJ);

        // Quand on click sur le bouton Attaquer
        Button_Attaque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res = fight();  // prend la valeur "victoire" ou "défaite"
                gestionNombres(res); // Calcul des valeurs à mettre à jour en fonction du résultat précédent

                // Envoi des Valeurs vers le mainActivity via un Intent
                Intent intent = new Intent(FightActivity.this,MainActivity.class);
                intent.putExtra("PuissanceJoueur", puissanceJ);
                intent.putExtra("PVJoueur", PVJ);
                intent.putExtra("resultatfight", res);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        //si Fuite ou bouton retour téléphone
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

        resultat= Integer.parseInt(puissanceJ) * rand1 - Integer.parseInt(puissanceADV) * rand2; //Fonction de fight

        if (resultat>=0){
            return "victoire";
        }
        else{
            return "defaite";
        }
    }

    public void gestionNombres(String res){

        if (res.equals("victoire")){  //En cas de victoire la puissance du joueur augmente de 10
            int puissanceJ_tps =Integer.parseInt(puissanceJ);
            puissanceJ_tps +=10;
            puissanceJ= Integer.toString(puissanceJ_tps);
        }
        if (res.equals("defaite")){  //En cas de défaite les PVs du joueurs baissent de 3
            int pv_tps =Integer.parseInt(PVJ);
            pv_tps-=3;
            PVJ =Integer.toString(pv_tps);
        }
    }

    public void bonus(){  //Toast qui affiche la valeur de la potion
        if (potion.equals("power")){
            Toast.makeText(FightActivity.this, "votre puissance est augmentée de: "+valeurPotion, Toast.LENGTH_SHORT).show();
        }else if (potion.equals("life")){
            Toast.makeText(FightActivity.this, "votre vie est augmentée de: "+valeurPotion, Toast.LENGTH_SHORT).show();
        }
    }
}
