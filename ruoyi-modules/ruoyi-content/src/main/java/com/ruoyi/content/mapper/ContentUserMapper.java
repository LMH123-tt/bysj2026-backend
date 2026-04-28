package com.ruoyi.content.mapper;

import java.util.List;
import com.ruoyi.content.domain.ContentUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * Content C-End用户Mapper接口
 * 
 * @author ruoyi
 */
@Mapper
public interface ContentUserMapper 
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
     * 删除Content C-End用户
     * 
     * @param userId Content C-End用户主键
     * @return 结果
     */
    public int deleteContentUserById(Long userId);

    /**
     * 批量删除Content C-End用户
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteContentUserByIds(Long[] userIds);

    /**
     * 校验用户名称是否唯一
     * 
     * @param userName 用户名称
     * @return 结果
     */
    public ContentUser checkUserNameUnique(String userName);

    /**
     * 获取用户浏览历史
     * 
     * @param userId 用户ID
     * @return 浏览历史列表
     */
    public java.util.List<java.util.Map<String, Object>> getViewHistory(Long userId);

    public int insertViewHistory(java.util.Map<String, Object> params);

    public List<ContentUser> searchUsers(String keyword);
}
