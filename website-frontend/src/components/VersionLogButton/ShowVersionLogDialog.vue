<template>
  <el-dialog width="50%" v-model="visible" @closed="$emit('update:visible',false)">
    <el-select v-model="chosenVersion" class="m-2" placeholder="Select">
      <el-option v-for="item in versions" :key="item.value" :label="item.value" :value="item.value"></el-option>
    </el-select>
  </el-dialog>
</template>

<script lang="ts">
import {onMounted, reactive, ref} from 'vue'
import {axiosConfiguration} from "@/main";
import axios from "axios";

const ShowVersionLogDialog = {
  name: "ShowVersionLogDialog",
  props: ['visible'],
  setup() {
    const versions = reactive([])
    const chosenVersion = ref('')

    onMounted(() => {
      axiosConfiguration()
      axios({
        url: '/versionLog/versions',
        method: 'POST',
      }).then((re) => {
        let vs: Array<string> = re.data.versions
        for (let i = 0; i < vs.length; i++) {
          versions.push({
            value: vs[i]
          })
        }
      })
    })

    return {
      versions,
      chosenVersion,
    }
  }
}
export default ShowVersionLogDialog
</script>

<style scoped>

</style>