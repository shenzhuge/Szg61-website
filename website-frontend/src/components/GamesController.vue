<template>
  <div class="full-screen content-center" id="root" @click="$emit('update:visible', false)" :style="{display: visible?'flex':'none'}">
    <div>
      <GuessColor v-if="game==='guessColor'"></GuessColor>
    </div>
  </div>
</template>

<script>
import GuessColor from './games/GuessColor.vue'

const GamesController = {
  name: "GamesController",
  components: {GuessColor},
  props: ['visible', 'game'],
  setup() {
  }
}
export default GamesController
</script>

<style scoped>
.full-screen {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
}

.content-center {
  display: flex;
  justify-content: center;
  flex-direction: column;
  text-align: center;
}
</style>