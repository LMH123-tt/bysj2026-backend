import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken, getUserType, getContentToken, isValidJwt, removeContentToken, removeToken } from '@/utils/auth'
import { isPathMatch } from '@/utils/validate'
import { isRelogin } from '@/utils/request'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/register', '/content/register', '/content/login', '/content/home', '/content/detail']

const isWhiteList = (path) => {
  return whiteList.some(pattern => isPathMatch(pattern, path))
}

const isContentPage = (path) => {
  return path.startsWith('/content/')
}

router.beforeEach((to, from, next) => {
  NProgress.start()
  let adminToken = getToken()
  let contentToken = getContentToken()

  if (contentToken && !isValidJwt(contentToken)) {
    removeContentToken()
    localStorage.removeItem('contentUser')
    contentToken = null
  }
  if (adminToken && !isValidJwt(adminToken)) {
    removeToken()
    adminToken = null
  }

  if (isContentPage(to.path)) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    if (contentToken) {
      if (!store.state.content.nickName) {
        store.dispatch('RestoreContentUser')
      }
      if (to.path === '/content/login') {
        next({ path: '/content/home' })
        NProgress.done()
      } else {
        next()
      }
    } else {
      if (isWhiteList(to.path)) {
        next()
      } else {
        next(`/content/login?redirect=${encodeURIComponent(to.fullPath)}`)
        NProgress.done()
      }
    }
  } else {
    if (adminToken) {
      to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
      const isLock = store.getters.isLock
      if (to.path === '/login') {
        next({ path: '/' })
        NProgress.done()
      } else if (isLock && to.path !== '/lock') {
        next({ path: '/lock' })
        NProgress.done()
      } else if (!isLock && to.path === '/lock') {
        next({ path: '/' })
        NProgress.done()
      } else {
        if (store.getters.roles.length === 0) {
          isRelogin.show = true
          store.dispatch('GetInfo').then(() => {
            isRelogin.show = false
            store.dispatch('GenerateRoutes').then(accessRoutes => {
              router.addRoutes(accessRoutes)
              next({ ...to, replace: true })
            })
          }).catch(err => {
            store.dispatch('LogOut').then(() => {
              Message.error(err)
              next({ path: '/' })
            })
          })
        } else {
          next()
        }
      }
    } else {
      if (isWhiteList(to.path)) {
        next()
      } else {
        next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
        NProgress.done()
      }
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
