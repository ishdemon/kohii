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

package kohii.v1.exo

import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView
import kohii.media.PlaybackInfo
import kohii.media.PlaybackInfo.Companion.TIME_UNSET
import kohii.media.VolumeInfo
import kohii.v1.Helper
import kohii.v1.Kohii
import kohii.v1.OnVolumeChangedListener
import kohii.v1.Playable.Options
import kohii.v1.PlayerEventListener
import java.util.concurrent.atomic.AtomicBoolean


/**
 * @author eneim (2018/06/24).
 */
class ExoHelper(
    private val kohii: Kohii,
    private val store: ExoStore,
    private val options: Options
) : Helper {

  private val listeners = Helper.PlayerEventListeners()  // original listener.
  private val listenerSet = AtomicBoolean(false)

  private var mediaSource: MediaSource? = null
  private var player: Player? = null

  override fun prepare(loadSource: Boolean) {
    if (player == null) {
      player = ExoStore.get(kohii.app).acquirePlayer(this.options.config)
      player!!.repeatMode = options.repeatMode
    }

    if (listenerSet.compareAndSet(false, true)) {
      player!!.addListener(listeners)
      if (player is SimpleExoPlayer) {
        (player as SimpleExoPlayer).apply {
          this.addVideoListener(listeners)
          this.addTextOutput(listeners)
          this.addMetadataOutput(listeners)
        }
      }
    }

    if (loadSource) {
      ensurePlayerView()
      ensureMediaSource()
    }
  }

  override var playerView: PlayerView? = null
    set(value) {
      if (field === value) return
      if (value == null)
        field!!.player = null
      else {
        if (this.player != null) {
          PlayerView.switchTargetView(this.player!!, field, value)
        }
      }

      field = value
    }

  override fun play() {
    ensurePlayerView()
    ensureMediaSource()
    player!!.playWhenReady = true
  }

  override fun pause() {
    player!!.playWhenReady = false
  }

  override fun reset() {
    playbackInfo.reset()
    player?.stop(true)
    // TODO [20180214] double check this when ExoPlayer 2.7.0 is released.
    // TODO [20180326] reusable MediaSource will be added after ExoPlayer 2.7.1.
    // TODO [20180626] double check 2.8.x
    this.mediaSource = null // so it will be re-prepared when play() is called.
  }

  override fun release() {
    this.playerView = null
    if (player != null) {
      player!!.stop(true)
      if (player is KohiiPlayer) {
        (player as KohiiPlayer).clearOnVolumeChangedListener()
      }

      if (listenerSet.compareAndSet(true, false)) {
        player!!.removeListener(listeners)
        if (player is SimpleExoPlayer) {
          (player as SimpleExoPlayer).apply {
            this.removeTextOutput(listeners)
            this.removeVideoListener(listeners)
            this.removeMetadataOutput(listeners)
          }
        }
      }

      store.releasePlayer(player!!, options.config)
    }

    this.player = null
    this.mediaSource = null
  }

  override fun addEventListener(listener: PlayerEventListener) {
    this.listeners.add(listener)
  }

  override fun removeEventListener(listener: PlayerEventListener) {
    this.listeners.remove(listener)
  }

  override fun addOnVolumeChangeListener(listener: OnVolumeChangedListener) {

  }

  override fun removeOnVolumeChangeListener(listener: OnVolumeChangedListener?) {

  }

  override val isPlaying: Boolean
    get() = player?.playWhenReady ?: false

  override val volumeInfo: VolumeInfo
    get() = playbackInfo.volumeInfo

  override fun setVolumeInfo(volumeInfo: VolumeInfo): Boolean {
    val changed = playbackInfo.volumeInfo != volumeInfo
    if (changed) {
      playbackInfo.volumeInfo = volumeInfo
      ExoStore.setVolumeInfo(player!!, playbackInfo.volumeInfo)
    }
    return changed
  }

  override var parameters: PlaybackParameters?
    get() = player!!.playbackParameters
    set(value) {
      player!!.playbackParameters = value
    }

  override var playbackInfo = PlaybackInfo()
    get() {
      updatePlaybackInfo()
      return field
    }
    set(value) {
      field.resumeWindow = value.resumeWindow
      field.resumePosition = value.resumePosition
      field.volumeInfo = value.volumeInfo

      if (player != null) {
        ExoStore.setVolumeInfo(player!!, field.volumeInfo)
        val haveResumePosition = field.resumeWindow != PlaybackInfo.INDEX_UNSET
        if (haveResumePosition) {
          player!!.seekTo(field.resumeWindow, field.resumePosition)
        }
      }
    }

  private fun updatePlaybackInfo() {
    if (player == null) return
    if (player!!.playbackState == Player.STATE_IDLE) return
    playbackInfo.resumeWindow = player?.currentWindowIndex ?: PlaybackInfo.INDEX_UNSET
    playbackInfo.resumePosition = if (player?.isCurrentWindowSeekable == true)
      Math.max(0, player?.currentPosition ?: 0)
    else
      TIME_UNSET
    playbackInfo.volumeInfo = VolumeInfo.SCRAP
  }

  private fun ensurePlayerView() {
    if (playerView != null && playerView!!.player !== player) playerView!!.player = player
  }

  private fun ensureMediaSource() {
    if (mediaSource == null) {  // Only actually prepare the source when play() is called.
      mediaSource = DefaultMediaSourceFactory(store).createMediaSource(this.options)
      if (player is KohiiPlayer) {
        (player as KohiiPlayer).prepare(mediaSource,
            playbackInfo.resumeWindow == PlaybackInfo.INDEX_UNSET, false)
      }
    }
  }
}