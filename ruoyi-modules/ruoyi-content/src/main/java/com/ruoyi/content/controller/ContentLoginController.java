package com.ruoyi.content.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.content.domain.ContentUser;
import com.ruoyi.content.service.IContentUserService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.service.TokenService;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.LoginUser;

@RestController
@RequestMapping("/content")
public class ContentLoginController
{
    @Autowired
    private IContentUserService contentUserService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public R<?> login(@RequestBody Map<String, String> loginForm)
    {
        String username = loginForm.get("username");
        String password = loginForm.get("password");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            return R.fail("用户名/密码不能为空");
        }

        ContentUser user = contentUserService.selectContentUserByUserName(username);

        if (user == null)
        {
            return R.fail("用户不存在");
        }

        if ("1".equals(user.getStatus()))
        {
            return R.fail("账号已停用");
        }

        if ("2".equals(user.getDelFlag()))
        {
            return R.fail("账号已删除");
        }

        if (!SecurityUtils.matchesPassword(password, user.getPassword()))
        {
            return R.fail("密码错误");
        }

        user.setPassword(null);
        user.setLoginIp(loginForm.get("loginIp") != null ? loginForm.get("loginIp") : "127.0.0.1");
        user.setLoginDate(new java.util.Date());
        contentUserService.updateContentUser(user);

        SysUser sysUser = new SysUser();
        sysUser.setUserId(user.getUserId());
        sysUser.setUserName(user.getUserName());
        sysUser.setNickName(user.getNickName());
        sysUser.setAvatar(user.getAvatar());

        LoginUser loginUser = new LoginUser();
        loginUser.setSysUser(sysUser);

        Map<String, Object> tokenMap = tokenService.createToken(loginUser);
        return R.ok(tokenMap);
    }
}
