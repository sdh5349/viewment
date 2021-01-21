<template>
  <v-card
    class="mx-auto overflow-hidden"
    height="400"
    width="344"
  >
    <v-system-bar color="deep-purple darken-3"></v-system-bar>
  
    <v-btn @click="onTest">테스트 버튼</v-btn>

    <v-app-bar
      color="deep-purple accent-4"
      dark
      prominent
    >
      

      <v-toolbar-title>뉴스피드</v-toolbar-title>


    </v-app-bar>

    <v-card-text>
      <FeedItem />
      <FeedItem />
      <FeedItem />
      <FeedItem />
    </v-card-text>


  </v-card>
  
</template>

<script>
import axios from 'axios'
import { mapState } from "vuex";
import "../../components/css/feed/feed-item.scss";
import FeedItem from "../../components/feed/FeedItem.vue";

export default {
  props: ["keyword"],

  components: { FeedItem },
  computed: {
    getToken() {
      const token = sessionStorage.getItem('jwt')

      const config = {
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      return config
    }
  },
  methods: {
    onTest() {
      axios.get('http://localhost:8080/test', this.getToken)
      .then((res) => {
        console.log(res)
      })
      .catch((err) => {
        alert('error :' + err.message)
      })
    }
  }
};
</script>

