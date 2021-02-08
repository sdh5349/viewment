<template>
 <div class="modal">
    <v-container class="py-0">
      <v-row>
        <v-col cols='12'>
          <div class="overlay" @click="$emit('close-Memorymodal')"></div>
        </v-col>
        <v-col cols='12'>
          <div class="modal-card">

            <v-text-field 
              v-model="memoryName"
              label='기억하기 이름'
              @click="resetMemoryName"
            ></v-text-field>

            <v-text-field 
              v-model="memoryRadius"
              label='반경(km)'
              @click="resetMemoryRadius"
            ></v-text-field>
      
            <v-btn @click='memoryClick' block color='primary'>기억 저장</v-btn>
          
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
  data() {
    return{
      memoryName: 'your memory',
      memoryRadius: 5,
      memoryInfo: {
        name: '',
        radius: '',
        lat: '',
        lng: '',
      }
    }
  },
  props: ['position'],
  methods: {
    memoryClick() {
      this.memoryInfo.name = this.memoryName
      this.memoryInfo.radius = this.memoryRadius
      this.memoryInfo.lat = this.position.lat
      this.memoryInfo.lng = this.position.lng
      this.$emit('onMemory', this.memoryInfo)
    },
    resetMemoryName() {
      this.memoryName = ''
    },
    resetMemoryRadius() {
      this.memoryRadius = ''
    }
  },
}
</script>

<style>

</style>