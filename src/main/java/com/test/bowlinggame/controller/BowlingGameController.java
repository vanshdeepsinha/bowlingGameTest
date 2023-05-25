package com.test.bowlinggame.controller;

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
	public void getRollingBall(@RequestBody Game game)
	{
		//GameOutput gameoutput = new GameOutput();
		String gameString = game.getBowlingGame();
		System.out.println(">>>>>>>>>>>>>>>>>"+gameString+"<<<<<<<<<<<<<<<<<<<");
	}
	

}
