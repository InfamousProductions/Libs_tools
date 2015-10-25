/*
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v7.widget;

import android.content.Context;
import android.view.View;

class CardViewApi21 implements CardViewImpl {

    @Override
    public void initialize(CardViewDelegate cardView, Context context, int backgroundColor,
            float radius) {
        cardView.setBackgroundDrawable(new RoundRectDrawable(backgroundColor, radius));
        View view = (View) cardView;
        // view.setClipToOutline(true);
        // view.setElevation(context.getResources().getDimension(R.dimen.cardview_elevation));
    }

    @Override
    public void setRadius(CardViewDelegate cardView, float radius) {
        ((RoundRectDrawable) (cardView.getBackground())).setRadius(radius);
    }

    @Override
    public void initStatic() {
    }

    @Override
    public float getRadius(CardViewDelegate cardView) {
        return ((RoundRectDrawable) (cardView.getBackground())).getRadius();
    }

}