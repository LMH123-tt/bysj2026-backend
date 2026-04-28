package com.ruoyi.content.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.content.mapper.UserFavoriteMapper;
import com.ruoyi.content.domain.UserFavorite;
import com.ruoyi.content.service.IUserFavoriteService;

/**
 * C-End User Favorite Service Implementation
 * 
 * @author ruoyi
 * @date 2026-04-09
 */
@Service
public class UserFavoriteServiceImpl implements IUserFavoriteService 
{
    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    /**
     * Query User Favorite
     * 
     * @param favoriteId User Favorite primary key
     * @return User Favorite
     */
    @Override
    public UserFavorite selectUserFavoriteByFavoriteId(Long favoriteId)
    {
        return userFavoriteMapper.selectUserFavoriteByFavoriteId(favoriteId);
    }

    /**
     * Query User Favorite List
     * 
     * @param userFavorite User Favorite
     * @return User Favorite collection
     */
    @Override
    public List<UserFavorite> selectUserFavoriteList(UserFavorite userFavorite)
    {
        return userFavoriteMapper.selectUserFavoriteList(userFavorite);
    }

    /**
     * Query User Favorite List by User ID
     * 
     * @param userId User ID
     * @return User Favorite collection
     */
    @Override
    public List<UserFavorite> selectUserFavoriteListByUserId(Long userId)
    {
        return userFavoriteMapper.selectUserFavoriteListByUserId(userId);
    }

    /**
     * Check if user has favorited content
     * 
     * @param userId User ID
     * @param contentId Content ID
     * @return result
     */
    @Override
    public boolean selectUserFavoriteExists(Long userId, Long contentId)
    {
        return userFavoriteMapper.selectUserFavoriteCount(userId, contentId) > 0;
    }

    /**
     * Add User Favorite
     * 
     * @param userFavorite User Favorite
     * @return result
     */
    @Override
    public int insertUserFavorite(UserFavorite userFavorite)
    {
        return userFavoriteMapper.insertUserFavorite(userFavorite);
    }

    /**
     * Delete User Favorite
     * 
     * @param favoriteId User Favorite primary key
     * @return result
     */
    @Override
    public int deleteUserFavoriteByFavoriteId(Long favoriteId)
    {
        return userFavoriteMapper.deleteUserFavoriteByFavoriteId(favoriteId);
    }

    /**
     * Delete User Favorite by User ID and Content ID
     * 
     * @param userId User ID
     * @param contentId Content ID
     * @return result
     */
    @Override
    public int deleteUserFavoriteByUserIdAndContentId(Long userId, Long contentId)
    {
        return userFavoriteMapper.deleteUserFavoriteByUserIdAndContentId(userId, contentId);
    }

    /**
     * Batch Delete User Favorites
     * 
     * @param favoriteIds User Favorite primary keys collection
     * @return result
     */
    @Override
    public int deleteUserFavoriteByFavoriteIds(Long[] favoriteIds)
    {
        return userFavoriteMapper.deleteUserFavoriteByFavoriteIds(favoriteIds);
    }

    /**
     * Delete User Favorites by User ID
     * 
     * @param userId User ID
     * @return result
     */
    @Override
    public int deleteUserFavoriteByUserId(Long userId)
    {
        return userFavoriteMapper.deleteUserFavoriteByUserId(userId);
    }
}
