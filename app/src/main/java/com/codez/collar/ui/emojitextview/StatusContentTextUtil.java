package com.codez.collar.ui.emojitextview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codez.collar.activity.TopicActivity;
import com.codez.collar.activity.UserActivity;
import com.codez.collar.activity.WebviewActivity;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.L;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by codez on 17/11/24.
 */
public class StatusContentTextUtil {

    private static final String AT = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";// @人
    private static final String TOPIC = "#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#";// ##话题
    private static final String URL = "http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]";// url
    private static final String EMOJI = "\\[(\\S+?)\\]";//emoji 表情

    private static final String ALL = "(" + AT + ")" + "|" + "(" + TOPIC + ")" + "|" + "(" + URL + ")" + "|" + "(" + EMOJI + ")";

    public static SpannableStringBuilder getWeiBoContent(String source, final Context context, TextView textView) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(source);
        //设置正则
        Pattern pattern = Pattern.compile(ALL);
        final Matcher matcher = pattern.matcher(spannableStringBuilder);

//        L.e(spannableStringBuilder.toString());
        if (matcher.find()) {
            if (!(textView instanceof EditText)) {
                textView.setMovementMethod(ClickableMovementMethod.getInstance());
                textView.setFocusable(false);
                textView.setClickable(false);
                textView.setLongClickable(false);
            }
            matcher.reset();
        }

        int offset = 0;
        while (matcher.find()) {
            final String at = matcher.group(1);
            final String topic = matcher.group(2);
            final String url = matcher.group(3);
            final String emoji = matcher.group(4);

//            L.e("offset:"+offset);
            //处理@用户
            if (at != null) {
                int start = matcher.start(1)-offset;
                int end = start + at.length();
//                L.e("1 start:" + start + " length:" + at.length() + " end:" + end+" size:"+spannableStringBuilder.length()+" at:"+at);
                WeiBoContentClickableSpan myClickableSpan = new WeiBoContentClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
                        context.startActivity(new Intent(context, UserActivity.class)
                        .putExtra(UserActivity.INTENT_KEY_SCREEN_NAME, at.substring(1)));//screen_name即为剔除@的字符，截取第二个字符及以后
                    }
                };
                spannableStringBuilder.setSpan(myClickableSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            //处理##话题
            if (topic != null) {
                int start = matcher.start(2)-offset;
                int end = start + topic.length();
//                L.e("2 start:" + start + " length:" + topic.length() + " end:" + end+" size:"+spannableStringBuilder.length()+" topic:"+topic);
                WeiBoContentClickableSpan clickableSpan = new WeiBoContentClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
                        context.startActivity(new Intent(context, TopicActivity.class)
                                .putExtra(TopicActivity.INTENT_TOPIC, topic));
                    }
                };
                spannableStringBuilder.setSpan(clickableSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

            // 处理url地址
            if (url != null) {
                int start = matcher.start(3)-offset;
                int end = start + url.length();
//                L.e("3 start:" + start + " length:" + url.length() + " end:" + end+" size:"+spannableStringBuilder.length()+" url:"+url);
                WeiBoContentClickableSpan urlClickableSpan = new WeiBoContentClickableSpan(context){
                    @Override
                    public void onClick(View widget) {
//                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        //调用自己的webviewactivity
                        context.startActivity(new Intent(context, WebviewActivity.class)
                                .putExtra(WebviewActivity.INTENT_URL, url));
                    }

                };
                offset += url.length() - 4;
                spannableStringBuilder.replace(start, end > 226 ? 226 : end, "点击链接");
                spannableStringBuilder.setSpan(urlClickableSpan, start, start+4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
            //emoji
            if (emoji != null) {
                int start = matcher.start(4)-offset;
                int end = start + emoji.length();
                String imgName = Emoticons.getImgName(emoji);
                if (!TextUtils.isEmpty(imgName)) {
                    int resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
                    Drawable emojiDrawable = null;
                    try {
                        emojiDrawable = context.getResources().getDrawable(resId);
                    } catch (Resources.NotFoundException e) {
                        L.e("not found:" + resId + " name:" + imgName);
                        e.printStackTrace();
                    }
                    if (emojiDrawable != null) {
                        emojiDrawable.setBounds(0, 0, DensityUtil.sp2px(context, 17), DensityUtil.sp2px(context, 17));
                        ImageSpan imageSpan = new ImageSpan(emojiDrawable, ImageSpan.ALIGN_BOTTOM) {
                            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                                Drawable b = getDrawable();
                                canvas.save();
                                int transY = bottom - b.getBounds().bottom;
                                transY -= paint.getFontMetricsInt().descent / 2;
                                canvas.translate(x, transY);
                                b.draw(canvas);
                                canvas.restore();
                            }
                        };
                        spannableStringBuilder.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }
            }
        }
        return spannableStringBuilder;
    }
    public static SpannableStringBuilder translateEmoji(String source, final Context context, TextView textView) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(source);
        //设置正则
        Pattern pattern = Pattern.compile(EMOJI);
        final Matcher matcher = pattern.matcher(spannableStringBuilder);

        if (matcher.find()) {
            if (!(textView instanceof EditText)) {
                textView.setMovementMethod(ClickableMovementMethod.getInstance());
                textView.setFocusable(false);
                textView.setClickable(false);
                textView.setLongClickable(false);
            }
            matcher.reset();
        }

        int offset = 0;
        while (matcher.find()) {
            final String emoji = matcher.group(1);
            //emoji
            if (emoji != null) {
                int start = matcher.start(1)-offset;
                int end = start + emoji.length();
                String imgName = Emoticons.getImgName(emoji);
                if (!TextUtils.isEmpty(imgName)) {
                    int resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
                    Drawable emojiDrawable = null;
                    try {
                        emojiDrawable = context.getResources().getDrawable(resId);
                    } catch (Resources.NotFoundException e) {
                        L.e("not found:" + resId + " name:" + imgName);
                        e.printStackTrace();
                    }
                    if (emojiDrawable != null) {
                        emojiDrawable.setBounds(0, 0, DensityUtil.sp2px(context, 17), DensityUtil.sp2px(context, 17));
                        ImageSpan imageSpan = new ImageSpan(emojiDrawable, ImageSpan.ALIGN_BOTTOM) {
                            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                                Drawable b = getDrawable();
                                canvas.save();
                                int transY = bottom - b.getBounds().bottom;
                                transY -= paint.getFontMetricsInt().descent / 2;
                                canvas.translate(x, transY);
                                b.draw(canvas);
                                canvas.restore();
                            }
                        };
                        spannableStringBuilder.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }
            }
        }
        return spannableStringBuilder;
    }
}
