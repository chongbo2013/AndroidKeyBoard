package com.moxiu.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 布局容器
 */
public class EmoticonsKeyBoard extends AutoHeightLayout implements View.OnClickListener, EmoticonsEditText.OnBackKeyClickListener {

    protected LayoutInflater mInflater;
    //照片按钮
    protected ImageView btn_photo;
    //输入框
    protected EmoticonsEditText mEtChat;
    //发送
    protected TextView mBtnSend;
    //面板容器
    protected FuncLayout mLyKvml;
    protected boolean mDispatchKeyEventPreImeLock = false;
    public EmoticonsKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflateKeyboardBar();
        initView();
        initFuncView();
    }
    /**
     * 将底部输入栏加载进来
     */
    protected void inflateKeyboardBar() {
        mInflater.inflate(R.layout.view_keyboard, this);
    }
    /**
     * 加载菜单栏进来
     *
     * @return
     */
    protected View inflateFunc() {
        return mInflater.inflate(R.layout.view_func_emoticon, null);
    }
    protected void initView() {
        btn_photo = ((ImageView) findViewById(R.id.btn_photo));
        mEtChat = ((EmoticonsEditText) findViewById(R.id.et_chat));
        mBtnSend = ((TextView) findViewById(R.id.btn_send));
        mLyKvml = ((FuncLayout) findViewById(R.id.ly_kvml));
        mEtChat.setOnBackKeyClickListener(this);
        btn_photo.setOnClickListener(this);
        btn_photo.setOnClickListener(this);
    }
    protected void initFuncView() {
        initEmoticonFuncView();
        initEditView();
    }
    protected void initEmoticonFuncView() {
        View keyboardView = inflateFunc();
        mLyKvml.addFuncView(keyboardView);
    }
    protected void initEditView() {
        mEtChat.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mEtChat.isFocused()) {
                    mEtChat.setFocusable(true);
                    mEtChat.setFocusableInTouchMode(true);
                }
                return false;
            }
        });
        mEtChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    mBtnSend.setVisibility(VISIBLE);
                } else {
                    mBtnSend.setVisibility(GONE);
                }
            }
        });
    }

    public void reset() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(this);
        mLyKvml.hideAllFuncView();
    }

    protected void setFuncViewHeight(int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLyKvml.getLayoutParams();
        params.height = height;
        mLyKvml.setLayoutParams(params);
    }
    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        mLyKvml.updateHeight(height);
    }
    @Override
    public void OnSoftPop(int height) {
        super.OnSoftPop(height);
        mLyKvml.setVisibility(true);
    }
    @Override
    public void OnSoftClose() {
        super.OnSoftClose();
//        setFuncViewHeight(EmoticonsKeyboardUtils.dip2px(getContext(), APPS_HEIGHT));
        if(mLyKvml.isShowPanel()) {
            mLyKvml.setResetKeyShowPanel();
            return;
        }
            mLyKvml.hideAllFuncView();

    }
    public void addOnFuncKeyBoardListener(FuncLayout.OnFuncKeyBoardListener l) {
        mLyKvml.addOnKeyBoardListener(l);
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_photo) {
            //点击照片
//            setFuncViewHeight(EmoticonsKeyboardUtils.dip2px(getContext(), APPS_HEIGHT));
            mLyKvml.toggleFuncView(isSoftKeyboardPop(), mEtChat);
        } else if (i == R.id.btn_send) {
            //发送消息
        }
    }
    @Override
    public void onBackKeyClick() {
        if (mLyKvml.isShown()) {
            mDispatchKeyEventPreImeLock = true;
            reset();
        }
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (mDispatchKeyEventPreImeLock) {
                    mDispatchKeyEventPreImeLock = false;
                    return true;
                }
                if (mLyKvml.isShown()) {
                    reset();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext())) {
            return false;
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext())) {
            return;
        }
        super.requestChildFocus(child, focused);
    }

    public boolean dispatchKeyEventInFullScreen(KeyEvent event) {
        if (event == null) {
            return false;
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext()) && mLyKvml.isShown()) {
                    reset();
                    return true;
                }
            default:
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    boolean isFocused;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        isFocused = mEtChat.getShowSoftInputOnFocus();
                    } else {
                        isFocused = mEtChat.isFocused();
                    }
                    if (isFocused) {
                        mEtChat.onKeyDown(event.getKeyCode(), event);
                    }
                }
                return false;
        }
    }

    public EmoticonsEditText getEtChat() {
        return mEtChat;
    }

    public ImageView getIvPhoto() {
        return btn_photo;
    }

    public TextView getTvSend() {
        return mBtnSend;
    }


    public void onPause(){
        reset();
    }
}
