package com.limon.make;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;

import com.limon.make.R;

/**
 * �ϳ����ý���Activity.
 * 
 * @author iFlytek
 * @since 20120823
 */
public class TtsPreferenceActivity extends PreferenceActivity implements
		OnPreferenceChangeListener {

	/**
	 * �ϳɽ�����ں���
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager preferenceManager = getPreferenceManager();
		preferenceManager.setSharedPreferencesName(getPackageName());
		preferenceManager.setSharedPreferencesMode(MODE_PRIVATE);

		addPreferencesFromResource(R.xml.preference_tts);

		// ��ʼ���������б����.
		ListPreference roleListPreference = (ListPreference) findPreference(getString(R.string.preference_key_tts_role));
		roleListPreference.setOnPreferenceChangeListener(this);
		roleListPreference.setSummary(roleListPreference.getEntry());

		// ��ʼ�����ٵ��϶�������.
		SeekBarPreference speedSeekBarPreference = (SeekBarPreference) findPreference(getString(R.string.preference_key_tts_speed));
		speedSeekBarPreference.setOnPreferenceChangeListener(this);
		speedSeekBarPreference.setSummary(String.valueOf(speedSeekBarPreference
				.getProgress()));

		// ��ʼ���������϶�������.
		SeekBarPreference volumeSeekBarPreference = (SeekBarPreference) findPreference(getString(R.string.preference_key_tts_volume));
		volumeSeekBarPreference.setOnPreferenceChangeListener(this);
		volumeSeekBarPreference.setSummary(String
				.valueOf(volumeSeekBarPreference.getProgress()));

		ListPreference musicListPreference = (ListPreference) findPreference(getString(R.string.preference_key_tts_music));
		musicListPreference.setOnPreferenceChangeListener(this);
		musicListPreference.setSummary(musicListPreference.getEntry());
	}

	/**
	 * // * OnPreferenceChangeListener�Ľӿڣ������ý��������޸�ʱ������.
	 */
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// ����ǵ����б���ListPreference�в���ѡ�е����ĸ�Ԫ��.
		if (preference instanceof ListPreference) {
			ListPreference listPreference = (ListPreference) preference;

			CharSequence[] entries = listPreference.getEntries();
			int index = listPreference.findIndexOfValue((String) newValue);

			listPreference.setSummary(entries[index]);
			// �����seekbar��SeekBarPreference�в���ѡ�е����ĸ�Ԫ��.
		} else if (preference instanceof SeekBarPreference) {
			SeekBarPreference seekBarPreference = (SeekBarPreference) preference;

			seekBarPreference.setSummary(newValue.toString());
		}
		return true;
	}
}
