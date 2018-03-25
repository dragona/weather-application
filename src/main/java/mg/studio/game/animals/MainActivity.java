package mg.studio.game.animals;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Animate the text Animals
        new Animations().shake(this, findViewById(R.id.tv_animals));

    }


    public void btnStart(View view) {
        //to do when the button is clicked
        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        intent.putExtra("score",0);
        intent.putExtra("chances",9);
        startActivity(intent);
    }

    public void btnHowTo(View view) {
        //to do when the button is clicked
        hoToPlay();
    }

    private void hoToPlay() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppThemeNoActionBar);


        // 3. Chain together various setter methods to set the dialog characteristics
        builder.setView(R.layout.how_to_play);


        // 5. Get the AlertDialog from create() the show
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
