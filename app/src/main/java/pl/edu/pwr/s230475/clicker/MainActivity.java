package pl.edu.pwr.s230475.clicker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    Gracz gracz=new Gracz();
    Boss boss=new Boss();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//ukrycie belki
        setContentView(R.layout.activity_main);
        ustawGre();
        nowyBoss();
        aktualizujStats();
        autokliki();
    }

    private void autokliki() {
        Thread t=new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try{
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boss.setHP(boss.getHP()-gracz.getAutoatak());
                                if(boss.getHP()<=0){
                                    gracz.setMoney(gracz.getMoney()+boss.getNagroda());
                                    nowyBoss();
                                }
                                aktualizujStats();
                            }
                        });
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }}
        };
        t.start();
    }


    public void buttonOnClick(View v) {
         uderz();
    }

    private void uderz() {
        boss.setHP(boss.getHP()-gracz.getAtak());
        if(boss.getHP()<=0){
            gracz.setMoney(gracz.getMoney()+boss.getNagroda());
            nowyBoss();
        }
        aktualizujStats();
    }

    private void nowyBoss() {
        boss.setLevel( boss.getLevel() +1);
        boss.setHP((boss.getLevel()*15)+(boss.getLevel()*15)+1);
        boss.setNagroda(((boss.getLevel()*boss.getHP())/15)+10);
        aktualizujStats();
        ImageButton imageButton=(ImageButton)findViewById(R.id.imageButton);
        int[] imageIds = {
                R.drawable.z1,
                R.drawable.z2,
                R.drawable.z3,
                R.drawable.z4,
                R.drawable.z5,
                R.drawable.z6
            };
        Random generator = new Random();
        int randomImageId = imageIds[generator.nextInt(imageIds.length)];
        imageButton.setImageResource(randomImageId);
    }

    private void aktualizujStats() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax((boss.getLevel()*15)+(boss.getLevel()*15)+1);
        progressStatus =(boss.getHP());
        progressBar.setProgress(progressStatus);

        TextView textViewAtak = (TextView) findViewById(R.id.textViewAtak);
        textViewAtak.setText("Atak: "+gracz.getAtak());
        TextView textViewAutoAtak = (TextView) findViewById(R.id.textViewAutoAtak);
        textViewAutoAtak.setText("AutoAtak: "+gracz.getAutoatak());

        TextView textViewBossHP = (TextView) findViewById(R.id.textViewBossHP);
        textViewBossHP.setText("HP: "+boss.getHP()+"/"+((boss.getLevel()*15)+(boss.getLevel()*15)+1));
        TextView textViewBossLevel = (TextView) findViewById(R.id.textViewBossLevel);
        textViewBossLevel.setText("Level: "+boss.getLevel()+"(nagroda: "+boss.getNagroda()+")");

        Button btnKA1 =(Button) findViewById(R.id.buttonKupAtak) ;
        btnKA1.setText("+1("+ gracz.getAtak()*10+")");
        Button btnKA2 =(Button) findViewById(R.id.buttonKupAtak2) ;
        int sum= (gracz.getAtak()*10)+((gracz.getAtak()+1)*10)+((gracz.getAtak()+2)*10)+((gracz.getAtak()+3)*10)+((gracz.getAtak()+4)*10)+((gracz.getAtak()+5)*10)+((gracz.getAtak()+6)*10)+((gracz.getAtak()+7)*10)+((gracz.getAtak()+8)*10)+((gracz.getAtak()+9)*10);

        btnKA2.setText("+10("+ sum+")");
        Button btnKAA1 =(Button) findViewById(R.id.buttonKupAutoAtak) ;
        btnKAA1.setText("+1(" + gracz.getAutoatak()*10+")"  );
        int sum2= (gracz.getAutoatak()*10)+((gracz.getAutoatak()+1)*10)+((gracz.getAutoatak()+2)*10)+((gracz.getAutoatak()+3)*10)+((gracz.getAutoatak()+4)*10)+((gracz.getAutoatak()+5)*10)+((gracz.getAutoatak()+6)*10)+((gracz.getAutoatak()+7)*10)+((gracz.getAutoatak()+8)*10)+((gracz.getAutoatak()+9)*10);

        Button btnKAA2 =(Button) findViewById(R.id.buttonKupAutoAtak2) ;
        btnKAA2.setText("+10(" + sum2+")"  );
        TextView textViewMoney = (TextView) findViewById(R.id.textViewMoney);

        textViewMoney.setText("Monety: "+gracz.getMoney());
    }

    public void buttonKupAtak(View v) {
       if(gracz.getMoney()>=(gracz.getAtak()*10))
        {
            gracz.setMoney(gracz.getMoney() - (gracz.getAtak() * 10));
            gracz.setAtak(gracz.getAtak() + 1);
            aktualizujStats();
        }
       else{
           Toast.makeText(this, "Potrzeba jeszcze: " + ( (gracz.getAtak() * 10) - gracz.getMoney())+" z "+(gracz.getAtak() * 10) , Toast.LENGTH_SHORT).show();
       }
    }

    public void buttonKupAutoAtak(View v) {
        if(gracz.getMoney()>=(gracz.getAutoatak()*10))
        {
            gracz.setMoney(gracz.getMoney() - (gracz.getAutoatak() * 10));
            gracz.setAutoatak(gracz.getAutoatak() + 1);
            aktualizujStats();
        }
        else{
            Toast.makeText(this, "Potrzeba jeszcze: " + ( (gracz.getAutoatak() * 10) - gracz.getMoney())+" z "+(gracz.getAutoatak() * 10) , Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonKupAtak2(View v) {
        int sum= (gracz.getAtak()*10)+((gracz.getAtak()+1)*10)+((gracz.getAtak()+2)*10)+((gracz.getAtak()+3)*10)+((gracz.getAtak()+4)*10)+((gracz.getAtak()+5)*10)+((gracz.getAtak()+6)*10)+((gracz.getAtak()+7)*10)+((gracz.getAtak()+8)*10)+((gracz.getAtak()+9)*10);
        if(gracz.getMoney()>=sum)
        {
            gracz.setMoney(gracz.getMoney() - sum);
            gracz.setAtak(gracz.getAtak() + 10);
            aktualizujStats();
        }
        else{
            Toast.makeText(this, "Potrzeba jeszcze: " + (sum - gracz.getMoney())+" z "+sum , Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonKupAutoAtak2(View v) {
        int sum= (gracz.getAutoatak()*10)+((gracz.getAutoatak()+1)*10)+((gracz.getAutoatak()+2)*10)+((gracz.getAutoatak()+3)*10)+((gracz.getAutoatak()+4)*10)+((gracz.getAutoatak()+5)*10)+((gracz.getAutoatak()+6)*10)+((gracz.getAutoatak()+7)*10)+((gracz.getAutoatak()+8)*10)+((gracz.getAutoatak()+9)*10);

        if(gracz.getMoney()>=sum)
        {
            gracz.setMoney(gracz.getMoney() - (gracz.getAutoatak() * 100));
            gracz.setAutoatak(gracz.getAutoatak() + 10);
            aktualizujStats();
        }
        else{
            Toast.makeText(this, "Potrzeba jeszcze: " + (sum - gracz.getMoney())+" z "+sum , Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonZapisz(View v) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("gmoney", gracz.getMoney());
        editor.putInt("gatak", gracz.getAtak());
        editor.putInt("gaatak", gracz.getAutoatak());

        editor.putInt("blevel", boss.getLevel());
        editor.putInt("bhp", boss.getHP());
        editor.putInt("bnagroda", boss.getNagroda());

        editor.commit();
        Toast.makeText(this, "Zapisano!", Toast.LENGTH_SHORT).show();
    }
    public void buttonWczytaj(View v){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
       gracz.setMoney(sharedPref.getInt("gmoney",1));
       gracz.setAtak(sharedPref.getInt("gatak",1));
       gracz.setAutoatak(sharedPref.getInt("gaatak",1));
       boss.setLevel(sharedPref.getInt("blevel",1));
        boss.setHP(sharedPref.getInt("bhp",10));
        boss.setNagroda(sharedPref.getInt("bnagroda",1));

        aktualizujStats();
        Toast.makeText(this, "Wczytano!", Toast.LENGTH_SHORT).show();
    }
    public void buttonReset(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Czy na pewno chcesz zresetować postępy?");
        builder.setMessage("Wszystko zostanie utracone ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Zresetowano postępy", Toast.LENGTH_SHORT).show();
                ustawGre();
            }
        });

        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Powrót do gry", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
        buttonZapisz(v);
        aktualizujStats();

    }
    @Override
    protected void onPause() {
        super.onPause();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("gmoney", gracz.getMoney());
        editor.putInt("gatak", gracz.getAtak());
        editor.putInt("gaatak", gracz.getAutoatak());

        editor.putInt("blevel", boss.getLevel());
        editor.putInt("bhp", boss.getHP());
        editor.putInt("bnagroda", boss.getNagroda());

        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        gracz.setMoney(sharedPref.getInt("gmoney",1));
        gracz.setAtak(sharedPref.getInt("gatak",1));
        gracz.setAutoatak(sharedPref.getInt("gaatak",1));
        boss.setLevel(sharedPref.getInt("blevel",1));
        boss.setHP(sharedPref.getInt("bhp",10));
        boss.setNagroda(sharedPref.getInt("bnagroda",1));

        aktualizujStats();
    }

void ustawGre(){
        gracz.setAtak(1);
        gracz.setAutoatak(1);
        gracz.setMoney(0);
        boss.setLevel(0);
        boss.setHP(0);
        boss.setNagroda(10);
    }

}
