<template>
  <el-dialog width="50%" :model-value="visible" @closed="$emit('hide')" @open="onDialogOpen">
    <el-row>
      <el-col :span="4">
        <el-switch v-if="identified && version.chosen===version.latest" v-model="editOn" active-text="edit"></el-switch>
      </el-col>
      <el-col :span="10" class="larger-text-right-center">
        版本：
      </el-col>
      <el-col :span="8">
        <el-select v-model="version.chosen" @change="selectionChange" style="width: 100%">
          <el-option v-for="item in version.all" :key="item.value" :label="item.value" :value="item.value"></el-option>
        </el-select>
      </el-col>
      <el-col :span="2">
        <el-button v-if="identified" :icon="Check" @click="vClose.dialogVisible=(version.chosen===version.latest)"></el-button>
      </el-col>
    </el-row>
    <div v-loading="loading">
      <div v-if="logs.description.length!==0" class="description-div" @click="dEdit.dialogVisible=editOn">{{ logs.description }}</div>
      <div v-if="logs.features.length!==0 || editOn" class="feature-div">
        <el-row>
          <span class="small-title">Features</span>
          <el-button v-if="editOn" @click="lEdit.target='Features'" round>添加</el-button>
        </el-row>
        <el-row>
          <el-col v-for="log in logs.features" style="padding-top: 10px">
            <label style="margin-left: 20px">{{ log.content }} ---{{ log.time }}</label>
          </el-col>
        </el-row>
      </div>
      <div v-if="logs.debugs.length!==0 || editOn" class="debug-div">
        <el-row>
          <span class="small-title">Bug fix</span>
          <el-button v-if="editOn" @click="lEdit.target='Bug fix'" round>添加</el-button>
        </el-row>
        <el-row>
          <el-col v-for="log in logs.debugs" style="padding-top: 10px">
            <label style="margin-left: 20px">{{ log.content }} ---{{ log.time }}</label>
          </el-col>
        </el-row>
      </div>
      <div v-if="logs.refactors.length!==0 || editOn" class="refactor-div">
        <el-row>
          <span class="small-title">Refactors</span>
          <el-button v-if="editOn" @click="lEdit.target='Refactors'" round>添加</el-button>
        </el-row>
        <el-row>
          <el-col v-for="log in logs.refactors" style="padding-top: 10px">
            <label style="margin-left: 20px">{{ log.content }} ---{{ log.time }}</label>
          </el-col>
        </el-row>
      </div>
    </div>
  </el-dialog>
  <el-dialog v-model="dEdit.dialogVisible" title="编辑版本描述" @opened="dEdit.newDescription=logs.description">
    <el-row>
      <label>版本描述：</label>
      <el-input v-model="dEdit.newDescription" @keydown.enter="dEdit.okClicked"></el-input>
    </el-row>
    <template #footer>
      <el-button @click="dEdit.dialogVisible=false">取消</el-button>
      <el-button type="primary" @click="dEdit.okClicked">确定</el-button>
    </template>
  </el-dialog>
  <el-dialog :model-value="lEdit.target!=='none'" :title="'添加 '+lEdit.target">
    <el-row>
      <label style="text-align: left">{{ lEdit.target }}</label>
      <el-input v-model="lEdit.content" @keydown.enter="lEdit.okClicked"></el-input>
    </el-row>
    <template #footer>
      <el-button @click="lEdit.target='none'">取消</el-button>
      <el-button type="primary" @click="lEdit.okClicked">确定</el-button>
    </template>
  </el-dialog>
  <el-dialog v-model="vClose.dialogVisible" title="开启下一个版本" @opened="vClose.nextVersion=version.latest">
    <el-row>
      <label>下一个版本号：</label>
      <el-input v-model="vClose.nextVersion"></el-input>
    </el-row>
    <template #footer>
      <el-button @click="vClose.dialogVisible=false">取消</el-button>
      <el-button type="primary" @click="vClose.versionClose">确定</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import {onMounted, reactive, ref} from 'vue'
import {Check} from '@element-plus/icons-vue'
import {axiosConfiguration} from "@/main";
import axios from "axios";
import FastElMessage from "@/utils/FastElMessage";

interface Log {
  content: string,
  time: string
}

interface LogInfo {
  description: string,
  features: Array<Log>,
  debugs: Array<Log>,
  refactors: Array<Log>
}

interface VersionInfo {
  all: Array<{
    value: string
  }>,
  chosen: string,
  latest: string
}

const ShowVersionLogDialog = {
  name: "ShowVersionLogDialog",
  props: ['visible', 'identified'],
  emits: ['hide'],
  setup() {
    const version: VersionInfo = reactive({
      all: [],
      chosen: '',
      latest: ''
    })
    const logs: LogInfo = reactive({
      description: '',
      features: [],
      debugs: [],
      refactors: []
    })

    const editOn = ref(false)
    const dEdit = reactive(useDescriptionEdit(version, logs))
    const lEdit = reactive(useLogEdit(version, logs, loadVersionLog))
    const vClose = reactive(useVersionClose(version))

    const loading = ref(false)

    function onDialogOpen() {
      if (version.all.length !== 0) return
      axios({
        url: '/versionLog/versions',
        method: 'POST',
      }).then((re) => {
        let vs: Array<string> = re.data.versions
        for (let i = 0; i < vs.length; i++) {
          version.all.push({
            value: vs[i]
          })
        }
        version.chosen = re.data.latest
        version.latest = re.data.latest
        // 初始获取最新版本的日志内容
        loadVersionLog(version.chosen, logs)
      })
    }

    function loadVersionLog(v: string, l: LogInfo) {
      axios({
        url: '/versionLog/' + v + '/log/',
        method: 'POST',
      }).then((re) => {
        l.description = re.data.description
        l.features = re.data.features
        l.debugs = re.data.debugs
        l.refactors = re.data.refactors
      })
    }

    function selectionChange(value) {
      loading.value = true;
      loadVersionLog(value, logs)
      loading.value = false;
    }

    onMounted(() => {
      axiosConfiguration()
    })

    return {
      version,
      logs,
      onDialogOpen,
      selectionChange,
      editOn,
      dEdit,
      lEdit,
      vClose,
      loading,
      Check,
    }
  }
}

function useDescriptionEdit(version: VersionInfo, logs: LogInfo) {
  const dialogVisible = ref(false)
  const newDescription = ref('')

  function okClicked() {
    dialogVisible.value = false
    if (logs.description === newDescription.value) return
    axios({
      url: '/versionLog/' + version.chosen + '/update/description',
      method: 'POST',
      data: {
        description: newDescription.value
      }
    }).then((re) => {
      if (re.data) {
        FastElMessage.success('修改成功')
        logs.description = newDescription.value
      } else FastElMessage.error('修改失败')
    })
  }

  return {
    dialogVisible,
    newDescription,
    okClicked,
  }
}

function useLogEdit(version: VersionInfo, logs: LogInfo, reload: Function) {
  const target = ref('none')
  const content = ref('')

  function okClicked() {
    axios({
      url: '/versionLog/' + version.chosen + '/add/log',
      method: 'POST',
      data: {
        type: target.value,
        content: content.value
      }
    }).then((re) => {
      if (re.data) {
        FastElMessage.success('添加成功')
        reload(version.chosen, logs)
      } else FastElMessage.error('添加失败')
      target.value = 'none'
      content.value = ''
    })
  }

  return {
    target,
    content,
    okClicked,
  }
}

function useVersionClose(version: VersionInfo) {
  const dialogVisible = ref(false)
  const nextVersion = ref('')

  function versionClose() {
    axios({
      url: '/versionLog/' + version.latest + '/close',
      method: 'POST',
      data: {
        nextVersion: nextVersion.value
      }
    }).then((re) => {
      if (re.data) {
        FastElMessage.success('完结版本成功')
        version.all.push({
          value: nextVersion.value
        })
        version.latest = nextVersion.value
        version.chosen = nextVersion.value
        dialogVisible.value = false
        nextVersion.value = ''
      } else FastElMessage.error('完结版本失败')
    })
  }

  return {
    dialogVisible,
    nextVersion,
    versionClose,
  }
}

export default ShowVersionLogDialog
</script>

<style scoped>
.larger-text-right-center {
  display: flex;
  justify-content: center;
  flex-direction: column;
  text-align: right;
  font-size: 15px;
}

.small-title {
  font-size: 24px;
  font-weight: bold;
  margin-right: 10px;
}

.description-div {
  background-color: #f3f3f3;
  border: 2px ridge #878787;
  border-radius: 10px;
  text-align: left;
  padding: 10px;
  margin: 10px 0;
}

.feature-div {
  background-color: #c4eec2;
  border-style: none none none solid;
  border-width: 5px;
  border-color: #20c926;
  text-align: left;
  padding: 5px;
}

.debug-div {
  background-color: #e7cac5;
  border-style: none none none solid;
  border-width: 5px;
  border-color: #d33d2a;
  text-align: left;
  padding: 5px;
}

.refactor-div {
  background-color: #bceceb;
  border-style: none none none solid;
  border-width: 5px;
  border-color: #2699db;
  text-align: left;
  padding: 5px;
}
</style>