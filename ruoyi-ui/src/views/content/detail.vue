<template>
  <div class="content-detail">
    <div class="detail-header">
      <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      <h1>{{ contentDetail.title }}</h1>
      <div class="meta-info">
        <span class="author">
          <svg-icon icon-class="user" />
          {{ contentDetail.author }}
        </span>
        <span class="publish-time">
          <svg-icon icon-class="time" />
          {{ formatDate(contentDetail.publishTime) }}
        </span>
        <span class="category">
          <svg-icon icon-class="category" />
          {{ categoryName }}
        </span>
      </div>
    </div>

    <div class="detail-content">
      <div class="cover-image" v-if="contentDetail.coverImage">
        <img :src="contentDetail.coverImage" :alt="contentDetail.title" />
      </div>

      <div class="content-body">
        <div class="summary" v-if="contentDetail.summary">
          <h3>内容摘要</h3>
          <p>{{ contentDetail.summary }}</p>
        </div>

        <div class="article-content">
          <div v-html="contentDetail.content"></div>
        </div>
      </div>
    </div>

    <div class="detail-footer">
      <div class="action-buttons">
        <el-button 
          :type="isLiked ? 'primary' : 'default'" 
          @click="handleLike"
          :loading="likeLoading"
        >
          <svg-icon icon-class="like" />
          {{ isLiked ? '已点赞' : '点赞' }} ({{ contentDetail.likeCount }})
        </el-button>

        <el-button 
          :type="isFavorited ? 'warning' : 'default'" 
          @click="handleFavorite"
          :loading="favoriteLoading"
        >
          <svg-icon icon-class="star" />
          {{ isFavorited ? '已收藏' : '收藏' }}
        </el-button>

        <el-button @click="handleShare">
          <svg-icon icon-class="share" />
          分享
        </el-button>
      </div>

      <div class="stats">
        <span class="stat-item">
          <svg-icon icon-class="eye" />
          {{ contentDetail.viewCount }} 次浏览
        </span>
        <span class="stat-item">
          <svg-icon icon-class="star" />
          {{ contentDetail.commentCount }} 条评论
        </span>
        <span class="stat-item">
          <svg-icon icon-class="share" />
          {{ contentDetail.shareCount }} 次分享
        </span>
      </div>
    </div>

    <div class="related-content" v-if="relatedContents.length > 0">
      <h3>相关推荐</h3>
      <div class="related-list">
        <div 
          v-for="item in relatedContents" 
          :key="item.contentId"
          class="related-item"
          @click="viewContent(item.contentId)"
        >
          <div class="related-image" v-if="item.coverImage">
            <img :src="item.coverImage" :alt="item.title" />
          </div>
          <div class="related-info">
            <h4>{{ item.title }}</h4>
            <p>{{ item.summary }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getContentDetail, getRecommendedContent, addFavorite, removeFavorite, getFavoriteList, getCategoryList, likeContent } from '@/api/content'

export default {
  name: "ContentDetail",
  data() {
    return {
      contentId: null,
      contentDetail: {},
      categoryName: '',
      relatedContents: [],
      isLiked: false,
      isFavorited: false,
      likeLoading: false,
      favoriteLoading: false,
      favoriteId: null,
      categories: []
    }
  },
  created() {
    this.contentId = this.$route.params.contentId
    this.loadCategories()
    this.loadContentDetail()
    this.loadRelatedContent()
    this.checkFavoriteStatus()
  },
  methods: {
    loadContentDetail() {
      getContentDetail(this.contentId).then(res => {
        this.contentDetail = res.data
        this.categoryName = this.getCategoryName(res.data.categoryId)
      }).catch(() => {
        this.$message.error('加载内容详情失败')
      })
    },
    loadRelatedContent() {
      getRecommendedContent({ pageSize: 6 }).then(res => {
        this.relatedContents = (res.data || res.rows || []).filter(item => item.contentId !== this.contentId)
      }).catch(() => {
        this.relatedContents = []
      })
    },
    loadCategories() {
      getCategoryList().then(res => {
        this.categories = res.data || []
      })
    },
    checkFavoriteStatus() {
      getFavoriteList().then(res => {
        const list = res.data || res.rows || []
        const found = list.find(f => f.contentId === this.contentId)
        if (found) {
          this.isFavorited = true
          this.favoriteId = found.favoriteId
        }
      }).catch(() => {})
    },
    getCategoryName(categoryId) {
      const cat = this.categories.find(c => c.categoryId === categoryId)
      return cat ? cat.categoryName : '未分类'
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return d.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      })
    },
    goBack() {
      this.$router.go(-1)
    },
    viewContent(contentId) {
      this.$router.push({
        path: `/content/detail/${contentId}`
      })
    },
    handleLike() {
      this.likeLoading = true
      likeContent(this.contentId).then(() => {
        this.isLiked = !this.isLiked
        this.contentDetail.likeCount += this.isLiked ? 1 : -1
        this.likeLoading = false
        this.$message.success(this.isLiked ? '点赞成功' : '取消点赞')
      }).catch(() => {
        this.likeLoading = false
        this.$message.error('操作失败')
      })
    },
    handleFavorite() {
      this.favoriteLoading = true
      if (this.isFavorited) {
        if (!this.favoriteId) {
          this.isFavorited = false
          this.favoriteLoading = false
          return
        }
        removeFavorite(this.favoriteId).then(() => {
          this.isFavorited = false
          this.favoriteId = null
          this.favoriteLoading = false
          this.$message.success('取消收藏成功')
        }).catch(() => {
          this.favoriteLoading = false
          this.$message.error('取消收藏失败')
        })
      } else {
        addFavorite({ contentId: this.contentId }).then(res => {
          this.isFavorited = true
          if (res.data && res.data.favoriteId) {
            this.favoriteId = res.data.favoriteId
          }
          this.favoriteLoading = false
          this.$message.success('收藏成功')
        }).catch(() => {
          this.favoriteLoading = false
          this.$message.error('收藏失败')
        })
      }
    },
    handleShare() {
      this.$message.info('分享功能开发中')
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.content-detail {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.detail-header {
  margin-bottom: 30px;
  
  h1 {
    margin: 20px 0;
    font-size: 28px;
    font-weight: 600;
    color: #333;
  }
  
  .meta-info {
    display: flex;
    gap: 20px;
    color: #999;
    font-size: 14px;
    
    span {
      display: flex;
      align-items: center;
      gap: 5px;
    }
  }
}

.detail-content {
  background: white;
  border-radius: 8px;
  padding: 30px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.cover-image {
  width: 100%;
  max-height: 400px;
  overflow: hidden;
  border-radius: 8px;
  margin-bottom: 30px;
  
  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.content-body {
  .summary {
    background: #f5f5f5;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 30px;
    
    h3 {
      margin: 0 0 10px 0;
      font-size: 16px;
      color: #333;
    }
    
    p {
      margin: 0;
      color: #666;
      line-height: 1.6;
    }
  }
  
  .article-content {
    line-height: 1.8;
    color: #333;
    font-size: 16px;
    
    ::v-deep img {
      max-width: 100%;
      height: auto;
      margin: 20px 0;
    }
    
    ::v-deep h1,
    ::v-deep h2,
    ::v-deep h3 {
      margin: 20px 0 10px 0;
      color: #333;
    }
    
    ::v-deep p {
      margin: 15px 0;
    }
  }
}

.detail-footer {
  background: white;
  border-radius: 8px;
  padding: 20px 30px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  
  .action-buttons {
    display: flex;
    gap: 10px;
  }
  
  .stats {
    display: flex;
    gap: 20px;
    color: #999;
    font-size: 14px;
    
    .stat-item {
      display: flex;
      align-items: center;
      gap: 5px;
    }
  }
}

.related-content {
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  
  h3 {
    margin: 0 0 20px 0;
    font-size: 18px;
    color: #333;
  }
}

.related-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.related-item {
  cursor: pointer;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-4px);
  }
  
  .related-image {
    width: 100%;
    height: 150px;
    overflow: hidden;
    border-radius: 8px;
    margin-bottom: 15px;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
  
  .related-info {
    h4 {
      margin: 0 0 10px 0;
      font-size: 14px;
      color: #333;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    
    p {
      margin: 0;
      font-size: 12px;
      color: #999;
      line-height: 1.5;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
  }
}
</style>
