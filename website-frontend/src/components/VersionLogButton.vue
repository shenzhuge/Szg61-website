<template>
  <el-button size="small" @click="logDialogVisible=true">更新日志</el-button>
  <ShowVersionLogDialog v-model:visible="logDialogVisible"></ShowVersionLogDialog>
</template>

<script lang="ts">
import {ref} from 'vue'
import ShowVersionLogDialog from './VersionLogButton/ShowVersionLogDialog.vue'

const VersionLogButton = {
  name: "VersionLogButton",
  components: {ShowVersionLogDialog},
  setup() {
    const logDialogVisible = ref(false)

    return {
      logDialogVisible
    }
  }
}
export default VersionLogButton
</script>

<style scoped>

</style>