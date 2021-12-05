package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class edittext_fira_sans_bold extends EditText {

    public edittext_fira_sans_bold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public edittext_fira_sans_bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public edittext_fira_sans_bold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/FiraSans-Bold.ttf");
            setTypeface(tf);
        }
    }

}