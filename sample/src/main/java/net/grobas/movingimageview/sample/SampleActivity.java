package net.grobas.movingimageview.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;

import net.grobas.view.MovingImageView;

public class SampleActivity extends ActionBarActivity {

    MovingImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        MovingImageView image = (MovingImageView) findViewById(R.id.image);
        image.getMovingAnimator().setInterpolator(new BounceInterpolator());
        image.getMovingAnimator().setSpeed(100);
        image.getMovingAnimator().addCustomMovement().addDiagonalMoveToDownRight().addHorizontalMoveToLeft().addDiagonalMoveToUpRight()
                .addVerticalMoveToDown().addHorizontalMoveToLeft().addVerticalMoveToUp().start();
    }

    public void action(View v){
        image.getMovingAnimator().setInterpolator(new BounceInterpolator());
        image.getMovingAnimator().setSpeed(100);
        image.getMovingAnimator().addCustomMovement().addDiagonalMoveToDownRight().addHorizontalMoveToLeft().addDiagonalMoveToUpRight()
                .addVerticalMoveToDown().addHorizontalMoveToLeft().addVerticalMoveToUp().start();

    }

}
