package com.limon.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.limon.make.R;

public class TabBarActivity extends ActivityGroup implements
		OnCheckedChangeListener {
	private int btnWidth = 64;// ��������£�ÿ����ť���64
	private LinearLayout contentViewLayout;// ��ҳ�������
	private RadioGroup tabBar;// ������
	private List<TabBarButton> buttonList;// �������İ�ť����
	private RadioGroup.LayoutParams buttonLayoutParams;// ��������ť�Ĳ��ֶ��󣬾�������widht��height

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbar);
		// �����ҳ�������
		contentViewLayout = (LinearLayout) findViewById(R.id.contentViewLayout);
		tabBar = (RadioGroup) findViewById(R.id.tabBar);
		tabBar.setOnCheckedChangeListener(this);
		buttonList = new ArrayList<TabBarButton>();
	}

	public void addTabButton(String label, int imageId, Intent intent) {
		TabBarButton btn = new TabBarButton(this);
		btn.setState(imageId, label, intent);
		buttonList.add(btn);
	}

	public void commit() {
		tabBar.removeAllViews();
		// ����豸����Ļ��ȣ�����һ��5���Ļ���ÿ����ť�ĳߴ�
		WindowManager windowManager = getWindowManager();
		int windowWidth = windowManager.getDefaultDisplay().getWidth();

		// ÿ��ͼ������64������һ���������ɶ��ٸ�ͼ��,����Ϊ�����������ж�
		int btnNum = windowWidth / 64;
		// �������Ĳ�����ť����һ���ĸ���(ÿ��64�Ŀ�ȣ���320����Ļ������5��)����ôҪ���¼���ÿ��ͼ���width�����򲻺ÿ�
		if (buttonList.size() < btnNum) {
			btnWidth = windowWidth / buttonList.size();
		}

		ButtonStateDrawable.setWIDTH(btnWidth);
		buttonLayoutParams = new RadioGroup.LayoutParams(btnWidth,
				LayoutParams.WRAP_CONTENT);
		// ���ذ�ť��Group�ϣ�������ÿ����ť��ID���������ʱ����������
		for (int i = 0; i < buttonList.size(); i++) {
			TabBarButton btn = buttonList.get(i);
			btn.setId(i);// ID��Indexһ�£������Ϳ�����onCheckedChanged�з���ʹ����
			tabBar.addView(btn, i, buttonLayoutParams);
		}

		setCurrentTab(0);
	}

	private void setCurrentTab(int index) {
		// ����RadioGroupѡ��״̬������UI
		tabBar.check(index);
		// ���������������������Button��Ӧ��Activity
		contentViewLayout.removeAllViews();
		TabBarButton btn = (TabBarButton) tabBar.getChildAt(index);
		// ����SDK�ĵ�˵����startActivity��ͬһ��ID�ĻỺ�棬�ڱ�д�ڲ�ActivityҪע�⣬ÿ��ˢ�µĴ�����OnResume�д���
		View tabView = getLocalActivityManager().startActivity(btn.getLabel(),
				btn.getIntent()).getDecorView();

		contentViewLayout.addView(tabView, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	// ��contentViewLayout���л���ť��Ӧ��Intent
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		setCurrentTab(checkedId);
	}

}
