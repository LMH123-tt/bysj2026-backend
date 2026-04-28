import store from '@/store'
import router from '@/router'
import { contentLogin, contentRegister } from '@/api/content'
import { setContentToken, removeContentToken, isValidJwt } from '@/utils/auth'

const contentUser = {
  state: {
    userId: null,
    userName: '',
    nickName: '',
    avatar: ''
  },

  mutations: {
    SET_CONTENT_USER_ID: (state, userId) => {
      state.userId = userId
    },
    SET_CONTENT_USER_NAME: (state, userName) => {
      state.userName = userName
    },
    SET_CONTENT_NICK_NAME: (state, nickName) => {
      state.nickName = nickName
    },
    SET_CONTENT_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    CLEAR_CONTENT_USER: (state) => {
      state.userId = null
      state.userName = ''
      state.nickName = ''
      state.avatar = ''
    }
  },

  actions: {
    ContentLogin({ commit }, loginForm) {
      return new Promise((resolve, reject) => {
        contentLogin(loginForm).then(res => {
          if (res.code === 200) {
            const data = res.data || res
            const token = data.access_token || data.token
            if (token && isValidJwt(token)) {
              setContentToken(token)
            } else {
              reject(new Error('登录返回的令牌格式无效'))
              return
            }
            const userId = data.userid || data.userId
            const userName = data.username || data.userName
            const nickName = data.nickName
            const avatar = data.avatar
            commit('SET_CONTENT_USER_ID', userId)
            commit('SET_CONTENT_USER_NAME', userName)
            commit('SET_CONTENT_NICK_NAME', nickName)
            commit('SET_CONTENT_AVATAR', avatar)
            localStorage.setItem('contentUser', JSON.stringify({
              userId: userId,
              userName: userName,
              nickName: nickName,
              avatar: avatar
            }))
            resolve()
          } else {
            reject(new Error(res.msg || '登录失败'))
          }
        }).catch(error => {
          reject(error)
        })
      })
    },

    ContentRegister({ commit }, registerForm) {
      return new Promise((resolve, reject) => {
        contentRegister(registerForm).then(res => {
          if (res.code === 200) {
            resolve()
          } else {
            reject(new Error(res.msg || '注册失败'))
          }
        }).catch(error => {
          reject(error)
        })
      })
    },

    ContentLogout({ commit }) {
      return new Promise(resolve => {
        commit('CLEAR_CONTENT_USER')
        localStorage.removeItem('contentUser')
        removeContentToken()
        resolve()
      })
    },

    RestoreContentUser({ commit }) {
      const contentUser = localStorage.getItem('contentUser')
      if (contentUser) {
        try {
          const user = JSON.parse(contentUser)
          commit('SET_CONTENT_USER_ID', user.userId)
          commit('SET_CONTENT_USER_NAME', user.userName)
          commit('SET_CONTENT_NICK_NAME', user.nickName)
          commit('SET_CONTENT_AVATAR', user.avatar)
        } catch (e) {
          console.error('Failed to restore content user:', e)
        }
      }
    }
  }
}

export default contentUser
