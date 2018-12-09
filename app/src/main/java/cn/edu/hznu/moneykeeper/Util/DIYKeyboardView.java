package cn.edu.hznu.moneykeeper.Util;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.TypedValue;

import java.util.List;

import cn.edu.hznu.moneykeeper.R;

/**
 * Created by pengchengfu on 2018/3/12.
 */

public class DIYKeyboardView extends KeyboardView {

    private Rect keyIconRect;

    public DIYKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DIYKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Keyboard keyboard = getKeyboard();
        if (keyboard == null) return;
        List<Keyboard.Key> keys = keyboard.getKeys();
        setPreviewEnabled(false);  // 设置点击按键不显示预览气泡

        if (keys != null && keys.size() > 0) {
            Paint paint = new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
            paint.setTypeface(font);
            paint.setAntiAlias(true);

            for (Keyboard.Key key : keys) {
                if (key.codes[0] == -4) {
                    Drawable dr = getContext().getResources().getDrawable(R.color.red);
                    dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    dr.draw(canvas);

                } else if (key.codes[0] == -5){
                    Drawable dr = getContext().getResources().getDrawable(R.color.md_black_down);
                    dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    dr.draw(canvas);
                    // 绘制删除按键图标
                    drawKeyIcon(key, canvas, getResources().getDrawable(R.drawable.delete));

                }
                else {
                    //按键背景全部设暗色
                    Drawable dr = getContext().getResources().getDrawable(R.color.md_black_down);
                    dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    dr.draw(canvas);
                }
                if (key.label != null) {
                    if (key.codes[0] == -4 || key.codes[0] == -5) {
                        //字体大小
                        paint.setTextSize(35 * 2);

                    } else {
                        //字体大小
                        paint.setTextSize(45 * 2);
                    }
                    if (key.codes[0] == -4) {
                        //字体颜色
                        paint.setColor(getContext().getResources().getColor(android.R.color.white));
                    } else {
                        //字体颜色
                        paint.setColor(getContext().getResources().getColor(R.color.white));
                    }
                    Rect rect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height);
                    Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
                    int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                    // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(key.label.toString(), rect.centerX(), baseline, paint);
                }
            }
        }
    }

    private void drawKeyIcon(Keyboard.Key key, Canvas canvas, Drawable iconDrawable) {
        if (iconDrawable == null) {
            return;
        }
        // 计算按键icon 的rect 范围
        if (keyIconRect == null || keyIconRect.isEmpty()) {
            // 得到 keyicon 的显示大小，因为图片放在不同的drawable-dpi目录下，显示大小也不一样
            int intrinsicWidth = iconDrawable.getIntrinsicWidth();
            int intrinsicHeight = iconDrawable.getIntrinsicHeight();
            int drawWidth = intrinsicWidth;
            int drawHeight = intrinsicHeight;
            // 限制图片的大小，防止图片按键范围
            if (drawWidth > key.width) {
                drawWidth = key.width;
                // 此时高就按照比例缩放
                drawHeight = (int) (drawWidth * 1.0f / intrinsicWidth * intrinsicHeight);
            } else if (drawHeight > key.height) {
                drawHeight = key.height;
                drawWidth = (int) (drawHeight * 1.0f / intrinsicHeight * intrinsicWidth);
            }
            // 获取图片的 x,y 坐标,图片在按键的正中间
            int left = key.x + key.width / 2 - drawWidth / 2;
            int top = key.y + key.height / 2 - drawHeight / 2;
            keyIconRect = new Rect(left, top, left + drawWidth, top + drawHeight);
        }

        if (keyIconRect != null && !keyIconRect.isEmpty()) {
            iconDrawable.setBounds(keyIconRect);
            iconDrawable.draw(canvas);
        }
    }

}


