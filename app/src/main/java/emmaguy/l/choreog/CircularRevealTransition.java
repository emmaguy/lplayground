package emmaguy.l.choreog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

public class CircularRevealTransition extends Transition {
    private static final String PROPNAME_WIDTH = "lplayground:circularReveal:width";
    private static final String PROPNAME_HEIGHT = "lplayground:circularReveal:height";
    private static final String PROPNAME_CENTER_X = "lplayground:circularReveal:centerX";
    private static final String PROPNAME_CENTER_Y = "lplayground:circularReveal:centerY";
    private static final String PROPNAME_WINDOW_X = "lplayground:circularReveal:windowX";
    private static final String PROPNAME_WINDOW_Y = "lplayground:circularReveal:windowY";
    private static final String[] sTransitionProperties = {
            PROPNAME_WIDTH,
            PROPNAME_HEIGHT,
            PROPNAME_CENTER_X,
            PROPNAME_CENTER_Y,
            PROPNAME_WINDOW_X,
            PROPNAME_WINDOW_Y
    };
    private int[] startLocation = new int[2];
    private int[] tempLocation = new int[2];

    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(TransitionValues values) {
        View view = values.view;

        values.values.put(PROPNAME_WIDTH, view.getWidth());
        values.values.put(PROPNAME_HEIGHT, view.getHeight());

        values.values.put(PROPNAME_CENTER_X, (view.getLeft() + view.getRight()) / 2);
        values.values.put(PROPNAME_CENTER_Y, (view.getTop() + view.getBottom()) / 2);

        view.getLocationInWindow(tempLocation);
        values.values.put(PROPNAME_WINDOW_X, tempLocation[0]);
        values.values.put(PROPNAME_WINDOW_Y, tempLocation[1]);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(final ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        int cx = (Integer) startValues.values.get(PROPNAME_CENTER_X);
        int cy = (Integer) startValues.values.get(PROPNAME_CENTER_Y);

        int startX = (Integer) startValues.values.get(PROPNAME_WINDOW_X);
        int startY = (Integer) startValues.values.get(PROPNAME_WINDOW_Y);

        int endX = (Integer) endValues.values.get(PROPNAME_WINDOW_X);
        int endY = (Integer) endValues.values.get(PROPNAME_WINDOW_Y);

        int startWidth = (Integer) startValues.values.get(PROPNAME_WIDTH);
        int startHeight = (Integer) startValues.values.get(PROPNAME_HEIGHT);

        int endWidth = (Integer) endValues.values.get(PROPNAME_WIDTH);
        int endHeight = (Integer) endValues.values.get(PROPNAME_HEIGHT);

        endValues.view.getLocationInWindow(tempLocation);
        startValues.view.getLocationInWindow(startLocation);
        Log.d("emmalol", "\n\ncreateAnimator \nstartValues: " + startValues.view +
                " \nendValues: " + endValues.view +
                " \ntempLocation[0]: " + tempLocation[0] + " tempLocation[1]: " + tempLocation[1] +
                " \ncx: " + cx + " cy: " + cy +
                " \nstartX: " + startX + " startY: " + startY +
                " \nendX: " + endX + " endY: " + endY +
                " \nstartY-loc[1]: " + (startY - startLocation[1]) +
                " \nendY-loc[1]: " + (endY - startLocation[1]));

        Animator revealAnimator = ViewAnimationUtils.createCircularReveal(startValues.view, cx, cy, 0, endWidth);

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", startX - startLocation[0], endX - tempLocation[0]);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", startY - startLocation[1], endY - tempLocation[1]);
        Animator translationAnimator = ObjectAnimator.ofPropertyValuesHolder(startValues.view, pvhX, pvhY);

        AnimatorSet anim = new AnimatorSet();
        anim.setInterpolator(getInterpolator());
        anim.playTogether(translationAnimator,
                createWidthAnimator(startValues.view, startWidth, endWidth),
                createHeightAnimator(startValues.view, startHeight, endHeight),
                revealAnimator);

        return anim;
    }

    private ValueAnimator createWidthAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.width = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private ValueAnimator createHeightAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
