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

package kohii.v1.sample.ui.player

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.LifecycleOwner
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import kohii.v1.Kohii
import kohii.v1.LifecycleOwnerProvider
import kohii.v1.Playback
import kohii.v1.Playback.Callback
import kohii.v1.Rebinder
import kohii.v1.sample.R
import kotlinx.android.synthetic.main.fragment_player.playerContainer
import kotlinx.android.synthetic.main.fragment_player.playerView

class PlayerDialogFragment : AppCompatDialogFragment(), LifecycleOwnerProvider, Callback {

  companion object {
    private const val KEY_INIT_DATA = "kohii::player::init_data"
    private const val KEY_REBINDER = "kohii:player:dialog:rebinder"

    fun newInstance(
      rebinder: Rebinder,
      initData: InitData
    ): PlayerDialogFragment {
      val bundle = Bundle().also {
        it.putParcelable(KEY_REBINDER, rebinder)
        it.putParcelable(KEY_INIT_DATA, initData)
      }
      return PlayerDialogFragment().also { it.arguments = bundle }
    }
  }

  // Interface to tell ParentFragment about status of this Dialog.
  interface Callback {

    fun onDialogActive()

    fun onDialogInActive(rebinder: Rebinder)
  }

  var rebinder: Rebinder? = null
  var playback: Playback<*, *>? = null

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)
    (dialog as? AppCompatDialog)?.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    return dialog
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_player, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    val initData = arguments?.getParcelable<InitData>(KEY_INIT_DATA)
    (playerContainer as AspectRatioFrameLayout).setAspectRatio(initData!!.aspectRatio)
  }

  override fun onStart() {
    super.onStart()
    val kohii = Kohii[this].also { it.register(this, arrayOf(playerContainer)) }
    this.rebinder = arguments?.getParcelable(KEY_REBINDER)
    this.rebinder?.rebind(kohii, playerView) {
      it.addCallback(this)
      playback = it
    }
  }

  override fun onActive(playback: Playback<*, *>) {
    (parentFragment as? Callback)?.onDialogActive()
  }

  // Would be called after onStop()
  override fun onInActive(playback: Playback<*, *>) {
    this.rebinder?.let {
      (parentFragment as? Callback)?.onDialogInActive(it)
    }
  }

  override fun onStop() {
    super.onStop()
    playback?.removeCallback(this@PlayerDialogFragment)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    playback = null
  }

  override fun provideLifecycleOwner(): LifecycleOwner {
    return this.viewLifecycleOwner
  }
}
