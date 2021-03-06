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

package kohii.v1.sample.common

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

/**
 * @author eneim (2018/07/27).
 */
open class BaseFragment : Fragment() {

  private val logTag by lazy { "Kohii::App:${javaClass.simpleName}" }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    Log.d(logTag, "onAttach() called: $context")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d(logTag, "onCreate() called: $savedInstanceState")
  }

  override fun onStart() {
    super.onStart()
    Log.d(logTag, "onStart() called")
  }

  override fun onStop() {
    super.onStop()
    Log.d(logTag, "onStop() called")
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.d(logTag, "onDestroy() called")
  }

  override fun onDetach() {
    super.onDetach()
    Log.d(logTag, "onDetach() called")
  }
}
