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
 * ButtonStateDrawable负责按钮图片和文字的绘制
 * 
 * @author Administrator
 * 
 */
public class ButtonStateDrawable extends Drawable {
	private String label;// 文字
	private Bitmap bitmap;// Image
	private Shader labelShader;// 文字是否高亮
	private boolean onState;// 是否是On状态
	private static int WIDTH;// Drawable的宽度
	private Context context;

	public ButtonStateDrawable(Context context, int imageId, String label,
			boolean onState) {
		super();
		this.context = context;
		// 获得Bitmap绘制资源
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
		// Drawable的宽度－图片的宽度＝左或右的留白宽度，为了图片居中
		int x = (WIDTH - bwidth) / 2;
		int y = 4;// top的空间，为了美观留2个像素
		Paint p = new Paint();
		p.setTextSize(10);
		p.setFakeBoldText(true);// 伪粗体，中文使用
		p.setTextAlign(Align.CENTER);
		p.setShader(labelShader);
		p.setAntiAlias(true);// 抗锯齿开启
		// 在（WIDTH / 2, y + bheight + 11）坐标，根据p指定的规则绘制出文本
		canvas.drawText(label, WIDTH / 2, y + bheight + 11, p);
		// 如果是on状态
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
