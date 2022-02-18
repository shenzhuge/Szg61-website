<template>
  <el-button :icon="icons.Avatar" size="large" style="margin: 0 5%" @click="buttonClicked" round>
    {{ userInfo.username === '' ? '未登录' : userInfo.username }}
  </el-button>
  <!-- 登录注册对话框 -->
  <el-dialog :title="loginCtl.isLogin?'登录':'注册'" v-model="loginCtl.dialogVisible">
    <el-row>
      <!-- 主体，设置左右间隔 -->
      <el-col :span="20" :offset="1">
        <!-- 主体表单-登录 -->
        <el-form v-if="loginCtl.isLogin" :model="loginCtl.loginInfo" label-width="80px">
          <!-- 用户名项 -->
          <el-form-item label="用户名">
            <el-input v-model="loginCtl.loginInfo.username" :prefix-icon="icons.User" placeholder="输入用户名"></el-input>
          </el-form-item>
          <!-- 密码项 -->
          <el-form-item label="密码">
            <el-input v-model="loginCtl.loginInfo.password" :prefix-icon="icons.Key" placeholder="输入密码" show-password></el-input>
          </el-form-item>
          <!-- 按钮组 -->
          <el-form-item>
            <el-button type="primary" @click="loginCtl.login">登录</el-button>
            <el-button type="text" @click="loginCtl.isLogin=false">没有账号？去注册</el-button>
          </el-form-item>
        </el-form>
        <!-- 主体表单-注册 -->
        <el-form v-else :model="loginCtl.registerInfo" :rules="loginCtl.registerRule" status-icon label-width="80px">
          <!-- 用户名项 -->
          <el-form-item label="用户名" prop="username">
            <el-input v-model="loginCtl.registerInfo.username" :prefix-icon="icons.User" placeholder="输入用户名"></el-input>
          </el-form-item>
          <!-- 密码项 -->
          <el-form-item label="密码" prop="password">
            <el-input v-model="loginCtl.registerInfo.password" :prefix-icon="icons.Key" placeholder="输入密码" type="password"></el-input>
          </el-form-item>
          <!-- 二次密码项 -->
          <el-form-item label="再次确认" prop="passwordTwice">
            <el-input v-model="loginCtl.registerInfo.passwordTwice" :prefix-icon="icons.Key" placeholder="再次输入密码" type="password"></el-input>
          </el-form-item>
          <!-- 按钮组 -->
          <el-form-item>
            <el-button type="primary" @click="loginCtl.register">注册</el-button>
            <el-button type="text" @click="loginCtl.isLogin=true">返回注册</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-dialog>
  <!-- 用户中心抽屉 -->
  <el-drawer title="用户中心" v-model="drawerVisible" direction="rtl">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="ID">{{ userInfo.userId }}</el-descriptions-item>
      <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
      <!-- 若未绑定邮箱显示绑定按钮 -->
      <el-descriptions-item label="邮箱">
        <el-button type="primary" size="small" v-if="!userInfo.email || userInfo.email===''">绑定邮箱</el-button>
        <span v-else>{{ userInfo.email }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="上次登录">{{ userInfo.lastLogin }}</el-descriptions-item>
    </el-descriptions>
    <el-button type="primary" @click="drawerVisible=false;loginCtl.logout()">退出登录</el-button>
  </el-drawer>
</template>

<script lang="ts">
import {reactive, ref, watch} from 'vue'
import {Key, User, Avatar} from '@element-plus/icons-vue'
import {useLoginCtl} from './AccountButton/loginCtl'

const AccountButton = {
  name: "AccountButton",
  props: ['user'],
  setup(props, context) {
    const icons = {Avatar, User, Key}

    // 登录成功后的用户信息存在这里
    const userInfo = reactive({
      userId: '',
      username: '',
      email: '',
      avatar: '',
      lastLogin: ''
    })

    // 监视userInfo的修改，同时向根组件传递userId
    watch(userInfo, value => {
      if (props.user !== value.userId) context.emit('update:user', value.userId)
    })

    // 对话框的控制逻辑，将响应式的userInfo引用传入以接收登录成功后的用户信息
    const loginCtl = reactive(useLoginCtl(userInfo))

    const drawerVisible = ref(false)

    function buttonClicked() {
      if (userInfo.username === '') loginCtl.dialogVisible = true
      else drawerVisible.value = true
    }

    return {
      icons,
      userInfo,
      loginCtl,
      buttonClicked,
      drawerVisible,
    }
  }
}
export default AccountButton
</script>

<style scoped>
</style>