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
 * An editable text view, extending {@link AutoCompleteTextView}, that
 * can show completion suggestions for the substring of the text where
 * the user is typing instead of necessarily for the entire thing.
 * <p>
 * You must provide a {@link Tokenizer} to distinguish the
 * various substrings.
 * <p>
 * <p>The following code snippet shows how to create a text view which suggests
 * various countries names while the user is typing:</p>
 * <p>
 * <pre class="prettyprint">
 * public class CountriesActivity extends Activity {
 * protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * setContentView(R.layout.autocomplete_7);
 * <p>
 * ArrayAdapter&lt;String&gt; adapter = new ArrayAdapter&lt;String&gt;(this,
 * android.R.layout.simple_dropdown_item_1line, COUNTRIES);
 * MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.edit);
 * textView.setAdapter(adapter);
 * textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
 * }
 * <p>
 * private static final String[] COUNTRIES = new String[] {
 * "Belgium", "France", "Italy", "Germany", "Spain"
 * };
 * }</pre>
 *
 * @author Igor Morais
 * @author mor41s.1gor@gmail.com
 */
public abstract class AbstractMultiAutoCompleteTextView extends android.support.v7.widget.AppCompatMultiAutoCompleteTextView {

    /**
     * @param context
     */
    public AbstractMultiAutoCompleteTextView(final Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attributeSet
     */
    public AbstractMultiAutoCompleteTextView(final Context context, final AttributeSet attributeSet) {
        super(context, attributeSet);

        setTypeface(getTypefaceAssetPath());
    }

    /**
     * @param context
     * @param attributeSet
     * @param defStyle
     */
    public AbstractMultiAutoCompleteTextView(final Context context, final AttributeSet attributeSet,
                                             final int defStyle) {

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
