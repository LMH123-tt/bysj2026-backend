package com.ruoyi.content.mapper;

import java.util.List;
import com.ruoyi.content.domain.UserFavorite;

/**
 * C-End User Favorite Mapper Interface
 * 
 * @author ruoyi
 * @date 2026-04-09
 */
public interface UserFavoriteMapper 
{
    /**
     * Query User Favorite
     * 
     * @param favoriteId User Favorite primary key
     * @return User Favorite
     */
    public UserFavorite selectUserFavoriteByFavoriteId(Long favoriteId);

    /**
     * Query User Favorite List
     * 
     * @param userFavorite User Favorite
     * @return User Favorite collection
     */
    public List<UserFavorite> selectUserFavoriteList(UserFavorite userFavorite);

    /**
     * Query User Favorite List by User ID
     * 
     * @param userId User ID
     * @return User Favorite collection
     */
    public List<UserFavorite> selectUserFavoriteListByUserId(Long userId);

    /**
     * Check if user has favorited content
     * 
     * @param userId User ID
     * @param contentId Content ID
     * @return count
     */
    public int selectUserFavoriteCount(Long userId, Long contentId);

    /**
     * Add User Favorite
     * 
     * @param userFavorite User Favorite
     * @return result
     */
    public int insertUserFavorite(UserFavorite userFavorite);

    /**
     * Delete User Favorite
     * 
     * @param favoriteId User Favorite primary key
     * @return result
     */
    public int deleteUserFavoriteByFavoriteId(Long favoriteId);

    /**
     * Delete User Favorite by User ID and Content ID
     * 
     * @param userId User ID
     * @param contentId Content ID
     * @return result
     */
    public int deleteUserFavoriteByUserIdAndContentId(Long userId, Long contentId);

    /**
     * Delete User Favorites
     * 
     * @param favoriteIds User Favorite primary keys collection
     * @return result
     */
    public int deleteUserFavoriteByFavoriteIds(Long[] favoriteIds);

    /**
     * Delete User Favorites by User ID
     * 
     * @param userId User ID
     * @return result
     */
    public int deleteUserFavoriteByUserId(Long userId);
}
