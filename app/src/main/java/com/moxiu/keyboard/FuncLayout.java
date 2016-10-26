package com.moxiu.keyboard;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义面板容器
 */
public class FuncLayout extends LinearLayout {
    protected int mHeight = 0;

    public FuncLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public void addFuncView(View view) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, params);
        view.setVisibility(GONE);
    }

    public void hideAllFuncView() {
        setVisibility(false);
    }

    /**
     * 点击 photo
     *
     * @param isSoftKeyboardPop
     * @param editText
     */
    public void toggleFuncView(boolean isSoftKeyboardPop, EditText editText) {

        //键盘打开
        if (isSoftKeyboardPop) {
            isKeyShowPanel=true;
            if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext())) {
                EmoticonsKeyboardUtils.closeSoftKeyboard(editText);
            } else {
                EmoticonsKeyboardUtils.closeSoftKeyboard(getContext());
            }
            showFuncView();
        } else {
            //面板显示 关闭
            if (isShown()) {
                hideAllFuncView();
            } else {//打开
                showFuncView();
            }
        }
    }

    public void showFuncView() {
        setVisibility(true);
    }


    public void updateHeight(int height) {
        this.mHeight = height;
    }

    public void setVisibility(boolean b) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        if (b) {
            setVisibility(VISIBLE);
            params.height = mHeight;
            if (mListenerList != null) {
                for (OnFuncKeyBoardListener l : mListenerList) {
                    l.OnFuncPop(mHeight);
                }
            }
        } else {
            setVisibility(GONE);
            params.height = 0;
            if (mListenerList != null) {
                for (OnFuncKeyBoardListener l : mListenerList) {
                    l.OnFuncClose();
                }
            }
        }
        setLayoutParams(params);
    }


    private List<OnFuncKeyBoardListener> mListenerList;

    public void addOnKeyBoardListener(OnFuncKeyBoardListener l) {
        if (mListenerList == null) {
            mListenerList = new ArrayList<>();
        }
        mListenerList.add(l);
    }
    boolean isKeyShowPanel=false;
    public boolean isShowPanel() {
        return isKeyShowPanel;
    }

    public void setResetKeyShowPanel() {
        isKeyShowPanel=false;
    }

    public interface OnFuncKeyBoardListener {
        /**
         * 功能布局弹起
         */
        void OnFuncPop(int height);

        /**
         * 功能布局关闭
         */
        void OnFuncClose();
    }


}