package caw.pd.component;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoScrollTextView extends TextView {
	private Paint mPaint;  
    private float mX;  
    //private static Lyric mLyric;  
    private Paint mPathPaint;  
    public String test = "test";  
    public int index = 0;  
    private List<String> list;  
    public float mTouchHistoryY;  
    private int mY;  
    private long currentDunringTime; // 当前行歌词持续的时间，用该时间来sleep  
    private float middleY;// y轴中间  
    private static final int DY = 50; // 每一行的间隔  
    public AutoScrollTextView(Context context) {  
        super(context);  
        init();  
    }  
    public AutoScrollTextView(Context context,List texts){
    	super(context);  
    	list=texts;
        init(); 
    }
    public AutoScrollTextView(Context context, AttributeSet attr) {  
        super(context, attr);  
        init();  
    }  
    public AutoScrollTextView(Context context, AttributeSet attr, int i) {  
        super(context, attr, i);  
        init();  
    }
    private void init() {  
        setFocusable(true);  
        //PlayListItem pli = new PlayListItem("Because Of You","/sdcard/MP3/Because Of You.mp3", 0L, true);  
        //mLyric = new Lyric(new File("/sdcard/MP3/Because Of You.lrc"), pli);  
        //list = mLyric.list;  
        // 非高亮部分  
        mPaint = new Paint();  
        mPaint.setAntiAlias(true);  
        mPaint.setTextSize(22);  
        mPaint.setColor(Color.WHITE);  
        mPaint.setTypeface(Typeface.SERIF);  
        // 高亮部分 当前歌词  
        mPathPaint = new Paint();  
        mPathPaint.setAntiAlias(true);  
        mPathPaint.setColor(Color.RED);  
        mPathPaint.setTextSize(22);  
        mPathPaint.setTypeface(Typeface.SANS_SERIF);  
    }  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        canvas.drawColor(0xEFeffff);  
        Paint p = mPaint;  
        Paint p2 = mPathPaint;  
        p.setTextAlign(Paint.Align.CENTER);  
        if (index == -1)  
            return;  
        p2.setTextAlign(Paint.Align.CENTER);  
        // 先画当前行，之后再画他的前面和后面，这样就保持当前行在中间的位置  
        canvas.drawText(list.get(index), mX, middleY, p2);  
        float tempY = middleY;  
        // 画出本句之前的句子  
        for (int i = index - 1; i >= 0; i--) {  
            // Sentence sen = list.get(i);  
            // 向上推移  
            tempY = tempY - DY;  
            if (tempY < 0) {  
                break;  
            }  
            canvas.drawText(list.get(i), mX, tempY, p);  
            // canvas.translate(0, DY);  
        }  
        tempY = middleY;  
        // 画出本句之后的句子  
        for (int i = index + 1; i < list.size(); i++) {  
            // 往下推移  
            tempY = tempY + DY;  
            if (tempY > mY) {  
                break;  
            }  
            canvas.drawText(list.get(i), mX, tempY, p);  
            // canvas.translate(0, DY);  
        }  
    }  
    protected void onSizeChanged(int w, int h, int ow, int oh) {  
        super.onSizeChanged(w, h, ow, oh);  
        mX = w * 0.5f; // remember the center of the screen  
        mY = h;  
        middleY = h * 0.5f;  
    }  
    //  
    /** 
     * @param time 
     *            当前歌词的时间轴 
     *  
     * @return currentDunringTime 歌词只需的时间 
     */  
    public long updateIndex(long time) {  
        // 歌词序号  
        //index = mLyric.getNowSentenceIndex(time);  
        if (index == -1)  
            return -1;  
        //Sentence sen = list.get(index);  
        String sen = list.get(index);  
        // 返回歌词持续的时间，在这段时间内sleep  
        //return currentDunringTime = sen.getDuring();  
        return 1000;
    }  
}
