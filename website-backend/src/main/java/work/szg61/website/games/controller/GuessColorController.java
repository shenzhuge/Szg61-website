package work.szg61.website.games.controller;

import com.alibaba.fastjson.JSONObject;
import work.szg61.website.games.service.GuessColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuessColorController {
    private final GuessColorService guessColorService;

    @Autowired
    public GuessColorController(GuessColorService guessColorService) {
        this.guessColorService = guessColorService;
    }

    @PostMapping("/games/guessColor/newGame")
    public JSONObject newGame() {
        JSONObject re = new JSONObject();
        return re;
    }
}
