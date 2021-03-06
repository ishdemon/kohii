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

package kohii.v1.sample.ui.pager.data;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

@SuppressWarnings({ "WeakerAccess", "unused" }) //
public class Variations implements Parcelable {

  @Nullable final String meta;

  public Variations(@Nullable String meta) {
    this.meta = meta;
  }

  @Nullable public String getMeta() {
    return meta;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.meta);
  }

  protected Variations(Parcel in) {
    this.meta = in.readString();
  }

  public static final Creator<Variations> CREATOR = new Creator<Variations>() {
    @Override public Variations createFromParcel(Parcel source) {
      return new Variations(source);
    }

    @Override public Variations[] newArray(int size) {
      return new Variations[size];
    }
  };
}
