<template>
  <v-row v-if="loading">
    <v-progress-circular
      :width="3"
      color="red"
      indeterminate
    ></v-progress-circular>
  </v-row>
  <v-row
    v-else
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >

    <!-- 프로필 카드 시작 -->
    <v-card
      outlined
      elevation="2"
      class="py-3"
    >
      <!-- 프로필 사진 및 사용자 본인인 경우 사용자 설정 버튼 -->
      <v-list-item three-line>
        <div class="relative-container">
            <v-avatar
            size="6rem"
          >
            <img
              v-if="this.profileUserInfo.profileImage"
              :src="profileImageUrl"
            >
            <img
              v-else
              src="@/assets/images/account.png"
            >
          </v-avatar>
          <!-- 계정설정 버튼 시작 -->
          <v-btn
            v-if="loginUserId === profileUserId"
            class="bottom-right-position"
            x-small
            icon
            @click="onEditAccountButton"
          >
            <v-icon
            large
            color="grey"
            >
            mdi-cog
            </v-icon>
          </v-btn>
          <!-- 계정설정 버튼 끝 -->
        </div>

        <v-list-item-content
          class="ml-2"
        >
          <v-list-item-title
            align="right"
            class="headline mb-2"
          >
            {{ profileUserInfo.nickname}}
          </v-list-item-title>
          <v-list-item-subtitle>
            {{ profileUserInfo.message}}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-card>    
    <!-- 프로필 카드 끝 -->
    

    <!-- 게시글, 팔로워 리스트, 팔로잉 리스트, 기억장소 리스트 시작 -->
    <v-row class="my-4 px-4">
      <v-btn
        class="col-3 disable-events"
        text
      >
        {{ profileUserInfo.countArticles }}
        <br/>
        게시글
      </v-btn>
      <v-btn 
        text 
        class="col-3"
        @click="onFollowerListButton"  
      >
        {{ profileUserInfo.countFollowers }}
        <br/>
        팔로워
      </v-btn>
      <v-btn 
        text 
        class="col-3"
        @click="onFollowingListButton"
      >
        {{ profileUserInfo.countFollowings }}
        <br/>
        팔로잉
      </v-btn>
      <v-btn 
        text 
        class="col-3"
        @click="onMemoryListButton"
      >
        {{ profileUserInfo.countMemories }}
        <br/>
        기억장소
      </v-btn>
    </v-row>
    <!-- 게시글, 팔로워 리스트, 팔로잉 리스트, 기억장소 리스트 끝 -->


    <!-- 프로필 편집/팔로잉하기/팔로우 취소하기 버튼 시작 -->
    <v-btn
      v-if="loginUserId !== profileUserId"
      class="my-4"
      :color="profileUserInfo.followed ? 'error' : 'primary'"
      block
      @click="onFollowButton"
    >
      <span v-if="profileUserInfo.followed">언팔로우</span>
      <span v-else>팔로우</span>
    </v-btn>
    <v-btn
      v-else
      class="my-4"
      color="success"
      block
      @click="onEditProfileButton"
    >프로필 편집
    </v-btn>
    <!-- 프로필 편집/팔로잉하기/팔로우 취소하기 버튼 끝 -->


    <!-- 피드, 지도 탭 시작 -->

    <!-- 탭 시작 -->
    <v-tabs v-model="tab" grow>
      <v-tabs-slider color="grey"></v-tabs-slider>
      <v-tab v-for="tabItem in tabItems" :key="tabItem.tabIcon">
        <v-icon>{{ tabItem.tabIcon }}</v-icon>
      </v-tab>
    </v-tabs>
    <!-- 탭 끝 -->

    <!-- 탭 선택에 따라 보여줄 컴포넌트 -->
    <v-tabs-items v-model="tab">
      <v-tab-item v-for="tabItem in tabItems" :key="tabItem.tabIcon">
        <!-- keep-alive 태그를 통해 탭 컴포넌트를 바꿀 때마다 재 생성하는 것이 아닌 데이터를 캐시해두고 다시 볼수있도록 하는 태그 -->
        <!-- TODO: 일단 없이 해보고 필요하다면 사용할 것임 -->
        <!-- <keep-alive>  -->
        <component class="mt-1 mx-0" v-bind:is="tabItem.content" :profile-user-id="profileUserId"></component>
        <!-- </keep-alive> -->
      </v-tab-item>
    </v-tabs-items>
    <!-- 탭 선택에 따라 보여줄 컴포넌트 끝 -->

    <!-- 피드, 지도 탭 끝 -->


    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'
import UserArticleGrid from '@/components/user/UserArticleGrid'
import UserArticleMap from '@/components/user/UserArticleMap'
import UserProfileImage from '@/components/user/UserProfileImage'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'Profile',
  components: {
    UserArticleGrid,
    UserArticleMap,
    UserProfileImage,
  },
  props: {
    profileUserId: String,
  },
  data() {
    return {
      loading: true,
      loginUserId: '',
      profileUserInfo: null,
      profileImageUrl: null,
      tab: null,
      tabItems: [
        { tabIcon: 'mdi-view-module', content: 'UserArticleGrid' },
        { tabIcon: 'mdi-map-marker', content: 'UserArticleMap' }
      ]
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
  watch: {
    profileUserId: function() {
      this.loading = true
      this.fetchData()
    },
  },
  created() {
    this.fetchData()
    console.log("새로 생성되었어요")
  },
  methods: {
    // 데이터 초기화 메서드
    fetchData() {
      axios.get(`${SERVER_URL}/users/${this.profileUserId}/page`, this.getToken)
      .then(res => {
        // 현재 보고있는 프로필 페이지 유저의 정보 초기화
        this.profileUserInfo = res.data
        console.log(res.data)
        // 현재 로그인한 유저의 uid 초기화
        this.loginUserId = sessionStorage.getItem('uid')
        if (this.profileUserInfo.profileImage) {
          this.profileImageUrl = `${SERVER_URL}/images/${this.profileUserInfo.profileImage.path}`
        } else {
          // a 유저 프로필을 본후 b 유저 프로필로 이동한뒤 뒤로가기를 누르면 a유저 프로필 페이지 임에도 불구하고
          // DOM에 b 유저 사진 데이터가 남아 있으므로 사진이 없는 경우 null 처리 해야함
          this.profileImageUrl = null
        }
        console.log(this.profileUserInfo)
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
    onFollowButton() {
      if (this.profileUserInfo.followed) {
        axios.delete(`${SERVER_URL}/users/${this.loginUserId}/followings/${this.profileUserId}`, this.getToken)
        .then(() => {
          this.profileUserInfo.countFollowers -= 1
          this.profileUserInfo.followed = !this.profileUserInfo.followed
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      } else {
        var params = {'targetUserId' : this.profileUserId }
        axios.post(`${SERVER_URL}/users/${this.loginUserId}/follow`, params, this.getToken)
        .then(() => {
          this.profileUserInfo.countFollowers += 1
          this.profileUserInfo.followed = !this.profileUserInfo.followed
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      }
    },
    onFollowerListButton() {
      this.$router.push({ 
        name: 'FollowList', 
        params: {
          profileUserId : this.profileUserId,
          activeTabId : 0
        }
      })
    },
    onFollowingListButton() {
      this.$router.push({ 
        name: 'FollowList', 
        params: {
          profileUserId : this.profileUserId,
          activeTabId : 1
        } 
      })
    },
    onMemoryListButton() {
      this.$router.push({ 
        name: 'MemoryList', 
        params: {
          profileUserId : this.profileUserId,
        }
      })
    },
    onEditProfileButton() {
      this.$router.push({ name: 'EditProfile' })
    },
    onEditAccountButton() {
      this.$router.push({ name: 'EditAccount' })
    },
  }
}
</script>

<style scoped>
/* 마우스를 버튼에 올려도 마우스 커서가 활성화 되지 않는다 */
  .disable-events {
    pointer-events: none
  }

/* 계정설정 버튼의 위치를 설정한다 */
  .bottom-right-position {
    position: absolute; 
    bottom: 0; 
    right: 0;
  }

/* 컨테이너를 relative position으로 바꾼다. */
.relative-container {
  position: relative
}
</style>