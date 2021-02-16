<template>
  <v-card
    class="mx-auto mt-5"
    flat
  >
      <v-virtual-scroll
        :items="scrollUsers"
        :item-height="50"
        class="scroll-container"
        @scroll.native="scrolling"
      >
      
      <template v-slot:default="{ item }">
        <v-list-item
          @click="goProfile(item)"
        >
          
        <v-list-item-content>
          <v-list-item-title >
          <div class="d-flex justify-space-between">
            <UserProfileImage 
              :profile-image="item.profileImage"
              :nickname="item.nickname"
              :size="2.7"
            />
          
              <v-btn
                v-if="item.followed && item.userId !== loginUserId"
                small 
                class="px-0"
                width="55"  
                elevation="1" 
                @click.stop="onFollowButton(item)"
              >
                언팔로우
              </v-btn>
              <v-btn
                v-else-if="item.userId !== loginUserId"
                small 
                class="px-0"
                width="55"
                color="primary" 
                elevation="1" 
                @click.stop="onFollowButton(item)"
              >
                팔로우
              </v-btn>

          <!-- </v-row> -->
           </div>
           </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        </template>
      </v-virtual-scroll>
  </v-card>
</template>

<script>
import axios from 'axios'
import UserProfileImage from '@/components/user/UserProfileImage'
import { mdiChessBishop } from '@mdi/js'

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
      scrollUsers: [],
      profileImageUrl: `${SERVER_URL}/images/`,
      loginUserId: sessionStorage.getItem('uid'),
      page: 0,
      size: 200,
      last: false,
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
    // User정보 가져오기
    getUsers() {
      var params = {page:this.page, size:10}
        if(this.search){
          axios.get(`${SERVER_URL}/users/like/${this.search}`, {params:params, headers:this.getToken.headers})
            .then(res => {
              console.log(res)
              
              // this.users = res.data.content
              this.scrollUsers.push(...res.data.content)
              console.log(this.scrollUsers)
              this.page += 1
              this.last = res.data.last
            })
            .catch((err)=> {
              alert('error'+err.message)
            })
        }
    },
    // click시 데이터 포함하여 프로필로 이동
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
    // 검색기록을 위한 localstorage 저장(중복제거)
    appendToStorage(History) {
      var tempArray
      if (localStorage.getItem('Historys') === null) {
        tempArray = [];
      } 
      else {
        tempArray = JSON.parse(localStorage.getItem('Historys'))
      }
      var index = tempArray.findIndex(x => x.HistoryTitle === History.HistoryTitle && x.HistoryProperty === History.HistoryProperty)
      if (index != -1){
        tempArray.splice(index, 1)
      }
      tempArray.push(History)
      localStorage.setItem('Historys', JSON.stringify(tempArray))   
    },
    // follow 기능
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
    // 스크롤이 맨 아래에 있고 더 요청할 유저의 정보가 남아있다면 팔로워 정보를 더 요청한다
    scrolling (event) {
      const scrollInfo = event.target
      if (scrollInfo && scrollInfo.scrollHeight - scrollInfo.scrollTop === scrollInfo.clientHeight && !this.last) {
        this.getUsers()
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
        this.getUsers()  
      }
    },
  },
}
</script>

<style scoped>
.scroll-container {
  width: 100%;
  height: 500px;
  overflow: hidden;
  margin-bottom: 50px;
}

</style>