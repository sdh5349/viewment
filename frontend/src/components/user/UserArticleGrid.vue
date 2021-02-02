<template>
  <v-row
  >
    <v-col
      v-for="n in 3"
      :key="n"
    >
      <v-card
        outlined
        tile
      >
      
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'UserArticleGrid',
  data() {
    return { 
    }
  }
}
</script>

<style>

</style>