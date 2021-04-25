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

import android.graphics.Typeface;
import android.util.SparseArray;

/**
 * @author Igor Morais
 * @author mor41s.1gor@gmail.com
 */
public final class FontCache {

    /**
     *
     */
    private final SparseArray<Typeface> fontArray = new SparseArray<Typeface>();

    /**
     * Default private constructor.
     */
    private FontCache() {

    }

    /**
     * @return
     */
    public static FontCache getInstance() {
        return LazyLoader.INSTANCE;
    }

    /**
     * @param key
     * @return
     */
    public Typeface get(final int key) {
        return fontArray.get(key);
    }

    /**
     * @param key
     * @param typeface
     */
    public void add(final int key, final Typeface typeface) {
        fontArray.append(key, typeface);
    }

    /**
     * @param key
     */
    public void remove(final int key) {
        fontArray.remove(key);
    }

    /**
     *
     */
    public void removeAll() {
        fontArray.clear();
    }

    /**
     * @author Igor Morais
     * @author Mor41s.1gor@gmail.com
     */
    static final class LazyLoader {
        static final FontCache INSTANCE = new FontCache();
    }
}
