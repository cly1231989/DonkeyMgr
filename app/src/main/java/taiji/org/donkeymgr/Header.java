package taiji.org.donkeymgr;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by cundong on 2015/10/9.
 * <p/>
 * RecyclerView的HeaderView，简单的展示一个TextView
 */
public class Header extends RelativeLayout {

    public Header(Context context) {
        super(context);
        init(context);
    }

    public Header(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Header(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.header, this);
    }
}