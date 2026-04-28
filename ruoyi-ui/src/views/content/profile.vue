<template>
  <div class="content-profile">
    <div class="profile-header">
      <div class="profile-card">
        <div class="avatar-section">
          <div class="avatar-wrapper">
            <img v-if="userInfo.avatar" :src="userInfo.avatar" alt="头像" class="avatar" />
            <div v-else class="avatar-placeholder">
              <svg-icon icon-class="user" />
            </div>
          </div>
          <div class="user-basic">
            <h2>{{ userInfo.nickName || userInfo.userName }}</h2>
            <p class="user-id">用户ID: {{ userInfo.userId }}</p>
            <p class="join-time">注册时间: {{ formatDate(userInfo.createTime) }}</p>
          </div>
        </div>
        <div class="stats-row">
          <div class="stat-item">
            <span class="stat-value">{{ favoritesCount }}</span>
            <span class="stat-label">我的收藏</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ viewCount }}</span>
            <span class="stat-label">浏览次数</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ contentCount }}</span>
            <span class="stat-label">发布内容</span>
          </div>
        </div>
      </div>
    </div>

    <div class="profile-container">
      <div class="sidebar">
        <div 
          v-for="item in menuItems" 
          :key="item.key"
          :class="['menu-item', activeTab === item.key ? 'active' : '']"
          @click="activeTab = item.key"
        >
          <svg-icon :icon-class="item.icon" />
          <span>{{ item.label }}</span>
        </div>
      </div>

      <div class="main-content">
        <!-- 个人信息 -->
        <div v-if="activeTab === 'info'" class="tab-content">
          <h3 class="tab-title">个人信息</h3>
          <el-form :model="userForm" class="info-form">
            <el-form-item label="用户名">
              <el-input v-model="userForm.userName" disabled />
            </el-form-item>
            <el-form-item label="昵称">
              <el-input v-model="userForm.nickName" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="userForm.email" />
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="userForm.phone" />
            </el-form-item>
            <el-form-item label="简介">
              <el-input type="textarea" v-model="userForm.intro" :rows="3" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updateUserInfo">保存修改</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 修改密码 -->
        <div v-if="activeTab === 'password'" class="tab-content">
          <h3 class="tab-title">修改密码</h3>
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" class="password-form">
            <el-form-item label="原密码" prop="oldPassword">
              <el-input type="password" v-model="passwordForm.oldPassword" placeholder="请输入原密码" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input type="password" v-model="passwordForm.newPassword" placeholder="请输入新密码" />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input type="password" v-model="passwordForm.confirmPassword" placeholder="请确认新密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updatePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 我的收藏 -->
        <div v-if="activeTab === 'favorites'" class="tab-content">
          <h3 class="tab-title">我的收藏</h3>
          <div v-if="favoritesLoading" class="loading">加载中...</div>
          <div v-else-if="favoritesList.length === 0" class="empty">暂无收藏</div>
          <div v-else class="favorites-list">
            <div v-for="item in favoritesList" :key="item.favoriteId" class="favorite-item">
              <div class="favorite-image">
                <img v-if="item.coverImage" :src="item.coverImage" :alt="item.title" />
                <div v-else class="image-placeholder">
                  <svg-icon icon-class="picture" />
                </div>
              </div>
              <div class="favorite-info">
                <h4 class="favorite-title">{{ item.title }}</h4>
                <p class="favorite-summary">{{ item.summary }}</p>
                <div class="favorite-meta">
                  <span>{{ item.categoryName }}</span>
                  <span>{{ item.createTime }}</span>
                </div>
              </div>
              <div class="favorite-actions">
                <el-button size="small" @click="viewContent(item)">查看</el-button>
                <el-button size="small" type="text" @click="removeFavoriteItem(item.favoriteId)">取消收藏</el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 浏览历史 -->
        <div v-if="activeTab === 'history'" class="tab-content">
          <h3 class="tab-title">浏览历史</h3>
          <div v-if="historyLoading" class="loading">加载中...</div>
          <div v-else-if="historyList.length === 0" class="empty">暂无浏览记录</div>
          <div v-else class="history-list">
            <div v-for="item in historyList" :key="item.id" class="history-item">
              <div class="history-image">
                <img v-if="item.coverImage" :src="item.coverImage" :alt="item.title" />
                <div v-else class="image-placeholder">
                  <svg-icon icon-class="picture" />
                </div>
              </div>
              <div class="history-info">
                <h4 class="history-title">{{ item.title }}</h4>
                <p class="history-summary">{{ item.summary }}</p>
                <div class="history-meta">
                  <span>{{ item.categoryName }}</span>
                  <span>浏览于 {{ item.viewTime }}</span>
                </div>
              </div>
              <div class="history-actions">
                <el-button size="small" @click="viewContent(item)">查看</el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 账户安全 -->
        <div v-if="activeTab === 'security'" class="tab-content">
          <h3 class="tab-title">账户安全</h3>
          <div class="security-list">
            <div class="security-item">
              <div class="security-info">
                <div class="security-icon">
                  <svg-icon icon-class="phone" />
                </div>
                <div>
                  <h4>绑定手机</h4>
                  <p>{{ userForm.phone || '未绑定' }}</p>
                </div>
              </div>
              <el-button size="small" type="text">修改</el-button>
            </div>
            <div class="security-item">
              <div class="security-info">
                <div class="security-icon">
                  <svg-icon icon-class="mail" />
                </div>
                <div>
                  <h4>绑定邮箱</h4>
                  <p>{{ userForm.email || '未绑定' }}</p>
                </div>
              </div>
              <el-button size="small" type="text">修改</el-button>
            </div>
            <div class="security-item">
              <div class="security-info">
                <div class="security-icon">
                  <svg-icon icon-class="lock" />
                </div>
                <div>
                  <h4>登录密码</h4>
                  <p>已设置密码</p>
                </div>
              </div>
              <el-button size="small" type="text" @click="activeTab = 'password'">修改</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getUserInfo, updateUserInfo, updatePassword, getFavoriteList, removeFavorite, getViewHistory } from '@/api/content'
import { Message } from 'element-ui'

export default {
  name: 'ContentProfile',
  data() {
    return {
      activeTab: 'info',
      userInfo: {},
      userForm: {
        userName: '',
        nickName: '',
        email: '',
        phone: '',
        intro: ''
      },
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: this.validateConfirmPassword, trigger: 'blur' }
        ]
      },
      favoritesList: [],
      favoritesLoading: false,
      favoritesCount: 0,
      historyList: [],
      historyLoading: false,
      viewCount: 0,
      contentCount: 0,
      menuItems: [
        { key: 'info', label: '个人信息', icon: 'user' },
        { key: 'password', label: '修改密码', icon: 'lock' },
        { key: 'favorites', label: '我的收藏', icon: 'star' },
        { key: 'history', label: '浏览历史', icon: 'history' },
        { key: 'security', label: '账户安全', icon: 'shield' }
      ]
    }
  },
  created() {
    this.loadUserInfo()
    this.loadFavorites()
    this.loadHistory()
  },
  methods: {
    validateConfirmPassword(rule, value, callback) {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return '-'
      return dateStr.substring(0, 10)
    },
    loadUserInfo() {
      getUserInfo().then(res => {
        this.userInfo = res.data
        this.userForm = {
          userName: res.data.userName || '',
          nickName: res.data.nickName || '',
          email: res.data.email || '',
          phone: res.data.phone || '',
          intro: res.data.intro || ''
        }
      }).catch(() => {
        this.$router.push('/login')
      })
    },
    loadFavorites() {
      this.favoritesLoading = true
      getFavoriteList({ pageNum: 1, pageSize: 10 }).then(res => {
        this.favoritesList = res.data?.list || []
        this.favoritesCount = res.data?.total || 0
      }).finally(() => {
        this.favoritesLoading = false
      })
    },
    loadHistory() {
      this.historyLoading = true
      getViewHistory().then(res => {
        this.historyList = res.data || []
        this.viewCount = this.historyList.length
      }).finally(() => {
        this.historyLoading = false
      })
    },
    updateUserInfo() {
      updateUserInfo(this.userForm).then(res => {
        if (res.code === 200) {
          Message.success('修改成功')
          this.loadUserInfo()
        } else {
          Message.error(res.msg || '修改失败')
        }
      }).catch(() => {
        Message.error('修改失败')
      })
    },
    updatePassword() {
      this.$refs.passwordFormRef.validate(valid => {
        if (valid) {
          updatePassword(this.passwordForm).then(res => {
            if (res.code === 200) {
              Message.success('密码修改成功')
              this.passwordForm = {
                oldPassword: '',
                newPassword: '',
                confirmPassword: ''
              }
            } else {
              Message.error(res.msg || '密码修改失败')
            }
          }).catch(() => {
            Message.error('密码修改失败')
          })
        }
      })
    },
    removeFavoriteItem(favoriteId) {
      this.$confirm('确定要取消收藏吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        removeFavorite(favoriteId).then(res => {
          if (res.code === 200) {
            Message.success('取消收藏成功')
            this.loadFavorites()
          } else {
            Message.error(res.msg || '取消收藏失败')
          }
        }).catch(() => {
          Message.error('取消收藏失败')
        })
      })
    },
    viewContent(item) {
      this.$router.push({ path: `/content/detail/${item.contentId}` })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.content-profile {
  min-height: 100vh;
  background: #f5f5f5;
}

.profile-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 0;
}

.profile-card {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  padding: 30px;
}

.avatar-section {
  display: flex;
  align-items: center;
  gap: 30px;
  padding-bottom: 30px;
  border-bottom: 1px solid #f0f0f0;
}

.avatar-wrapper {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  border: 4px solid #f0f0f0;
  
  .avatar {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .avatar-placeholder {
    width: 100%;
    height: 100%;
    background: #f5f5f5;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 48px;
    color: #ccc;
  }
}

.user-basic {
  flex: 1;
  
  h2 {
    margin: 0 0 10px 0;
    font-size: 24px;
    font-weight: 600;
    color: #333;
  }
  
  .user-id, .join-time {
    margin: 5px 0;
    font-size: 14px;
    color: #999;
  }
}

.stats-row {
  display: flex;
  gap: 60px;
  padding-top: 20px;
}

.stat-item {
  text-align: center;
  
  .stat-value {
    display: block;
    font-size: 28px;
    font-weight: 600;
    color: #667eea;
  }
  
  .stat-label {
    font-size: 14px;
    color: #999;
  }
}

.profile-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
  display: flex;
  gap: 20px;
}

.sidebar {
  width: 200px;
  background: white;
  border-radius: 8px;
  padding: 15px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  
  .menu-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 12px 20px;
    cursor: pointer;
    transition: all 0.3s;
    font-size: 14px;
    color: #666;
    
    &:hover {
      background: #f5f5f5;
    }
    
    &.active {
      background: #f0f4ff;
      color: #667eea;
      border-left: 3px solid #667eea;
    }
  }
}

.main-content {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 25px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.tab-content {
  .tab-title {
    margin: 0 0 25px 0;
    font-size: 18px;
    font-weight: 600;
    color: #333;
    padding-bottom: 15px;
    border-bottom: 1px solid #f0f0f0;
  }
  
  .loading, .empty {
    text-align: center;
    padding: 60px 20px;
    color: #999;
    font-size: 16px;
  }
}

.info-form, .password-form {
  max-width: 500px;
  
  .el-form-item {
    margin-bottom: 20px;
  }
}

.favorites-list, .history-list {
  .favorite-item, .history-item {
    display: flex;
    align-items: center;
    gap: 20px;
    padding: 20px;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
  }
  
  .favorite-image, .history-image {
    width: 120px;
    height: 80px;
    border-radius: 8px;
    overflow: hidden;
    flex-shrink: 0;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .image-placeholder {
      width: 100%;
      height: 100%;
      background: #f5f5f5;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24px;
      color: #ccc;
    }
  }
  
  .favorite-info, .history-info {
    flex: 1;
    
    .favorite-title, .history-title {
      margin: 0 0 8px 0;
      font-size: 16px;
      font-weight: 600;
      color: #333;
    }
    
    .favorite-summary, .history-summary {
      margin: 0 0 10px 0;
      font-size: 14px;
      color: #666;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
    
    .favorite-meta, .history-meta {
      display: flex;
      gap: 15px;
      font-size: 12px;
      color: #999;
    }
  }
  
  .favorite-actions, .history-actions {
    flex-shrink: 0;
  }
}

.security-list {
  .security-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .security-info {
      display: flex;
      align-items: center;
      gap: 15px;
      
      .security-icon {
        width: 40px;
        height: 40px;
        background: #f0f4ff;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
        color: #667eea;
      }
      
      h4 {
        margin: 0 0 5px 0;
        font-size: 15px;
        font-weight: 500;
        color: #333;
      }
      
      p {
        margin: 0;
        font-size: 14px;
        color: #999;
      }
    }
  }
}
</style>
