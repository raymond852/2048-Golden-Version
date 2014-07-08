package com.game.core;


import java.util.Random;

import android.util.SparseIntArray;


public class Grid {
	
	private int capacity;
	private int column;
	private int row;
	private int max;
	private int randomTileIndex;
	private int targetTileVal;
	
	private SparseIntArray occupied_index;
	
	private SparseIntArray should_move_index;
	private SparseIntArray should_move_val;
	
	public static final int CELL_EMPTY = 0;
	
	public static final int INVALID_RANDOM_INDEX=-1;
	
	public static final int MERGE_DIRECTION_LEFT = 0;
	public static final int MERGE_DIRECTION_RIGHT = 1;
	public static final int MERGE_DIRECTION_TOP = 2;
	public static final int MERGE_DIRECTION_BOTTOM = 3;
	
	public static final int MERGE_INVALID_MAX = -2;
	public static final int MERGE_FAIL = -3;
	public static final int MERGE_NO_OP = -1;
	
	public static final int GAME_WIN = 1;
	public static final int GAME_ON = 0;
	public static final int GAME_OVER = -4;
	
	public static final int TARGET_TILE_VAL = 2048;
	
	//probability of generating a 2 or a 4
	public static int RANDOM_MIN_TILE_PROBABILITY = 60;
	
	
	public Grid (int size){
		if (size<0)
			throw new Error("Grid size cannot be negative");
		this.capacity = size*size;
		this.column = size;
		this.row = size;
		this.max = 0;
		this.targetTileVal = Grid.TARGET_TILE_VAL;
		this.randomTileIndex = Grid.INVALID_RANDOM_INDEX;
		this.occupied_index = new SparseIntArray(this.capacity){
			public int get(int key){
				return this.get(key,CELL_EMPTY);
			}
		};
		this.should_move_index = new SparseIntArray(this.capacity){
			public int get(int key){
				return this.get(key,CELL_EMPTY);
			}
		};
		this.should_move_val = new SparseIntArray(this.capacity){
			public int get(int key){
				return this.get(key,CELL_EMPTY);
			}
		};
	}
	
	public Grid(int row, int column){
		if (row<0 || column<0)
			throw new Error("Grid size cannot be negative");
		this.capacity = row*column;
		this.column = row;
		this.row = column;
		this.max = 0;
		this.targetTileVal = Grid.TARGET_TILE_VAL;
		this.randomTileIndex = Grid.INVALID_RANDOM_INDEX;
		this.occupied_index = new SparseIntArray(this.capacity){
			public int get(int key){
				return this.get(key,CELL_EMPTY);
			}
		};
		this.should_move_index = new SparseIntArray(this.capacity){
			public int get(int key){
				return this.get(key,CELL_EMPTY);
			}
		};
		this.should_move_val = new SparseIntArray(this.capacity){
			public int get(int key){
				return this.get(key,CELL_EMPTY);
			}
		};
	}
	
	protected void fillCell(int row,int column,int val){
		if (this.column<1 && this.row<1){
			throw new Error("Initialize error");
		}
		else if (row<0 || row>this.row || column<0 || column>this.column){
			throw new Error("Function argument error");

		}
		else if (val<0){
			throw new Error("Function argument error");
		}
		else {
			 fillCell((row-1)*this.column+column-1,val);
		}
	}
	
	
	protected void fillCell(int index,int value){
		if (index>this.capacity)throw new Error("Function argument error");
		else {
			this.occupied_index.put(index,value);
		}
	}
	
	protected boolean eraseCell(int row,int column,int val){
		if (this.column<1 && this.row<1){
			throw new Error("Initialize error");
		}
		else if (row<0 || row>this.row || column<0 || column>this.column){
			throw new Error("Function argument error");

		}
		else if (val<0){
			throw new Error("Function argument error");
		}
		else {
			return eraseCell((row-1)*this.column+column-1);
		}
	}
	
	protected boolean eraseCell(int index){
		if (index>this.capacity)return false;
		else {
			this.occupied_index.delete(index);
			return true;
		}
	}
	
	protected void replaceCell(int fromCell,int toCell,int val){
		should_move_val.put(fromCell, occupied_index.get(fromCell));
		fillCell(toCell,val);
		eraseCell(fromCell);
		should_move_index.put(fromCell,toCell);
		
	}
	
	protected void empty(){
		this.occupied_index.clear();
		this.should_move_index.clear();
		this.should_move_val.clear();
		this.randomTileIndex = Grid.INVALID_RANDOM_INDEX;
		this.max = 0;
	}
	
	protected boolean randomAvailCell(int val){
		if (this.occupied_index.size()>this.capacity){
			return false;
		}
		Random r = new Random();
		int index = r.nextInt(capacity);
		
		if (!isOccupied(index)){
			this.occupied_index.put(index, val);
			this.randomTileIndex = index;
			return true;
		}
		
		for (int i=1;i<capacity;i++){
			if (index-i>=0){
				if (!isOccupied(index-i)){
					this.occupied_index.put(index-i, val);
					this.randomTileIndex = index-i;
					return true;
				}
			}
			if (index+i<capacity){
				if (!isOccupied(index+i)){
					this.occupied_index.put(index+i, val);
					this.randomTileIndex = index+i;
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isOccupied(int index){
		int key_val = this.occupied_index.get(index);
		if (key_val!= CELL_EMPTY){
			return true;
		}
		return false;
	}
	
	public int getCellVal(int row, int column){
		if (this.column<1 && this.row<1){
			throw new Error("Initialize Error");
		}
		else if (row<0 || row>this.row || column<0 || column>this.column){
			throw new Error("Function argument error");
		}
		
		int index = (row-1)*this.column+column-1;
		return getCellVal(index);
	}
	
	public int getCellVal(int index){
		if (index >= this.capacity)return CELL_EMPTY;
		
		int ret = occupied_index.get(index,CELL_EMPTY);
		
		if (ret == CELL_EMPTY)return CELL_EMPTY;
		return ret;
	}
	
	public boolean isFull(){
		return this.occupied_index.size()>=this.capacity?true:false;
	}
	
	public int merge(int direction){
        this.randomTileIndex = Grid.INVALID_RANDOM_INDEX;
        this.should_move_index.clear();
        this.should_move_val.clear();
        
		if (max >= this.targetTileVal)return MERGE_NO_OP;
		int mergedScore = 0;
		if (this.occupied_index.size()==0)
			return MERGE_NO_OP;
		boolean moved = false;
		switch (direction){
		case MERGE_DIRECTION_LEFT:
			for (int i=0;i<this.row;i++){
				if (this.column == 1)break;
				int j = 0;
				
				int offset = i*this.column;
				int curr_index = offset;
				
				while (j<this.column){
					
					if (isOccupied(offset+j)){
						if (curr_index < offset+j){
							
							if (isOccupied(curr_index)){
								int curr_index_val = getCellVal(curr_index);
								int j_index_val = getCellVal(offset+j);
								if (curr_index_val == j_index_val){
									replaceCell(offset+j,curr_index,curr_index_val*2);			
									moved = true;
									mergedScore+=curr_index_val*2;
									if (curr_index_val*2>max)max = curr_index_val*2;
								} else {
									if (curr_index+1 != offset+j){
										replaceCell(offset+j,curr_index+1,j_index_val);					
										moved = true;
									}
									if (j_index_val>max)max = j_index_val;
									if (curr_index_val>max)max = curr_index_val;
								}
								curr_index++;
							}
							else {
								int curr_index_val = getCellVal(offset+j);
								if (curr_index_val>max) {
									max = curr_index_val;
								}
								replaceCell(offset+j,curr_index,curr_index_val);
							    moved = true;
							}
						}
					}
					j++;
				}
			}
			break;
		case MERGE_DIRECTION_RIGHT:
			for (int i=0;i<this.row;i++){
				if (this.column == 1)break;
				int j = this.column - 1;
				
				int offset = i*this.column;
				int curr_index = offset + this.column - 1;
				
				while (j>=0){
					
					if (isOccupied(offset+j)){
						if (curr_index > offset+j){
							
							if (isOccupied(curr_index)){
								int curr_index_val = getCellVal(curr_index);
								int j_index_val = getCellVal(offset+j);
								if (curr_index_val == j_index_val){
									replaceCell(offset+j,curr_index,curr_index_val*2);
									moved = true;
									mergedScore+=curr_index_val*2;
									if (curr_index_val*2>max)max = curr_index_val*2;
								} else {
									if (curr_index-1 != offset+j){
										replaceCell(offset+j,curr_index-1,j_index_val);
										moved = true;
									}
									if (j_index_val>max)max = j_index_val;
									if (curr_index_val>max)max = curr_index_val;
								}
								curr_index--;
							}
							else {
								int curr_index_val = getCellVal(offset+j);
								if (curr_index_val>max) {
									max = curr_index_val;
								}
								replaceCell(offset+j,curr_index,curr_index_val);
							    moved = true;
							}
						}
					}
					j--;
				}
			}
			break;
		case MERGE_DIRECTION_BOTTOM:
			for (int i=0;i<this.column;i++){
				if (this.row == 1)break;
				int j = this.column * (this.row-1)+i;
				int curr_index = j;
				
				while (j>=i){
					
					if (isOccupied(j)){
						if (curr_index > j){
							
							if (isOccupied(curr_index)){
								int curr_index_val = getCellVal(curr_index);
								int j_index_val = getCellVal(j);
								if (curr_index_val == j_index_val){
									replaceCell(j,curr_index,curr_index_val*2);
									moved = true;
									mergedScore+=curr_index_val*2;
									if (curr_index_val*2>max)max = curr_index_val*2;
								} else {
									if (curr_index-this.column != j){
										replaceCell(j,curr_index-this.column,j_index_val);
										moved = true;
									}
									if (j_index_val>max)max = j_index_val;
									if (curr_index_val>max)max = curr_index_val;
								}
								curr_index-=this.column;
							}
							else {
								int curr_index_val = getCellVal(j);
								if (curr_index_val>max) {
									max = curr_index_val;
								}
								replaceCell(j,curr_index,curr_index_val);
							    moved = true;
							}
						}
					}
					j-=this.column;
				}
			}
			break;
		case MERGE_DIRECTION_TOP:
			for (int i=0;i<this.column;i++){
				if (this.row == 1)break;
				int j = i;
				int curr_index = j;
				
				while (j<=this.column * (this.row-1)+i){
					
					if (isOccupied(j)){
						if (curr_index < j){
							
							if (isOccupied(curr_index)){
								int curr_index_val = getCellVal(curr_index);
								int j_index_val = getCellVal(j);
								if (curr_index_val == j_index_val){
									replaceCell(j,curr_index,curr_index_val*2);
									moved = true;
									mergedScore+=curr_index_val*2;
									if (curr_index_val*2>max)max = curr_index_val*2;
								} else {
									if (curr_index+this.column != j){
										replaceCell(j,curr_index+this.column,j_index_val);
										moved = true;
									}
									if (j_index_val>max)max = j_index_val;
									if (curr_index_val>max)max = curr_index_val;
								}
								curr_index+=this.column;
							}
							else {
								int curr_index_val = getCellVal(j);
								if (curr_index_val>max) {
									max = curr_index_val;
								}
								replaceCell(j,curr_index,curr_index_val);
							    moved = true;
							}
						}
					}
					j+=this.column;
				}
			}
			break;	
			default:return MERGE_FAIL;
		}
		
		if (max==MERGE_INVALID_MAX)return MERGE_INVALID_MAX;
		if (moved){
			Random rand = new Random();
			int randVal = rand.nextInt(100);
			boolean success = false;
			if (randVal<Grid.RANDOM_MIN_TILE_PROBABILITY){
				success = randomAvailCell(2);
			}
			else {
				success = randomAvailCell(4);
			}
			 
			if (!success){
				throw new Error("Generate random tile error");
			}
			return mergedScore;
		}
		return Grid.MERGE_NO_OP;
	}
	
	public int checkGameState(){
		if (max>=targetTileVal)return Grid.GAME_WIN;
		if (this.occupied_index.size()<capacity)return Grid.GAME_ON;
		for (int i=0;i<capacity;i++){
			int currTileVal = occupied_index.get(i);
			int nextTileVal = 
					((i+1)%this.column==0)?
					CELL_EMPTY:occupied_index.get(i+1);
			int downTileVal = occupied_index.get(i+this.column);
			if (nextTileVal == currTileVal 
					|| downTileVal == currTileVal)
				return Grid.GAME_ON;
		}
		return Grid.GAME_OVER;
	}
	
	
	public int getMax(){
		return this.max;
	}
	
	public int getTargetVal(){
		return this.targetTileVal;
	}
	
	public void setTargetVal(int val){
		this.targetTileVal = val;
	}
	
	public SparseIntArray getShouldMoveIndex(){
		return this.should_move_index;
	}
	
	public SparseIntArray getShouldMoveVal(){
		return this.should_move_val;
	}
	
	public int getCurrentRandomTileIndex(){
		return this.randomTileIndex;
	}
	
	public int columnCount(){
		return this.column;
	}
	
	public int rowCount(){
		return this.row;
	}
	
}
