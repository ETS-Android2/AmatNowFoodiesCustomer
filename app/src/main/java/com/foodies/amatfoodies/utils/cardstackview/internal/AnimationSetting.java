package com.foodies.amatfoodies.utils.cardstackview.internal;

import android.view.animation.Interpolator;

import com.foodies.amatfoodies.utils.cardstackview.Direction;


public interface AnimationSetting {
    Direction getDirection();
    int getDuration();
    Interpolator getInterpolator();
}
