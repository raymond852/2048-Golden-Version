package com.game.beta;

import java.util.HashMap;





import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;

import android.view.Window;
import android.widget.Button;

import android.widget.TextView;
import android.widget.ImageView;

import android.view.View.OnClickListener;

import com.game.core.GameView;


public class MainActivity extends Activity {
	
	public static final String BEST_SCORE="BEST_SCORE";
	
	private int score;
	private int bestScore;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		reset();
		
		
		GameView gv = (GameView)findViewById(R.id.GameView);
		gv.setOnGameStateListener(new GameView.GameState() {
			
			@Override
			public void onGameWin() {
				// TODO Auto-generated method stub
				showCustomDialog("You win this game!",R.drawable.you);
			}
			
			@Override
			public void onGameScoreChanged(int score) {
				// TODO Auto-generated method stub
				MainActivity.this.score = score;
				TextView s = (TextView)findViewById(R.id.Score);
				s.setText(String.valueOf(score));
			}
			
			@Override
			public void onGameOver() {
				// TODO Auto-generated method stub
				showCustomDialog("Game over!",R.drawable.chi);
			}

			@Override
			public void onGameMaxTile(int tileVal) {
				// TODO Auto-generated method stub
				TextView max = (TextView)findViewById(R.id.Current_max);
				max.setText("Current Tile: ".concat(String.valueOf(tileVal)));
			}
		});
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
		if (score>bestScore){
			saveBestScore(score);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (score>bestScore){
			saveBestScore(score);
		}
	}
	
	private void showCustomDialog(String content,int imgResId){
		final Dialog dialog = new Dialog(MainActivity.this);	
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		
		TextView text = (TextView)dialog.findViewById(R.id.DialogText);
		text.setText(content);
		text.setTextSize(15);
		ImageView image = (ImageView)dialog.findViewById(R.id.DialogImage);
		image.setImageResource(imgResId);
		
		Button dialogButton = (Button)dialog.findViewById(R.id.DialogButton);
	
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				GameView gv = (GameView)findViewById(R.id.GameView);
				gv.reset();
				if (score>bestScore){
					saveBestScore(score);
					bestScore = score;
					TextView b = (TextView)findViewById(R.id.Best);
					b.setText(String.valueOf(score));
				}
				reset();
			}
		});

		dialog.show();
	}
	
	private void reset(){
		score = 0;
		SharedPreferences settings = getSharedPreferences(BEST_SCORE, Context.MODE_PRIVATE);
		bestScore = settings.getInt(BEST_SCORE, 0);
		
		TextView s = (TextView)findViewById(R.id.Score);
		s.setText(String.valueOf(score));
		TextView b = (TextView)findViewById(R.id.Best);
		b.setText(String.valueOf(bestScore));
		TextView max = (TextView)findViewById(R.id.Current_max);
		max.setText("Current Tile: ".concat(String.valueOf(0)));
	}
	
	private void saveBestScore(int score){
		 SharedPreferences settings = getSharedPreferences(BEST_SCORE, Context.MODE_PRIVATE);
	     SharedPreferences.Editor editor = settings.edit();
	     editor.putInt(BEST_SCORE,score);
	      // Commit the edits!
	     editor.commit();
	}
	
}
