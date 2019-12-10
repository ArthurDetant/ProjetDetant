package com.example.projetdetant;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {
    Vector puissanceM = new Vector();
    String valeurPotion;
    String puissanceBase="100";
    String pvBase="10";
    String popupVie;
    String popupPuissance;
    String popupNomJ = "Anonyme";
    int monstremin=1;
    int monstremax=150;
    int potionVie;
    int potionPuissance;
    int potp;
    int potv;
    int niveau=1;
    boolean popup = false;
    private boolean customMatch = false;
    CustomPopup customPopup;
    PopupNom popupNom;

    //ListView des meilleurs scores
    ArrayList<String> listItems = new ArrayList<>();

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
        try {
            getFileList();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        initialiserPuissance(monstremin,monstremax);
        initialiserPotions();
        popupNom = new PopupNom(this);
        popupNom.build();



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

        if(i == potp){
            intent.putExtra("Potion", "power");
            valeurPotion = potionPuissance();
            intent.putExtra("Valeur", valeurPotion);
            potp = -1;
        } else if(i == potv){
            intent.putExtra("Potion", "life");
            valeurPotion= potionVie();
            intent.putExtra("Valeur", valeurPotion);
            potv=-1;
        } else {
            intent.putExtra("Potion", "false");
            intent.putExtra("Valeur", "false");
        }
        intent.putExtra("NomJoueur", popupNomJ);
        intent.putExtra("idMonstre", numRoom);
        intent.putExtra("PuissanceMonstre", puissancemonstre);
        intent.putExtra("PuissanceJoueur", textView_Puissance.getText().toString());
        intent.putExtra("PVJoueur", textView_PV.getText().toString());

        startActivityForResult(intent, FIGHT);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode== Activity.RESULT_CANCELED){
            fuite();
            resultat_combat.setText("fuite");
            buttontps.setImageResource(R.drawable.loutre);
            checkGameResult();
            return;
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
                popup= false;
                restart();
                return true;
            case R.id.quitter:
                finish();
                return true;
            case R.id.paramètre:
                customPopup = new CustomPopup(this);
                customPopup.build();
                return true;
            case R.id.ListeScore:
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void initialiserPuissance(int min, int max){
        for (int i=0 ; i<16; i++){
            int n = genererInt(min,max);
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void checkGameResult(){
        int pv = Integer.parseInt(textView_PV.getText().toString());
        int nb_piece = Integer.parseInt(textView_piecenonexplorees.getText().toString());
        if(pv <=0){
            textView_PV.setText("0");
            Toast.makeText(MainActivity.this, "Partie perdue", Toast.LENGTH_SHORT).show();
            textView_Affichage.setText("Partie perdue");
            if (customMatch == false){
            sauvegarde();
            }
        }else
        if (nb_piece== 0){
            Toast.makeText(MainActivity.this, "Partie gagnée", Toast.LENGTH_SHORT).show();
            textView_Affichage.setText("Partie gagnée");
            niveauSuivant();
        }
    }


    public void initialiserPotions(){
        potionPuissance = genererInt(5,10);
        potionVie = genererInt(1,3);
        potp = genererInt(0,15);
        do {
            potv = genererInt(0, 15);
        }while(potp == potv);
    }
    public String potionPuissance(){
        int puissanceJ_tps =Integer.parseInt(textView_Puissance.getText().toString());
        puissanceJ_tps += potionPuissance;
        textView_Puissance.setText(Integer.toString(puissanceJ_tps));
        String p = Integer.toString(potionPuissance);
        return p;

    }
    public String potionVie(){
        int pv_tps =Integer.parseInt(textView_PV.getText().toString());
        pv_tps += potionVie;
        textView_PV.setText(Integer.toString(pv_tps));
        String p = Integer.toString(potionVie);
        return p;
    }
    public int genererInt(int min, int max){
        Random random = new Random();
        int nb;
        nb = min + random.nextInt(max+1 - min);
        return nb;
    }
    private void restart(){
        if (popup == false){
            puissanceBase="100";
            pvBase="10";
            monstremin=1;
            monstremax=150;
            textView_Puissance.setText(puissanceBase);
            textView_PV.setText(pvBase);
            popupNomJ = "Anonyme";
            customMatch = false;
        }
        popupNom = new PopupNom(this);
        popupNom.build();

        resultat_combat.setText("...");
        textView_piecenonexplorees.setText("16");
        textView_Affichage.setText("Résultat du combat");
        puissanceM.clear();
        initialiserPuissance(monstremin,monstremax);
        initialiserPotions();
        reinitialiserImage();
        reinitialiserTag();
    }
    public void nextLevel(){
        niveau++;
        int pbase =Integer.parseInt(puissanceBase);
        pbase+=50;
        puissanceBase =Integer.toString(pbase);
        monstremin+=75;
        monstremax+=75;
        textView_Puissance.setText(puissanceBase);
        textView_PV.setText(pvBase);
        resultat_combat.setText("...");
        textView_piecenonexplorees.setText("16");
        textView_Affichage.setText("Résultat du combat");
        puissanceM.clear();
        initialiserPuissance(monstremin,monstremax);
        initialiserPotions();
        reinitialiserImage();
        reinitialiserTag();
    }
    public void reinitialiserImage(){
        room01.setImageResource(R.drawable.porte);
        room02.setImageResource(R.drawable.porte);
        room03.setImageResource(R.drawable.porte);
        room04.setImageResource(R.drawable.porte);
        room05.setImageResource(R.drawable.porte);
        room06.setImageResource(R.drawable.porte);
        room07.setImageResource(R.drawable.porte);
        room08.setImageResource(R.drawable.porte);
        room09.setImageResource(R.drawable.porte);
        room10.setImageResource(R.drawable.porte);
        room11.setImageResource(R.drawable.porte);
        room12.setImageResource(R.drawable.porte);
        room13.setImageResource(R.drawable.porte);
        room14.setImageResource(R.drawable.porte);
        room15.setImageResource(R.drawable.porte);
        room16.setImageResource(R.drawable.porte);
    }
    public void reinitialiserTag(){
        room01.setTag("?");
        room02.setTag("?");
        room03.setTag("?");
        room04.setTag("?");
        room05.setTag("?");
        room06.setTag("?");
        room07.setTag("?");
        room08.setTag("?");
        room09.setTag("?");
        room10.setTag("?");
        room11.setTag("?");
        room12.setTag("?");
        room13.setTag("?");
        room14.setTag("?");
        room15.setTag("?");
        room16.setTag("?");
    }

    private void niveauSuivant(){
        AlertDialog.Builder myPopup = new AlertDialog.Builder(this);
        myPopup.setTitle("Felicitations !");
        myPopup.setMessage("Voulez vous passer au niveau suivant ?");
        myPopup.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nextLevel();
            }
        });
        myPopup.setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        myPopup.show();
    }

    public void onPop(View v){
        popupVie = customPopup.getVie();
        popupPuissance = customPopup.getPuissance();
        String puissanceMax = customPopup.getPuissanceMaxMonstre();

        if (popupVie.trim().matches("") || popupPuissance.matches("") ||  puissanceMax.trim().matches("") || Integer.parseInt(puissanceMax)<150){
            Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs ! ou puissance max monstre >= 150", Toast.LENGTH_SHORT).show();
            return;
        }
        textView_PV.setText(popupVie);
        textView_Puissance.setText(popupPuissance);
        monstremax = Integer.parseInt(puissanceMax);
        monstremin = monstremax-149;
        popup = true;
        restart();
        customMatch = true;

        customPopup.dismiss();
    }

    public void validPopName(View v){
        popupNomJ = popupNom.getName();

        if(popupNomJ.trim().equals("")){
            Toast.makeText(MainActivity.this, "Veuillez entrer un nom !", Toast.LENGTH_SHORT).show();
            return;
        }

        popupNom.dismiss();
    }











    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sauvegarde(){
        Toast.makeText(MainActivity.this, "Sauvegarde du score en cours...", Toast.LENGTH_SHORT).show();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
        Date now = new Date();
        String date = formatter.format(now);

        String score = popupNomJ + " - Niveau : " + niveau + " - Puissance : " + textView_Puissance.getText().toString()+ " - " + date  ;

        int insert = 0;
        int index = 0;
        int nb = 0;
        boolean done = false;
        String stage = "";
        String power = "";
        String currentStage = "";
        String currentPower = "";
        String currentScore = "";


        while (index != score.length() -1) {
            if(score.substring(index, index+1).matches(":")){
                int j = index+2;
                nb++;
                while(!score.substring(j,j+1).matches(" ")) {
                    if (!score.substring(j, j + 1).matches(" ")) {
                        if (nb == 1) {
                            stage += score.substring(j, j + 1);

                        } else if (nb == 2) {
                            power += score.substring(j, j + 1);
                        }
                    }
                    j++;
                }

            }
            index++;
        }
        if(listItems.isEmpty()){
            listItems.add(score);
            if(listItems.size() > 10){
                for (int i = 10; i<=listItems.size(); i++){
                    listItems.remove(i);
                }
            }
        } else {
            for (int i = 0; i < listItems.size(); i++) {
                nb=0;
                index = 0;
                currentPower = "";
                currentStage = "";

                currentScore = listItems.get(i); //Récupération élément Listview

                while (index != currentScore.length() - 1) {
                    if((index+1) > currentScore.length()){
                        break;
                    } else if (currentScore.substring(index, index + 1).matches(":")) {
                        int j = index + 2;
                        nb++;
                        while (!currentScore.substring(j, j + 1).matches(" ")) {
                            if (!currentScore.substring(j, j + 1).matches(" ")) {
                                if (nb == 1) { //Premier ':'
                                    currentStage += currentScore.substring(j, j + 1); //Récupération niveau
                                } else if (nb == 2) { //Deuxième ':'
                                    currentPower += currentScore.substring(j, j + 1); //Récupération puissance
                                }
                            }
                            j++;
                        }
                    }
                    index++;
                }

                if (((Integer.parseInt(stage) > Integer.parseInt(currentStage)) || (Integer.parseInt(stage) == Integer.parseInt(currentStage) && Integer.parseInt(power) > Integer.parseInt(currentPower))) && !done ) {

                    insert = i;
                    done = true;
                }
            }
            if(done) {
                listItems.add(insert, score);
                if(listItems.size() > 10){
                    for (int i = 10; i<=listItems.size(); i++){
                        listItems.remove(i);
                    }
                }
            } else {
                listItems.add(score);
                if(listItems.size() > 10){
                    for (int i = 10; i<=listItems.size(); i++){
                        listItems.remove(i);
                    }
                }
            }
        }

        try {
            FileOutputStream outputStream= openFileOutput("sauvegarde", MODE_PRIVATE);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

            for (int i = 0; i < listItems.size(); i++) {
                myOutWriter.append(listItems.get(i)+"\n");
            }

            myOutWriter.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getFileList() throws IOException {

        String value = "";

        FileInputStream inputStream = openFileInput("sauvegarde");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"utf8"), 8192);
        int content;

        while ((content=br.read())!=-1){

            if (content==10){ // code \n
                listItems.add(value);
                value = "";
                continue;
            }
            value += (char) content;
        }
        inputStream.close();
        br.close();


    }

}
