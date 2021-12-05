package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by wolfsoft1 on 31/1/18.
 */

public class textview_fira_sans_light extends TextView {
    public textview_fira_sans_light(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public textview_fira_sans_light(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public textview_fira_sans_light(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/FiraSans-Light.ttf");
            setTypeface(tf);
        }
    }
}
