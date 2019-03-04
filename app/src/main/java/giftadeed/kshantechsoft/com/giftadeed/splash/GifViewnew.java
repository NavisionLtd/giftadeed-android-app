package giftadeed.kshantechsoft.com.giftadeed.splash;

/**
 * Created by ADMIN on 11-11-2016.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

import giftadeed.kshantechsoft.com.giftadeed.R;

public class GifViewnew extends View {
    private InputStream gifInputStream;
    private Movie gifMovie;
    private int movieWidth, movieHeight;
    private long movieDuration;
    private long movieStart;

    public GifViewnew(Context context) {
        super(context);
        init(context);
    }

    public GifViewnew(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GifViewnew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressWarnings("ResourceType")
    private void init(Context context) {
        setFocusable(true);
        gifInputStream = context.getResources().openRawResource(R.drawable.progress);
        gifMovie = Movie.decodeStream(gifInputStream);
        movieWidth = gifMovie.width();
        movieHeight = gifMovie.height();
        movieDuration = gifMovie.duration();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(movieWidth, movieHeight);
    }

    public int getMovieWidth() {
        return movieWidth;
    }

    public int getMovieHeight() {
        return movieHeight;
    }

    public long getMovieDuration() {
        return movieDuration;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long start = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0) {
            movieStart = start;
        }

        if (gifMovie != null) {

            int dur = gifMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }

            int relTime = (int) ((start - movieStart) % dur);
            gifMovie.setTime(relTime);
            gifMovie.draw(canvas, 0, 0);
            invalidate();
        }
    }
}