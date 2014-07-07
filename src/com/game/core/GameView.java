package com.game.core;

import java.util.HashMap;

import com.game.beta.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import android.widget.ImageView;
import android.widget.LinearLayout;


public class GameView extends FrameLayout {

	public interface GameState{
		void onGameOver();
		void onGameWin();
		void onGameScoreChanged(int score);
		void onGameMaxTile(int tileVal);
	}
	
	private FrameLayout animLayer;
	private LinearLayout tileLayer;
	private Grid grid;
	private HashMap<Integer,Integer> tileViewId;
	private HashMap<Integer,Integer> animViewId;
	private int tileSize;
	private Handler mHandler;
	private GameState gs; 
	
	private int score;
	private int maxTileVal;
	
	private final int[] icons = {R.drawable.wen, R.drawable.jiao,
			R.drawable.jing, R.drawable.ou, R.drawable.re,
			R.drawable.shuang, R.drawable.wa, R.drawable.xia,
			R.drawable.xia2, R.drawable.xian, R.drawable.xiao,
			R.drawable.you
	};
	
    public GameView(Context context) {
		super(context);
		this.grid  = new Grid(4);
		this.score = 0;
		this.maxTileVal = -1;
		this.tileViewId = new HashMap<Integer,Integer>();
		this.animViewId = new HashMap<Integer,Integer>();
		this.mHandler = new Handler();
		this.initGameView(context);
		this.setOnTouchListener(new OnSwipeListener(this.getContext()){
			@Override
			public void onSwipeRight() {
				int mergedScore = grid.merge(Grid.MERGE_DIRECTION_RIGHT);
				if (mergedScore>0){
					score += mergedScore;
					if (gs!=null)gs.onGameScoreChanged(score);
				}
				if (grid.checkGameState() == Grid.GAME_OVER && gs!=null){
					gs.onGameOver();
				}
				else if (grid.checkGameState() == Grid.GAME_WIN && gs!=null){
					gs.onGameWin();
				}
				int max = grid.getMax();
				if (max>maxTileVal){ 
					maxTileVal = max;
					if (gs!=null)gs.onGameMaxTile(max);
				}
				animate(grid.getShouldMoveIndex(),grid.getShouldMoveVal(),Grid.MERGE_DIRECTION_RIGHT);
		    }
			@Override
			public void onSwipeLeft() {
				int mergedScore = grid.merge(Grid.MERGE_DIRECTION_LEFT);
				if (mergedScore>0){
					score += mergedScore;
					if (gs!=null)gs.onGameScoreChanged(score);
				}
				if (grid.checkGameState() == Grid.GAME_OVER && gs!=null){
					gs.onGameOver();
				}
				else if (grid.checkGameState() == Grid.GAME_WIN && gs!=null){
					gs.onGameWin();
				}
				int max = grid.getMax();
				if (max>maxTileVal){ 
					maxTileVal = max;
					if (gs!=null)gs.onGameMaxTile(max);
				}
				animate(grid.getShouldMoveIndex(),grid.getShouldMoveVal(),Grid.MERGE_DIRECTION_LEFT);
		    }
			@Override
			public void onSwipeTop() {
				int mergedScore = grid.merge(Grid.MERGE_DIRECTION_TOP);
				if (mergedScore>0){
					score += mergedScore;
					if (gs!=null)gs.onGameScoreChanged(score);
				}
				if (grid.checkGameState() == Grid.GAME_OVER && gs!=null){
					gs.onGameOver();
				}
				else if (grid.checkGameState() == Grid.GAME_WIN && gs!=null){
					gs.onGameWin();
				}
				int max = grid.getMax();
				if (max>maxTileVal){ 
					maxTileVal = max;
					if (gs!=null)gs.onGameMaxTile(max);
				}
				animate(grid.getShouldMoveIndex(),grid.getShouldMoveVal(),Grid.MERGE_DIRECTION_TOP);
		    }
			@Override
			public void onSwipeBottom() {
				int mergedScore = grid.merge(Grid.MERGE_DIRECTION_BOTTOM);
				if (mergedScore>0){
					score += mergedScore;
					if (gs!=null)gs.onGameScoreChanged(score);
				}
				if (grid.checkGameState() == Grid.GAME_OVER && gs!=null){
					gs.onGameOver();
				}
				else if (grid.checkGameState() == Grid.GAME_WIN && gs!=null){
					gs.onGameWin();
				}
				int max = grid.getMax();
				if (max>maxTileVal){ 
					maxTileVal = max;
					if (gs!=null)gs.onGameMaxTile(max);
				}
				animate(grid.getShouldMoveIndex(),grid.getShouldMoveVal(),Grid.MERGE_DIRECTION_BOTTOM);
		    }			
		});
	}
	
    public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.grid  = new Grid(4);
		this.tileViewId = new HashMap<Integer,Integer>();
		this.animViewId = new HashMap<Integer,Integer>();
		this.mHandler = new Handler();
		this.initGameView(context);
		this.setOnTouchListener(new OnSwipeListener(this.getContext()){
			@Override
			public void onSwipeRight() {
				int mergedScore = grid.merge(Grid.MERGE_DIRECTION_RIGHT);
				if (mergedScore>0){
					score += mergedScore;
					if (gs!=null)gs.onGameScoreChanged(score);
				}
				if (grid.checkGameState() == Grid.GAME_OVER && gs!=null){
					gs.onGameOver();
				}
				else if (grid.checkGameState() == Grid.GAME_WIN && gs!=null){
					gs.onGameWin();
				}
				int max = grid.getMax();
				if (max>maxTileVal){ 
					maxTileVal = max;
					if (gs!=null)gs.onGameMaxTile(max);
				}
				animate(grid.getShouldMoveIndex(),grid.getShouldMoveVal(),Grid.MERGE_DIRECTION_RIGHT);
		    }
			@Override
			public void onSwipeLeft() {
				int mergedScore = grid.merge(Grid.MERGE_DIRECTION_LEFT);
				if (mergedScore>0){
					score += mergedScore;
					if (gs!=null)gs.onGameScoreChanged(score);
				}
				if (grid.checkGameState() == Grid.GAME_OVER && gs!=null){
					gs.onGameOver();
				}
				else if (grid.checkGameState() == Grid.GAME_WIN && gs!=null){
					gs.onGameWin();
				}
				int max = grid.getMax();
				if (max>maxTileVal){ 
					maxTileVal = max;
					if (gs!=null)gs.onGameMaxTile(max);
				}
				animate(grid.getShouldMoveIndex(),grid.getShouldMoveVal(),Grid.MERGE_DIRECTION_LEFT);
		    }
			@Override
			public void onSwipeTop() {
				int mergedScore = grid.merge(Grid.MERGE_DIRECTION_TOP);
				if (mergedScore>0){
					score += mergedScore;
					if (gs!=null)gs.onGameScoreChanged(score);
				}
				if (grid.checkGameState() == Grid.GAME_OVER && gs!=null){
					gs.onGameOver();
				}
				else if (grid.checkGameState() == Grid.GAME_WIN && gs!=null){
					gs.onGameWin();
				}
				int max = grid.getMax();
				if (max>maxTileVal){ 
					maxTileVal = max;
					if (gs!=null)gs.onGameMaxTile(max);
				}
				animate(grid.getShouldMoveIndex(),grid.getShouldMoveVal(),Grid.MERGE_DIRECTION_TOP);
		    }
			@Override
			public void onSwipeBottom() {
				int mergedScore = grid.merge(Grid.MERGE_DIRECTION_BOTTOM);
				if (mergedScore>0){
					score += mergedScore;
					if (gs!=null)gs.onGameScoreChanged(score);
				}
				if (grid.checkGameState() == Grid.GAME_OVER && gs!=null){
					gs.onGameOver();
				}
				else if (grid.checkGameState() == Grid.GAME_WIN && gs!=null){
					gs.onGameWin();
				}
				int max = grid.getMax();
				if (max>maxTileVal){ 
					maxTileVal = max;
					if (gs!=null)gs.onGameMaxTile(max);
				}
				animate(grid.getShouldMoveIndex(),grid.getShouldMoveVal(),Grid.MERGE_DIRECTION_BOTTOM);
		    }			
		});
	}
 
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		tileSize = (Math.min(w, h))/grid.columnCount();

		addTiles(tileSize,tileSize);
		
		for (int i=0;i<2;i++){
			grid.randomAvailCell(2);
		}
		this.updateTileView();
	}

    public void setOnGameStateListener(GameState gs){
    	this.gs = gs;
    }

	private void initGameView(Context ctx){
    	this.tileLayer = new LinearLayout(ctx);
    	this.animLayer = new FrameLayout(ctx);
    	this.tileLayer.setOrientation(LinearLayout.VERTICAL);
    	FrameLayout.LayoutParams animlp = 
    			new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	LinearLayout.LayoutParams tilelp = 
    			new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    	this.addView(tileLayer,animlp);
    	this.addView(animLayer,tilelp);
    }
    
    @SuppressLint("NewApi")
	private void addTiles(int tile_w, int tile_h){
    	
    	 LinearLayout line;
		 LinearLayout.LayoutParams lineLp;
    	 for(int i=0;i<grid.rowCount();i++){
    		 line = new LinearLayout(getContext());
    		 lineLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,tile_h);
    		 tileLayer.addView(line,lineLp);
    		 
    		 for (int j=0;j<grid.columnCount();j++){
              ImageView image = new ImageView(getContext());  
              if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {

              	int id = Util.generateViewId();
              	tileViewId.put(i*grid.rowCount()+j, id);
                image.setId(id);

              } else {

              	int id = View.generateViewId();
              	tileViewId.put(i*grid.rowCount()+j, id);
                image.setId(id);

              }
              image.setImageResource(icons[0]);
              image.setPadding(3, 3, 3, 3);
              image.setBackgroundColor(Color.argb(255, 0xbb, 0xad, 0xa0));
              ViewGroup.LayoutParams lp =new ViewGroup.LayoutParams(tile_w,tile_h);
           
              image.setLayoutParams(lp);
              line.addView(image);
    		 }
          }  
    }
    
    private void updateTileView(){
 
		for (int i=0;i<grid.columnCount()*grid.rowCount();i++){
			int val = grid.getCellVal(i);
			int imageIndex=0;
			
			while (val>1){
				val = val/2;
				imageIndex++;
			}
			 
			Integer indexObj = tileViewId.get(i);
			ImageView img = (ImageView)findViewById(indexObj.intValue());
			img.setImageResource(icons[imageIndex]);
			img.setPadding(3, 3, 3, 3);
			img.setBackgroundColor(Color.argb(255, 0xbb, 0xad, 0xa0));
	
			if (i == grid.getCurrentRandomTileIndex()){
				Animation a = new RotateAnimation(0.0f, 360.0f,
		                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		                0.5f);
		        a.setDuration(300);
		        img.startAnimation(a);
			}	
		}
    }
    
    public void reset(){
    	grid.empty();
    	for (int i=0;i<2;i++){
			grid.randomAvailCell(2);
		}
		this.updateTileView();
		this.animViewId.clear();
		this.score = 0;
		mHandler.post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				animLayer.removeAllViews();
			}
			
		});
    }
    
    @SuppressLint("NewApi")
	private void animate(SparseIntArray index, SparseIntArray value,int direction){
		SparseIntArray tmpIdx = index.clone();
		SparseIntArray tmpVal = value.clone();
		int step;
		
		if (direction == Grid.MERGE_DIRECTION_LEFT || direction == Grid.MERGE_DIRECTION_RIGHT)
			step = 1;
		else step = grid.columnCount();
		
		for (int i=0;i<tmpIdx.size();i++){
			int fromCell = tmpIdx.keyAt(i);
			int toCell = tmpIdx.get(fromCell);
			int cellVal = tmpVal.get(fromCell);
			animateOneTile(fromCell,toCell,cellVal,step);
		}
    }
    
    
    @SuppressLint("NewApi")
	private void animateOneTile(final int fromCell, final int toCell, int cellVal,int step){
    	
    	LayoutParams lp = new LayoutParams(this.tileSize,this.tileSize); 	
    	ImageView imgV = new ImageView(getContext());
    	
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
          	int id = Util.generateViewId();
          	animViewId.put(fromCell, id);
            imgV.setId(id);

        } else {
          	int id = View.generateViewId();
          	animViewId.put(fromCell, id);
            imgV.setId(id);
        }
		
		int imageIndex = 0;
		while (cellVal>1){
			cellVal = cellVal/2;
			imageIndex++;
		}
		imgV.setImageResource(icons[imageIndex]);
		imgV.setPadding(3, 3, 3, 3);
		imgV.setBackgroundColor(Color.argb(255, 0xbb, 0xad, 0xa0));	
		lp.leftMargin = fromCell%grid.columnCount()*tileSize;
		lp.topMargin = fromCell/grid.rowCount()*tileSize;
		imgV.setLayoutParams(lp);
		animLayer.addView(imgV);
    	
		TranslateAnimation ta;
		if (step>1){
			ta = new TranslateAnimation(0, 0 , 0, tileSize*(toCell-fromCell)/step);
		}
		else {
			ta = new TranslateAnimation(0,tileSize*(toCell-fromCell), 0 , 0);
		}
		ta.setDuration(100);
		ta.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				int imgResId = tileViewId.get(fromCell);
				ImageView imgV = (ImageView)tileLayer.findViewById(imgResId);
				imgV.setImageResource(icons[0]);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				int animId = animViewId.get(fromCell);
				animViewId.remove(animId);
				final ImageView animV = (ImageView)animLayer.findViewById(animId);
				animV.setVisibility(View.INVISIBLE);
				int toId = tileViewId.get(toCell);
				ImageView toV = (ImageView)tileLayer.findViewById(toId);
				int val = grid.getCellVal(toCell);
				int imageIndex = 0;
				while (val>1){
					val = val/2;
					imageIndex++;
				}
				toV.setImageResource(icons[imageIndex]);
				
				
				int rdnIdx = grid.getCurrentRandomTileIndex();
				int rdnVal = grid.getCellVal(rdnIdx);
				ImageView rdnV = (ImageView)tileLayer.findViewById(tileViewId.get(rdnIdx));
				int rdnImgIdx = 0;
				while (rdnVal>1){
					rdnVal = rdnVal/2;
					rdnImgIdx++;
				}
				rdnV.setImageResource(icons[rdnImgIdx]);
				Animation a = new RotateAnimation(0.0f, 360.0f,
		                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		                0.5f);
		        a.setDuration(300);
				rdnV.startAnimation(a);
				animViewId.remove(fromCell);
				mHandler.post(new Runnable(){
					public void run() {
						animLayer.removeView(animV);
				    }		
				});
				
			}
		});
		imgV.startAnimation(ta);
    }
}
