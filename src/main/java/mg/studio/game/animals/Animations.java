package mg.studio.game.animals;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Discipulus on 3/24/18.
 */

public class Animations extends AppCompatActivity{

    protected  void shake(Context context, View viewToAnimate){
        final Animation animShake = AnimationUtils.loadAnimation(context, R.anim.shake);
        viewToAnimate.startAnimation(animShake);
    }
}
