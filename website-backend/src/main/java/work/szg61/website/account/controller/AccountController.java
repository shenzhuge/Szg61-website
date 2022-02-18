package work.szg61.website.account.controller;

import com.alibaba.fastjson.JSONObject;
import work.szg61.website.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 注册请求
     *
     * @param info     请求体，包含注册信息
     */
    @PostMapping("/account/register")
    public JSONObject register(@RequestBody JSONObject info) {
        String username = info.getString("username");
        String password = info.getString("password");

        return accountService.register(username, password);
    }

    /**
     * 登录请求
     *
     * @param info     请求体，包含登录信息
     */
    @PostMapping("/account/login")
    public JSONObject login(@RequestBody JSONObject info) {
        String username = info.getString("username");
        String password = info.getString("password");

        return accountService.login(username, password);
    }

    /**
     * 自动登录，获取cookie并登录
     *
     * @param userId 从浏览器获取的cookie中的用户id
     */
    @PostMapping("/account/autoLogin")
    public JSONObject autoLogin(@CookieValue(name = "userId", defaultValue = "unknown") String userId) {
        if (userId.equals("unknown")) {
            JSONObject re = new JSONObject();
            re.put("status", "no cookie");
            return re;
        }
        return accountService.autoLogin(userId);
    }

    /**
     * 注销账号
     *
     * @param info 客户端信息，包含一个id
     * @return 直接返回字符串，ok为成功，否则为错误信息
     */
    @PostMapping("/account/destroy")
    public String destroyAccount(@RequestBody JSONObject info) {
        if (!accountService.destroyAccount(info.getString("id"))) return "注销失败，账号不存在";

        return "ok";
    }

}
