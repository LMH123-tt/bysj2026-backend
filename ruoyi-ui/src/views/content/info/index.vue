<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="内容标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入内容标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="内容分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable>
          <el-option v-for="item in categoryOptions" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId" />
        </el-select>
      </el-form-item>
      <el-form-item label="内容类型" prop="contentType">
        <el-select v-model="queryParams.contentType" placeholder="请选择类型" clearable>
          <el-option label="文章" value="article" />
          <el-option label="视频" value="video" />
          <el-option label="图片" value="image" />
          <el-option label="音频" value="audio" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="内容状态" clearable>
          <el-option label="草稿" value="0" />
          <el-option label="已发布" value="1" />
          <el-option label="已下架" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['content:info:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['content:info:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['content:info:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['content:info:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="contentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="contentId" width="60" />
      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="分类" align="center" prop="categoryName" width="100" />
      <el-table-column label="作者" align="center" prop="author" width="100" />
      <el-table-column label="类型" align="center" prop="contentType" width="80">
        <template slot-scope="scope">
          <el-tag size="small">{{ contentTypeFormat(scope.row.contentType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : scope.row.status === '0' ? 'info' : 'danger'">{{ statusFormat(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="推荐" align="center" prop="isRecommended" width="60">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isRecommended === '1' ? 'warning' : 'info'" size="mini">{{ scope.row.isRecommended === '1' ? '是' : '否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="置顶" align="center" prop="isTop" width="60">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isTop === '1' ? 'danger' : 'info'" size="mini">{{ scope.row.isTop === '1' ? '是' : '否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="浏览" align="center" prop="viewCount" width="60" />
      <el-table-column label="点赞" align="center" prop="likeCount" width="60" />
      <el-table-column label="发布时间" align="center" prop="publishTime" width="150" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['content:info:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['content:info:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="内容标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入内容标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内容分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
                <el-option v-for="item in categoryOptions" :key="item.categoryId" :label="item.categoryName" :value="item.categoryId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="作者" prop="author">
              <el-input v-model="form.author" placeholder="请输入作者" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内容类型" prop="contentType">
              <el-select v-model="form.contentType" placeholder="请选择类型" style="width: 100%">
                <el-option label="文章" value="article" />
                <el-option label="视频" value="video" />
                <el-option label="图片" value="image" />
                <el-option label="音频" value="audio" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面图片" prop="coverImage">
          <el-input v-model="form.coverImage" placeholder="请输入封面图片URL" />
        </el-form-item>
        <el-form-item label="内容摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" :rows="3" placeholder="请输入内容摘要" />
        </el-form-item>
        <el-form-item label="正文内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="8" placeholder="请输入正文内容（支持HTML）" />
        </el-form-item>
        <el-row>
          <el-col :span="8">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="草稿" value="0" />
                <el-option label="已发布" value="1" />
                <el-option label="已下架" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否推荐" prop="isRecommended">
              <el-radio-group v-model="form.isRecommended">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否置顶" prop="isTop">
              <el-radio-group v-model="form.isTop">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入标签，多个用逗号分隔" />
        </el-form-item>
        <el-form-item label="来源URL" prop="sourceUrl">
          <el-input v-model="form.sourceUrl" placeholder="请输入来源URL" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listContent, getContent, addContent, updateContent, delContent } from '@/api/content/admin'
import { listCategory } from '@/api/content/admin'

export default {
  name: "ContentInfo",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      contentList: [],
      categoryOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        categoryId: null,
        contentType: null,
        status: null
      },
      form: {},
      rules: {
        title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
        categoryId: [{ required: true, message: "请选择分类", trigger: "change" }],
        contentType: [{ required: true, message: "请选择类型", trigger: "change" }]
      }
    }
  },
  created() {
    this.getList()
    this.getCategoryOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listContent(this.queryParams).then(response => {
        this.contentList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getCategoryOptions() {
      listCategory({ status: '0' }).then(response => {
        this.categoryOptions = response.rows || response.data || []
      })
    },
    contentTypeFormat(type) {
      const map = { article: '文章', video: '视频', image: '图片', audio: '音频' }
      return map[type] || type
    },
    statusFormat(status) {
      const map = { '0': '草稿', '1': '已发布', '2': '已下架' }
      return map[status] || '未知'
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        contentId: null, title: null, summary: null, content: null, categoryId: null,
        author: null, coverImage: null, contentType: 'article', status: '0',
        isRecommended: '0', isTop: '0', tags: null, sourceUrl: null, remark: null
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.contentId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加内容"
    },
    handleUpdate(row) {
      this.reset()
      const contentId = row.contentId || this.ids[0]
      getContent(contentId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改内容"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.contentId != null) {
            updateContent(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addContent(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const contentIds = row.contentId || this.ids
      this.$modal.confirm('是否确认删除内容编号为"' + contentIds + '"的数据项？').then(() => {
        return delContent(contentIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('content/info/export', { ...this.queryParams }, `content_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
