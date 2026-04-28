package com.ruoyi.content.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.web.domain.AjaxResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.content.domain.ContentUser;
import com.ruoyi.content.service.IContentUserService;
import com.ruoyi.content.mapper.ContentInfoMapper;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.utils.ServletUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.security.utils.SecurityUtils;

import static com.ruoyi.common.core.web.domain.AjaxResult.success;

/**
 * Content C-End用户Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/content/user")
public class ContentUserController
{
    @Autowired
    private IContentUserService contentUserService;

    @Autowired
    private ContentInfoMapper contentInfoMapper;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody ContentUser user)
    {
        Map<String, Object> result = new HashMap<>();
        try {
            if (!contentUserService.checkUserNameUnique(user))
            {
                result.put("code", 500);
                result.put("msg", "注册失败，登录账号已存在");
                return result;
            }
            int count = contentUserService.registerUser(user);
            if (count > 0)
            {
                result.put("code", 200);
                result.put("msg", "注册成功");
            }
            else
            {
                result.put("code", 500);
                result.put("msg", "注册失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "注册失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Map<String, Object> getUserInfo(HttpServletRequest request)
    {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = null;
            try {
                userId = SecurityUtils.getUserId();
            } catch (Exception ignored) {}
            if (userId == null) {
                String userIdStr = ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID);
                if (StringUtils.isNotEmpty(userIdStr)) {
                    userId = Convert.toLong(userIdStr, null);
                }
            }
            if (userId == null) {
                result.put("code", 401);
                result.put("msg", "用户未登录");
                return result;
            }
            ContentUser user = contentUserService.selectContentUserById(userId);
            if (user != null) {
                user.setPassword(null);
                result.put("code", 200);
                result.put("data", user);
            } else {
                result.put("code", 500);
                result.put("msg", "用户不存在");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "获取用户信息失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Map<String, Object> updateUserInfo(@RequestBody ContentUser user)
    {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = SecurityUtils.getUserId();
            user.setUserId(userId);
            int count = contentUserService.updateContentUser(user);
            if (count > 0) {
                result.put("code", 200);
                result.put("msg", "更新成功");
            } else {
                result.put("code", 500);
                result.put("msg", "更新失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Map<String, Object> updatePassword(@RequestBody Map<String, String> params)
    {
        Map<String, Object> result = new HashMap<>();
        try {
            String oldPassword = params.get("oldPassword");
            String newPassword = params.get("newPassword");
            
            Long userId = SecurityUtils.getUserId();
            ContentUser user = contentUserService.selectContentUserById(userId);
            
            if (user == null) {
                result.put("code", 500);
                result.put("msg", "用户不存在");
                return result;
            }
            
            if (!SecurityUtils.matchesPassword(oldPassword, user.getPassword())) {
                result.put("code", 500);
                result.put("msg", "原密码错误");
                return result;
            }
            
            contentUserService.updatePassword(userId, newPassword);
            result.put("code", 200);
            result.put("msg", "密码修改成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "密码修改失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取浏览历史
     */
    @GetMapping("/history")
    public AjaxResult getViewHistory()
    {
        try {
            Long userId = SecurityUtils.getUserId();
            List<Map<String, Object>> history = contentUserService.getViewHistory(userId);
            return success(history);
        } catch (Exception e) {
            return AjaxResult.error("获取浏览历史失败：" + e.getMessage());
        }
    }

    @GetMapping("/search")
    public AjaxResult searchUsers(@RequestParam String keyword)
    {
        try {
            List<ContentUser> users = contentUserService.searchUsers(keyword);
            for (ContentUser user : users)
            {
                user.setPassword(null);
            }
            return success(users);
        } catch (Exception e) {
            return AjaxResult.error("搜索用户失败：" + e.getMessage());
        }
    }

    @GetMapping("/profile/{userId}")
    public AjaxResult getUserProfile(@PathVariable Long userId)
    {
        try {
            ContentUser user = contentUserService.selectContentUserById(userId);
            if (user == null)
            {
                return AjaxResult.error("用户不存在");
            }
            user.setPassword(null);
            Map<String, Object> profile = new HashMap<>();
            profile.put("user", user);
            profile.put("contentCount", contentInfoMapper.countByUserId(userId));
            return success(profile);
        } catch (Exception e) {
            return AjaxResult.error("获取用户信息失败：" + e.getMessage());
        }
    }
}
