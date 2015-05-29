/*
 * Copyright (C) 2014 Albert Grobas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.grobas.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.grobas.animation.MovingViewAnimator;

/**
 * Custom ImageView for moving images around the screen. Uses <code>MovingObjectAnimator</code>
 * for animation effects.
 */
public class MovingImageView extends ImageView {

    //control vars
    private float canvasWidth, canvasHeight;
    private float imageWidth, imageHeight;
    private float offsetWidth, offsetHeight;
    private int movementType;

    //user vars
    private float maxRelativeSize, minRelativeOffset;
    private int mSpeed;
    private long startDelay;
    private int mRepetitions;
    private boolean loadOnCreate;

    //Our custom animator
    private MovingViewAnimator mAnimator;

    public MovingImageView(Context context) {
        this(context, null);
    }

    public MovingImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.MovingImageView, defStyle, 0);

        try {
            maxRelativeSize = attributes.getFloat(R.styleable.MovingImageView_miv_max_relative_size, 3.0f);
            minRelativeOffset = attributes.getFloat(R.styleable.MovingImageView_miv_min_relative_offset, 0.2f);
            mSpeed = attributes.getInt(R.styleable.MovingImageView_miv_speed, 50);
            mRepetitions = attributes.getInt(R.styleable.MovingImageView_miv_repetitions, -1);
            startDelay = attributes.getInt(R.styleable.MovingImageView_miv_start_delay, 0);
            loadOnCreate = attributes.getBoolean(R.styleable.MovingImageView_miv_load_on_create, true);
        } finally {
            attributes.recycle();
        }

        init();
    }

    private void init() {
        //mandatory
        super.setScaleType(ScaleType.MATRIX);
        mAnimator = new MovingViewAnimator(this);
    }

    /**
     * Updates canvas size, includes padding.
     *
     * @param w new width.
     * @param h new height.
     * @param oldW old width.
     * @param oldH old height.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        //update canvas size
        canvasWidth = (float) w - (float) (getPaddingLeft() + getPaddingRight());
        canvasHeight = (float) h - (float) (getPaddingTop() + getPaddingBottom());
        //after canvas changes need an update
        updateAll();
    }

    private void updateAll() {
        if (getDrawable() != null) {
            updateImageSize();
            updateOffsets();
            updateAnimator();
        }
    }

    private void updateImageSize() {
        imageWidth = getDrawable().getIntrinsicWidth();
        imageHeight = getDrawable().getIntrinsicHeight();
    }

    /**
     * Offset is the difference between image and canvas including the min relative size.
     * Determines the base path animation length.
     */
    private void updateOffsets() {
        float minSizeX = imageWidth * minRelativeOffset;
        float minSizeY = imageHeight * minRelativeOffset;
        offsetWidth = (imageWidth - canvasWidth - minSizeX) > 0 ? imageWidth - canvasWidth : 0;
        offsetHeight = (imageHeight - canvasHeight - minSizeY) > 0 ? imageHeight - canvasHeight : 0;
    }

    /**
     * Gets scale and sets the real length path on Animator.
     */
    private void updateAnimator() {
        if (canvasHeight == 0 && canvasWidth == 0)
            return;

        float scale = calculateTypeAndScale();
        if (scale == 0)
            return;

        float w = (imageWidth * scale) - canvasWidth;
        float h = (imageHeight * scale) - canvasHeight;

        mAnimator.updateValues(movementType, w, h);
        mAnimator.setStartDelay(startDelay);
        mAnimator.setSpeed(mSpeed);
        mAnimator.setRepetition(mRepetitions);

        if (loadOnCreate)
            mAnimator.start();
    }

    /**
     * Sets the best movement type and scale.
     *
     * @return image scale.
     */
    private float calculateTypeAndScale() {
        movementType = MovingViewAnimator.AUTO_MOVE;
        float scale = 1f;
        float scaleByImage = Math.max(imageWidth / canvasWidth, imageHeight / canvasHeight);
        Matrix m = new Matrix();

        //Image is too small to performs any animation, needs a scale
        if (offsetWidth == 0 && offsetHeight == 0) {
            float sW = canvasWidth / imageWidth;
            float sH = canvasHeight / imageHeight;

            if (sW > sH) {
                scale = Math.min(sW, maxRelativeSize);
                m.setTranslate((canvasWidth - imageWidth * scale) / 2f, 0);
                movementType = MovingViewAnimator.VERTICAL_MOVE;

            } else if (sW < sH) {
                scale = Math.min(sH, maxRelativeSize);
                m.setTranslate(0, (canvasHeight - imageHeight * scale) / 2f);
                movementType = MovingViewAnimator.HORIZONTAL_MOVE;

            } else {
                scale = Math.max(sW, maxRelativeSize);
                movementType = (scale == sW) ? MovingViewAnimator.NONE_MOVE :
                        MovingViewAnimator.DIAGONAL_MOVE;
            }

          //Width too small to perform any horizontal animation, scale to width
        } else if (offsetWidth == 0) {
            scale = canvasWidth / imageWidth;
            movementType = MovingViewAnimator.VERTICAL_MOVE;

          //Height too small to perform any vertical animation, scale to height
        } else if (offsetHeight == 0) {
            scale = canvasHeight / imageHeight;
            movementType = MovingViewAnimator.HORIZONTAL_MOVE;

          //Enough size but too big, resize down
        } else if (scaleByImage > maxRelativeSize) {
            scale = maxRelativeSize / scaleByImage;
            if(imageWidth * scale < canvasWidth || imageHeight * scale < canvasHeight) {
                scale = Math.max(canvasWidth / imageWidth, canvasHeight / imageHeight);
            }
        }

        m.preScale(scale, scale);
        setImageMatrix(m);
        return scale;
    }

    /**
     * Don't touch this!
     *
     * @param scaleType deprecated for force setup to ScaleType.Matrix
     */
    @Override
    @Deprecated
    public void setScaleType(ScaleType scaleType) {
        //super.setScaleType(scaleType);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        updateAll();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        updateAll();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        updateAll();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        updateAll();
    }

    /**
     * Returns the animator.
     *
     * @return Moving Animator.
     */
    public MovingViewAnimator getMovingAnimator() {
        return mAnimator;
    }

    public float getMaxRelativeSize() {
        return maxRelativeSize;
    }

    public void setMaxRelativeSize(float max) {
        maxRelativeSize = max;
        updateAnimator();
    }

    public float getMinRelativeOffset() {
        return minRelativeOffset;
    }

    public void setMinRelativeOffset(float min) {
        minRelativeOffset = min;
        updateAnimator();
    }

    public boolean isLoadOnCreate() {
        return loadOnCreate;
    }

    public void setLoadOnCreate(boolean loadOnCreate) {
        this.loadOnCreate = loadOnCreate;
    }

}
