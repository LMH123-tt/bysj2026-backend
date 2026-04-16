import Cookies from 'js-cookie'

const AdminTokenKey = 'Admin-Token'
const ContentTokenKey = 'Content-Token'
const ExpiresInKey = 'Admin-Expires-In'

export function isValidJwt(token) {
  if (!token || typeof token !== 'string') return false
  return token.split('.').length === 3
}

export function getToken() {
  return Cookies.get(AdminTokenKey)
}

export function setToken(token) {
  return Cookies.set(AdminTokenKey, token, { path: '/', sameSite: 'Lax' })
}

export function removeToken() {
  Cookies.remove(AdminTokenKey, { path: '/' })
  Cookies.remove(ExpiresInKey, { path: '/' })
}

export function getContentToken() {
  return Cookies.get(ContentTokenKey)
}

export function setContentToken(token) {
  return Cookies.set(ContentTokenKey, token, { path: '/', sameSite: 'Lax' })
}

export function removeContentToken() {
  Cookies.remove(ContentTokenKey, { path: '/' })
}

export function getExpiresIn() {
  return Cookies.get(ExpiresInKey) || -1
}

export function setExpiresIn(time) {
  return Cookies.set(ExpiresInKey, time, { path: '/', sameSite: 'Lax' })
}

export function removeExpiresIn() {
  return Cookies.remove(ExpiresInKey, { path: '/' })
}

export function getUserType() {
  const contentToken = Cookies.get(ContentTokenKey)
  const adminToken = Cookies.get(AdminTokenKey)
  if (contentToken && !adminToken) {
    return 'user'
  }
  return 'admin'
}
