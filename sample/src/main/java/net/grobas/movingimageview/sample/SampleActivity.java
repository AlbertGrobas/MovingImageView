package net.grobas.movingimageview.sample;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.grobas.view.MovingImageView;

public class SampleActivity extends AppCompatActivity {

    MovingImageView image;
    boolean toggleState = true;
    boolean toggleCustomMovement = true;
    int[] imageList = {R.drawable.anotherworld, R.drawable.futurecity, R.drawable.spacecargo, R.drawable.city};
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        image = (MovingImageView) findViewById(R.id.image);
        image.getMovingAnimator().addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i("Sample MovingImageView", "Start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i("Sample MovingImageView", "End");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i("Sample MovingImageView", "Cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i("Sample MovingImageView", "Repeat");
            }
        });
    }

    public void clickImage(View v) {
        if (toggleState) {
            image.getMovingAnimator().pause();
            Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
        } else {
            image.getMovingAnimator().resume();
            Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();
        }
        toggleState = !toggleState;
    }

    public void clickTitle(View v) {
        pos = (pos + 1) >= imageList.length ? 0 : pos + 1;
        image.setImageResource(imageList[pos]);
        toggleCustomMovement = true;
        Toast.makeText(this, "Next picture", Toast.LENGTH_SHORT).show();
    }

    public void clickText(View v) {
        if(toggleCustomMovement) {
            image.getMovingAnimator().addCustomMovement().addDiagonalMoveToDownRight().addHorizontalMoveToLeft().addDiagonalMoveToUpRight()
                    .addVerticalMoveToDown().addHorizontalMoveToLeft().addVerticalMoveToUp().start();
            Toast.makeText(this, "Custom movement", Toast.LENGTH_SHORT).show();
        } else {
            image.getMovingAnimator().clearCustomMovement();
            Toast.makeText(this, "Default movement", Toast.LENGTH_SHORT).show();
        }
        toggleCustomMovement = !toggleCustomMovement;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(image != null)
            image.getMovingAnimator().cancel();
    }
}
