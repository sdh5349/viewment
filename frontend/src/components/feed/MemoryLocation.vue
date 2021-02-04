<template>
 <div class="modal ">
    <div class="overlay" @click="$emit('close-Memorymodal')"></div>
    <div class="modal-card">

      <v-text-field 
        v-model="memoryName"
        label='기억이름'>
     </v-text-field>


      <v-text-field 
        v-model="memoryRadius"
        label='반경'>
        km
     </v-text-field>
      
      <v-btn @click='memoryClick'>
        기억 저장
      </v-btn>

    </div>
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