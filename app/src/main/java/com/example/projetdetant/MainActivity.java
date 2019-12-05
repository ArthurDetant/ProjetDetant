package com.example.projetdetant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {
    Vector puissanceM = new Vector();
    String resultatPartie;

    private ImageButton room01;
    private ImageButton room02;
    private ImageButton room03;
    private ImageButton room04;
    private ImageButton room05;
    private ImageButton room06;
    private ImageButton room07;
    private ImageButton room08;
    private ImageButton room09;
    private ImageButton room10;
    private ImageButton room11;
    private ImageButton room12;
    private ImageButton room13;
    private ImageButton room14;
    private ImageButton room15;
    private ImageButton room16;
    private ImageButton buttontps;

    private TextView resultat_combat;
    private TextView textView_PV;
    private TextView textView_piecenonexplorees;
    private TextView textView_Puissance;
    private TextView textView_Affichage;

    public static final int FIGHT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultat_combat= findViewById(R.id.resultat_combat);
        textView_PV= findViewById(R.id.textView_PV);
        textView_piecenonexplorees= findViewById(R.id.textView_piecenonexplorees);
        textView_Puissance= findViewById(R.id.textView_Puissance);
        textView_Affichage= findViewById(R.id.textView_Affichage);

        room01 = findViewById(R.id.room01);
        room02 = findViewById(R.id.room02);
        room03 = findViewById(R.id.room03);
        room04 = findViewById(R.id.room04);
        room05 = findViewById(R.id.room05);
        room06 = findViewById(R.id.room06);
        room07 = findViewById(R.id.room07);
        room08 = findViewById(R.id.room08);
        room09 = findViewById(R.id.room09);
        room10 = findViewById(R.id.room10);
        room11 = findViewById(R.id.room11);
        room12 = findViewById(R.id.room12);
        room13 = findViewById(R.id.room13);
        room14 = findViewById(R.id.room14);
        room15 = findViewById(R.id.room15);
        room16 = findViewById(R.id.room16);

        initialiserPuissance();

    }

    public void onClick(View view){
        buttontps= findViewById(view.getId());
        if(buttontps.getTag() == "Vaincu"){
            Toast.makeText(MainActivity.this, "le monstre est déjà mort!", Toast.LENGTH_SHORT).show();
            return;
        }else if(!textView_Affichage.getText().toString().matches("Résultat du combat")){
            Toast.makeText(MainActivity.this, "Veuillez recommencer une partie.", Toast.LENGTH_SHORT).show();
            return;
        }

        String Room = getResources().getResourceEntryName(view.getId());
        String numRoom = Room.substring(4);
        int i = Integer.parseInt(numRoom)-1;
        String puissancemonstre = puissanceM.get(i).toString();

        Intent intent = new Intent(MainActivity.this, FightActivity.class);
        intent.putExtra("PuissanceMonstre", puissancemonstre);
        intent.putExtra("PuissanceJoueur", textView_Puissance.getText().toString());
        intent.putExtra("PVJoueur", textView_PV.getText().toString());
        startActivityForResult(intent, FIGHT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode== Activity.RESULT_CANCELED){
            fuite();
            resultat_combat.setText("fuite");
            buttontps.setImageResource(R.drawable.loutre);
            System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
            System.out.println(textView_PV.getText().toString());

        }

        if (requestCode ==FIGHT){
            textView_Puissance.setText(data.getStringExtra("PuissanceJoueur"));
            textView_PV.setText(data.getStringExtra("PVJoueur"));
            resultat_combat.setText(data.getStringExtra("resultatfight"));
            Toast.makeText(MainActivity.this, resultat_combat.getText().toString(), Toast.LENGTH_SHORT).show();
            if ( resultat_combat.getText().toString().equals("defaite")){
                buttontps.setImageResource(R.drawable.loutre);
            } else if (resultat_combat.getText().toString().equals("victoire")){
                buttontps.setImageResource(R.drawable.loutrepasenforme);
                buttontps.setTag("Vaincu");
                piece();
            }

        }
       checkGameResult();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.redemarrer:
                //TODO New Game
                return true;
            case R.id.quitter:
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void initialiserPuissance(){
        for (int i=0 ; i<16; i++){
            Random r = new Random();
            int n = r.nextInt(150);
            String power = Integer.toString(n);
            puissanceM.add(power);
            
        }
    }
    public void fuite(){
        Toast.makeText(MainActivity.this, "FUITE", Toast.LENGTH_SHORT).show();
        int pv_tps =Integer.parseInt(textView_PV.getText().toString());
        pv_tps-=1;
        textView_PV.setText(Integer.toString(pv_tps));
    }
    public void piece(){

        int piece =Integer.parseInt(textView_piecenonexplorees.getText().toString());
        piece--;
        textView_piecenonexplorees.setText(Integer.toString(piece));
    }
    public void checkGameResult(){

        int pv = Integer.parseInt(textView_PV.getText().toString());
        int nb_piece = Integer.parseInt(textView_piecenonexplorees.getText().toString());
        if(pv <=0){
            Toast.makeText(MainActivity.this, "Partie perdue", Toast.LENGTH_SHORT).show();
            textView_Affichage.setText("Partie perdue");
        }else
        if (nb_piece== 0){
            Toast.makeText(MainActivity.this, "Partie gagnée", Toast.LENGTH_SHORT).show();
            textView_Affichage.setText("Partie gagnée");

        }
    }
}
