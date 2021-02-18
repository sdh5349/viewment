<template>
  <div>
  <v-sheet
    class="mx-0 px-0 border-sheet"
    elevation="5"
    max-width="100%"
  >
    <v-slide-group
      v-model="model"
      class="pa-0"
      mandatory
    >
      <v-slide-item
        v-for="(article, i) in articles"
        :key="i"
      >
        <v-card
          class="ma-1"
          height="100px"
          width="100px"
          @click="clickArticle(article)"
        > 
          <v-img
            @onClick="clickArticle"
            :src="SERVER + '/images/'+ article.thumbnail.path"
          >
          </v-img>
        </v-card>
      </v-slide-item>
    </v-slide-group>
  </v-sheet>
  </div>

</template>

<script>
export default {
  props: ['articles'],
  data() {
    return {
      model: null,
      SERVER: process.env.VUE_APP_SERVER_URL
    }
  },
  methods: {
    clickArticle(res) {
      
      this.$emit('onClick', res)
    },
  },
}
</script>

<style scoped>
.bind-sheet {
  border: 0;
}
</style>