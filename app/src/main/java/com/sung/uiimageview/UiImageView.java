package com.sung.uiimageview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * 自带随机动画的imageview(用于tabhome页)
 */
public class UiImageView extends RelativeLayout implements Animation.AnimationListener {
    public static final String TAG = UiImageView.class.getSimpleName();
    private Context mContext;
    /**
     * 动画目标（img1为主view img2辅助动画构成）
     */
    private ImageView img1, img2;
    /**
     * 动画（下往上/上往下/右往左/左往右/渐变）
     */
    private Animation img1_bottom2top, img1_top2bottom, img1_right2left, img1_left2right;
    private Animation img2_bottom2top, img2_top2bottom, img2_right2left, img2_left2right;
    private Animation alpha_show, alpha_hide;
    /**
     * 当前展示的图片position
     */
    private int current_item = -1;
    /**
     * 动画长度（淡入淡出因为两个动画组合 实际展出是双倍时间）
     */
    private final int Animation_Duration = 1200;
    /**
     * 动画延迟（startAnim()方法内有随机生成 未注释的话使用该默认值）
     */
    private int Animation_Delay = 2000;
    /**
     * 两个imageview各自的动画
     */
    private List<Animation> res_anim_img1 = new ArrayList<>();
    private List<Animation> res_anim_img2 = new ArrayList<>();
    /**
     * 展示图片资源
     */
    private List<Integer> res_pic = new ArrayList();

    private Handler animStart = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (img1 == null || img2 == null) return;
            //随机展示动画约束
            int position = (int) (Math.random() * (res_anim_img1.size()));
            img1.startAnimation(res_anim_img1.get(position));
            img2.startAnimation(res_anim_img2.get(position));
        }
    };

    public UiImageView(Context context) {
        super(context);
        this.mContext = context;
    }

    public UiImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public UiImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    /**
     * ！需要判定代码是否已经执行 否则多次调用既浪费资源又容易出错
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //test
        if (res_pic.size() == 0) {
            res_pic.add(android.R.color.transparent);
            res_pic.add(android.R.color.transparent);
        }
        if (img1 == null || img2 == null) {
            img1 = new ImageView(mContext);
            img2 = new ImageView(mContext);
            img1.setScaleType(ImageView.ScaleType.FIT_END);
            img2.setScaleType(ImageView.ScaleType.FIT_END);
            int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
            int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
            Log.e(TAG, "onMeasure: " + measureWidth + "/" + measureHeight);
            LayoutParams params = new LayoutParams(measureWidth, measureHeight);
            //先添加img2再img1 保证img1在上层
            this.addView(img2, params);
            this.addView(img1, params);
        }
        //初次
        if (current_item == -1) {
            initAnim();
            init();
        }
    }

    private void init() {
        if (res_pic == null || res_pic.size() == 0 || img1 == null || img2 == null) return;
        if (res_pic.size() == 1) {
            img1.setImageResource(res_pic.get(0));
            return;
        }
        img1.clearAnimation();
        img2.clearAnimation();
        img1.setImageResource(res_pic.get(0));
        img2.setImageResource(res_pic.get(1));

        current_item = 0;
        //拟开始第一个动画
        startAnim();
    }

    private void initAnim() {
        if (res_anim_img1.size() != 0 && res_anim_img2.size() != 0) return;
        res_anim_img1.clear();
        res_anim_img2.clear();

        //img1动画
        img1_bottom2top = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f);
        img1_top2bottom = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
        img1_right2left = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        img1_left2right = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        //img2动画
        img2_bottom2top = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        img2_top2bottom = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f);
        img2_right2left = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        img2_left2right = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        //淡入淡如另外设置（只作用img1）
        //show和hide只需要加入一个进list 这是一组 并且在动画结束有特殊处理
        alpha_show = new AlphaAnimation(0f, 1f);
        alpha_hide = new AlphaAnimation(1f, 0f);
        alpha_show.setInterpolator(new AccelerateInterpolator());
        alpha_show.setDuration(Animation_Duration);
        alpha_show.setAnimationListener(this);

        res_anim_img1.add(img1_bottom2top);
        res_anim_img1.add(img1_top2bottom);
        res_anim_img1.add(img1_right2left);
        res_anim_img1.add(img1_left2right);
        res_anim_img1.add(alpha_hide);
        res_anim_img2.add(img2_bottom2top);
        res_anim_img2.add(img2_top2bottom);
        res_anim_img2.add(img2_right2left);
        res_anim_img2.add(img2_left2right);
        res_anim_img2.add(alpha_hide);

        for (Animation animation : res_anim_img1) {
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(Animation_Duration);
            animation.setAnimationListener(this);
        }
        for (Animation animation : res_anim_img2) {
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(Animation_Duration);
        }
    }

    /**
     * 单次延迟启动动画
     */
    private void startAnim() {
        //营造当多个view同时在界面上时不同步切换效果
        Animation_Delay = (int) (2000 + (Math.random() * 25) * 100);
        animStart.sendEmptyMessageDelayed(0, Animation_Delay);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        //淡入淡出开始时把img2不可见 否则img1透明时能看到img2
        img2.setVisibility((animation == alpha_show || animation == alpha_hide) ? INVISIBLE : VISIBLE);
    }

    /**
     * 更正view动画
     * 更正view图片
     * 更正当前展示目标
     */
    @Override
    public void onAnimationEnd(Animation animation) {
        //防闪烁清下动画
        img1.clearAnimation();
        img2.clearAnimation();

        //当是淡入淡出动画时特殊处理
        if (animation == alpha_hide) {
            img1.startAnimation(alpha_show);
            img1.setImageResource(res_pic.get((current_item + 1) == res_pic.size() ? 0 : (current_item + 1)));
        }
        //其他动画
        else {
            current_item++;
            if (current_item == res_pic.size()) {
                current_item = 0;
            }
            img1.setImageResource(res_pic.get(current_item));
            img2.setImageResource(res_pic.get((current_item + 1) == res_pic.size() ? 0 : (current_item + 1)));
            startAnim();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    /**
     * 补充数据
     */
    public void addPicData(List pics) {
        if (pics == null || pics.size() == 0) return;
        res_pic.clear();
        res_pic = pics;
        initAnim();
        init();
    }
}
