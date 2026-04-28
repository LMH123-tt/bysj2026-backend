package com.ruoyi.content.service;

import java.util.List;
import com.ruoyi.content.domain.ContentUser;

/**
 * Content C-End用户Service接口
 * 
 * @author ruoyi
 */
public interface IContentUserService 
{
    /**
     * 查询Content C-End用户
     * 
     * @param userId Content C-End用户主键
     * @return Content C-End用户
     */
    public ContentUser selectContentUserById(Long userId);

    /**
     * 根据用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象
     */
    public ContentUser selectContentUserByUserName(String userName);

    /**
     * 查询Content C-End用户列表
     * 
     * @param contentUser Content C-End用户
     * @return Content C-End用户集合
     */
    public List<ContentUser> selectContentUserList(ContentUser contentUser);

    /**
     * 新增Content C-End用户
     * 
     * @param contentUser Content C-End用户
     * @return 结果
     */
    public int insertContentUser(ContentUser contentUser);

    /**
     * 修改Content C-End用户
     * 
     * @param contentUser Content C-End用户
     * @return 结果
     */
    public int updateContentUser(ContentUser contentUser);

    /**
     * 批量删除Content C-End用户
     * 
     * @param userIds 需要删除的Content C-End用户主键集合
     * @return 结果
     */
    public int deleteContentUserByIds(Long[] userIds);

    /**
     * 删除Content C-End用户信息
     * 
     * @param userId Content C-End用户主键
     * @return 结果
     */
    public int deleteContentUserById(Long userId);

    /**
     * 校验用户名称是否唯一
     * 
     * @param user 用户信息
     * @return 结果
     */
    public boolean checkUserNameUnique(ContentUser user);

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int resetPwd(ContentUser user);

    /**
     * 用户注册
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int registerUser(ContentUser user);

    /**
     * 更新用户密码
     * 
     * @param userId 用户ID
     * @param password 新密码
     * @return 结果
     */
    public int updatePassword(Long userId, String password);

    /**
     * 获取用户浏览历史
     * 
     * @param userId 用户ID
     * @return 浏览历史列表
     */
    public java.util.List<java.util.Map<String, Object>> getViewHistory(Long userId);

    public List<ContentUser> searchUsers(String keyword);
}
