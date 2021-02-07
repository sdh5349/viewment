<template>
  <div>
  <v-sheet
    class="mx-auto"
    elevation="8"
    max-width="800"
  >
    <v-slide-group
      v-model="model"
      class="pa-4"
      mandatory
      show-arrows
    >
      <v-slide-item
        v-for="(article, i) in articles"
        :key="i"
      >
        <v-card
          class="ma-1"
          height="50"
          width="50"
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

<style>

</style>