/*
 * Copyright (c) 2018 Nam Nguyen, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kohii.media

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author eneim (2018/06/24).
 */
@Parcelize
data class VolumeInfo(
  var mute: Boolean,
  var volume: Float
) : Parcelable {

  constructor() : this(false, 1F)

  constructor(original: VolumeInfo) : this(original.mute, original.volume)

  fun setTo(
    mute: Boolean,
    volume: Float
  ) {
    this.mute = mute
    this.volume = volume
  }

  companion object {
    val SCRAP = VolumeInfo(false, 1F)
  }
}
