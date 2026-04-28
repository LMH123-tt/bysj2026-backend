package com.ruoyi.content.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.content.domain.ContentUser;
import com.ruoyi.content.mapper.ContentUserMapper;
import com.ruoyi.content.service.IContentUserService;

/**
 * Content C-End用户Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class ContentUserServiceImpl implements IContentUserService 
{
    @Autowired
    private ContentUserMapper contentUserMapper;

    /**
     * 查询Content C-End用户
     * 
     * @param userId Content C-End用户主键
     * @return Content C-End用户
     */
    @Override
    public ContentUser selectContentUserById(Long userId)
    {
        return contentUserMapper.selectContentUserById(userId);
    }

    /**
     * 根据用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象
     */
    @Override
    public ContentUser selectContentUserByUserName(String userName)
    {
        return contentUserMapper.selectContentUserByUserName(userName);
    }

    /**
     * 查询Content C-End用户列表
     * 
     * @param contentUser Content C-End用户
     * @return Content C-End用户
     */
    @Override
    public List<ContentUser> selectContentUserList(ContentUser contentUser)
    {
        return contentUserMapper.selectContentUserList(contentUser);
    }

    /**
     * 新增Content C-End用户
     * 
     * @param contentUser Content C-End用户
     * @return 结果
     */
    @Override
    @Transactional
    public int insertContentUser(ContentUser contentUser)
    {
        contentUser.setCreateBy(SecurityUtils.getUsername());
        contentUser.setPwdUpdateDate(new java.util.Date());
        // 密码加密
        if (StringUtils.isNotEmpty(contentUser.getPassword()))
        {
            contentUser.setPassword(SecurityUtils.encryptPassword(contentUser.getPassword()));
        }
        return contentUserMapper.insertContentUser(contentUser);
    }

    /**
     * 修改Content C-End用户
     * 
     * @param contentUser Content C-End用户
     * @return 结果
     */
    @Override
    @Transactional
    public int updateContentUser(ContentUser contentUser)
    {
        contentUser.setUpdateBy(SecurityUtils.getUsername());
        // 密码加密
        if (StringUtils.isNotEmpty(contentUser.getPassword()))
        {
            contentUser.setPassword(SecurityUtils.encryptPassword(contentUser.getPassword()));
            contentUser.setPwdUpdateDate(new java.util.Date());
        }
        return contentUserMapper.updateContentUser(contentUser);
    }

    /**
     * 批量删除Content C-End用户
     * 
     * @param userIds 需要删除的Content C-End用户主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteContentUserByIds(Long[] userIds)
    {
        return contentUserMapper.deleteContentUserByIds(userIds);
    }

    /**
     * 删除Content C-End用户信息
     * 
     * @param userId Content C-End用户主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteContentUserById(Long userId)
    {
        return contentUserMapper.deleteContentUserById(userId);
    }

    /**
     * 校验用户名称是否唯一
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(ContentUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        ContentUser info = contentUserMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(ContentUser user)
    {
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return contentUserMapper.updateContentUser(user);
    }

    /**
     * 用户注册
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int registerUser(ContentUser user)
    {
        if (StringUtils.isEmpty(user.getUserName()) && StringUtils.isNotEmpty(user.getNickName()))
        {
            user.setUserName(user.getNickName());
        }
        if (StringUtils.isEmpty(user.getNickName()) && StringUtils.isNotEmpty(user.getUserName()))
        {
            user.setNickName(user.getUserName());
        }
        user.setCreateBy("register");
        user.setPwdUpdateDate(new java.util.Date());
        if (StringUtils.isNotEmpty(user.getPassword()))
        {
            user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        }
        return contentUserMapper.insertContentUser(user);
    }

    /**
     * 更新用户密码
     * 
     * @param userId 用户ID
     * @param password 新密码
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePassword(Long userId, String password)
    {
        ContentUser user = new ContentUser();
        user.setUserId(userId);
        user.setPassword(SecurityUtils.encryptPassword(password));
        user.setPwdUpdateDate(new java.util.Date());
        return contentUserMapper.updateContentUser(user);
    }

    /**
     * 获取用户浏览历史
     * 
     * @param userId 用户ID
     * @return 浏览历史列表
     */
    @Override
    public java.util.List<java.util.Map<String, Object>> getViewHistory(Long userId)
    {
        return contentUserMapper.getViewHistory(userId);
    }

    @Override
    public List<ContentUser> searchUsers(String keyword)
    {
        ContentUser query = new ContentUser();
        query.setUserName(keyword);
        query.setNickName(keyword);
        return contentUserMapper.searchUsers(keyword);
    }
}
