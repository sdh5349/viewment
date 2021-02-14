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
            v-if="user.profileImage"
            :src="profileImageUrl+user.profileImage.path"
          ></v-img>
            <!-- :src="user.profileImage.path" -->
          <v-icon
            v-else
            color="primary"
            size="85"
          >mdi-account-circle</v-icon>
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

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  data() {
    return {
    users: [],
    profileImageUrl: `${SERVER_URL}/images/`,
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
      var params = {page:0, size:10}
        if(this.search){
          axios.get(`${SERVER_URL}/users/like/${this.search}`, {params:params, headers:this.getToken.headers})
            .then((res) => {
            console.log(res)     
            this.users = res.data
            console.log(this.users)
            console.log(this.users.content)
            })
            .catch((err)=> {
              alert('error'+err.message)
            })
        }
    },
    goProfile(user) {
      this.$router.push({ name: 'Profile', params: { profileUserId : user.userId }})
      if(user.profileImage) {
        this.Historys = 
          {
            HistoryTitle: user.nickname,
            HistoryContent: user.userId, 
            HistoryImage: user.profileImage.path,
            HistoryProperty: "User",
          }
      }
      else {
        this.Historys = 
          {
            HistoryTitle: user.nickname,
            HistoryContent: user.userId,
            HistoryIcon: "mdi-account-circle",
            HistoryProperty: "User",
          }
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
    onTab: {
      immediate: true,
      handler(onTab) {
        fetch(`/${onTab}`).then((data) => {
          this.getUsers()
        })
      }
    },
    search: function() {
      if(this.onTab===3){
        console.log("유저a")
        this.getUsers()  
      }
    },
  },
}
</script>

<style>

</style>