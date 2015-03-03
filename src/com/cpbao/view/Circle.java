package com.cpbao.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.surroundmenu.R;
/**   
 * @Title: Circle.java 
 * @Package: com.cpbao.view  
 * @author: LHF   
 * @date: 2014年11月28日 下午4:33:55 
 * @description:控件circle，自适配的圆形菜单，依次弹出，收缩，点击后会有色彩变幻的扩散圆环效果
 *
 */
public class Circle extends View {
	// static List<Integer> colors = new ArrayList<Integer>();
	static List<Integer> colors = new ArrayList<Integer>();  
	Paint paint;
	private float r = 1.5f;// 图片缩放倍数
	private float scale;// 适配倍数
	private int bitmapR = 90;

	private Timer timer1;
	private TimerTask task1;
	private Timer timer2;
	private TimerTask task2;
	private Context context;
	private int tempJ = -1;
	private boolean flagStop2;// 触摸接受事件标志位
	private boolean clearFlag = true;
	private boolean otherButtomFlag = true;
	private boolean backMoveFlag;
	private int tempx;
	private int tempy;
	private int tempCount;
	private int tempNum = 1;

	ArrayList<Circles> circles;

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				listener.onTouchUp();
			}
			invalidate();
		}
	};
	private int drawFlag;
	private Path path;
	private Paint paint2;
	private float cx;
	private float cy;
	private int bigR;
	private int centerCx;
	private int centerCy;
	private boolean flagStop;
	protected int tempJJ;
	private static int previousTempJ = -1;
	private AllotAngle allotAngle;
	private List<Integer> angles;
	private List<Integer> pics;

	public Circle(Context context, int cx, int cy) {
		super(context);
		initAngle(cx, cy);
		init();
	}

	public int getTempJJ() {
		return tempJJ;
	}

	private void initAngle(int cx, int cy) {
		scale = getResources().getDisplayMetrics().density;
		bigR = (int) (scale * 115);
		bitmapR = (int)(scale * 45);
		otherButtomFlag = true;

		centerCx = cx;
		centerCy = cy;
		// 边界判断
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);

		allotAngle = new AllotAngle(cx, cy, bigR, 7, wm);
		angles = allotAngle.getAngles();
		pics = new ArrayList<Integer>();
		pics.add(R.drawable.ssq);
		pics.add(R.drawable.d3d);
		pics.add(R.drawable.l7lc);
		pics.add(R.drawable.pl3);
		pics.add(R.drawable.pl5);
		pics.add(R.drawable.k3);
		pics.add(R.drawable.dlt);
	}

	// 最一开始 Start ！！！
	public Circle(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 汗。。。
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

	// 开始first
	public void init() {
		paint = new Paint();
		paint.setAntiAlias(true);

		// 把色彩固定下来不用每次的计算了，直接查就能得到
		for (int i = 1; i < 100; i++) {
			colors.add(changeColor2((double) i / (double) 100));
		}

		circles = new ArrayList<Circles>();
		// 角度很重要
		// 第一个是中心点，角度与扩展出去的速度
		circles.add(new Circles(centerCx, centerCy, setPaint(), 0, 0, bigR, 1,
				20, BitmapFactory.decodeResource(getResources(),
						R.drawable.cancel)));
		// 增长速率不能小于11
		for (int i = 0; i < angles.size(); i++) {
			circles.add(new Circles(centerCx, centerCy, setPaint(), angles
					.get(i), (int) (scale * 5), bigR, 1, (int) (scale * 10),
					BitmapFactory.decodeResource(getResources(), pics.get(i))));
		}

		// 检测按到哪一个，需要上一界面的OnTouch转发过来
		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					buttonDown(event);
					break;
				case MotionEvent.ACTION_MOVE:

					break;
				case MotionEvent.ACTION_UP:
					if (clearFlag) {
						handler.sendEmptyMessage(1);
					}
					break;

				default:
					break;
				}
				return true;
			}

			// 按钮按下，判断是否按到按钮上
			private void buttonDown(MotionEvent event) {
				if (flagStop2) {

					int x = (int) event.getX();
					int y = (int) event.getY();
					for (int i = 1; i < circles.size(); i++) {

						tempx = circles.get(i).getCx();
						tempy = circles.get(i).getCy();
						int tempw = Math.abs((int) (x - tempx));
						int temph = Math.abs((int) (y - tempy));
						// bitmapR为假设图片的半径其实比图片要大,当手指滑动在这个半径内的时候则认为滑动到按钮上
						int temp = (int) Math.sqrt(tempw * tempw + temph
								* temph);
//						System.out.println("buttonDown:" + temp);
						if (temp < bitmapR && otherButtomFlag) {
							// System.out.println(222+"x:"+x+"y:"+y);
							// System.out.println(222 + "," + i);
							// 防止多次触发
							if (i != tempJ) {
								tempJ = i;
								// 前一个 用于按钮退回
								previousTempJ = tempJ;
								// 当前 用于按钮退回
								// nowTempJ = tempJ;
								tempJJ = tempJ;
								// System.out.println("in+previousTempJ:"+previousTempJ);
								
//								Toast.makeText(getContext(), "i:" + tempJ, 1)
//										.show();

								circles.get(tempJJ).setColorTemp(1);
								clearFlag = false;
								otherButtomFlag = false;
								backMoveFlag = true;
								tempNum = 1;
								if (timer2 != null) {
									timer2.cancel();
									task2.cancel();
									timer2 = null;
									task2 = null;
								}
								// //按钮震动
								timer2 = new Timer();
								task2 = new TimerTask() {
									@Override
									public void run() {
										change2(circles.get(tempJJ), 0);

										// }

										handler.sendEmptyMessage(0);
									}

								};
								timer2.schedule(task2, 0, 20);

							}
						}
					}

				}
			}
		});

	}

	public void startAnim2() {

		// 按钮扩散
		timer1 = new Timer();
		task1 = new TimerTask() {

			@Override
			public void run() {

				if (!flagStop) {
					// 延时计数器,用于调整依次弹出的快慢
					if (tempCount < 10) {
						tempCount++;
					} else {
						tempCount = 0;

						// 可以依次的出现或消失
						if (tempNum < circles.size()) {
							tempNum++;
						}
					}
					for (int i = 1; i < circles.size(); i++) {
						int tempx = circles.get(i).getCx();
						int tempy = circles.get(i).getCy();
						int tempw = Math.abs((int) (tempx - centerCx));
						int temph = Math.abs((int) (tempy - centerCy));
						// 保证其他按钮最远距离在一个圆的范围内,如果超出就停下Timer
						int tempr = (int) Math.sqrt(tempw * tempw + temph
								* temph);
						if (tempr < bigR && !backMoveFlag) {
							
							//依次的出现或消失。。。
							if (i < tempNum) {
								circles.get(i).move(tempr);
							}

						} else if (tempr > (scale * 18) && backMoveFlag) {
							
							//依次的出现或消失。。。
							if (i != tempJJ && i >= circles.size() - tempNum) {
								circles.get(i).backMove();
							}
						} else if (tempr <= (scale * 18) && backMoveFlag) {
							circles.get(i).getPaint().setAlpha(0);
						} else {
							// 触摸开始接受事件
							flagStop2 = true;
							if (i == circles.size() - 1) {
								flagStop = true;
							}
						}
					}
//					handler.sendEmptyMessage(0);
					postInvalidate();

				} else {
					clearTimer();
				}
			}

		};
		timer1.schedule(task1, 0, 6);

		drawFlag = 2;
	}

	public void clearTimer() {
		if (timer1 != null) {
			flagStop = false;
			otherButtomFlag = true;
			backMoveFlag = false;
			timer1.cancel();
			task1.cancel();
			timer1 = null;
			task1 = null;
			System.gc();
		}
	}

	public void clearTimer2() {
		if (timer2 != null) {
			otherButtomFlag = true;
			backMoveFlag = false;
			timer2.cancel();
			task2.cancel();
			timer2 = null;
			task2 = null;
			System.gc();
			// flagStop2 = false;
		}
	}

	// 绘制second
	@Override
	protected void onDraw(final Canvas canvas) {
		for (int i = 0; i < circles.size(); i++) {

			// canvas.drawCircle(circles.get(i).getCx(), circles.get(i)
			// .getCy(), circles.get(i).getR(), circles.get(i).getPaint());

			Matrix matrix = new Matrix();
			// 切记，算法算完之后再做修正，否则会很麻烦
			// matrix.setScale(circles.get(i).getR(), circles.get(i).getR(),
			// circles.get(i).getBitmap().getWidth()/2,
			// circles.get(i).getBitmap().getHeight()/2);
			matrix.postTranslate(circles.get(i).getCx()
					- circles.get(i).getBitmap().getWidth() / 2, circles.get(i)
					.getCy() - circles.get(i).getBitmap().getHeight() / 2);
			if (i != 0) {
				canvas.drawCircle(circles.get(i).getCx(), circles.get(i)
						.getCy(), circles.get(i).getRR(), circles.get(i)
						.getPaint());
			}
			canvas.drawBitmap(circles.get(i).getBitmap(), matrix, circles
					.get(i).getPaint());
		}

	}

	public interface UpListener {
		public void onTouchUp();
	};

	public UpListener listener;

	public void setListener(UpListener listener) {
		this.listener = listener;
	}

	// 创建 paint
	public Paint setPaint() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(0xff000000);
		paint.setAlpha(255);
		paint.setStrokeWidth(10*scale);
		paint.setStyle(Style.STROKE);
		return paint;
	}

	// paint改变色彩,r,x,y,width 用于画图的handler
	private void change2(Circles bean, int userFlag) {
		Paint paint = bean.getPaint();
		int alpha = bean.getPaint().getAlpha();
		int r = (int) bean.getRR();
		int width = (int) paint.getStrokeWidth();
		int temp = bean.getColorTemp();

		// System.out.println("alpha"+alpha);
		alpha -= 6;
		// 0为circleBeans
		// if (userFlag == 0) {
		if (alpha < 0) {
			alpha = 0;
			clearFlag = true;
			handler.sendEmptyMessage(1);
			// 优化方便回收
			// Circles bean2 = circles.get(0);
			// bean2 = null;
			// circles.remove(0);
		}

		r += (int) (scale * 1.5);

		// System.out.println(width);
		width += (int) (scale * 0.5);

		// color
		// temp += 0.03;
		// if (temp >= 1) {
		// temp = 0.01;
		// }
		temp += 1;
		if (temp >= 100) {
			temp = 1;
		}

		bean.setRR(r);
		bean.setColorTemp(temp);
		// paint.setColor(changeColor2(temp));
		paint.setColor(colors.get(temp));
		// TODO 竟然和位置有关系。。。不能放在color之前！！！
		paint.setAlpha(alpha);
		paint.setStrokeWidth(width);
	}

	// 0.01-1
	private int changeColor2(double rate) {
		int baseColor = 0;

		if (rate >= 0 && rate <= 0.25) {

			baseColor = 0x0000fa | ((int) (rate * 1000) << (8));

		} else if (rate > 0.25 && rate <= 0.5) {

			baseColor = 0x00fa00 | (0xfa - ((int) (rate * 1000) - 250));

		} else if (rate > 0.5 && rate <= 0.75) {

			baseColor = 0x00fa00 | (((int) (rate * 1000) - 500) << (16));

		} else if (rate > 0.75 && rate <= 1) {

			baseColor = 0xfa0000 | ((0xfa - ((int) (rate * 1000) - 750)) << (8));
		}

		baseColor += 0xff000000;
		return baseColor;
	}

}
