package com.cpbao.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.cpbao.view.Circle.UpListener;
import com.example.surroundmenu.R;

/**
 * 
 * @author LHF
 *
 */
public class MainActivity extends Activity {

	// private Circle circle;
	private RelativeLayout layout;
	private Circle circle;
	boolean flagInOut = true;// 进入，出去标志位
	boolean isShow = true;// 显示控制，已触发标志位
	boolean isClick;// 点击控制，点击过或每没点击过

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// circle = new Circle(this);
		// EditText editText = (EditText) findViewById(R.id.et_1);
		// String string = editText.getText().toString();
		// System.out.println(string);
		layout = (RelativeLayout) findViewById(R.id.rl_touch);
		// 需要有，否则转发问题
		layout.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				System.out.println("onLongClick");
				return false;
			}
		});

		layout.setOnTouchListener(new OnTouchListener() {

			private int downX;
			private int downY;
			private int r;
			Handler handler = new Handler();

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				final MotionEvent tempEvent = event;
				Runnable runnable = null;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (circle != null) {
						// 有圆圈了才可以触发点击控制
						circle.dispatchTouchEvent(event);
					}
					
					//使用全新的长按检测方式，参考View，在500ms后检测是否还按着。。。
					runnable = new Runnable() {

						@Override
						public void run() {
							MenuControl(tempEvent);
						}
					};
					handler.postDelayed(runnable,
							ViewConfiguration.getLongPressTimeout());
					break;
				case MotionEvent.ACTION_UP:
						handler.removeCallbacks(runnable);
					break;
				default:
					break;
				}
				return false;
			}

			// 菜单控件
			private void MenuControl(MotionEvent event) {
				// 判断是长按的同时只显示一次。。。
				if (layout.isPressed() && isShow) {
					downX = (int) event.getX();
					downY = (int) event.getY();
					circle = new Circle(MainActivity.this, downX, downY);
//					System.out.println("isShow");
					LayoutParams layoutParams = new LayoutParams(
							getWindowManager().getDefaultDisplay().getWidth(),
							getWindowManager().getDefaultDisplay().getHeight());
					// layoutParams.leftMargin = (int) (event.getX()-200);
					// layoutParams.topMargin = (int) (event.getY()-200);
					// Log.i("LHF",
					// "ACTION_DOWN:event.getX():"+event.getX()+",event.getY()"+event.getY());
					layout.addView(circle, layoutParams);
					r = (int) circle.getR();
					circle.clearTimer();
					circle.init();
					circle.startAnim2();

					circle.setListener(new UpListener() {

						@Override
						public void onTouchUp() {
							// 点击之后才能全重置，需要加入特效延时判断
							// Log.i("LHF",
							// "ACTION_UP:event.getX():"+event.getX()+",event.getY()"+event.getY());
							// 注意顺序
							if (circle != null) {
								layout.removeView(circle);
								circle.clearTimer2();
								circle.clearTimer();
								circle = null;
							}

							// 只显示一次重置。。。
							isShow = true;
						}
					});

					isShow = false;
				}
			}
		});
	}

}
