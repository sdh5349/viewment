<template>
  <v-virtual-scroll
    :items="followings"
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
              <!-- 팔로우/언팔로우 버튼 시작 -->
              <v-btn
                v-if="item.followed"
                small 
                class="align-self-center"  
                elevation="1" 
                @click.stop="onUnfollowButton(item.userId)"
              >
                언팔로우
              </v-btn>
              <v-btn
                v-else
                small 
                class="align-self-center" 
                color="primary" 
                elevation="1" 
                @click.stop="onFollowButton(item.userId)"
              >
                팔로우
              </v-btn>
              <!-- 팔로우/언팔로우 버튼 끝 -->
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
  name: 'FollowingList',
  props: {
    profileUserId: String
  },
  data() {
    return {
      followings: [
        {
          "userId": "1",
          "nickname": "user1",
          "profileImage": null,
          "followed": true
        },
        {
          "userId": "2",
          "nickname": "user2",
          "profileImage": null,
          "followed": true
        },
        {
          "userId": "3",
          "nickname": "user3",
          "profileImage": null,
          "followed": true
        }
      ],
      page: 0,
      size: 200,
      // last: false,
    }
  },
  create() {
    // this.readMore()
  },
  methods: {
    readMore() {
      // 필요한 데이터 가져오기
      axios.get(`${SERVER_URL}/users/${this.profileUserId}/followings?page=${this.page}&size=${this.size}`)
      .then(res => {
        this.followings.push(...res.data)
        this.page += 1
      })
      .catch(err => {
        alert("오류"); // TODO: 오류페이지로 변경
        console.log('Error', err.message);
        // self.$router.push({ name: 'Error' })
      })
    },
    scrolling (event) {
      const scrollInfo = event.target
      if (scrollInfo && scrollInfo.scrollHeight - scrollInfo.scrollTop === scrollInfo.clientHeight) {
        // this.$emit('scroll-end')
        console.log("스크롤 끝!")
        // 실제 요청시 받아올 것임
        // this.readMore()
      }
    },
    onProfileListItem (targetUserId) {
      console.log(targetUserId)
    },
    onUnfollowButton (targetUserId) {
      console.log(targetUserId)
    },
    onFollowButton (targetUserId) {
      console.log(targetUserId)
    },
  }
}
</script>

<style scoped>
</style>