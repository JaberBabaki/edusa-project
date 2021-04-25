/*
 * Copyright (c) 2014 HollowSoft @IgorMorais
 *
 * Licensed under the Apache License, Version 2.0 (the _License_);
 * you may not use this file except in compliance with the License.
 *
 *          You may obtain a copy of the License at
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an _AS IS_ BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package fontdroid;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * <p>
 * A checkbox is a specific type of two-states button that can be either checked
 * or unchecked. A example usage of a checkbox inside your activity would be the
 * following:
 * </p>
 * <p>
 * <pre class="prettyprint">
 * public class MyActivity extends Activity {
 * protected void onCreate(Bundle icicle) {
 * super.onCreate(icicle);
 * <p>
 * setContentView(R.layout.content_layout_id);
 * <p>
 * final CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_id);
 * if (checkBox.isChecked()) {
 * checkBox.setChecked(false);
 * }
 * }
 * }
 * </pre>
 * <p>
 * <p>
 * See the <a href="{@docRoot}
 * guide/topics/ui/controls/checkbox.html">Checkboxes</a> guide.
 * </p>
 * <p>
 * <p>
 * <strong>XML attributes</strong>
 * </p>
 * <p>
 * See {@link android.R.styleable#CompoundButton CompoundButton Attributes},
 * {@link android.R.styleable#Button Button Attributes},
 * {@link android.R.styleable#TextView TextView Attributes},
 * {@link android.R.styleable#View View Attributes}
 * </p>
 *
 * @author Igor Morais
 * @author mor41s.1gor@gmail.com
 */
public abstract class AbstractCheckBox extends android.support.v7.widget.AppCompatButton {

    /**
     * @param context
     */
    public AbstractCheckBox(final Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attributeSet
     */
    public AbstractCheckBox(final Context context, final AttributeSet attributeSet) {
        super(context, attributeSet);

        setTypeface(getTypefaceAssetPath());
    }

    /**
     * @param context
     * @param attributeSet
     * @param defStyle
     */
    public AbstractCheckBox(final Context context, final AttributeSet attributeSet, final int defStyle) {
        super(context, attributeSet, defStyle);

        setTypeface(getTypefaceAssetPath());
    }

    /**
     * @param typefaceAssetPath
     */
    private void setTypeface(final String typefaceAssetPath) {

        if (!isInEditMode() && !StringUtility.isNullOrEmpty(typefaceAssetPath)) {

            setTypeface(getTypeface(typefaceAssetPath, TypefaceCache.PathType.ASSET));
        }
    }

    /**
     * @param typefacePath
     * @param pathType
     * @return
     */
    protected Typeface getTypeface(final String typefacePath, final TypefaceCache.PathType pathType) {
        return getTypeface(typefacePath, pathType, true);
    }

    /**
     * @param typefacePath
     * @param pathType
     * @param tryCache
     * @return
     */
    protected Typeface getTypeface(final String typefacePath, final TypefaceCache.PathType pathType, final boolean tryCache) {

        return tryCache ? TypefaceCache.getInstance().get(getContext(), typefacePath, pathType)
                : pathType == TypefaceCache.PathType.ASSET ? Typeface.createFromAsset(getContext().getAssets(), typefacePath)
                : Typeface.createFromFile(typefacePath);
    }

    /**
     * @param typefacePath
     * @param pathType
     */
    public void setTypeface(final String typefacePath, final TypefaceCache.PathType pathType) {
        setTypeface(typefacePath, pathType, true);
    }

    /**
     * @param typefacePath
     * @param pathType
     * @param tryCache
     */
    public void setTypeface(final String typefacePath, final TypefaceCache.PathType pathType, final boolean tryCache) {
        setTypeface(getTypeface(typefacePath, pathType, tryCache));
    }

    /**
     * Get {@code Typeface} path from the specified asset folder.
     *
     * @return The {@code Typeface} path from the specified asset folder.
     */
    protected abstract String getTypefaceAssetPath();
}
