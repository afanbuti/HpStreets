/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.limon.common;

import java.lang.ref.SoftReference;
import java.util.WeakHashMap;

import android.graphics.drawable.Drawable;

/**
 * Caches downloaded images, saves bandwidth and user's packets
 * 
 * @author Lukasz Wisniewski
 */
// public class ImageCache extends WeakHashMap<String, Bitmap> {
public class ImageCache extends WeakHashMap<String, SoftReference<Drawable>> {
	private static final long serialVersionUID = 1L;

	public boolean isCached(String url) {
		return containsKey(url) && get(url) != null;
	}

}