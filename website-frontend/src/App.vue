<template>
  <!-- 整体容器 -->
  <el-container class="full">
    <!-- 顶部导航栏 -->
    <el-header style="padding: 0">
      <el-row>
        <!-- 右上标志 -->
        <el-col :xs="6" :sm="4" :md="3" :lg="2" class="line-down content-center">
          <label class="logo-font">Szg61</label>
        </el-col>
        <!-- 中部导航菜单 -->
        <el-col :xs="12" :sm="16" :md="18" :lg="20">
          <el-menu mode="horizontal">
            <!-- 游戏子菜单 -->
            <el-sub-menu index="game">
              <template #title>
                <el-icon>
                  <coordinate></coordinate>
                </el-icon>
                <span>游戏</span>
              </template>
              <!-- 子菜单选项 -->
              <el-menu-item index="guessColor" @click="gameVisible=true; game='guessColor'">猜颜色</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-col>
        <!-- 右上用户入口 -->
        <el-col :xs="6" :sm="4" :md="3" :lg="2" class="line-down content-center">
          <AccountButton v-model:user="userId"></AccountButton>
        </el-col>
      </el-row>
    </el-header>
    <!-- 中心主体 -->
    <el-main class="content-center">
      <!-- 上下30%空白，主体 -->
      <div style="height: 40%">
        <!-- 第一行，搜索框 -->
        <el-row justify="space-around">
          <el-col :xs="20" :sm="16" :lg="12">
            <!-- 搜索框，组件详见SearchBox.vue -->
            <SearchBox></SearchBox>
          </el-col>
        </el-row>
      </div>
    </el-main>
    <!-- 底部信息 -->
    <el-footer class="content-center">
      <label class="footer-font">
        京ICP备 <a href="https://beian.miit.gov.cn" target="_blank">2022003379号-1</a>
        &nbsp;&nbsp;&nbsp;&nbsp;
        京公安网备 <a href="http://www.beian.gov.cn" target="_blank">11011502005512号</a>
      </label>
      <label class="footer-font">☯ 2022-2023 szg61.work | made by szg</label>
      <span>
        <TestButton v-if="identified"></TestButton>
        <VersionLogButton :identified="identified"></VersionLogButton>
      </span>
    </el-footer>
  </el-container>
  <!-- 游戏抽屉 -->
  <GamesController v-model:visible="gameVisible" :game="game"></GamesController>
</template>

<script lang="ts">
import {ref, computed} from 'vue';
import {Coordinate, Avatar} from '@element-plus/icons-vue'
import SearchBox from './components/SearchBox.vue'
import AccountButton from './components/AccountButton.vue'
import GamesController from './components/GamesController.vue'
import TestButton from './components/TestButton.vue'
import VersionLogButton from './components/VersionLogButton.vue'

const App = {
  name: 'App',
  components: {Coordinate, Avatar, SearchBox, AccountButton, GamesController, TestButton, VersionLogButton},
  setup() {
    const {userId, identified} = useUserCtl()

    const gameVisible = ref(false)
    const game = ref('')

    return {
      userId,
      identified,
      gameVisible,
      game,
    }
  }
}

function useUserCtl() {
  const userId = ref('')
  const identified = computed(() => userId.value === 'EOfLCtx_ws')

  return {
    userId, identified
  }
}

export default App
</script>

<style scoped>
.line-down {
  border-bottom: solid 1px #e6e6e6;
}

.content-center {
  display: flex;
  justify-content: center;
  flex-direction: column;
  text-align: center;
}

.logo-font {
  font-family: "Papyrus", cursive;
  font-weight: bold;
}

.footer-font {
  color: darkgray;
  font-size: 12px;
}

.full {
  position: absolute;
  height: 100%;
  width: 100%;
}
</style>
