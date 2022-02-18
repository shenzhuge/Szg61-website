<template>
  <canvas id="guessColor" style="width: 600px;height: auto" @click.stop=""></canvas>
</template>

<script>
import {onMounted} from 'vue'
import {MainController} from './ts/GuessColor.ts'

const GuessColor = {
  name: "GuessColor",
  setup() {
    onMounted(() => {
      const app = new MainController(document.getElementById('guessColor'))
      app.test()
    })
  }
}

export default GuessColor
</script>

<style scoped>

</style>