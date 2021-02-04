<template>
  <v-card
    class="mx-auto mt-5"
    flat
  >
    <v-list>
      <v-list-item
        v-for="user in users.slice(0,9)"
        :key="user.nickname"
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
     users: [],
    }
  },
  props: {
    search : {
      type: String,
    },
    onTab : {
      type: Number,
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
    getUsers() {
      axios.get(`${SERVER_URL}/users/like/${this.search}`, this.getToken)
        .then((res) => {
        console.log(res)     
        console.log("성공")  
        this.users = res.data
        })
        .catch((err)=> {
          alert('error'+err.message)
        })
    },
  },
  watch: {
    search: function() {
      console.log("유저a")
      if(this.onTab===3){
        this.getUsers()
        
      }

    },
    onTab: function() {
      console.log("유저b")
      if(this.onTab===3){
        this.getUsers()
      }
    }
  },
}
</script>

<style>

</style>