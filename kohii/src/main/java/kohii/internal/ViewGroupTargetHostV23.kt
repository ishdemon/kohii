/*
 * Copyright (c) 2019 Nam Nguyen, nam@ene.im
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

package kohii.internal

import android.view.View
import android.view.View.OnScrollChangeListener
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import kohii.v1.PlaybackManager

@RequiresApi(23)
internal open class ViewGroupTargetHostV23(
  override val host: ViewGroup,
  manager: PlaybackManager
) : ViewGroupTargetHostBase(host, manager), OnScrollChangeListener {

  override fun onAdded() {
    super.onAdded()
    host.setOnScrollChangeListener(this)
  }

  override fun onRemoved() {
    super.onRemoved()
    host.setOnScrollChangeListener(null as OnScrollChangeListener?)
  }

  override fun onScrollChange(
    v: View?,
    scrollX: Int,
    scrollY: Int,
    oldScrollX: Int,
    oldScrollY: Int
  ) {
    manager.dispatchRefreshAll()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ViewGroupTargetHostV23) return false
    if (!super.equals(other)) return false
    if (host != other.host) return false
    return true
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + host.hashCode()
    return result
  }
}
