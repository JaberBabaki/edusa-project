package ir.edusa.parents.Utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;


/**
 * Created by pouya on 5/10/17.
 */

public class InstantAutoComplete extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    public InstantAutoComplete(Context context) {
        super(context);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            if (getFilter() != null) {
                performFiltering(getText(), 0);
            }
        }
    }
}