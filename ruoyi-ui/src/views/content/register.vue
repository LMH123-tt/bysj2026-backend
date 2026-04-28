<template>
  <div class="content-register">
    <div class="register-container">
      <div class="register-header">
        <h2>用户注册</h2>
        <p>加入内容分发平台，发现更多精彩内容</p>
      </div>

      <el-form 
        ref="registerForm" 
        :model="registerForm" 
        :rules="registerRules" 
        class="register-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            type="text"
            auto-complete="off"
            placeholder="请输入用户名"
          >
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            auto-complete="off"
            placeholder="请输入密码"
            @keyup.enter.native="handleRegister"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            auto-complete="off"
            placeholder="请确认密码"
            @keyup.enter.native="handleRegister"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        
        <el-form-item prop="nickName">
          <el-input
            v-model="registerForm.nickName"
            type="text"
            auto-complete="off"
            placeholder="请输入昵称"
          >
            <svg-icon slot="prefix" icon-class="people" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        
        <el-form-item prop="phonenumber">
          <el-input
            v-model="registerForm.phonenumber"
            type="text"
            auto-complete="off"
            placeholder="请输入手机号"
          >
            <svg-icon slot="prefix" icon-class="phone" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        
        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            type="text"
            auto-complete="off"
            placeholder="请输入邮箱"
          >
            <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        
        <el-form-item prop="code" v-if="captchaEnabled">
          <el-input
            v-model="registerForm.code"
            auto-complete="off"
            placeholder="请输入验证码"
            style="width: 63%"
            @keyup.enter.native="handleRegister"
          >
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
          <div class="register-code">
            <img :src="codeUrl" @click="getCode" class="register-code-img"/>
          </div>
        </el-form-item>
        
        <el-form-item style="width:100%;">
          <el-button
            :loading="loading"
            size="medium"
            type="primary"
            style="width:100%;"
            @click.native.prevent="handleRegister"
          >
            <span v-if="!loading">注册</span>
            <span v-else>注册中...</span>
          </el-button>
          <div style="margin-top: 15px; text-align: center;">
            <router-link class="link-type" :to="'/content/login'">返回登录</router-link>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login"

export default {
  name: "ContentRegister",
  data() {
    // 自定义验证规则
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"))
      } else {
        callback()
      }
    }
    
    return {
      codeUrl: "",
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        nickName: "",
        phonenumber: "",
        email: "",
        code: "",
        uuid: ""
      },
      registerRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入用户名" },
          { min: 2, max: 20, message: "用户名长度在 2 到 20 个字符", trigger: "blur" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入密码" },
          { min: 5, max: 20, message: "密码长度在 5 到 20 个字符", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入密码" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ],
        nickName: [
          { required: true, trigger: "blur", message: "请输入昵称" },
          { min: 2, max: 30, message: "昵称长度在 2 到 30 个字符", trigger: "blur" }
        ],
        phonenumber: [
          { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }
        ],
        email: [
          { type: "email", message: "请输入正确的邮箱地址", trigger: "blur" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      captchaEnabled: true
    }
  },
  created() {
    this.getCode()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.registerForm.uuid = res.uuid
        }
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          // 调用content模块的注册接口
          this.$store.dispatch("ContentRegister", this.registerForm).then(() => {
            this.$message.success("注册成功，请登录")
            this.$router.push({ path: "/content/login" })
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
.content-register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../../assets/images/login-background.jpg");
  background-size: cover;
}

.register-container {
  border-radius: 6px;
  background: #ffffff;
  width: 450px;
  padding: 30px 30px 15px 30px;
  z-index: 1;
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
  
  h2 {
    margin: 0 0 10px 0;
    color: #333;
    font-size: 24px;
  }
  
  p {
    margin: 0;
    color: #909399;
    font-size: 14px;
  }
}

.register-form {
  margin-top: 30px;
  
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.register-code {
  width: 33%;
  height: 38px;
  float: right;
  
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.register-code-img {
  height: 38px;
}
</style>
