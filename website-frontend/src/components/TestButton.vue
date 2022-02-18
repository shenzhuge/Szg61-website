<template>
  <el-button size="small" @click="ButtonClicked">测试</el-button>
</template>

<script lang="ts">
import {axiosConfiguration} from "@/main";
import axios from "axios";

const TestButton = {
  name: "Test",
  setup() {
    function AccountTest() {
      let username = 'username' + Math.floor(Math.random() * 1000)
      let password = 'password' + Math.floor(Math.random() * 1000)
      let id = ''

      axiosConfiguration()
      // 测试注册
      axios({
        url: '/account/register',
        method: 'POST',
        data: {username: username, password: password}
      }).then(function (re) {
        if (re.data.status !== 'ok') alert(re.data.status)
        else {
          id = re.data.id
          // 测试登录
          axios({
            url: '/account/login',
            method: 'POST',
            data: {username: username, password: password}
          }).then(function (re) {
            if (re.data.status !== 'ok') alert(re.data.status)
            else {
              // 测试自动登录
              axios({
                url: '/account/autoLogin',
                method: 'POST',
                data: {username: username, password: password}
              }).then(function (re) {
                if (re.data.status !== 'ok') alert(re.data.status)
                else {
                  // 测试登出
                  axios({
                    url: '/account/logout',
                    method: 'POST',
                  }).then(function () {
                    // 测试注销
                    axios({
                      url: '/account/destroy',
                      method: 'POST',
                      data: {id: id}
                    }).then(function (re) {
                      if (re.data !== 'ok') alert(re.data)
                      else {
                        alert("测试通过")
                      }
                    })
                  })
                }
              })
            }
          })
        }
      })
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