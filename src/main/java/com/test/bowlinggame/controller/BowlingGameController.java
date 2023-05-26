package com.test.bowlinggame.controller;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.bowlinggame.entity.Frame;
import com.test.bowlinggame.entity.Game;
import com.test.bowlinggame.entity.GameOutput;

/**
 * 
 * @author Vanshdeep
 *
 */

@RestController
public class BowlingGameController {
	
	@PostMapping("/getbowlingscore")
	public ResponseEntity<GameOutput> getRollingBall(@RequestBody Game game)
	{
		GameOutput gameoutput = new GameOutput();
		ArrayList<Frame> frameList = new ArrayList<Frame>();
		
		if (game != null) {
			String gameString = game.getBowlingGame();
			
			String[] frameParts = gameString.split("-");
			if (frameParts.length != 10) {
				 gameoutput.setCode(500);
				 gameoutput.setMessage("Must have 10 frames in complete game");
				 return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
			}
			
			char spare = '/';
    		char lowerStrike = 'x';
    		for (int i = 0; i < frameParts.length; i++) {
    			//validation for last frame
    			String frame = frameParts[i];
    			Integer frameNumber = i + 1;
    			
    			if (i == 9){
    				//if 10th frame used two throws
    				if (frame.length() == 2){
    					
    					char firstCharofFrame = Character.toLowerCase(frame.charAt(0)); 
    					char secondCharofFrame = Character.toLowerCase(frame.charAt(1));     					
    					//first or second char cant be x or \
    					//validate the first throw in a frame cant be spare
    					if (firstCharofFrame == spare || firstCharofFrame == lowerStrike || secondCharofFrame == lowerStrike || secondCharofFrame == spare){
	    		    		gameoutput.setCode(500);
	    		    		gameoutput.setMessage("10th frame with two throws cannot be a spare or strike. Frame #: " + frameNumber);
	    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
    					}
    					// if 10th frame is open sum of throws must be less than 10
    					int firstCharInt = Character.getNumericValue(firstCharofFrame);
    					int secondCharInt = Character.getNumericValue(secondCharofFrame);
    					int sum = firstCharInt + secondCharInt;
    					if (sum >= 10 || sum < 0){
	    		    		gameoutput.setCode(500);
	    		    		gameoutput.setMessage("Open 10th frame sum is greater than 10 or less than 0 for the first and second throw. Frame #: " + frameNumber);
	    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
    					}
    					
    				   //if 10th frame used three throws
    				} else if (frame.length() == 3){
    					char firstCharofFrame = Character.toLowerCase(frame.charAt(0)); 
    					char secondCharofFrame = Character.toLowerCase(frame.charAt(1)); 
    					char thirdCharofFrame = Character.toLowerCase(frame.charAt(2)); 
    					//first char cannot be a spare
    					if (firstCharofFrame == spare){
	    		    		gameoutput.setCode(500);
	    		    		gameoutput.setMessage("10th frame with three throws cannot have spare as first throw. Frame #: " + frameNumber);
	    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
    					}
    		
    					Boolean isFirstCharNumber = Character.isDigit(firstCharofFrame);
    					
    					//if first char is a number second char must be a spare
    					if (isFirstCharNumber && secondCharofFrame != '/' ){
	    		    		gameoutput.setCode(500);
	    		    		gameoutput.setMessage("10th frame with three throws with first throw open must have a spare for the second throw. Frame #: " + frameNumber);
	    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
    					}
    					
    					//if strike middle char cant be spare
    					if (firstCharofFrame == 'x' && secondCharofFrame == '/' ){
	    		    		gameoutput.setCode(500);
	    		    		gameoutput.setMessage("10th frame with three throws with first throw strike cannot have a spare for the second throw. Frame #: " + frameNumber);
	    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
    					}
    					
    					//2nd throw and third throw cannot both be spares
    					if (secondCharofFrame == '/' && thirdCharofFrame == '/' ){
	    		    		gameoutput.setCode(500);
	    		    		gameoutput.setMessage("10th frame with three throws cannot have second and third throws as spares. Frame #: " + frameNumber);
	    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
    					}
    					
						if (secondCharofFrame == 'x' && thirdCharofFrame == '/'){
	    		    		gameoutput.setCode(500);
	    		    		gameoutput.setMessage("10th frame with three throws cannot have second throw strike and third throw a spare. Frame #: " + frameNumber);
	    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
						}
    					
    					int secondCharInt = Character.getNumericValue(secondCharofFrame);
    					int thirdCharInt = Character.getNumericValue(thirdCharofFrame);
    					//if the second and third throw are numbers they cannot be greater than 10 or less than 0
    					if (secondCharInt != -1 && thirdCharInt != -1){
    						if (secondCharofFrame != 'x' && thirdCharofFrame != 'x'){
		    					int sum = secondCharInt + thirdCharInt;
		    					if (sum >= 10 || sum < 0){
			    		    		gameoutput.setCode(500);
			    		    		gameoutput.setMessage("Open 10th frame sum is greater than 10 or less than 0 for the second and third throw. Frame #: " + frameNumber);
			    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
		    					}
    						}
    					}
    					//third char can be spare
    					//middle and last char can be strike or number
    					
    			    //if 10th frame didnt use two or three throws we have an error
    				}else{
    		    		gameoutput.setCode(500);
    		    		gameoutput.setMessage("10th frame must have 2 or 3 rolls. Frame #: " + frameNumber);
    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
    				}
    			// if not the 10th frame	
    			}else{
    				if (frame.length() == 1){
    					//validate if there is one char in a frame that it is a strike
    					if (!frame.equalsIgnoreCase("x")){
	    		    		gameoutput.setCode(500);
	    		    		gameoutput.setMessage("One shot thrown and wasnt marked as a strike. Frame #: " + frameNumber);
	    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
    					}
    				} else if (frame.length() == 2){
    					
    					if (frame.toLowerCase().contains("x")){
    						
	    		    		gameoutput.setCode(500);
	    		    		gameoutput.setMessage("Cannot have two throws and contain a strike. Frame #: " + frameNumber);
	    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
    					}
    					char firstCharofFrame = frame.charAt(0);
    					char secondCharofFrame = frame.charAt(1);
    					
    					//validate the first throw in a frame cant be spare
    					if (firstCharofFrame == spare){
	    		    		gameoutput.setCode(500);
	    		    		gameoutput.setMessage("First throw of frame cannot be a spare. Frame #: " + frameNumber);
	    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
    					}
    					
    					if(secondCharofFrame != spare){
	    					int firstCharInt = Character.getNumericValue(firstCharofFrame);
	    					int secondCharInt = Character.getNumericValue(secondCharofFrame);
	    					int sum = firstCharInt + secondCharInt;
	    					if (sum >= 10 || sum < 0){
		    		    		gameoutput.setCode(500);
		    		    		gameoutput.setMessage("Open frame sum is greater than 10 or less than 0. Frame #: " + frameNumber);
		    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
	    					}
   
    					}
    				}
    				else{
    					//validate that any frame that is not a strike and also not in 10th frame should contain two throws
    		    		gameoutput.setCode(500);
    		    		gameoutput.setMessage("Bowling frame must contain two throws if not a strike. Frame #: " + frameNumber);
    		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
    				}
    			}
    			//adding frame to framelist for processing
    			Integer frameCounter = i + 1;
    			Frame processFrame = new Frame();
    			processFrame.setFrameNumber(frameCounter);
    			processFrame.setFrameString(frame.toLowerCase());
    			frameList.add(processFrame);    			
    		}

    		Integer totalScore = calculateTotalScore(frameList);
    		int[] frameScores = new int[10];
    		
    		for (int i = 0; i < frameList.size(); i++) {
    			Frame currentBowlingframe = frameList.get(i);
    			totalScore = totalScore + currentBowlingframe.getFrameScore();
    			currentBowlingframe.setFrameRunningTotal(totalScore);
    			frameScores[i] = totalScore;
    			
    		}
    		gameoutput.setFrameScores(frameScores);
    		gameoutput.setScore(totalScore);
    		gameoutput.setCode(200);
    		gameoutput.setMessage("Success");
    		
    	}else {
    		gameoutput.setCode(500);
    		gameoutput.setMessage("Bowling game string does not exist in request");
    		System.out.println("code 500 ");
    	}
	    
		  return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.OK);
      }
	
	
	public static int calculateTotalScore(ArrayList<Frame> frameList) {
		   int totalScore = 0;
		   
			for (int i = 0; i < frameList.size(); i++) {
			   Frame currentBowlingframe = frameList.get(i);
			   String currentFrameString = currentBowlingframe.getFrameString();
			   char firstThrowOfCurrentFrame = currentFrameString.charAt(0);
			   //if the last or second to last frame
			   if (i == 9 || i == 8){
				   if (i == 8){
					   //if first throw of 9th frame is a strike
					   if (firstThrowOfCurrentFrame == 'x'){
						   Frame tenthBowlingFrame = frameList.get(i+1);
						   String tenthBowlingFrameString = tenthBowlingFrame.getFrameString();
						   char firstThrowOfTenthFrame = tenthBowlingFrameString.charAt(0);
						   char secondThrowOnTenthFrame = tenthBowlingFrameString.charAt(1);

						   //if first throw of 10th frame is a strike
						   if (firstThrowOfTenthFrame == 'x'){	  
							   //if the second throw of 10th frame is a strike, we got a turkey
							   if (secondThrowOnTenthFrame == 'x'){
								   currentBowlingframe.setFrameScore(30);
							   //if the first throw on the 9th frame and first throw on 10th frame were strikes but the second throw on the tenth frame was not
							   }else{
								   int secondThrowOnTenthFrameInteger = Character.getNumericValue(secondThrowOnTenthFrame);
								   int sum = 20 + secondThrowOnTenthFrameInteger;
								   currentBowlingframe.setFrameScore(sum);
							   }
						   
						   // if the first throw on the 9th frame was a strike but the first throw 10th frame was not a strike  
						   }else{
							   int secondThrowOfTenthFrameInteger = Character.getNumericValue(secondThrowOnTenthFrame);
							   if (secondThrowOfTenthFrameInteger == -1){
								   //second throw of 10 frame is spare, set to 20
								   currentBowlingframe.setFrameScore(20);
								   
							   }else{
								   //second throw of 10 frame is open add the value of the first throw on the tenth frame and 10 for first strike bonus
								   int firstThrowOfTenthFrameInteger = Character.getNumericValue(firstThrowOfTenthFrame);
								   int sum = firstThrowOfTenthFrameInteger + secondThrowOfTenthFrameInteger + 10;
								   currentBowlingframe.setFrameScore(sum);
							   } 
						   }
					   //else if first throw of 9th frame is not strike
					   }else{
						   char secondThrowOfCurrentFrame = currentFrameString.charAt(1);  
						   //handle spare on second throw
						   if (secondThrowOfCurrentFrame == '/' ){
							   Frame nextBowlingFrame = frameList.get(i+1);
							   String nextFrameString = nextBowlingFrame.getFrameString();
							   char firstThrowOfNextFrame = nextFrameString.charAt(0);
							   //if the first throw of the next frame is a strike, current score is 20
							   if (firstThrowOfNextFrame == 'x'){
								   currentBowlingframe.setFrameScore(20);
							   //if the first throw of next frame is not a strike add 10 for the spare and sum in the open value
							   }else{
								   int firstThrowOfNextFrameInteger = Character.getNumericValue(firstThrowOfNextFrame);
								   int sum = 10 + firstThrowOfNextFrameInteger;
								   currentBowlingframe.setFrameScore(sum);
							   }
						   }else{
							   int firstThrowOfCurrentFrameInteger = Character.getNumericValue(firstThrowOfCurrentFrame);
							   int secondThrowOfCurrentFrameInteger = Character.getNumericValue(secondThrowOfCurrentFrame);
							   int sum = firstThrowOfCurrentFrameInteger + secondThrowOfCurrentFrameInteger;
							   currentBowlingframe.setFrameScore(sum);
							   }
					   }
					   
				   //else if 10th frame
				   }else{
					   // if the first throw of the tenth frame is strike
					   if (firstThrowOfCurrentFrame == 'x'){
						   char secondThrowOnTenthFrame = currentFrameString.charAt(1);
						   char thirdThrowOnTenthFrame = currentFrameString.charAt(2);
						   int thirdThrowOnTenthFrameInteger = Character.getNumericValue(thirdThrowOnTenthFrame);
						   int secondThrowOnTenthFrameInteger = Character.getNumericValue(secondThrowOnTenthFrame);
						   
						   //if the second throw of 10th frame is a strike,
						   if (secondThrowOnTenthFrame == 'x'){
							   //if third throw of the tenth from is a strike, turkey material
							   if (thirdThrowOnTenthFrame == 'x'){
								   currentBowlingframe.setFrameScore(30);
							   //if the third throw of the tenth frame is not strike, must be numeric add 20 to it
							   }else{   
								   int sum = 20 + thirdThrowOnTenthFrameInteger;
								   currentBowlingframe.setFrameScore(sum);
							   }
						   //if the second throw on the 10th frame is not a strike, it must be a numeric, check for spare or numeric in third throw
						   }else{
							   //third throw of 10 frame is spare, set to 20
							   if (thirdThrowOnTenthFrameInteger == -1){
								   currentBowlingframe.setFrameScore(20);
								   
							   //third throw of 10 frame is open add the value of the first throw on the tenth frame and 10 for first strike bonus  
							   }else{
								   int sum = thirdThrowOnTenthFrameInteger + secondThrowOnTenthFrameInteger + 10;
								   currentBowlingframe.setFrameScore(sum);
							   } 
						   }
					   //if the first throw on the tenth frame is not a strike	   
					   }else{
						   char secondThrowOfCurrentFrame = currentFrameString.charAt(1);  
						   //handle spare on second throw
						   if (secondThrowOfCurrentFrame == '/' ){
							   char thirdThrowOfTenthFrame = currentFrameString.charAt(2);
							   //if the first throw of the next frame is a strike, current score is 20
							   if (thirdThrowOfTenthFrame == 'x'){
								   currentBowlingframe.setFrameScore(20);
							   //if the first throw of next frame is not a strike add 10 for the spare and sum in the open value
							   }else{
								   int thirdThrowOfNexFrameInteger = Character.getNumericValue(thirdThrowOfTenthFrame);
								   int sum = 10 + thirdThrowOfNexFrameInteger;
								   currentBowlingframe.setFrameScore(sum);
							   }
						   //handle open case
						   }else{
								   int firstThrowOfCurrentFrameInteger = Character.getNumericValue(firstThrowOfCurrentFrame);
								   int secondThrowOfCurrentFrameInteger = Character.getNumericValue(secondThrowOfCurrentFrame);
								   int sum = firstThrowOfCurrentFrameInteger + secondThrowOfCurrentFrameInteger;
								   currentBowlingframe.setFrameScore(sum);
							   }
					   }
				   }
			   }
			   
			   //if not 10th or 9th frame
			   else{
				   //handle strike case for current frame
				   if (firstThrowOfCurrentFrame == 'x'){
					   Frame nextBowlingFrame = frameList.get(i+1);
					   String nextFrameString = nextBowlingFrame.getFrameString();
					   char firstThrowOfNextFrame = nextFrameString.charAt(0);
					   
					   //handle strike case for next frame, indicating two strikes in a row
					   if (firstThrowOfNextFrame == 'x'){
						   Frame lastBowlingFrame = frameList.get(i+2);
						   String lastFrameString = lastBowlingFrame.getFrameString();
						   char firstThrowOfLastFrame = lastFrameString.charAt(0);
						   
						   //handle three strikes in a row, a turkey has been hit
						   if (firstThrowOfLastFrame == 'x'){ 
							   currentBowlingframe.setFrameScore(30);
							   
						   //if the first and second throw were a strike but the third one wasnt
						   }else{
							   int firstThrowOfLastFrameInteger = Character.getNumericValue(firstThrowOfLastFrame);
							   int sum = 20 + firstThrowOfLastFrameInteger;
							   currentBowlingframe.setFrameScore(sum);
						   }
					   //if the first throw was a strike but the second one was not
					   }else{
						   char secondThrowOfNextFrame = nextFrameString.charAt(1);
						   int secondThrowOfNextFrameInteger = Character.getNumericValue(secondThrowOfNextFrame);
						   if (secondThrowOfNextFrameInteger == -1){
							   //second throw of next frame is spare
							   currentBowlingframe.setFrameScore(20);
							   
						   }else{
							   //second throw of next frame is open add the value of the first throw on the next frame 10 for first strike and setscore
							   int firstThrowOfNextFrameInteger = Character.getNumericValue(firstThrowOfNextFrame);
							   int sum = secondThrowOfNextFrameInteger + firstThrowOfNextFrameInteger + 10;
							   currentBowlingframe.setFrameScore(sum);
						   } 
					   }
					   //if the first throw was not a strike
				   }else{
					   char secondThrowOfCurrentFrame = currentFrameString.charAt(1);  
					   //handle spare on second throw
					   if (secondThrowOfCurrentFrame == '/' ){
						   Frame nextBowlingFrame = frameList.get(i+1);
						   String nextFrameString = nextBowlingFrame.getFrameString();
						   char firstThrowOfNextFrame = nextFrameString.charAt(0);
						   //if the first throw of the next frame is a strike, current score is 20
						   if (firstThrowOfNextFrame == 'x'){
							   currentBowlingframe.setFrameScore(20);
						   //if the first throw of next frame is not a strike add 10 for the spare and sum in the open value
						   }else{
							   int firstThrowOfNextFrameInteger = Character.getNumericValue(firstThrowOfNextFrame);
							   int sum = 10 + firstThrowOfNextFrameInteger;
							   currentBowlingframe.setFrameScore(sum);
						   }
					   //handle open case
					   }else{
						   int firstThrowOfCurrentFrameInteger = Character.getNumericValue(firstThrowOfCurrentFrame);
						   int secondThrowOfCurrentFrameInteger = Character.getNumericValue(secondThrowOfCurrentFrame);
						   int sum = firstThrowOfCurrentFrameInteger + secondThrowOfCurrentFrameInteger;
						   currentBowlingframe.setFrameScore(sum);
						   }
				   }
			   }	   
			}
		   return totalScore; 
		}
  }
