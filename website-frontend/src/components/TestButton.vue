<template>
  <el-button size="small" @click="ButtonClicked">测试</el-button>
</template>

<script lang="ts">
import {axiosConfiguration} from "@/main";
import FastElMessage from "@/utils/FastElMessage";
import axios from "axios";

const TestButton = {
  name: "Test",
  setup() {
    async function AccountTest() {
      let username = 'username' + Math.floor(Math.random() * 1000)
      let password = 'password' + Math.floor(Math.random() * 1000)
      let id = ''

      axiosConfiguration()
      let re

      // 测试注册
      re = await axios({
        url: '/account/register',
        method: 'POST',
        data: {username: username, password: password}
      })
      if (re.data.status !== 'ok') {
        FastElMessage.warning(re.data.status)
        return
      } else id = re.data.id

      // 测试登录
      re = await axios({
        url: '/account/login',
        method: 'POST',
        data: {username: username, password: password}
      })
      if (re.data.status !== 'ok') {
        FastElMessage.warning(re.data.status)
        return
      }

      // 测试自动登录
      re = await axios({
        url: '/account/autoLogin',
        method: 'POST',
      })
      if (re.data.status !== 'ok') {
        FastElMessage.warning(re.data.status)
        return
      }

      // 测试注销
      re = await axios({
        url: '/account/destroy',
        method: 'POST',
        data: {id: id}
      })
      if (re.data !== 'ok') FastElMessage.warning(re.data)
      else FastElMessage.success("测试通过")
    }

    function ButtonClicked() {
      AccountTest()
    }

    return {
      ButtonClicked
    }
  }
}

export default TestButton
</script>

<style scoped>

</style>