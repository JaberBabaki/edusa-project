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

/**
 * @author Igor Morais
 * @author mor41s.1gor@gmail.com
 */
public class TypefaceException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -8566803889164965809L;

    /**
     *
     */
    public TypefaceException() {
        super();
    }

    /**
     * @param detailMessage
     */
    public TypefaceException(final String detailMessage) {
        super(detailMessage);
    }

    /**
     * @param exception
     */
    public TypefaceException(final RuntimeException exception) {
        super(exception);
    }

    /**
     * @param detailMessage
     * @param exception
     */
    public TypefaceException(final String detailMessage, final RuntimeException exception) {
        super(detailMessage, exception);
    }
}
