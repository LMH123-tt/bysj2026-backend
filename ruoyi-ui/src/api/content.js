import request from '@/utils/request'

export function contentLogin(data) {
  return request({
    url: '/content/login',
    headers: {
      isToken: false,
      repeatSubmit: false
    },
    method: 'post',
    data: data
  })
}

export function contentRegister(data) {
  return request({
    url: '/content/user/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

export function getCategoryList() {
  return request({
    url: '/content/category/enabled',
    method: 'get'
  })
}

export function getContentList(params) {
  return request({
    url: '/content/info/published',
    method: 'get',
    params: params
  })
}

export function getContentDetail(contentId) {
  return request({
    url: '/content/info/view/' + contentId,
    method: 'get'
  })
}

export function addFavorite(data) {
  return request({
    url: '/content/favorite',
    method: 'post',
    data: data
  })
}

export function removeFavorite(favoriteId) {
  return request({
    url: '/content/favorite/' + favoriteId,
    method: 'delete'
  })
}

export function getFavoriteList(params) {
  return request({
    url: '/content/favorite/my',
    method: 'get',
    params: params
  })
}

export function likeContent(contentId) {
  return request({
    url: '/content/info/like/' + contentId,
    method: 'post'
  })
}

export function addComment(data) {
  return request({
    url: '/content/comment',
    method: 'post',
    data: data
  })
}

export function getCommentList(params) {
  return request({
    url: '/content/comment/list',
    method: 'get',
    params: params
  })
}

export function deleteComment(commentId) {
  return request({
    url: '/content/comment/' + commentId,
    method: 'delete'
  })
}

export function getUserInfo() {
  return request({
    url: '/content/user/info',
    method: 'get'
  })
}

export function updateUserInfo(data) {
  return request({
    url: '/content/user/update',
    method: 'put',
    data: data
  })
}

export function updatePassword(data) {
  return request({
    url: '/content/user/password',
    method: 'put',
    data: data
  })
}

export function getViewHistory(params) {
  return request({
    url: '/content/user/history',
    method: 'get',
    params: params
  })
}

export function getRecommendedContent(params) {
  return request({
    url: '/content/info/recommended',
    method: 'get',
    params: params
  })
}

export function searchContent(params) {
  return request({
    url: '/content/info/search',
    method: 'get',
    params: params
  })
}