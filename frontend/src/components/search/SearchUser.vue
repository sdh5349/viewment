<template>
  <v-card
    class="mx-auto mt-5"
    flat
  >
    <v-list>
      <v-list-item
        v-for="user in nickname.slice(0,9)"
        :key="user.content.nickname"
      >
        <v-list-item-avatar>
          <v-img
            :alt="`${user.content.nickname} avatar`"
            :src="user.content.profileImage"
          ></v-img>
        </v-list-item-avatar>

        <v-list-item-content>
          <v-list-item-title v-text="user.content.nickname"></v-list-item-title>
        </v-list-item-content>

        <v-list-item-icon>
          <v-icon :color="user.content.followed ? 'deep-red accent-4' : 'grey'">
            mdi-heart
          </v-icon>
        </v-list-item-icon>
      </v-list-item>
    </v-list>
  </v-card>
</template>

<script>
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  data() {
    return {
     nickname: [],
    }
  },
  props: {
    search : {
      type: String,
    }
  },
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
    getUsers(nickname) {
      console.log(this.getToken)
      axios.get(`${SERVER_URL}/users/like/${nickname}`, this.getToken)
        .then((res) => {
        console.log(res)        
        })
        .catch((err)=> {
          alert('error'+err.message)
        })
    },
  },
  watch: {
    search: function(res) {
      this.getUsers(res)
    }
  },
}
</script>

<style>

</style>