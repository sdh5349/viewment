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
            ></v-text-field>

            <v-text-field 
              v-model="memoryRadius"
              label='반경(km)'
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
  methods: {
    memoryClick() {
      this.memoryInfo.name = this.memoryName
      this.memoryInfo.radius = this.memoryRadius
      this.$emit('onMemory', this.memoryInfo)
    }
  },
}
</script>

<style>

</style>