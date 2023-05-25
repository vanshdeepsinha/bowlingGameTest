package com.test.bowlinggame.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.bowlinggame.entity.Game;
import com.test.bowlinggame.entity.GameOutput;

/**
 * 
 * @author Vanshdeep
 *
 */

@RestController
public class BowlingGameController {
	
	@RequestMapping(value="/getbowlingscore", method = RequestMethod.POST)
	public ResponseEntity<GameOutput> getRollingBall(@RequestBody Game game)
	{
		GameOutput gameoutput = new GameOutput();
		
		if (game != null) {
			String gameString = game.getBowlingGame();
			
			String[] frameParts = gameString.split("-");
			if (frameParts.length != 10) {
				 gameoutput.setCode(500);
				 gameoutput.setMessage("Must have 10 frames in complete game");
				 return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
			}
			
			char spare = '/';
			for (int i = 0; i < frameParts.length; i++) {
    			String frame = frameParts[i];
    			Integer frameNumber = i + 1;
    			
    			if (frame.length() == 1){
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
				} else{
					//validate that any frame that is not a strike and not frame 10 should contain two throws
		    		gameoutput.setCode(500);
		    		gameoutput.setMessage("Bowling frame must contain two throws if not a strike. Frame #: " + frameNumber);
		    		return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.CONFLICT);
				  } 		
		     }
	   }
		  return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.OK);
    }
  }
