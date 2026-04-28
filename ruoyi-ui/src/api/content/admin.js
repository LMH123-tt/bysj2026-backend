import request from '@/utils/request'

export function listCategory(query) {
  return request({
    url: '/content/category/list',
    method: 'get',
    params: query
  })
}

export function getCategory(categoryId) {
  return request({
    url: '/content/category/' + categoryId,
    method: 'get'
  })
}

export function addCategory(data) {
  return request({
    url: '/content/category',
    method: 'post',
    data: data
  })
}

export function updateCategory(data) {
  return request({
    url: '/content/category',
    method: 'put',
    data: data
  })
}

export function delCategory(categoryIds) {
  return request({
    url: '/content/category/' + categoryIds,
    method: 'delete'
  })
}

export function listContent(query) {
  return request({
    url: '/content/info/list',
    method: 'get',
    params: query
  })
}

export function getContent(contentId) {
  return request({
    url: '/content/info/' + contentId,
    method: 'get'
  })
}

export function addContent(data) {
  return request({
    url: '/content/info',
    method: 'post',
    data: data
  })
}

export function updateContent(data) {
  return request({
    url: '/content/info',
    method: 'put',
    data: data
  })
}

export function delContent(contentIds) {
  return request({
    url: '/content/info/' + contentIds,
    method: 'delete'
  })
}

export function listContentUser(query) {
  return request({
    url: '/content/user/list',
    method: 'get',
    params: query
  })
}

export function getContentUser(userId) {
  return request({
    url: '/content/user/' + userId,
    method: 'get'
  })
}

export function updateContentUser(data) {
  return request({
    url: '/content/user',
    method: 'put',
    data: data
  })
}

export function delContentUser(userIds) {
  return request({
    url: '/content/user/' + userIds,
    method: 'delete'
  })
}

export function resetContentUserPwd(userId, password) {
  return request({
    url: '/content/user/resetPwd',
    method: 'put',
    params: { userId, password }
  })
}

export function listComment(query) {
  return request({
    url: '/content/comment/list',
    method: 'get',
    params: query
  })
}

export function updateComment(data) {
  return request({
    url: '/content/comment',
    method: 'put',
    data: data
  })
}

export function delComment(commentIds) {
  return request({
    url: '/content/comment/' + commentIds,
    method: 'delete'
  })
}
