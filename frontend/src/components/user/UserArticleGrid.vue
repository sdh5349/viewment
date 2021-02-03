<template>
  <v-row>
    <!-- <v-col
      v-for="item in items"
      :key="item.id"
      class="d-flex child-flex"
      cols="4"
    > -->
    <v-col
      v-for="n in 9"
      :key="n"
      class="d-flex child-flex"
      cols="4"
    >
      <v-img
        :src="`https://picsum.photos/300/300?image=${n * 5 + 10}`"
        :lazy-src="`https://picsum.photos/10/6?image=${n * 5 + 10}`"
        aspect-ratio="1"
        class="grey lighten-2"
      >
        <template v-slot:placeholder>
          <v-row
            class="fill-height ma-0"
            align="center"
            justify="center"
          >
            <v-progress-circular
              indeterminate
              color="grey lighten-5"
            ></v-progress-circular>
          </v-row>
        </template>
      </v-img>
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
      items: [
        {
          id: 1,
          content: 'a'
        },
        {
          id: 2,
          content: 'a'
        },
        {
          id: 3,
          content: 'a'
        },
        {
          id: 4,
          content: 'a'
        },
        {
          id: 5,
          content: 'a'
        },
        {
          id: 6,
          content: 'a'
        },
        {
          id: 7,
          content: 'a'
        },
        {
          id: 8,
          content: 'a'
        },
        {
          id: 9,
          content: 'a'
        },
      ]
    }
  }
}
</script>

<style>

</style>