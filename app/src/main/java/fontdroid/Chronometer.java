/*
 * Copyright (c) 2014 HollowSoft @IgorMorais
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

import ir.edusa.parents.R;


/**
 * Class that implements a simple timer.
 * <p>
 * You can give it a start time in the {@link android.os.SystemClock#elapsedRealtime} timebase,
 * and it counts up from that, or if you don't give it a base time, it will use the
 * time at which you call {@link #start}.  By default it will display the current
 * timer value in the form "MM:SS" or "H:MM:SS", or you can use {@link #setFormat}
 * to format the timer value into an arbitrary string.
 *
 * @author Igor Morais
 * @author mor41s.1gor@gmail.com
 * @attr ref android.R.styleable#Chronometer_format
 * @attr ref com.hollowsoft.library.utility.R.styleable#Typeface_typefaceAssetPath
 * @attr ref com.hollowsoft.library.utility.R.styleable#Typeface_typefaceFilePath
 * @attr ref com.hollowsoft.library.utility.R.styleable#Typeface_typefaceCache
 */
public class Chronometer extends android.widget.Chronometer {

    /**
     * @param context
     */
    public Chronometer(final Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attributeSet
     */
    public Chronometer(final Context context, final AttributeSet attributeSet) {
        super(context, attributeSet);

        setTypeface(attributeSet, 0);
    }

    /**
     * @param context
     * @param attributeSet
     * @param defStyle
     */
    public Chronometer(final Context context, final AttributeSet attributeSet, final int defStyle) {
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

                    setTypeface(getTypeface(typefaceFilePath, TypefaceCache.PathType.FILE, typefaceCache));
                }

            } else {
                setTypeface(getTypeface(typefaceAssetPath, TypefaceCache.PathType.ASSET, typefaceCache));
            }

            typedArray.recycle();
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
}
