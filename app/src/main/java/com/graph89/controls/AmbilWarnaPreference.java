//  http://code.google.com/p/android-color-picker/

/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.graph89.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

import com.eanema.graph89.R;

public class AmbilWarnaPreference extends Preference
{
	int	value;
	int	defValue	= 0;

	public AmbilWarnaPreference(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		defValue = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "defaultValue", 50);

		setWidgetLayoutResource(R.layout.ambilwarna_pref_widget);
	}

	@Override
	protected void onBindView(View view)
	{
		super.onBindView(view);

		// Set our custom views inside the layout
		final View kotak = view.findViewById(R.id.ambilwarna_pref_widget_kotak);
		if (kotak != null)
		{
			kotak.setBackgroundColor(value);
		}
	}

	@Override
	protected void onClick()
	{
		new AmbilWarnaDialog(getContext(), defValue, value, new AmbilWarnaDialog.OnAmbilWarnaListener()
		{
			@Override
			public void onOk(AmbilWarnaDialog dialog, int color)
			{
				if (!callChangeListener(color)) return; // They don't want the
														// value to be set
				value = color;
				persistInt(value);
				notifyChanged();
			}

			@Override
			public void onCancel(AmbilWarnaDialog dialog)
			{
				// nothing to do
			}
		}).show();
	}

	public void forceSetValue(int value)
	{
		this.value = value;
		persistInt(value);
		notifyChanged();
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index)
	{
		// This preference type's value type is Integer, so we read the default
		// value from the attributes as an Integer.
		return a.getInteger(index, 0);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue)
	{
		if (restoreValue)
		{ // Restore state
			value = getPersistedInt(value);
		}
		else
		{ // Set state
			int value = (Integer) defaultValue;
			this.value = value;
			persistInt(value);
		}
	}

	/*
	 * Suppose a client uses this preference type without persisting. We must
	 * save the instance state so it is able to, for example, survive
	 * orientation changes.
	 */
	@Override
	protected Parcelable onSaveInstanceState()
	{
		final Parcelable superState = super.onSaveInstanceState();
		if (isPersistent()) return superState; // No need to save instance state
												// since it's persistent

		final SavedState myState = new SavedState(superState);
		myState.value = value;
		return myState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state)
	{
		if (!state.getClass().equals(SavedState.class))
		{
			// Didn't save state for us in onSaveInstanceState
			super.onRestoreInstanceState(state);
			return;
		}

		// Restore the instance state
		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		this.value = myState.value;
		notifyChanged();
	}

	/**
	 * SavedState, a subclass of {@link BaseSavedState}, will store the state of
	 * MyPreference, a subclass of Preference.
	 * <p>
	 * It is important to always call through to super methods.
	 */
	private static class SavedState extends BaseSavedState
	{
		int	value;

		public SavedState(Parcel source)
		{
			super(source);
			value = source.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			super.writeToParcel(dest, flags);
			dest.writeInt(value);
		}

		public SavedState(Parcelable superState)
		{
			super(superState);
		}

		@SuppressWarnings("unused")
		public static final Parcelable.Creator<SavedState>	CREATOR	= new Parcelable.Creator<SavedState>()
																	{
																		public SavedState createFromParcel(Parcel in)
																		{
																			return new SavedState(in);
																		}

																		public SavedState[] newArray(int size)
																		{
																			return new SavedState[size];
																		}
																	};
	}
}
