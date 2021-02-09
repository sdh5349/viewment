<template>
  <v-row
    v-if="loading"
    style="height: 50vh;"
    class="fill-height ma-0"
    align="center"
    justify="center"
  >
    <v-progress-circular
      indeterminate
      color="primary"
    ></v-progress-circular>
  </v-row>
  <v-virtual-scroll
    v-else
    :items="articleLikeUsers"
    :item-height="50"
    @scroll.native="scrolling"
  >
    <template v-slot:default="{ item }">
      <v-list-item
        @click="onProfileListItem(item.userId)"
      >
        <v-list-item-content class="pa-0">
          <v-list-item-title>
            <div class="d-flex justify-space-between">
              
              <!-- 아이콘 or 프로필 썸네일, 사용자 닉네임 시작 -->
              <UserProfileImage
                :profile-image="item.profileImage"
                :nickname="item.nickname"
                :size="2.7"
              />
              <!-- 아이콘 or 프로필 썸네일, 사용자 닉네임 끝 -->

              <!-- 팔로우/언팔로우 버튼 시작 -->
              <v-btn
                v-if="item.followed && item.userId !== loginUserId"
                small 
                class="align-self-center px-0"
                width="55"
                elevation="1" 
                @click.stop="onFollowButton(item)"
              >
                언팔로우
              </v-btn>
              <v-btn
                v-else-if="item.userId !== loginUserId"
                small 
                class="align-self-center px-0" 
                width="55"
                color="primary" 
                elevation="1" 
                @click.stop="onFollowButton(item)"
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
import UserProfileImage from '@/components/user/UserProfileImage'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'ArticleLikeUserList',
  components: {
    UserProfileImage
  },
  props: {
    articleId: [String, Number]
  },
  data() {
    return {
      loading: true,
      loginUserId: '',
      articleLikeUsers: [],
      imageServerPrefix: `${SERVER_URL}/images/`,
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
  created() {
    // 현재 프로필 사용자의 팔로워 정보를 원하는 갯수 만큼 요청하는 메서드 readMore와 동일하나 loading 변수를 한번만 false로 할당하기위해
    this.loginUserId = sessionStorage.getItem('uid')
    this.readMore()
  },
  methods: {
    // 현재 게시글을 좋아요 누른사람의 정보를 원하는 갯수 만큼 요청하는 메서드
    readMore() {
      // 필요한 데이터 가져오기
      this.loading = true
      
      axios.get(`${SERVER_URL}/articles/${this.articleId}/like-users?page=${this.page}&size=${this.size}`, this.getToken)
      .then(res => {
        this.articleLikeUsers.push(...res.data.content)
        this.page += 1
        this.last = res.data.last
      })
      .then(() => {
        this.loading = false
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
        axios.delete(`${SERVER_URL}/users/${this.loginUserId}/followers/${targetUser.userId}`, this.getToken)
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
        axios.delete(`${SERVER_URL}/users/${this.loginUserId}/followings/${targetUser.userId}`, this.getToken)
        .then(() => {
          this.followers[targetUserIdx].followed = !this.followers[targetUserIdx].followed
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

<style>

</style>