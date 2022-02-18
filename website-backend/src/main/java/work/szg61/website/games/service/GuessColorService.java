package work.szg61.website.games.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class GuessColorService {
    public JSONObject newGame() {
        return new JSONObject();
    }
}
