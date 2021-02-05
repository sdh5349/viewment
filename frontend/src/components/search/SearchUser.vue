<template>
  <v-card
    class="mx-auto mt-5"
    flat
  >
    <v-list>
      <v-list-item
        v-for="user in users.content"
        :key="user.nickname"
        @click="goProfile(user)"
      >
        <v-list-item-avatar>
          <v-img
            :alt="`${user.nickname} avatar`"
            :src="user.profileImage"
          ></v-img>
        </v-list-item-avatar>

        <v-list-item-content>
          <v-list-item-title v-text="user.nickname"></v-list-item-title>
        </v-list-item-content>

        <v-list-item-icon>
          <v-icon :color="user.followed ? 'deep-red accent-4' : 'grey'">
            mdi-heart
          </v-icon>
        </v-list-item-icon>
      </v-list-item>
    </v-list>
  </v-card>
</template>

<script>
import axios from 'axios'
// import { params } from 'vee-validate/dist/types/rules/alpha'
// import { params } from 'vee-validate/dist/types/rules/alpha'

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
      console.log(this.getToken.headers)
      var params = {page:0, size:10}
      axios.get(`${SERVER_URL}/users/like/${this.search}`, {params:params, headers:this.getToken.headers})
        .then((res) => {
        console.log(res)     
        console.log("성공")  
        this.users = res.data
        })
        .catch((err)=> {
          alert('error'+err.message)
        })
    },
    goProfile(user) {
      this.$router.push({ name: 'Profile', params: { profileUserId : user.userId }})
      this.Historys = 
        {
          HistoryTitle: user.nickname,
          HistoryContent: user.userId, 
          HistoryIcon: user.profileImage,
          HistoryProperty: "User",
        }
      this.appendToStorage(this.Historys)
    },
    appendToStorage(Historys) {
      var str = localStorage.getItem("Historys");
      var obj = {};
      var limitMax = 6;
      try {
        obj = JSON.parse(str);
      } catch {
        obj = {};
      }
      if(!obj){
        obj = {};
        obj["Historys"] = [];
      }
      obj["Historys"].push(Historys);
      if (limitMax && limitMax < obj["Historys"].length) {
        let tempList = [];
        for(let i = obj["Historys"].length-limitMax; i < obj["Historys"].length; i++) {
          tempList.push(obj["Historys"][i]);
        }
        obj["Historys"] = tempList;
      }
      localStorage.setItem("Historys", JSON.stringify(obj));
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