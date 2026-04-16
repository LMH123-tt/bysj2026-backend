<template>
  <div class="login">
    <div class="login-brand">
      <div class="brand-icon">
        <i class="el-icon-s-promotion"></i>
      </div>
      <h2 class="brand-title">内容分发平台</h2>
      <p class="brand-subtitle">Content Distribution Platform</p>
    </div>
    <div class="login-type-switch">
      <div 
        :class="['type-item', loginType === 'user' ? 'active' : '']"
        @click="switchLoginType('user')"
      >
        用户登录
      </div>
      <div 
        :class="['type-item', loginType === 'admin' ? 'active' : '']"
        @click="switchLoginType('admin')"
      >
        管理员登录
      </div>
    </div>

    <!-- 用户登录表单 -->
    <el-form 
      v-if="loginType === 'user'"
      ref="userLoginForm" 
      :model="userLoginForm" 
      :rules="userLoginRules" 
      class="login-form"
    >
      <h3 class="title">内容分发平台 - 用户登录</h3>
      <el-form-item prop="username">
        <el-input
          v-model="userLoginForm.username"
          type="text"
          auto-complete="off"
          placeholder="请输入用户名"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="userLoginForm.password"
          type="password"
          auto-complete="off"
          placeholder="请输入密码"
          @keyup.enter.native="handleUserLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="userLoginForm.code"
          auto-complete="off"
          placeholder="请输入验证码"
          style="width: 63%"
          @keyup.enter.native="handleUserLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="userLoginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleUserLogin"
        >
          <span v-if="!loading">用户登录</span>
          <span v-else>登录中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/content/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>

    <!-- 管理员登录表单 -->
    <el-form 
      v-if="loginType === 'admin'"
      ref="adminLoginForm" 
      :model="adminLoginForm" 
      :rules="adminLoginRules" 
      class="login-form"
    >
      <h3 class="title">{{title}}</h3>
      <el-form-item prop="username">
        <el-input
          v-model="adminLoginForm.username"
          type="text"
          auto-complete="off"
          placeholder="账号"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="adminLoginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleAdminLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="adminLoginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleAdminLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="adminLoginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleAdminLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>{{ footerContent }}</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from '@/utils/jsencrypt'
import defaultSettings from '@/settings'

export default {
  name: "Login",
  data() {
    return {
      loginType: 'user', // 默认显示用户登录
      title: process.env.VUE_APP_TITLE,
      footerContent: defaultSettings.footerContent,
      codeUrl: "",
      userLoginForm: {
        username: "212121",
        password: "212121",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      adminLoginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      userLoginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      adminLoginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      captchaEnabled: true,
      register: false,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCode()
    this.getCookie()
  },
  methods: {
    switchLoginType(type) {
      this.loginType = type
      this.getCode()
    },
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          if (this.loginType === 'user') {
            this.userLoginForm.uuid = res.uuid
          } else {
            this.adminLoginForm.uuid = res.uuid
          }
        }
      })
    },
    getCookie() {
      const username = Cookies.get("username")
      const password = Cookies.get("password")
      const rememberMe = Cookies.get('rememberMe')
      const loginForm = this.loginType === 'user' ? this.userLoginForm : this.adminLoginForm
      loginForm.username = username === undefined ? loginForm.username : username
      loginForm.password = password === undefined ? loginForm.password : decrypt(password)
      loginForm.rememberMe = rememberMe === undefined ? false : Boolean(rememberMe)
    },
    handleUserLogin() {
      this.$refs.userLoginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.userLoginForm.rememberMe) {
            Cookies.set("username", this.userLoginForm.username, { expires: 30 })
            Cookies.set("password", encrypt(this.userLoginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.userLoginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove("username")
            Cookies.remove("password")
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch("Login", { ...this.userLoginForm, userType: 'user' }).then(() => {
            this.$router.push({ path: this.redirect || "/content/home" }).catch(()=>{})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    },
    handleAdminLogin() {
      this.$refs.adminLoginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.adminLoginForm.rememberMe) {
            Cookies.set("username", this.adminLoginForm.username, { expires: 30 })
            Cookies.set("password", encrypt(this.adminLoginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.adminLoginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove("username")
            Cookies.remove("password")
            Cookies.remove('rememberMe')
          }
          // 管理员登录调用系统模块的登录接口
          this.$store.dispatch("Login", this.adminLoginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{})
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.03'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  }

  &::after {
    content: '';
    position: absolute;
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, rgba(64, 158, 255, 0.15) 0%, transparent 70%);
    top: -100px;
    right: -100px;
    border-radius: 50%;
  }
}

.login-brand {
  position: relative;
  z-index: 1;
  text-align: center;
  margin-bottom: 24px;

  .brand-icon {
    width: 56px;
    height: 56px;
    background: linear-gradient(135deg, #409EFF, #53a8ff);
    border-radius: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 12px auto;
    box-shadow: 0 4px 16px rgba(64, 158, 255, 0.3);

    i {
      font-size: 28px;
      color: #fff;
    }
  }

  .brand-title {
    font-size: 26px;
    font-weight: 700;
    color: #fff;
    letter-spacing: 3px;
    margin: 0;
  }

  .brand-subtitle {
    font-size: 13px;
    color: rgba(255, 255, 255, 0.6);
    margin-top: 6px;
    letter-spacing: 1px;
  }
}

.login-type-switch {
  display: flex;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  padding: 4px;
  margin-bottom: 20px;
  z-index: 1;
  backdrop-filter: blur(10px);
  
  .type-item {
    padding: 10px 36px;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s;
    font-size: 14px;
    color: rgba(255, 255, 255, 0.7);
    letter-spacing: 1px;
    
    &:hover {
      color: #fff;
    }
    
    &.active {
      background: #409EFF;
      color: white;
      font-weight: 500;
      box-shadow: 0 2px 8px rgba(64, 158, 255, 0.4);
    }
  }
}

.title {
  margin: 0px auto 24px auto;
  text-align: center;
  color: #303133;
  font-size: 20px;
  font-weight: 600;
}

.login-form {
  border-radius: 16px;
  background: #ffffff;
  width: 420px;
  padding: 32px 32px 12px 32px;
  z-index: 1;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);

  .el-input {
    height: 40px;
    input {
      height: 40px;
    }
  }
  .input-icon {
    height: 40px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  padding: 16px 0;
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
  letter-spacing: 1px;
  z-index: 1;
}
.login-code-img {
  height: 40px;
}
</style>
