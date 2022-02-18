package work.szg61.website.account.service;

import com.alibaba.fastjson.JSONObject;
import work.szg61.website.account.entity.AccountEntity;
import work.szg61.website.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    private final Random random = new Random();
    private final String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * 注册
     *
     * @return 注册结果，status为ok即为注册成功，同时包含账号信息。否则status中为错误信息
     */
    public JSONObject register(String username, String password) {
        JSONObject re = new JSONObject();
        if (!judgeUserInfo(username, password)) {
            re.put("status", "用户信息不合法");
            return re;
        }
        if (accountRepository.findByUsername(username).isPresent()) {
            re.put("status", "用户名已存在");
            return re;
        }

        AccountEntity newEntity = new AccountEntity();
        newEntity.setId(getNewId());
        newEntity.setUsername(username);
        newEntity.setPassword(password);
        long time = new Date().getTime();
        newEntity.setRegisterTime(time);
        newEntity.setLastLogin(time);

        try {
            newEntity = accountRepository.save(newEntity);
            re.put("status", "ok");
            putAccountInfoMap(re, newEntity);
        } catch (Exception e) {
            e.printStackTrace();
            re.put("status", "注册失败");
        }

        return re;
    }

    /**
     * 登录
     *
     * @return 登录结果，status为ok即为登录成功，同时包含账号信息。否则status中为错误信息
     */
    public JSONObject login(String username, String password) {
        JSONObject re = new JSONObject();
        if (!judgeUserInfo(username, password)) {
            re.put("status", "用户信息不合法");
            return re;
        }

        Optional<AccountEntity> e = accountRepository.findByUsername(username);
        if (e.isPresent()) {
            if (e.get().getPassword().equals(password)) {
                re.put("status", "ok");
                putAccountInfoMap(re, e.get());
                updateLoginTime(e.get());
            } else {
                re.put("status", "密码错误");
            }
        } else {
            re.put("status", "用户名未注册");
        }
        return re;
    }

    /**
     * 自动登录
     *
     * @param id session中包含的在线用户id，用此id来定位用户
     * @return 登录结果，status为ok即为登录成功，同时包含账号信息。否则status中为错误信息
     */
    public JSONObject autoLogin(String id) {
        long i = idConvert(id);
        Optional<AccountEntity> e = accountRepository.findById(i);

        JSONObject re = new JSONObject();
        if (e.isPresent()) {
            re.put("status", "ok");
            putAccountInfoMap(re, e.get());
        } else re.put("status", "自动登录失败");

        return re;
    }

    /**
     * 注销账号，从数据库移除账号信息
     *
     * @param id 要注销账号的id
     * @return 若id不存在则返回false
     */
    public boolean destroyAccount(String id) {
        long i = idConvert(id);
        if (!accountRepository.existsById(i)) return false;
        accountRepository.deleteById(i);
        return true;
    }

    /**
     * 更新数据库中的上次登录时间
     *
     * @param e 要更新的词条
     */
    private void updateLoginTime(AccountEntity e) {
        e.setLastLogin(new Date().getTime());
        accountRepository.save(e);
    }

    /**
     * 判断用户信息是否符合格式
     */
    private boolean judgeUserInfo(String username, String password) {
        if (username == null || password == null) return false;

        else if (username.length() > 16) return false;
        else if (password.length() < 3 || password.length() > 16) return false;

        return true;
    }

    /**
     * 获取一个随机id，与已存在的其它id不重复
     *
     * @return 60位（二进制）长整数
     */
    private long getNewId() {
        long id;
        do {
            id = random.nextLong() >>> 4;
        } while (accountRepository.existsById(id));
        return id;
    }

    /**
     * 将数字id转换为字符串id
     */
    private String idConvert(long id) {
        StringBuilder sb = new StringBuilder();
        long mask = 0x0FC0_0000_0000_0000L;

        for (int i = 54; i >= 0; i -= 6) {
            sb.append(characters.charAt((int) ((id & mask) >> i)));
            mask = mask >> 6;
        }

        return sb.toString();
    }

    /**
     * 将字符串id转换为数字id
     */
    private long idConvert(String id) {
        char[] c = id.toCharArray();
        long re = characters.indexOf(c[0]);
        for (int i = 1; i < c.length; i++) {
            re = re << 6;
            re += characters.indexOf(c[i]);
        }
        return re;
    }

    /**
     * 将用户信息写入Map
     *
     * @param map           写入目标
     * @param accountEntity 用户信息
     */
    private void putAccountInfoMap(JSONObject map, AccountEntity accountEntity) {
        map.put("id", idConvert(accountEntity.getId()));
        map.put("username", accountEntity.getUsername());
        // map.put("password", accountEntity.getPassword());
        map.put("email", accountEntity.getEmail());
        map.put("avatar", accountEntity.getAvatar());
        map.put("lastLogin", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(accountEntity.getLastLogin())));
    }
}
