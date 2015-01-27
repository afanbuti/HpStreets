package com.limon.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;

import com.limon.make.R;

/**
 * ButtonStateDrawable����ťͼƬ�����ֵĻ���
 * 
 * @author Administrator
 * 
 */
public class ButtonStateDrawable extends Drawable {
	private String label;// ����
	private Bitmap bitmap;// Image
	private Shader labelShader;// �����Ƿ����
	private boolean onState;// �Ƿ���On״̬
	private static int WIDTH;// Drawable�Ŀ���
	private Context context;

	public ButtonStateDrawable(Context context, int imageId, String label,
			boolean onState) {
		super();
		this.context = context;
		// ���Bitmap������Դ
		this.bitmap = BitmapFactory.decodeResource(context.getResources(),
				imageId);
		this.label = label;
		this.onState = onState;
		if (onState) {
			labelShader = new LinearGradient(0, 0, 0, 10, new int[] {
					Color.WHITE, Color.LTGRAY }, null, Shader.TileMode.MIRROR);
		} else {
			labelShader = new LinearGradient(0, 0, 0, 10, new int[] {
					Color.LTGRAY, Color.DKGRAY }, null, Shader.TileMode.MIRROR);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		int bwidth = bitmap.getWidth();
		int bheight = bitmap.getHeight();
		// Drawable�Ŀ��ȣ�ͼƬ�Ŀ��ȣ�����ҵ����׿��ȣ�Ϊ��ͼƬ����
		int x = (WIDTH - bwidth) / 2;
		int y = 4;// top�Ŀռ䣬Ϊ��������2������
		Paint p = new Paint();
		p.setTextSize(10);
		p.setFakeBoldText(true);// α���壬����ʹ��
		p.setTextAlign(Align.CENTER);
		p.setShader(labelShader);
		p.setAntiAlias(true);// ����ݿ���
		// �ڣ�WIDTH / 2, y + bheight + 11�����꣬����pָ���Ĺ�����Ƴ��ı�
		canvas.drawText(label, WIDTH / 2, y + bheight + 11, p);
		// �����on״̬
		if (onState) {
			p.setShader(null);
			Shader bgShader = new LinearGradient(
					15,
					y - 1,
					WIDTH - 15,
					32 + y + 1,
					new int[] {
							context.getResources().getColor(R.color.tabbar_1),
							context.getResources().getColor(R.color.tabbar_2) },
					null, Shader.TileMode.CLAMP);
			p.setShader(bgShader);
			p.setStyle(Paint.Style.FILL);
			RectF rect = new RectF(15, y - 1, WIDTH - 15, 32 + y + 1);
			canvas.drawRoundRect(rect, 5.0f, 5.0f, p);
		}

		canvas.drawBitmap(bitmap, x, y, p);
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub

	}

	public static void setWIDTH(int wIDTH) {
		WIDTH = wIDTH;
	}
}