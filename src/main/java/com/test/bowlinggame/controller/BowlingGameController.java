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
		}
		
	    return new ResponseEntity<GameOutput>(gameoutput, HttpStatus.OK);
	}
	

}
