package com.game.beta;

import java.util.HashMap;




import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;

import com.game.core.Grid;
import com.game.core.OnSwipeListener;
import com.game.core.Util;

public class MainActivity extends Activity {
	
	public static final String BEST_SCORE="BEST_SCORE";
	
	private Grid grid;
	private HashMap<Integer,Integer> viewId;
	private int score;
	private int bestScore;
	
	private final int[] icons = {R.drawable.wen, R.drawable.jiao,
			R.drawable.jing, R.drawable.ou, R.drawable.re,
			R.drawable.shuang, R.drawable.wa, R.drawable.xia,
			R.drawable.xia2, R.drawable.xian, R.drawable.xiao,
			R.drawable.you
	};

	@SuppressLint("NewApi")
	private void init(){
		grid = new Grid(4,10000);
		viewId = new HashMap<Integer,Integer>();
		score = 0;
		SharedPreferences settings = getSharedPreferences(BEST_SCORE, Context.MODE_PRIVATE);
		bestScore = settings.getInt(BEST_SCORE, 0);
		
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int screenWidth = size.x;
		
		GridLayout l = (GridLayout)findViewById(R.id.GridLayout1);
		TextView s = (TextView)findViewById(R.id.Score);
		s.setText(String.valueOf(score));
		TextView b = (TextView)findViewById(R.id.Best);
		b.setText(String.valueOf(bestScore));
		
        for(int i=0;i<16;i++){  
           // View view = View.inflate(this, R.layout.item, null);  
            ImageView image = new ImageView(this);  
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {

            	int id = Util.generateViewId();
            	viewId.put(i, id);
                image.setId(id);

            } else {

            	int id = Util.generateViewId();
            	viewId.put(i, id);
                image.setId(View.generateViewId());

            }
            image.setImageResource(icons[0]);
            image.setPadding(3, 3, 3, 3);
            image.setBackgroundColor(Color.argb(255, 0xbb, 0xad, 0xa0));
            ViewGroup.LayoutParams p =new ViewGroup.LayoutParams((screenWidth)/4,(screenWidth)/4);
            GridLayout.LayoutParams gp = new GridLayout.LayoutParams(p);
         
         
            image.setLayoutParams(gp);
            l.addView(image);
        }  
        
		l.setOnTouchListener(new OnSwipeListener(this){
			@Override
			public void onSwipeRight() {
				MainActivity.this.mergeOperation(Grid.MERGE_DIRECTION_RIGHT);
		
		    }
			@Override
			public void onSwipeLeft() {
				MainActivity.this.mergeOperation(Grid.MERGE_DIRECTION_LEFT);
			
		    }
			@Override
			public void onSwipeTop() {
				MainActivity.this.mergeOperation(Grid.MERGE_DIRECTION_TOP);
		
		    }
			@Override
			public void onSwipeBottom() {
				MainActivity.this.mergeOperation(Grid.MERGE_DIRECTION_BOTTOM);

		    }			
			
			
		});
		
		for (int i=0;i<2;i++){
			grid.randomAvailCell(2);
		}
		this.updateView();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		SharedPreferences settings = getSharedPreferences(BEST_SCORE, Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putInt(BEST_SCORE,bestScore);

	      // Commit the edits!
	      editor.commit();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		SharedPreferences settings = getSharedPreferences(BEST_SCORE, Context.MODE_PRIVATE);
	      SharedPreferences.Editor editor = settings.edit();
	      
	      if (score>bestScore){
	      editor.putInt(BEST_SCORE,score);

	      // Commit the edits!
	      editor.commit();
	      }
	}

	private void updateView(){
		
		for (int i=0;i<16;i++){
			int val = grid.getCellVal(i);
			int imageIndex=0;
			
			while (val>1){
				val = val/2;
				imageIndex++;
			}
			 
				Integer indexObj = viewId.get(i);
				ImageView img = (ImageView)findViewById(indexObj.intValue());
				
				if (i == grid.getCurrentRandomTileIndex())img.startAnimation( 
					    AnimationUtils.loadAnimation(this, R.anim.rotate) );
				img.setImageResource(icons[imageIndex]);
				img.setPadding(3, 3, 3, 3);
				img.setBackgroundColor(Color.argb(255, 0xbb, 0xad, 0xa0));
		}
		TextView s = (TextView)findViewById(R.id.Score);
		s.setText(String.valueOf(score));	
		
		TextView m = (TextView)findViewById(R.id.Current_max);
		m.setText("Current Tile: ".concat(String.valueOf(grid.getMax())));
		
		if (grid.getMax()>=Grid.TARGET_TILE_VAL){
			
			final Dialog dialog = new Dialog(this);	
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_dialog);
			
			TextView text = (TextView)dialog.findViewById(R.id.DialogText);
			text.setText("You win the game!");
			text.setTextSize(15);
			ImageView image = (ImageView)dialog.findViewById(R.id.DialogImage);
			image.setImageResource(R.drawable.xiao);
			
			Button dialogButton = (Button)dialog.findViewById(R.id.DialogButton);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					int best = 0;
					if (MainActivity.this.score>MainActivity.this.bestScore){
						best = MainActivity.this.score;
						
					}
					MainActivity.this.reset();
					updateView();
					if (best>0){
					TextView b = (TextView)findViewById(R.id.Best);
					b.setText(String.valueOf(best));
					bestScore = best;
					
					SharedPreferences settings = getSharedPreferences(BEST_SCORE, Context.MODE_PRIVATE);
				    SharedPreferences.Editor editor = settings.edit();				      		
				    editor.putInt(BEST_SCORE,best);

				    // Commit the edits!
				    editor.commit();								
					}			
				}
			});
 
			dialog.show();
		}
	}
	
	private void reset(){
		grid = new Grid(4,10000);
		score = 0;
		for (int i=0;i<2;i++){
			grid.randomAvailCell(2);
		}
	}
	
	private void mergeOperation(int direction){
		int merged_score = grid.merge(direction);
		if (merged_score>=0){
			score+=merged_score;
			updateView();
		}
		else if (merged_score==Grid.MERGE_NO_OP && grid.isFull()){
			final Dialog dialog = new Dialog(this);	
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_dialog);
			
			TextView text = (TextView)dialog.findViewById(R.id.DialogText);
			text.setText("Game over!");
			text.setTextSize(15);
			ImageView image = (ImageView)dialog.findViewById(R.id.DialogImage);
			image.setImageResource(R.drawable.chi);
			
			Button dialogButton = (Button)dialog.findViewById(R.id.DialogButton);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					int best = 0;
					if (MainActivity.this.score>MainActivity.this.bestScore){
						best = MainActivity.this.score;
						
					}
					MainActivity.this.reset();
					updateView();
					if (best>0){
					TextView b = (TextView)findViewById(R.id.Best);
					b.setText(String.valueOf(best));
					bestScore = best;
					
					SharedPreferences settings = getSharedPreferences(BEST_SCORE, Context.MODE_PRIVATE);
				    SharedPreferences.Editor editor = settings.edit();				      		
				    editor.putInt(BEST_SCORE,best);

				    // Commit the edits!
				    editor.commit();								
					}			
				}
			});
 
			dialog.show();
		}
		
	}
}
