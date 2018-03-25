package mg.studio.game.animals;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CongratulationActivity extends AppCompatActivity {

    int Score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation);
        Intent intent = getIntent();
        Score = intent.getIntExtra("score",0);
        TextView tex = findViewById(R.id.tv_score);
        tex.setText(Integer.toString(Score));

    }

    public void btnPlayagain(View view) {
        Intent intent = new Intent(CongratulationActivity.this, PlayActivity.class);
        intent.putExtra("score",0);
        intent.putExtra("chances",9);
        startActivity(intent);
        CongratulationActivity.this.finish();
    }
}
