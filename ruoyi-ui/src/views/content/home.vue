<template>
  <div class="content-home">
    <div class="top-navbar">
      <div class="navbar-inner">
        <div class="navbar-brand" @click="$router.push('/content/home')">
          <i class="el-icon-reading"></i>
          <span>内容分发平台</span>
        </div>
        <div class="navbar-nav">
          <router-link to="/content/home" class="nav-link active-link">
            <i class="el-icon-s-home"></i> 首页
          </router-link>
          <template v-if="isLoggedIn">
            <router-link to="/content/profile" class="nav-link">
              <i class="el-icon-user"></i> 个人中心
            </router-link>
          </template>
        </div>
        <div class="navbar-right">
          <template v-if="isLoggedIn">
            <el-dropdown trigger="click" @command="handleCommand">
              <span class="user-dropdown">
                <div class="user-avatar-small">
                  <i class="el-icon-user-solid"></i>
                </div>
                <span class="user-name">{{ currentUser }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item icon="el-icon-user" command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item icon="el-icon-switch-button" command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" size="small" @click="$router.push('/content/login')">登录</el-button>
            <el-button size="small" @click="$router.push('/content/register')">注册</el-button>
          </template>
        </div>
      </div>
    </div>

    <div class="hero-section">
      <div class="hero-overlay"></div>
      <div class="hero-particles">
        <span v-for="i in 6" :key="i" :class="'particle particle-' + i"></span>
      </div>
      <div class="hero-content">
        <h1 class="hero-title">内容分发平台</h1>
        <p class="hero-subtitle">汇聚优质内容，连接创作者与读者</p>
        <div class="hero-search">
          <el-input
            v-model="searchQuery"
            placeholder="搜索你感兴趣的内容..."
            prefix-icon="el-icon-search"
            size="large"
            clearable
            @keyup.enter.native="handleSearch"
            @clear="handleClearSearch"
          >
            <el-button slot="append" type="primary" @click="handleSearch">搜索</el-button>
          </el-input>
          <div class="hot-tags" v-if="!searchQuery">
            <span class="hot-label"><i class="el-icon-fire"></i> 热搜：</span>
            <span
              v-for="tag in hotTags"
              :key="tag"
              class="hot-tag"
              @click="quickSearch(tag)"
            >{{ tag }}</span>
          </div>
        </div>
        <div class="hero-stats">
          <div class="stat-item">
            <div class="stat-number">{{ animatedStats.contentCount }}+</div>
            <div class="stat-label">优质内容</div>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <div class="stat-number">{{ animatedStats.creatorCount }}+</div>
            <div class="stat-label">创作者</div>
          </div>
          <div class="stat-divider"></div>
          <div class="stat-item">
            <div class="stat-number">{{ animatedStats.userCount }}+</div>
            <div class="stat-label">活跃用户</div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-container">
      <div class="featured-section" v-if="featuredList.length > 0">
        <div class="section-header">
          <h2><i class="el-icon-star-on"></i> 精选推荐</h2>
        </div>
        <el-carousel height="280px" :interval="5000" arrow="hover" indicator-position="outside">
          <el-carousel-item v-for="(group, index) in featuredGroups" :key="index">
            <div class="featured-grid">
              <div
                v-for="item in group"
                :key="item.contentId"
                class="featured-card"
                @click="viewContent(item)"
              >
                <div class="featured-img" v-if="item.coverImage">
                  <img :src="item.coverImage" :alt="item.title" />
                </div>
                <div class="featured-img featured-placeholder" v-else>
                  <i class="el-icon-picture-outline"></i>
                </div>
                <div class="featured-info">
                  <h3>{{ item.title }}</h3>
                  <p>{{ item.summary || '暂无简介' }}</p>
                  <div class="featured-meta">
                    <span><i class="el-icon-view"></i> {{ item.viewCount || 0 }}</span>
                    <span><i class="el-icon-star-on"></i> {{ item.likeCount || 0 }}</span>
                  </div>
                </div>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>

      <div class="category-section">
        <div class="section-header">
          <h2><i class="el-icon-menu"></i> 内容分类</h2>
        </div>
        <div class="category-grid">
          <div
            :class="['category-card', activeCategory === null ? 'active' : '']"
            @click="selectCategory(null)"
          >
            <div class="category-icon">
              <i class="el-icon-s-grid"></i>
            </div>
            <div class="category-name">全部</div>
          </div>
          <div
            v-for="category in categories"
            :key="category.categoryId"
            :class="['category-card', activeCategory === category.categoryId ? 'active' : '']"
            @click="selectCategory(category.categoryId)"
          >
            <div class="category-icon">
              <i :class="getCategoryIcon(category.categoryId)"></i>
            </div>
            <div class="category-name">{{ category.categoryName }}</div>
          </div>
        </div>
      </div>

      <div class="content-section">
        <div class="section-header">
          <h2>
            <i class="el-icon-document"></i>
            <template v-if="isSearchMode">
              搜索结果 "{{ searchKeyword }}"
            </template>
            <template v-else>
              {{ activeCategoryName || '全部内容' }}
            </template>
          </h2>
          <div class="section-actions">
            <div class="sort-options">
              <span class="sort-label">排序：</span>
              <el-radio-group v-model="sortBy" size="small" @change="handleSortChange">
                <el-radio-button label="latest">最新</el-radio-button>
                <el-radio-button label="popular">最热</el-radio-button>
                <el-radio-button label="views">最多浏览</el-radio-button>
              </el-radio-group>
            </div>
            <div class="view-toggle">
              <el-radio-group v-model="viewMode" size="small">
                <el-radio-button label="grid"><i class="el-icon-s-grid"></i></el-radio-button>
                <el-radio-button label="list"><i class="el-icon-s-unfold"></i></el-radio-button>
              </el-radio-group>
            </div>
          </div>
        </div>

        <div v-if="loading" class="loading-state">
          <div class="loading-spinner">
            <div class="spinner-ring"></div>
            <span>加载中...</span>
          </div>
        </div>
        <div v-else-if="contentList.length === 0" class="empty-state">
          <div class="empty-icon">
            <i class="el-icon-folder-opened"></i>
          </div>
          <p v-if="isSearchMode">未找到与"{{ searchKeyword }}"相关的内容</p>
          <p v-else>暂无内容</p>
          <span v-if="!isSearchMode">该分类下还没有发布内容，敬请期待</span>
          <el-button v-if="isSearchMode" type="text" @click="handleClearSearch">清除搜索</el-button>
        </div>
        <div v-else :class="['content-grid', viewMode === 'list' ? 'list-mode' : '']">
          <div
            v-for="content in contentList"
            :key="content.contentId"
            class="content-card"
            @click="viewContent(content)"
          >
            <div class="card-image" v-if="content.coverImage">
              <img :src="content.coverImage" :alt="content.title" />
              <div class="card-badge" v-if="content.isTop">
                <el-tag size="mini" type="danger" effect="dark">置顶</el-tag>
              </div>
              <div class="card-category-tag" v-if="content.categoryName">
                <el-tag size="mini" effect="dark" type="info">{{ content.categoryName }}</el-tag>
              </div>
            </div>
            <div class="card-image card-image-placeholder" v-else>
              <i :class="getCategoryIcon(content.categoryId) || 'el-icon-document'"></i>
              <div class="card-badge" v-if="content.isTop">
                <el-tag size="mini" type="danger" effect="dark">置顶</el-tag>
              </div>
            </div>
            <div class="card-info">
              <h3 class="card-title">{{ content.title }}</h3>
              <p class="card-summary">{{ content.summary || '暂无简介' }}</p>
              <div class="card-meta">
                <span class="meta-item">
                  <i class="el-icon-view"></i>
                  {{ formatCount(content.viewCount) }}
                </span>
                <span class="meta-item">
                  <i class="el-icon-star-on"></i>
                  {{ formatCount(content.likeCount) }}
                </span>
                <span class="meta-item">
                  <i class="el-icon-chat-dot-round"></i>
                  {{ formatCount(content.commentCount) }}
                </span>
              </div>
              <div class="card-footer">
                <div class="author-info">
                  <div class="author-avatar">
                    <i class="el-icon-user-solid"></i>
                  </div>
                  <span class="author-name">{{ content.author || '匿名' }}</span>
                  <span class="publish-time">{{ formatTime(content.publishTime) }}</span>
                </div>
                <el-button
                  size="mini"
                  :type="isFavorited(content.contentId) ? 'warning' : 'default'"
                  :icon="isFavorited(content.contentId) ? 'el-icon-star-on' : 'el-icon-star-off'"
                  circle
                  @click.stop="toggleFavorite(content)"
                ></el-button>
              </div>
            </div>
          </div>
        </div>

        <div class="pagination-wrapper" v-if="total > 0">
          <el-pagination
            background
            layout="prev, pager, next, total"
            :total="total"
            :page-size="pageSize"
            :current-page.sync="currentPage"
            @current-change="handlePageChange"
          ></el-pagination>
        </div>
      </div>
    </div>

    <div class="footer-section">
      <div class="footer-inner">
        <div class="footer-brand">
          <i class="el-icon-reading"></i>
          <span>内容分发平台</span>
        </div>
        <p>基于微服务与多级缓存架构 &copy; 2026</p>
      </div>
    </div>

    <el-backtop :bottom="50" :right="30"></el-backtop>
  </div>
</template>

<script>
import { getCategoryList, getContentList, getUserInfo, addFavorite, removeFavorite, getFavoriteList, searchContent, getRecommendedContent } from '@/api/content'

export default {
  name: "ContentHome",
  data() {
    return {
      currentUser: '',
      isLoggedIn: false,
      searchQuery: '',
      searchKeyword: '',
      isSearchMode: false,
      categories: [],
      activeCategory: null,
      contentList: [],
      featuredList: [],
      favorites: [],
      favoriteMap: {},
      loading: false,
      viewMode: 'grid',
      sortBy: 'latest',
      currentPage: 1,
      pageSize: 12,
      total: 0,
      hotTags: ['技术', '设计', '生活', '编程', 'AI'],
      animatedStats: {
        contentCount: 0,
        creatorCount: 0,
        userCount: 0
      },
      targetStats: {
        contentCount: 1000,
        creatorCount: 500,
        userCount: 10000
      }
    }
  },
  computed: {
    activeCategoryName() {
      const cat = this.categories.find(c => c.categoryId === this.activeCategory)
      return cat ? cat.categoryName : ''
    },
    featuredGroups() {
      const groups = []
      const size = 3
      for (let i = 0; i < this.featuredList.length; i += size) {
        groups.push(this.featuredList.slice(i, i + size))
      }
      return groups.length > 0 ? groups : []
    }
  },
  created() {
    this.restoreUser()
    this.loadCategories()
    this.loadContent()
    this.loadFeatured()
    this.loadFavorites()
    this.animateStats()
  },
  methods: {
    restoreUser() {
      const contentUser = localStorage.getItem('contentUser')
      if (contentUser) {
        try {
          const user = JSON.parse(contentUser)
          if (user && user.nickName) {
            this.currentUser = user.nickName || user.userName
            this.isLoggedIn = true
            return
          }
        } catch (e) { /* ignore */ }
      }
      this.loadUserInfo()
    },
    loadUserInfo() {
      getUserInfo().then(res => {
        if (res.data) {
          this.currentUser = res.data.nickName || res.data.userName
          this.isLoggedIn = true
        }
      }).catch(() => {
        this.currentUser = ''
        this.isLoggedIn = false
      })
    },
    loadCategories() {
      getCategoryList().then(res => {
        this.categories = res.data || []
      })
    },
    loadContent() {
      this.loading = true
      const params = {
        pageNum: this.currentPage,
        pageSize: this.pageSize,
        status: '1'
      }
      if (this.activeCategory !== null) {
        params.categoryId = this.activeCategory
      }
      if (this.sortBy === 'popular') {
        params.orderByColumn = 'like_count'
        params.isAsc = 'desc'
      } else if (this.sortBy === 'views') {
        params.orderByColumn = 'view_count'
        params.isAsc = 'desc'
      } else {
        params.orderByColumn = 'publish_time'
        params.isAsc = 'desc'
      }
      getContentList(params).then(res => {
        if (res.rows) {
          this.contentList = res.rows
          this.total = res.total || 0
        } else {
          this.contentList = res.data || []
          this.total = this.contentList.length
        }
      }).finally(() => {
        this.loading = false
      })
    },
    loadFeatured() {
      getRecommendedContent({ pageSize: 6 }).then(res => {
        this.featuredList = res.data || res.rows || []
      }).catch(() => {
        this.featuredList = []
      })
    },
    loadFavorites() {
      if (!this.isLoggedIn) return
      getFavoriteList().then(res => {
        const list = res.data || res.rows || []
        this.favorites = list.map(f => f.contentId)
        this.favoriteMap = {}
        list.forEach(f => {
          this.favoriteMap[f.contentId] = f.favoriteId
        })
      }).catch(() => {})
    },
    selectCategory(categoryId) {
      this.activeCategory = categoryId
      this.currentPage = 1
      this.isSearchMode = false
      this.searchKeyword = ''
      this.loadContent()
    },
    getCategoryIcon(categoryId) {
      const icons = {
        1: 'el-icon-video-camera',
        2: 'el-icon-picture',
        3: 'el-icon-document',
        4: 'el-icon-headset',
        5: 'el-icon-reading'
      }
      return icons[categoryId] || 'el-icon-folder'
    },
    handleSearch() {
      if (!this.searchQuery || !this.searchQuery.trim()) {
        this.$message.warning('请输入搜索关键词')
        return
      }
      this.searchKeyword = this.searchQuery.trim()
      this.isSearchMode = true
      this.currentPage = 1
      this.loading = true
      const params = {
        keyword: this.searchKeyword,
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }
      searchContent(params).then(res => {
        if (res.rows) {
          this.contentList = res.rows
          this.total = res.total || 0
        } else {
          this.contentList = res.data || []
          this.total = this.contentList.length
        }
      }).catch(() => {
        this.contentList = []
        this.total = 0
      }).finally(() => {
        this.loading = false
      })
    },
    handleClearSearch() {
      this.searchQuery = ''
      this.searchKeyword = ''
      this.isSearchMode = false
      this.currentPage = 1
      this.loadContent()
    },
    quickSearch(tag) {
      this.searchQuery = tag
      this.handleSearch()
    },
    handleSortChange() {
      if (this.isSearchMode) return
      this.currentPage = 1
      this.loadContent()
    },
    handlePageChange(page) {
      this.currentPage = page
      if (this.isSearchMode) {
        this.loading = true
        searchContent({
          keyword: this.searchKeyword,
          pageNum: this.currentPage,
          pageSize: this.pageSize
        }).then(res => {
          if (res.rows) {
            this.contentList = res.rows
            this.total = res.total || 0
          } else {
            this.contentList = res.data || []
          }
        }).finally(() => {
          this.loading = false
        })
      } else {
        this.loadContent()
      }
      window.scrollTo({ top: 400, behavior: 'smooth' })
    },
    viewContent(content) {
      this.$router.push({ path: `/content/detail/${content.contentId}` })
    },
    toggleFavorite(content) {
      if (!this.isLoggedIn) {
        this.$message.warning('请先登录')
        this.$router.push('/content/login')
        return
      }
      if (this.isFavorited(content.contentId)) {
        const favoriteId = this.favoriteMap[content.contentId]
        if (favoriteId) {
          removeFavorite(favoriteId).then(() => {
            this.favorites = this.favorites.filter(id => id !== content.contentId)
            delete this.favoriteMap[content.contentId]
            this.$message.success('取消收藏')
          }).catch(() => {
            this.$message.error('操作失败')
          })
        } else {
          this.favorites = this.favorites.filter(id => id !== content.contentId)
          this.$message.success('取消收藏')
        }
      } else {
        addFavorite({ contentId: content.contentId }).then(res => {
          this.favorites.push(content.contentId)
          if (res.data && res.data.favoriteId) {
            this.favoriteMap[content.contentId] = res.data.favoriteId
          }
          this.$message.success('收藏成功')
        }).catch(() => {
          this.favorites.push(content.contentId)
          this.$message.success('收藏成功')
        })
      }
    },
    isFavorited(contentId) {
      return this.favorites.includes(contentId)
    },
    handleCommand(command) {
      if (command === 'profile') {
        this.$router.push('/content/profile')
      } else if (command === 'logout') {
        this.$confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('ContentLogout').then(() => {
            this.currentUser = ''
            this.isLoggedIn = false
            this.favorites = []
            this.favoriteMap = {}
            this.$message.success('已退出登录')
          })
        })
      }
    },
    formatCount(count) {
      if (!count) return '0'
      if (count >= 10000) {
        return (count / 10000).toFixed(1) + 'w'
      }
      if (count >= 1000) {
        return (count / 1000).toFixed(1) + 'k'
      }
      return String(count)
    },
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      const minutes = Math.floor(diff / 60000)
      const hours = Math.floor(diff / 3600000)
      const days = Math.floor(diff / 86400000)
      if (minutes < 1) return '刚刚'
      if (minutes < 60) return minutes + '分钟前'
      if (hours < 24) return hours + '小时前'
      if (days < 30) return days + '天前'
      return date.getFullYear() + '-' + String(date.getMonth() + 1).padStart(2, '0') + '-' + String(date.getDate()).padStart(2, '0')
    },
    animateStats() {
      const duration = 2000
      const steps = 60
      const interval = duration / steps
      let step = 0
      const timer = setInterval(() => {
        step++
        const progress = step / steps
        const ease = 1 - Math.pow(1 - progress, 3)
        this.animatedStats.contentCount = Math.round(this.targetStats.contentCount * ease)
        this.animatedStats.creatorCount = Math.round(this.targetStats.creatorCount * ease)
        this.animatedStats.userCount = Math.round(this.targetStats.userCount * ease)
        if (step >= steps) {
          clearInterval(timer)
        }
      }, interval)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.content-home {
  min-height: 100vh;
  background: #f0f2f5;
}

.top-navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(12px);
  box-shadow: 0 1px 8px rgba(0, 0, 0, 0.08);
  height: 56px;

  .navbar-inner {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .navbar-brand {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    font-size: 18px;
    font-weight: 700;
    color: #1a1a2e;

    i {
      font-size: 24px;
      color: #409EFF;
    }
  }

  .navbar-nav {
    display: flex;
    gap: 24px;

    .nav-link {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 14px;
      color: #606266;
      text-decoration: none;
      padding: 6px 12px;
      border-radius: 6px;
      transition: all 0.3s;

      &:hover {
        color: #409EFF;
        background: #ecf5ff;
      }

      &.active-link {
        color: #409EFF;
        font-weight: 600;
      }
    }
  }

  .navbar-right {
    display: flex;
    align-items: center;
    gap: 10px;

    .user-dropdown {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      padding: 4px 8px;
      border-radius: 6px;
      transition: background 0.3s;

      &:hover {
        background: #f5f7fa;
      }
    }

    .user-avatar-small {
      width: 30px;
      height: 30px;
      border-radius: 50%;
      background: linear-gradient(135deg, #409EFF, #66b1ff);
      display: flex;
      align-items: center;
      justify-content: center;

      i {
        font-size: 14px;
        color: #fff;
      }
    }

    .user-name {
      font-size: 14px;
      color: #303133;
      font-weight: 500;
    }
  }
}

.hero-section {
  position: relative;
  min-height: 420px;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 40%, #0f3460 70%, #1a1a2e 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  margin-top: 56px;

  .hero-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.04'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  }

  .hero-particles {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    overflow: hidden;

    .particle {
      position: absolute;
      width: 6px;
      height: 6px;
      background: rgba(64, 158, 255, 0.3);
      border-radius: 50%;
      animation: floatUp 8s infinite ease-in-out;
    }

    .particle-1 { left: 10%; animation-delay: 0s; animation-duration: 7s; }
    .particle-2 { left: 25%; animation-delay: 1s; animation-duration: 9s; }
    .particle-3 { left: 45%; animation-delay: 2s; animation-duration: 6s; }
    .particle-4 { left: 65%; animation-delay: 0.5s; animation-duration: 8s; }
    .particle-5 { left: 80%; animation-delay: 1.5s; animation-duration: 10s; }
    .particle-6 { left: 92%; animation-delay: 3s; animation-duration: 7.5s; }
  }

  .hero-content {
    position: relative;
    z-index: 1;
    text-align: center;
    padding: 50px 20px;
    max-width: 800px;
    width: 100%;
  }

  .hero-title {
    font-size: 44px;
    font-weight: 700;
    color: #fff;
    margin: 0 0 12px 0;
    letter-spacing: 6px;
    text-shadow: 0 2px 20px rgba(64, 158, 255, 0.3);
  }

  .hero-subtitle {
    font-size: 18px;
    color: rgba(255, 255, 255, 0.75);
    margin: 0 0 32px 0;
    letter-spacing: 3px;
  }

  .hero-search {
    max-width: 560px;
    margin: 0 auto 32px auto;

    ::v-deep .el-input__inner {
      border-radius: 25px 0 0 25px;
      height: 48px;
      border: none;
      font-size: 15px;
      padding-left: 40px;
    }

    ::v-deep .el-input-group__append {
      border-radius: 0 25px 25px 0;
      background: linear-gradient(135deg, #409EFF, #66b1ff);
      border: none;
      color: #fff;
      padding: 0 28px;
      font-size: 15px;
      font-weight: 500;
    }
  }

  .hot-tags {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 12px;
    gap: 8px;
    flex-wrap: wrap;

    .hot-label {
      font-size: 12px;
      color: rgba(255, 255, 255, 0.5);

      i {
        color: #f56c6c;
      }
    }

    .hot-tag {
      font-size: 12px;
      color: rgba(255, 255, 255, 0.7);
      padding: 2px 10px;
      border-radius: 12px;
      background: rgba(255, 255, 255, 0.1);
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        background: rgba(64, 158, 255, 0.3);
        color: #fff;
      }
    }
  }

  .hero-stats {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 30px;

    .stat-item {
      text-align: center;
    }

    .stat-number {
      font-size: 30px;
      font-weight: 700;
      color: #fff;
      text-shadow: 0 0 20px rgba(64, 158, 255, 0.4);
    }

    .stat-label {
      font-size: 13px;
      color: rgba(255, 255, 255, 0.6);
      margin-top: 4px;
    }

    .stat-divider {
      width: 1px;
      height: 36px;
      background: rgba(255, 255, 255, 0.15);
    }
  }
}

@keyframes floatUp {
  0% {
    transform: translateY(100%) scale(0);
    opacity: 0;
  }
  20% {
    opacity: 1;
  }
  80% {
    opacity: 1;
  }
  100% {
    transform: translateY(-400px) scale(1.5);
    opacity: 0;
  }
}

.main-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
}

.featured-section {
  margin-bottom: 36px;

  .section-header {
    margin-bottom: 16px;

    h2 {
      font-size: 20px;
      font-weight: 600;
      color: #333;
      margin: 0;

      i {
        margin-right: 8px;
        color: #e6a23c;
      }
    }
  }

  ::v-deep .el-carousel__item {
    background: transparent;
  }

  ::v-deep .el-carousel__indicator .el-carousel__button {
    background: #c0c4cc;
  }

  ::v-deep .el-carousel__indicator.is-active .el-carousel__button {
    background: #409EFF;
  }

  .featured-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16px;
    padding: 4px;
  }

  .featured-card {
    background: #fff;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
    cursor: pointer;
    transition: all 0.3s;
    display: flex;
    flex-direction: column;

    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
    }

    .featured-img {
      height: 140px;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: transform 0.3s;
      }

      &:hover img {
        transform: scale(1.05);
      }
    }

    .featured-placeholder {
      background: linear-gradient(135deg, #e8ecf1, #d3dce6);
      display: flex;
      align-items: center;
      justify-content: center;

      i {
        font-size: 36px;
        color: #c0c4cc;
      }
    }

    .featured-info {
      padding: 12px 16px;
      flex: 1;

      h3 {
        margin: 0 0 6px 0;
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      p {
        margin: 0 0 8px 0;
        font-size: 12px;
        color: #909399;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        line-height: 1.5;
      }

      .featured-meta {
        display: flex;
        gap: 12px;

        span {
          font-size: 11px;
          color: #c0c4cc;

          i {
            margin-right: 2px;
          }
        }
      }
    }
  }
}

.category-section {
  margin-bottom: 30px;

  .section-header {
    margin-bottom: 16px;

    h2 {
      font-size: 20px;
      font-weight: 600;
      color: #333;
      margin: 0;

      i {
        margin-right: 8px;
        color: #409EFF;
      }
    }
  }
}

.category-grid {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;

  .category-card {
    background: #fff;
    border-radius: 10px;
    padding: 12px 20px;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    gap: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    border: 2px solid transparent;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    }

    &.active {
      border-color: #409EFF;
      background: linear-gradient(135deg, #e8f4fd, #d6ecfa);

      .category-icon i {
        color: #409EFF;
      }

      .category-name {
        color: #409EFF;
        font-weight: 600;
      }
    }

    .category-icon {
      width: 32px;
      height: 32px;
      border-radius: 8px;
      background: #f5f7fa;
      display: flex;
      align-items: center;
      justify-content: center;

      i {
        font-size: 16px;
        color: #909399;
      }
    }

    .category-name {
      font-size: 14px;
      color: #606266;
    }
  }
}

.content-section {
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    flex-wrap: wrap;
    gap: 12px;

    h2 {
      font-size: 20px;
      font-weight: 600;
      color: #333;
      margin: 0;

      i {
        margin-right: 8px;
        color: #409EFF;
      }
    }

    .section-actions {
      display: flex;
      align-items: center;
      gap: 16px;

      .sort-options {
        display: flex;
        align-items: center;
        gap: 8px;

        .sort-label {
          font-size: 13px;
          color: #909399;
        }
      }
    }
  }
}

.loading-state,
.empty-state {
  text-align: center;
  padding: 80px 20px;
  color: #909399;

  .loading-spinner {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 16px;

    .spinner-ring {
      width: 40px;
      height: 40px;
      border: 3px solid #e4e7ed;
      border-top-color: #409EFF;
      border-radius: 50%;
      animation: spin 0.8s linear infinite;
    }
  }

  .empty-icon {
    i {
      font-size: 56px;
      color: #dcdfe6;
    }
  }

  p {
    font-size: 16px;
    margin: 16px 0 8px 0;
    color: #606266;
  }

  span {
    font-size: 13px;
    color: #c0c4cc;
  }
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;

  &.list-mode {
    grid-template-columns: 1fr;

    .content-card {
      display: flex;
      flex-direction: row;

      .card-image {
        width: 240px;
        min-width: 240px;
        height: auto;
      }

      .card-image-placeholder {
        height: 160px;
      }

      .card-info {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
      }
    }
  }
}

.content-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  cursor: pointer;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  }

  .card-image {
    width: 100%;
    height: 200px;
    overflow: hidden;
    position: relative;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    &:hover img {
      transform: scale(1.05);
    }

    .card-badge {
      position: absolute;
      top: 10px;
      left: 10px;
    }

    .card-category-tag {
      position: absolute;
      bottom: 10px;
      right: 10px;
    }
  }

  .card-image-placeholder {
    background: linear-gradient(135deg, #e8ecf1, #d3dce6);
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;

    i {
      font-size: 48px;
      color: #c0c4cc;
    }

    .card-badge {
      position: absolute;
      top: 10px;
      left: 10px;
    }
  }

  .card-info {
    padding: 16px 20px;
  }

  .card-title {
    margin: 0 0 8px 0;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .card-summary {
    margin: 0 0 12px 0;
    font-size: 13px;
    color: #909399;
    line-height: 1.6;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .card-meta {
    display: flex;
    gap: 16px;
    margin-bottom: 12px;

    .meta-item {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 12px;
      color: #c0c4cc;

      i {
        font-size: 14px;
      }
    }
  }

  .card-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 12px;
    border-top: 1px solid #f2f6fc;

    .author-info {
      display: flex;
      align-items: center;
      gap: 8px;

      .author-avatar {
        width: 24px;
        height: 24px;
        border-radius: 50%;
        background: linear-gradient(135deg, #e8ecf1, #d3dce6);
        display: flex;
        align-items: center;
        justify-content: center;

        i {
          font-size: 12px;
          color: #909399;
        }
      }

      .author-name {
        font-size: 12px;
        color: #606266;
      }

      .publish-time {
        font-size: 11px;
        color: #c0c4cc;
      }
    }
  }
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 32px;
  padding-bottom: 10px;
}

.footer-section {
  text-align: center;
  padding: 30px 20px;
  background: #fff;
  margin-top: 40px;
  border-top: 1px solid #ebeef5;

  .footer-inner {
    max-width: 1200px;
    margin: 0 auto;

    .footer-brand {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 6px;
      margin-bottom: 8px;
      font-size: 16px;
      font-weight: 600;
      color: #303133;

      i {
        font-size: 20px;
        color: #409EFF;
      }
    }

    p {
      margin: 0;
      color: #909399;
      font-size: 13px;
    }
  }
}
</style>
