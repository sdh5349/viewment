<template>
  <v-virtual-scroll
    :items="followers"
    :item-height="50"
    height="auto"
    max-height="100vh"
    @scroll.native="scrolling"
  >
    <template v-slot:default="{ item }">
      <v-list-item
        @click="onProfileListItem(item.userId)"
      >
        <v-list-item-content>
          <v-list-item-title>
            <div class="d-flex justify-space-between">
              <div>
                <!-- 아이콘 or 프로필 썸네일, 사용자 닉네임 시작 -->
                <v-list-item-avatar
                  v-if="item.profileImage"
                  size="36"
                  class="my-0"
                >
                  <img
                    src=""
                  >
                </v-list-item-avatar>
                <v-icon
                  v-else
                  class="mr-4" 
                  left 
                  large
                > 
                  mdi-account-circle 
                </v-icon>
                  {{ item.nickname}}
                <!-- 아이콘 or 프로필 썸네일, 사용자 닉네임 끝 -->
              </div>
              <!-- 팔로워 삭제(본인 프로필 일 경우)/팔로우/언팔로우 버튼 시작 -->
              <v-btn 
                v-if="profileUserId === loginUserId"
                small 
                class="align-self-center px-0"
                width="55"
                color="error" 
                elevation="1" 
                @click.stop="onFollowerDeleteButton(item)"
              >
                삭제
              </v-btn>
              <v-btn
                v-else-if="item.followed"
                small 
                class="align-self-center px-0"
                width="55"  
                elevation="1" 
                @click.stop="onFollowButton(item)"
              >
                언팔로우
              </v-btn>
              <v-btn
                v-else
                small 
                class="align-self-center px-0"
                width="55"
                color="primary" 
                elevation="1" 
                @click.stop="onFollowButton(item)"
              >
                팔로우
              </v-btn>
              <!-- 팔로워 삭제 버튼 끝 -->
            </div>
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </template>
  </v-virtual-scroll>
</template>

<script>
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'FollowerList',
  props: {
    profileUserId: String
  },
  data() {
    return {
      loginUserId: '',
      followers: null,
      page: 0,
      size: 200,
      last: false,
    }
  },
  create() {
    this.readMore()
    this.loginUserId = sessionStorage.getItem('uid')
  },
  methods: {
    // 현재 프로필 사용자의 팔로워 정보를 원하는 갯수 만큼 요청하는 메서드
    readMore() {
      // 필요한 데이터 가져오기
      axios.get(`${SERVER_URL}/users/${this.profileUserId}/followers?page=${this.page}&size=${this.size}`)
      .then(res => {
        this.followers.push(...res.data.content)
        this.page += 1
        this.last = res.data.last
      })
      .catch(err => {
        alert("오류"); // TODO: 오류페이지로 변경
        console.log('Error', err.message);
        // self.$router.push({ name: 'Error' })
      })
    },
    // 스크롤이 맨 아래에 있고 더 요청할 유저의 정보가 남아있다면 팔로워 정보를 더 요청한다
    scrolling (event) {
      const scrollInfo = event.target
      if (scrollInfo && scrollInfo.scrollHeight - scrollInfo.scrollTop === scrollInfo.clientHeight && !this.last) {
        this.readMore()
      }
    },
    // 팔로워의 프로필로 이동하는 메서드
    onProfileListItem (targetUserId) {
      this.$router.push({ 
        name: 'Profile', 
        params: {
          profileUserId : targetUserId,
        }
      })
    },
    // 본인 팔로워 리스트일 경우 삭제를 희망하는 유저의 인덱스를 찾아 삭제하는 메서드
    onFollowerDeleteButton (targetUser) {
      if (confirm("삭제하시겠습니까?")) {
        axios.delete(`${SERVER_URL}/users/${this.loginUserId}/followers/${targetUser.userId}`)
        .then(() => {
        const targetUserIdx = this.followers.indexOf(targetUser)
        this.followers.splice(targetUserIdx, 1)
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      }
    },
    // 팔로우/언팔로우 메서드
    onFollowButton (targetUser) {
      const targetUserIdx = this.followers.indexOf(targetUser)
      
      if (targetUser.followed) {
        axios.delete(`${SERVER_URL}/users/${this.loginUserId}/follows/${targetUser.userId}`)
        .then(() => {
          this.followers[targetUserIdx].followed = !this.followers[targetUserIdx].followed
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      } else {
        var params = {'targetUserId' : this.targetUser.userId }
        axios.post(`${SERVER_URL}/users/${this.loginUserId}/follow`, params)
        .then(() => {
          this.followers[targetUserIdx].followed = !this.followers[targetUserIdx].followed 
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      }
    },
  }
}
</script>

<style scoped>
</style>