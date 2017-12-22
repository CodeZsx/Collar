package com.codez.collar.ui.emoji;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.codez.collar.R;
import com.codez.collar.ui.emojitextview.Emoticons;
import com.codez.collar.utils.DensityUtil;
import com.codez.collar.utils.L;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmojiUtil {
    private static ArrayList<Emoji> emojiList;

    public static ArrayList<Emoji> getEmojiList() {
        if (emojiList == null) {
            emojiList = generateEmojis();
        }
        return emojiList;
    }

    private static ArrayList<Emoji> generateEmojis() {
        ArrayList<Emoji> list = new ArrayList<>();
//        for (int i = 0; i < EmojiResArray.length; i++) {
//            Emoji emoji = new Emoji();
//            emoji.setResourceId(EmojiResArray[i]);
//            emoji.setContent(EmojiTextArray[i]);
//            list.add(emoji);
//        }
        Iterator it = emojiMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();

            Emoji emoji = new Emoji();
            emoji.setResourceId((Integer) entry.getValue());
            emoji.setContent((String) entry.getKey());
            list.add(emoji);
        }
        return list;
    }

    public static Map<String, Integer> emojiMap;

    static {
        emojiMap = new LinkedHashMap<>();
        //表情面板表情
        emojiMap.put("[微笑]", R.drawable.d_hehe);
        emojiMap.put("[可爱]", R.drawable.d_keai);
        emojiMap.put("[太开心]", R.drawable.d_taikaixin);
        emojiMap.put("[鼓掌]", R.drawable.d_guzhang);
        emojiMap.put("[嘻嘻]", R.drawable.d_xixi);
        emojiMap.put("[哈哈]", R.drawable.d_haha);
        emojiMap.put("[笑cry]", R.drawable.d_xiaoku);
        emojiMap.put("[挤眼]", R.drawable.d_jiyan);
        emojiMap.put("[馋嘴]", R.drawable.d_chanzui);
        emojiMap.put("[黑线]", R.drawable.d_heixian);
        emojiMap.put("[汗]", R.drawable.d_han);
        emojiMap.put("[挖鼻]", R.drawable.d_wabishi);
        emojiMap.put("[哼]", R.drawable.d_heng);
        emojiMap.put("[怒]", R.drawable.d_nu);
        emojiMap.put("[委屈", R.drawable.d_weiqu);
        emojiMap.put("[可怜]", R.drawable.d_kelian);
        emojiMap.put("[失望]", R.drawable.d_shiwang);
        emojiMap.put("[悲伤]", R.drawable.d_beishang);
        emojiMap.put("[泪]", R.drawable.d_lei);
        emojiMap.put("[允悲]", R.drawable.d_yunbei);//thumb

        emojiMap.put("[害羞]", R.drawable.d_haixiu);
        emojiMap.put("[污]", R.drawable.d_wu);//thumb
        emojiMap.put("[爱你]", R.drawable.d_aini);
        emojiMap.put("[亲亲]", R.drawable.d_qinqin);
        emojiMap.put("[色]", R.drawable.d_huaxin);
        emojiMap.put("[舔屏]", R.drawable.d_tian);//
        emojiMap.put("[憧憬]", R.drawable.d_chongjing);//thumb
        emojiMap.put("[doge]", R.drawable.d_doge);
        emojiMap.put("[喵喵]", R.drawable.d_miao);
//        emojiMap.put("[二哈]", R.drawable.d_erha);
        emojiMap.put("[坏笑]", R.drawable.d_huaixiao);//thumb
        emojiMap.put("[阴险]", R.drawable.d_yinxian);
        emojiMap.put("[笑而不语]", R.drawable.d_xiaoerbuyu);//thumb
        emojiMap.put("[偷笑]", R.drawable.d_touxiao);
        emojiMap.put("[酷]", R.drawable.d_ku);
//        emojiMap.put("[并不简单]", R.drawable.d_bingbujiandan);
        emojiMap.put("[思考]", R.drawable.d_sikao);
        emojiMap.put("[疑问]", R.drawable.d_yiwen);
        emojiMap.put("[费解]", R.drawable.d_feijie);//thumb
        emojiMap.put("[晕]", R.drawable.d_yun);

        emojiMap.put("[衰]", R.drawable.d_shuai);
//        emojiMap.put("[骷髅]", R.drawable.d_kulou);
        emojiMap.put("[嘘]", R.drawable.d_xu);
        emojiMap.put("[闭嘴]", R.drawable.d_bizui);
        emojiMap.put("[傻眼]", R.drawable.d_shayan);
        emojiMap.put("[吃惊]", R.drawable.d_chijing);
        emojiMap.put("[吐]", R.drawable.d_tu);
        emojiMap.put("[感冒]", R.drawable.d_ganmao);
        emojiMap.put("[生病]", R.drawable.d_shengbing);
        emojiMap.put("[拜拜]", R.drawable.d_baibai);
        emojiMap.put("[鄙视]", R.drawable.d_bishi);
        emojiMap.put("[白眼]", R.drawable.landeln_baiyan);//thumb
        emojiMap.put("[左哼哼]", R.drawable.d_zuohengheng);
        emojiMap.put("[右哼哼]", R.drawable.d_youhengheng);
        emojiMap.put("[抓狂]", R.drawable.d_zhuakuang);
        emojiMap.put("[怒骂]", R.drawable.d_numa);
        emojiMap.put("[打脸]", R.drawable.d_dalian);
        emojiMap.put("[顶]", R.drawable.d_ding);
        emojiMap.put("[互粉]", R.drawable.f_hufen);
        emojiMap.put("[钱]", R.drawable.d_qian);

        emojiMap.put("[哈欠]", R.drawable.d_dahaqi);
        emojiMap.put("[困]", R.drawable.d_kun);
        emojiMap.put("[睡]", R.drawable.d_shuijiao);
//        emojiMap.put("[吃瓜]", R.drawable.chigua);
//        emojiMap.put("[抱抱]", R.drawable.baobao);
//        emojiMap.put("[摊手]", R.drawable.tanshou);
//        emojiMap.put("[跪了]", R.drawable.guile);
        emojiMap.put("[心]", R.drawable.l_xin);
        emojiMap.put("[伤心]", R.drawable.l_shangxin);
        emojiMap.put("[鲜花]", R.drawable.w_xianhua);
        emojiMap.put("[男孩儿]", R.drawable.d_nanhaier);
        emojiMap.put("[女孩儿]", R.drawable.d_nvhaier);
        emojiMap.put("[握手]", R.drawable.h_woshou);
        emojiMap.put("[作辑]", R.drawable.h_zuoyi);
        emojiMap.put("[赞]", R.drawable.h_zan);
        emojiMap.put("[耶]", R.drawable.h_ye);
        emojiMap.put("[good]", R.drawable.h_good);
        emojiMap.put("[弱]", R.drawable.ruo);
//        emojiMap.put("[NO]", R.drawable.no);
        emojiMap.put("[ok]", R.drawable.h_ok);

        emojiMap.put("[haha]", R.drawable.h_haha);
        emojiMap.put("[来]", R.drawable.h_lai);
        emojiMap.put("[拳头]", R.drawable.h_quantou);
//        emojiMap.put("[加油]", R.drawable.jiayou);

        emojiMap.put("[奥特曼]", R.drawable.d_aoteman);
        emojiMap.put("[馋嘴]", R.drawable.d_chanzui);
        emojiMap.put("[肥皂]", R.drawable.d_feizao);

        emojiMap.put("[马到成功]", R.drawable.d_madaochenggong);
        emojiMap.put("[草泥马]", R.drawable.d_shenshou);
        emojiMap.put("[兔子]", R.drawable.d_tuzi);
        emojiMap.put("[熊猫]", R.drawable.d_xiongmao);
        emojiMap.put("[炸鸡啤酒]", R.drawable.d_zhajipijiu);
        emojiMap.put("[猪头]", R.drawable.d_zhutou);
        emojiMap.put("[最右]", R.drawable.d_zuiyou);
        emojiMap.put("[给力]", R.drawable.f_geili);
        emojiMap.put("[互粉]", R.drawable.f_hufen);
        emojiMap.put("[囧]", R.drawable.f_jiong);
        emojiMap.put("[萌]", R.drawable.f_meng);
        emojiMap.put("[神马]", R.drawable.f_shenma);
        emojiMap.put("[威武]", R.drawable.f_v5);
        emojiMap.put("[喜]", R.drawable.f_xi);
        emojiMap.put("[织]", R.drawable.f_zhi);
        emojiMap.put("[蛋糕]", R.drawable.o_dangao);
        emojiMap.put("[飞机]", R.drawable.o_feiji);
        emojiMap.put("[干杯]", R.drawable.o_ganbei);
        emojiMap.put("[话筒]", R.drawable.o_huatong);
        emojiMap.put("[蜡烛]", R.drawable.o_lazhu);
        emojiMap.put("[礼物]", R.drawable.o_liwu);
        emojiMap.put("[绿丝带]", R.drawable.o_lvsidai);
        emojiMap.put("[围脖]", R.drawable.o_weibo);
        emojiMap.put("[围观]", R.drawable.o_weiguan);
        emojiMap.put("[音乐]", R.drawable.o_yinyue);
        emojiMap.put("[照相机]", R.drawable.o_zhaoxiangji);
        emojiMap.put("[钟]", R.drawable.o_zhong);
        emojiMap.put("[鲜花]", R.drawable.w_xianhua);
        emojiMap.put("[浮云]", R.drawable.w_fuyun);
        emojiMap.put("[沙尘暴]", R.drawable.w_shachenbao);
        emojiMap.put("[太阳]", R.drawable.w_taiyang);
        emojiMap.put("[微风]", R.drawable.w_weifeng);
        emojiMap.put("[下雨]", R.drawable.w_xiayu);
        emojiMap.put("[月亮]", R.drawable.w_yueliang);

    }

    static {
        emojiList = generateEmojis();
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void handlerEmojiText(TextView comment, String content, Context context) throws IOException {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        while (m.find()) {
            iterator = emojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getContent())) {
                    //转换为Span并设置Span的大小
                    sb.setSpan(new ImageSpan(context, decodeSampledBitmapFromResource(context.getResources(), emoji.getResourceId()
                                    , dip2px(context, 18), dip2px(context, 18))),
                            m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        comment.setText(sb);
    }


    public static SpannableStringBuilder transEmoji(String source, final Context context) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(source);
        //设置正则
        Pattern pattern = Pattern.compile("\\[(\\S+?)\\]");
        final Matcher matcher = pattern.matcher(spannableStringBuilder);

        if (matcher.find()) {

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
