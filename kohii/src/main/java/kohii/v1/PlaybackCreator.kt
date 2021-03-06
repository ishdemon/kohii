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

package kohii.v1

import kohii.v1.Playback.Config

interface PlaybackCreator<CONTAINER, PLAYER> {

  fun createPlayback(
    manager: PlaybackManager,
    target: Target<CONTAINER, PLAYER>,
    playable: Playable<PLAYER>,
    config: Config
  ): Playback<CONTAINER, PLAYER>
}
