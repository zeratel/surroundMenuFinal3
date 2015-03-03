package com.cpbao.view;

import java.util.ArrayList;
import java.util.List;

import android.view.WindowManager;

/**   
 * @Title: AllotAngle.java 
 * @Package: com.cpbao.view  
 * @author: LHF   
 * @date: 2014年11月28日 下午4:33:55 
 * @description:按钮越界分配,自适配型，在任意边，或角度都可以平均分配
 *
 */
public class AllotAngle {

    private	boolean top;
    private	boolean buttom;
    private	boolean left;
    private	boolean right;
    private	int topDistance;
    private	int buttomDistance;
    private	int leftDistance;
    private	int rightDistance;
    
    private int starAngle = 10;
    private int gapAngle = 40;
	private int tempSum;
	
	//转换起始角度，及间隔角度
	public AllotAngle(int centerCx, int centerCy, int R, int sum,WindowManager wm) {
		super();
		tempSum = sum-1;
		
		//计算碰撞
		if (centerCx + R >= wm.getDefaultDisplay().getWidth()) {
			rightDistance = wm.getDefaultDisplay().getWidth() - centerCx;
			right = true;
		}
		//有的时候底部需要适配actionbar -scale*100
		if (centerCy + R >= wm.getDefaultDisplay().getHeight()) {
			buttomDistance = wm.getDefaultDisplay().getHeight() - centerCy;
			buttom = true;
		}
		if (centerCx - R <= 0) {
			leftDistance = centerCx;
			left = true;
		}
		if (centerCy - R <= 0) {
			topDistance = centerCy;
			top = true;
		}
		
		gapAngle = 360/sum;
		starAngle = 90;
		//计算初始角度及间隔角度
		if (left) {
//			gapAngle = 180/tempSum;
//			starAngle = -90;
			double arccosleft = Math.acos((double)leftDistance/(double)R)*180/Math.PI;
			gapAngle =  ((360-(25+2*(int)Math.round(arccosleft)))/tempSum);
			starAngle = 180+(int)Math.round(arccosleft)+15;
		}
		if (right) {
//			gapAngle = 180/tempSum;
//			starAngle = 90;
			double arccosright = Math.acos((double)rightDistance/(double)R)*180/Math.PI;
			gapAngle =  ((360-(25+2*(int)Math.round(arccosright)))/tempSum);
			starAngle = (int)Math.round(arccosright)+15;
		}
		
		if (top) {
//			gapAngle = 180/tempSum;
//			starAngle = 180;
			double arccostop = Math.acos((double)topDistance/(double)R)*180/Math.PI;
			gapAngle =  ((360-(25+2*(int)Math.round(arccostop)))/tempSum);
			starAngle = 90+(int)Math.round(arccostop)+15;
			
			if (left) {
				
//				gapAngle = 90/tempSum;
//				starAngle = -90;
				arccostop = Math.acos((double)topDistance/(double)R)*180/Math.PI;
				double arccosleft = Math.acos((double)leftDistance/(double)R)*180/Math.PI;
				gapAngle =  ((360-(115+(int)Math.round(arccostop)+(int)Math.round(arccosleft)))/tempSum);
				starAngle = 180+(int)Math.round(arccosleft)+15;
				
			}
			if (right) {
//				gapAngle = 90/tempSum;
//				starAngle = 180;
				//补充为20度,总角度补充为20度
				
//				System.out.println("topDistance:"+(double)topDistance/(double)R+",rightDistance:"+(double)rightDistance/(double)R);
				
				arccostop = Math.acos((double)topDistance/(double)R)*180/Math.PI;
				double arccosright = Math.acos((double)rightDistance/(double)R)*180/Math.PI;
//				System.out.println("arccostop:"+arccostop+",arccosright:"+arccosright);
				
				gapAngle =  ((360-(115+(int)Math.round(arccostop)+(int)Math.round(arccosright)))/tempSum);
				starAngle = (90+(int)Math.round(arccostop)+15);
				
//				System.out.println("gapAngle:"+gapAngle+",starAngle:"+starAngle);
			}
		}
		if (buttom) {
//			gapAngle = 180/tempSum;
//			starAngle = 0;
			double arccosbuttom = Math.acos((double)buttomDistance/(double)R)*180/Math.PI;
			gapAngle =  ((360-(25+2*(int)Math.round(arccosbuttom)))/tempSum);
			starAngle = -(90-(int)Math.round(arccosbuttom))+15;
			
			if (left) {
//				gapAngle = 90/tempSum;
//				starAngle = 0;
				arccosbuttom = Math.acos((double)buttomDistance/(double)R)*180/Math.PI;
				double arccosleft = Math.acos((double)leftDistance/(double)R)*180/Math.PI;
				gapAngle =  ((360-(115+(int)Math.round(arccosbuttom)+(int)Math.round(arccosleft)))/tempSum);
				starAngle = -(90-(int)Math.round(arccosbuttom))+15;
				
			}
			if (right) {
//				gapAngle = 90/tempSum;
//				starAngle = 90;
				arccosbuttom = Math.acos((double)buttomDistance/(double)R)*180/Math.PI;
				double arccosright = Math.acos((double)rightDistance/(double)R)*180/Math.PI;
				gapAngle =  ((360-(115+(int)Math.round(arccosbuttom)+(int)Math.round(arccosright)))/tempSum);
				starAngle = (int)Math.round(arccosright)+15;
				
			}
		}
		
	}


	public List<Integer> getAngles() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(starAngle);
		for (int i = 0; i < tempSum; i++) {
			starAngle += gapAngle;
			list.add(starAngle);
		}
		return list;
	}

	
}
