<template>
  <el-dialog width="50%" v-model="visible" @closed="$emit('update:visible',false)"></el-dialog>
</template>

<script lang="ts">
const ShowVersionLogDialog = {
  name: "ShowVersionLogDialog",
  props: ['visible'],
  setup() {

  }
}
export default ShowVersionLogDialog
</script>

<style scoped>

</style>