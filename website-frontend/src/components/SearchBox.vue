<template>
  <el-input v-model="searchPara.keyword" placeholder="输入搜索关键字" @keydown.enter="doSearch">
    <!-- 前置下拉选框 -->
    <template #prepend>
      <el-select v-model="searchPara.searchEngine" style="width: 80px">
        <el-option label="百度" :value="1"></el-option>
        <el-option label="360" :value="2"></el-option>
      </el-select>
    </template>
    <!-- 后置按钮 -->
    <template #append>
      <el-button @click="doSearch">
        <el-icon>
          <Search></Search>
        </el-icon>
      </el-button>
    </template>
  </el-input>
</template>

<script lang="ts">
import {Search} from '@element-plus/icons-vue'
import {reactive} from "vue";

const SearchBox = {
  name: "SearchBox",
  components: {Search},
  setup() {
    // 参数，关键词和搜索引擎编号
    const searchPara = reactive({
      keyword: '',
      searchEngine: 1
    })

    // 使用选定的搜索引擎搜索，若关键词为空则打开搜索引擎的首页
    function doSearch() {
      switch (searchPara.searchEngine) {
        case 1:
          if (searchPara.keyword === '') window.open('https://www.baidu.com/')
          else window.open('https://www.baidu.com/s?ie=UTF-8&wd=' + searchPara.keyword)
          break
        case 2:
          if (searchPara.keyword === '') window.open('https://www.so.com/')
          else window.open('https://www.so.com/s?q=' + searchPara.keyword)
          break
      }
    }

    return {
      searchPara,
      doSearch,
    }
  }
}
export default SearchBox
</script>

<style scoped>

</style>