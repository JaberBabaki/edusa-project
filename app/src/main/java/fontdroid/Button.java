/*
 * Copyright (c) 2013 HollowSoft @IgorMorais
 *
 * Licensed under the Apache License, Version 2.0 (the _License_);
 * you may not use this file except in compliance with the License.
 *
 * 			You may obtain a copy of the License at
 * 			http://www.apache.org/licenses/LICENSE-2.0
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
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;



import fontdroid.TypefaceCache.PathType;
import ir.edusa.parents.R;


/**
 * Represents a push-button widget. Push-buttons can be pressed, or clicked, by
 * the user to perform an action.
 * <p>
 * <p>
 * A typical use of a push-button in an activity would be the following:
 * </p>
 * <p>
 * <pre>
 * public class MyActivity extends Activity {
 *     protected void onCreate(Bundle icicle) {
 *         super.onCreate(icicle);
 *
 *         setContentView(R.layout.content_layout_id);
 *
 *         final Button button = (Button) findViewById(R.id.button_id);
 *         button.setOnClickListener(new View.OnClickListener() {
 *             public void onClick(View v) {
 *                 // Perform action on click
 *             }
 *         });
 *     }
 * }
 * </pre>
 * <p>
 * <p>
 * However, instead of applying an {@link OnClickListener
 * OnClickListener} to the button in your activity, you can assign a method to
 * your button in the XML layout, using the {@link android.R.attr#onClick
 * android:onClick} attribute. For example:
 * </p>
 * <p>
 * <pre>
 * &lt;Button
 *     android:layout_height="wrap_content"
 *     android:layout_width="wrap_content"
 *     android:text="@string/self_destruct"
 *     android:onClick="selfDestruct" /&gt;
 * </pre>
 * <p>
 * <p>
 * Now, when a user clicks the button, the Android system calls the activity's
 * {@code selfDestruct(View)} method. In order for this to work, the method must
 * be public and accept a {@link android.view.View} as its only parameter. For
 * example:
 * </p>
 * <p>
 * <pre>
 * public void selfDestruct(View view) {
 *     // Kabloey
 * }
 * </pre>
 * <p>
 * <p>
 * The {@link android.view.View} passed into the method is a reference to the
 * widget that was clicked.
 * </p>
 * <p>
 * <h3>Button style</h3>
 * <p>
 * <p>
 * Every Button is styled using the system's default button background, which is
 * often different from one device to another and from one version of the
 * platform to another. If you're not satisfied with the default button style
 * and want to customize it to match the design of your application, then you
 * can replace the button's background image with a <a href="{@docRoot}
 * guide/topics/resources/drawable-resource.html#StateList">state list
 * drawable</a>. A state list drawable is a drawable resource defined in XML
 * that changes its image based on the current state of the button. Once you've
 * defined a state list drawable in XML, you can apply it to your Button with
 * the {@link android.R.attr#background android:background} * attribute. For *
 * more information and an example, see <a * href="{@docRoot} attribute. For *
 * more information and an example, see <a href="{@docRoot} * attribute. For
 * more information and an example, see <a * href="{@docRoot} attribute. For
 * more information and an example, see <a href="{@docRoot}
 * guide/topics/resources/drawable-resource.html#StateList">State List
 * Drawable</a>.
 * </p>
 * <p>
 * <p>
 * See the <a href="{@docRoot}guide/topics/ui/controls/button.html">Buttons</a>
 * guide.
 * </p>
 * <p>
 * <p>
 * <strong>XML attributes</strong>
 * </p>
 * <p>
 * See {@link android.R.styleable#Button Button Attributes},
 * {@link android.R.styleable#TextView TextView Attributes},
 * {@link android.R.styleable#View View Attributes}
 * </p>
 *
 * @author Igor Morais
 * @author mor41s.1gor@gmail.com
 * @attr ref com.hollowsoft.library.utility.R.styleable#Typeface_typefaceAssetPath
 * @attr ref com.hollowsoft.library.utility.R.styleable#Typeface_typefaceFilePath
 * @attr ref com.hollowsoft.library.utility.R.styleable#Typeface_typefaceCache
 */
public class Button extends android.support.v7.widget.AppCompatButton {

    /**
     * @param context
     */
    public Button(final Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attributeSet
     */
    public Button(final Context context, final AttributeSet attributeSet) {
        super(context, attributeSet);

        setTypeface(attributeSet, 0);
    }

    /**
     * @param context
     * @param attributeSet
     * @param defStyle
     */
    public Button(final Context context, final AttributeSet attributeSet, final int defStyle) {
        super(context, attributeSet, defStyle);

        setTypeface(attributeSet, defStyle);
    }

    /**
     * @param attributeSet
     * @param defStyle
     */
    private void setTypeface(final AttributeSet attributeSet, final int defStyle) {

        if (!isInEditMode()) {

            final TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.Typeface,
                    defStyle, R.style.TextAppearance_Hollow);

            final boolean typefaceCache = typedArray.getBoolean(R.styleable.Typeface_typefaceCache, true);

            final String typefaceAssetPath = typedArray.getString(R.styleable.Typeface_typefaceAssetPath);
            if (StringUtility.isNullOrEmpty(typefaceAssetPath)) {

                final String typefaceFilePath = typedArray.getString(R.styleable.Typeface_typefaceFilePath);
                if (!StringUtility.isNullOrEmpty(typefaceFilePath)) {

                    setTypeface(getTypeface(typefaceFilePath, PathType.FILE, typefaceCache));
                }

            } else {
                setTypeface(getTypeface(typefaceAssetPath, PathType.ASSET, typefaceCache));
            }

            typedArray.recycle();
        }
    }

    /**
     * @param typefacePath
     * @param pathType
     * @return
     */
    protected Typeface getTypeface(final String typefacePath, final PathType pathType) {
        return getTypeface(typefacePath, pathType, true);
    }

    /**
     * @param typefacePath
     * @param pathType
     * @param tryCache
     * @return
     */
    protected Typeface getTypeface(final String typefacePath, final PathType pathType, final boolean tryCache) {

        return tryCache ? TypefaceCache.getInstance().get(getContext(), typefacePath, pathType)
                : pathType == PathType.ASSET ? Typeface.createFromAsset(getContext().getAssets(), typefacePath)
                : Typeface.createFromFile(typefacePath);
    }

    /**
     * @param typefacePath
     * @param pathType
     */
    public void setTypeface(final String typefacePath, final PathType pathType) {
        setTypeface(typefacePath, pathType, true);
    }

    /**
     * @param typefacePath
     * @param pathType
     * @param tryCache
     */
    public void setTypeface(final String typefacePath, final PathType pathType, final boolean tryCache) {
        setTypeface(getTypeface(typefacePath, pathType, tryCache));
    }
}
