package com.cpbao.view;

import android.graphics.Bitmap;
import android.graphics.Paint;

/**   
 * @Title: Circles.java 
 * @Package: com.cpbao.view  
 * @author: LHF   
 * @date: 2014年11月28日 下午4:33:55 
 * @description:circle的bean，同时融合了的增长算法和收缩算法
 *
 */
public class Circles{

	private int cx;//圆中心
	private int cy;//圆中心
//	private int touchCx;//圆中心
//	private int touchCy;//圆中心
	private float r;//图片缩放倍数
	private Paint paint;//paint
	private int cxIncrement;//x增长
	private int cyIncrement;//y增长
	private int cRIncrement;//r增长
	private int angle;//角度
	private int R;//小圆所到边缘的大圆
	private int RR;//色彩变幻的大圆
	private Bitmap bitmap;//所需要的图
	private int colorTemp = 1;//色彩变幻
	
	//中心。。。                paint    角度              扩散增长率           外边的大R     图片缩放倍数          色彩变幻的大圆 图片
	public Circles(int cx, int cy, Paint paint,int angle ,int cRIncrement,int R,float r,int RR,Bitmap bitmap) {
		super();
		this.cx = cx;
		this.cy = cy;
		this.paint = paint;
		this.cRIncrement = cRIncrement;
		this.angle = angle;
		this.R = R;
		this.r = r;
		this.bitmap = bitmap;
		this.RR = RR;
//		arithmetic(angle);
	}
	//通过算法获取时改变
	public int getCx() {
		return cx;
	}
	public void setCx(int cx) {
		this.cx = cx;
	}
	public int getCy() {
		return cy;
	}
	public void setCy(int cy) {
		this.cy = cy;
	}
	public Paint getPaint() {
		return paint;
	}
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
	public float getR() {
		return r;
	}
	public void setR(float r) {
		this.r = r;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public int getcRIncrement() {
		return cRIncrement;
	}
	
	public void setcRIncrement(int cRIncrement) {
		this.cRIncrement = cRIncrement;
	}
	
//	public int getTouchCx() {
//		return touchCx;
//	}
//	public int getTouchCy() {
//		return touchCy;
//	}
	
	public int getRR() {
		return RR;
	}
	public void setRR(int rR) {
		RR = rR;
	}
	public int getColorTemp() {
		return colorTemp;
	}
	public void setColorTemp(int colorTemp) {
		this.colorTemp = colorTemp;
	}
	//角度与增量比值，见三角函数，角度从第一象限逆时针算起
	private void arithmetic(int angle,int tempr){
		
		//增量变为变化量,减速用
//		if (tempr > 0 && cRIncrement>0) {
//			int tempCRIncrement = (int)((cRIncrement/R)*(double)(-tempr+R))+3;
//			cRIncrement = tempCRIncrement;
//		}
		
		//角度与增量比值，见三角函数
//		cxIncrement 横向增长
			cxIncrement = (int) Math.round(cRIncrement*Math.cos((angle * Math.PI) / 180));
//		cyIncrement 纵向增长
			cyIncrement = -(int) Math.round(cRIncrement*Math.sin((angle * Math.PI) / 180));
	}
	private void arithmetic(int angle){
		
		//角度与增量比值，见三角函数
//		cxIncrement 横向增长
		cxIncrement = (int) Math.round(cRIncrement*Math.cos((angle * Math.PI) / 180));
//		cyIncrement 纵向增长
		cyIncrement = -(int) Math.round(cRIncrement*Math.sin((angle * Math.PI) / 180));
	}
	
	
	public int getCxIncrement() {
		return cxIncrement;
	}
	public void setCxIncrement(int cxIncrement) {
		this.cxIncrement = cxIncrement;
	}
	public int getCyIncrement() {
		return cyIncrement;
	}
	public void setCyIncrement(int cyIncrement) {
		this.cyIncrement = cyIncrement;
	}
	//移动中心的cx与cy。。。
	public void move(int tempr) {
		arithmetic(angle,tempr);
		cx += cxIncrement;
		cy += cyIncrement;
//		touchCx = cx;
//		touchCy = cy;
		
	}
	public void backMove() {
		arithmetic(angle);
		cx -= cxIncrement;
		cy -= cyIncrement;
		
		
	}
	
}
