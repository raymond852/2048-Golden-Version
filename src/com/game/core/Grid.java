package com.game.core;


import java.util.Random;

import android.util.SparseIntArray;


public class Grid {
	
	private int capacity;
	private int column;
	private int row;
	private int max;
	private int randomTileIndex;
	
	private SparseIntArray occupied_index;
	
	
	
	public static final int CELL_EMPTY = 0;
	public static final int INVALID_RANDOM_INDEX=-1;
	
	public static final int MERGE_DIRECTION_LEFT = 0;
	public static final int MERGE_DIRECTION_RIGHT = 1;
	public static final int MERGE_DIRECTION_TOP = 2;
	public static final int MERGE_DIRECTION_BOTTOM = 3;
	
	public static final int MERGE_INVALID_MAX = -2;
	public static final int MERGE_FAIL = -3;
	public static final int MERGE_NO_OP = -1;
	public static final int TARGET_TILE_VAL = 2048;
	
	public Grid (int size, int target_val){
		if (size<0)
			throw new Error("Grid size cannot be negative");
		this.capacity = size*size;
		this.column = size;
		this.row = size;
		this.max = 0;
		this.randomTileIndex = Grid.INVALID_RANDOM_INDEX;
		this.occupied_index = new SparseIntArray(this.capacity){
			public int get(int key){
				return this.get(key,CELL_EMPTY);
			}
		};
	}
	
	public void fillCell(int row,int column,int val){
		if (this.column<1 && this.row<1){
			throw new Error("error0");
		}
		else if (row<0 || row>this.row || column<0 || column>this.column){
			throw new Error("err1");

		}
		else if (val<0){
			throw new Error("err2");
		}
		else {
			 fillCell((row-1)*this.column+column-1,val);
		}
	}
	
	public void fillCell(int index,int value){
		if (index>this.capacity)throw new Error("err1");
		else {
			this.occupied_index.put(index,value);
		}
	}
	
	public boolean eraseCell(int row,int column,int val){
		if (this.column<1 && this.row<1){
			throw new Error("error0");
		}
		else if (row<0 || row>this.row || column<0 || column>this.column){
			throw new Error("err1");

		}
		else if (val<0){
			throw new Error("err2");
		}
		else {
			return eraseCell((row-1)*this.column+column-1);
		}
	}
	
	public boolean eraseCell(int index){
		if (index>this.capacity)return false;
		else {
			this.occupied_index.delete(index);
			return true;
		}
	}
	
	public void empty(){
		this.occupied_index.clear();
	}
	
	public boolean randomAvailCell(int val){
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
			throw new Error("error0");
		}
		else if (row<0 || row>this.row || column<0 || column>this.column){
			throw new Error("err1");
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
		if (max >= TARGET_TILE_VAL)return MERGE_NO_OP;
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
									fillCell(curr_index,curr_index_val*2);
									eraseCell(offset+j);
									moved = true;
									mergedScore+=curr_index_val*2;
									if (curr_index_val*2>max)max = curr_index_val*2;
								} else {
									if (curr_index+1 != offset+j){
										fillCell(curr_index+1,j_index_val);
										eraseCell(offset+j);
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
							    fillCell(curr_index,curr_index_val);
							    eraseCell(offset+j);
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
									fillCell(curr_index,curr_index_val*2);
									eraseCell(offset+j);
									moved = true;
									mergedScore+=curr_index_val*2;
									if (curr_index_val*2>max)max = curr_index_val*2;
								} else {
									if (curr_index-1 != offset+j){
										fillCell(curr_index-1,j_index_val);
										eraseCell(offset+j);
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
							    fillCell(curr_index,curr_index_val);
							    eraseCell(offset+j);
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
									fillCell(curr_index,curr_index_val*2);
									eraseCell(j);
									moved = true;
									mergedScore+=curr_index_val*2;
									if (curr_index_val*2>max)max = curr_index_val*2;
								} else {
									if (curr_index-this.column != j){
										fillCell(curr_index-this.column,j_index_val);
										eraseCell(j);
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
							    fillCell(curr_index,curr_index_val);
							    eraseCell(j);
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
									fillCell(curr_index,curr_index_val*2);
									eraseCell(j);
									moved = true;
									mergedScore+=curr_index_val*2;
									if (curr_index_val*2>max)max = curr_index_val*2;
								} else {
									if (curr_index+this.column != j){
										fillCell(curr_index+this.column,j_index_val);
										eraseCell(j);
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
							    fillCell(curr_index,curr_index_val);
							    eraseCell(j);
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
			boolean success = randomAvailCell(2);
			if (!success){
				throw new Error("error0");
			}
			return mergedScore;
		}
		return Grid.MERGE_NO_OP;
	}
	
	public SparseIntArray getAllCells(){
		return this.occupied_index;
	}
	
	public int getMax(){
		return this.max;
	}
	
	public int getCurrentRandomTileIndex(){
		return this.randomTileIndex;
	}
	
}
