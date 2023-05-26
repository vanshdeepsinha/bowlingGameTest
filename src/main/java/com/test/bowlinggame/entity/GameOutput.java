package com.test.bowlinggame.entity;

public class GameOutput {
	
	 private Integer score;
	 private Integer code;
	 private String message;
	 int[] frameScores = new int[10];
	 
	 
	 public Integer getScore() {
			return score;
	 }
	 
	 public void setScore(Integer score) {
			this.score = score;
	 }
	 
	 public Integer getCode() {
			return code;
	  }
	 
	 public void setCode(Integer code) {
			this.code = code;
	  }
	 
	 public String getMessage() {
			return message;
	  }
	 
	 public void setMessage(String message) {
		    this.message = message;
	  }
	 	 
	 public int[] getFrameScores() {
			return frameScores;
	  }
	 
	 public void setFrameScores(int[] frameScores) {
			this.frameScores = frameScores;
	  }	 
}
