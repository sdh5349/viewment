<template>
  <v-card
    class="mx-auto mt-5"
    flat
  >
    <v-list>
      <v-list-item
        v-for="user in users"
        :key="user.nickname"
        @click="goProfile(user)"
      >
        
        <v-list-item-avatar>
          <UserProfileImage 
            :profile-image="user.profileImage"
          />
        </v-list-item-avatar>
            <!-- :nickname="user.nickname" -->
          <v-list-item-content>
            <v-list-item-title >{{user.nickname}}</v-list-item-title>
          </v-list-item-content>
          
          <v-list-item-icon>
            <v-btn
              v-if="user.followed && user.userId !== loginUserId"
              small 
              class="px-0"
              width="55"  
              elevation="1" 
              @click.stop="onFollowButton(user)"
            >
              언팔로우
            </v-btn>
            <v-btn
              v-else-if="user.userId !== loginUserId"
              small 
              class="px-0"
              width="55"
              color="primary" 
              elevation="1" 
              @click.stop="onFollowButton(user)"
            >
              팔로우
            </v-btn>

          </v-list-item-icon>
        <!-- </v-row> -->
      </v-list-item>
    </v-list>
  </v-card>
</template>

<script>
import axios from 'axios'
import UserProfileImage from '@/components/user/UserProfileImage'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'SearchUser',
  components: {
    UserProfileImage
  },
  props: {
    search : {
      type: String,
    },
    onTab : {
      type: Number,
    }
  },
  data() {
    return {
      users: [],
      profileImageUrl: `${SERVER_URL}/images/`,
      loginUserId: sessionStorage.getItem('uid')
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
            .then(res => {
              this.users = res.data.content
            })
            .catch((err)=> {
              alert('error'+err.message)
            })
        }
    },
    goProfile(user) {
      this.$router.push({ name: 'Profile', params: { profileUserId : user.userId }})
      if(user.profileImage) {
        this.History = 
          {
            HistoryTitle: user.nickname,
            HistoryContent: user.userId, 
            HistoryImage: user.profileImage,
            HistoryProperty: "User",
          }
      }
      else {
        this.History = 
          {
            HistoryTitle: user.nickname,
            HistoryContent: user.userId,
            HistoryIcon: "mdi-account-circle",
            HistoryProperty: "User",
          }
      }
      this.appendToStorage(this.History)
    },
    appendToStorage(History) {
      var tempArray

      if (localStorage.getItem('Historys') === null) {
        tempArray = [];
      } else {
        tempArray = JSON.parse(localStorage.getItem('Historys'))
      }

      tempArray.push(History);
      localStorage.setItem('Historys', JSON.stringify(tempArray)); 
    },
    onFollowButton (targetUser) {
      const targetUserIdx = this.users.indexOf(targetUser)
      
      if (targetUser.followed) {
        axios.delete(`${SERVER_URL}/users/${this.loginUserId}/followings/${targetUser.userId}`, this.getToken)
        .then(() => {
          this.users[targetUserIdx].followed = !this.users[targetUserIdx].followed
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      } else {
        var params = {'targetUserId' : targetUser.userId }
        axios.post(`${SERVER_URL}/users/${this.loginUserId}/follow`, params, this.getToken)
        .then(() => {
          this.users[targetUserIdx].followed = !this.users[targetUserIdx].followed 
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      }
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
    search() {
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