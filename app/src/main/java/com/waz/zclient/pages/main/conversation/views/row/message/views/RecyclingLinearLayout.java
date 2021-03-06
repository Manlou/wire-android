/**
 * Wire
 * Copyright (C) 2016 Wire Swiss GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.waz.zclient.pages.main.conversation.views.row.message.views;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import com.waz.zclient.pages.main.conversation.views.row.message.ConversationItemViewController;
import com.waz.zclient.ui.views.TouchFilterableLayout;

public class RecyclingLinearLayout extends LinearLayout implements TouchFilterableLayout<LinearLayout> {

    private ConversationItemViewController controller;
    private boolean filterAllClickEvents;
    private GestureDetectorCompat gestureDetectorCompat;
    private TouchFilterableLayout.OnClickListener onClickListener;
    private TouchFilterableLayout.OnLongClickListener onLongClickListener;

    public RecyclingLinearLayout(Context context) {
        this(context, null);
    }

    public RecyclingLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclingLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (!filterAllClickEvents) {
                    return false;
                }
                if (onClickListener != null) {
                    onClickListener.onClick();
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (!filterAllClickEvents) {
                    return;
                }
                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                if (onLongClickListener != null) {
                    onLongClickListener.onLongClick();
                }
            }
        });
    }

    public void setViewController(ConversationItemViewController controller) {
        this.controller = controller;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (controller != null) {
            controller.recycle();
        }
    }

    @Override
    public LinearLayout getLayout() {
        return this;
    }

    @Override
    public void setOnClickListener(TouchFilterableLayout.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void setOnLongClickListener(TouchFilterableLayout.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public void setFilterAllClickEvents(boolean filterAllClickEvents) {
        this.filterAllClickEvents = filterAllClickEvents;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return filterAllClickEvents || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return filterAllClickEvents || super.onTouchEvent(event);
    }
}
