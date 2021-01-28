<template>
  <v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
    <h1>프로필</h1>


    <!-- 프로필 카드 시작 -->
    <v-card
      outlined
      elevation="2"
      class="py-3"
    >
      <v-list-item three-line>
        <v-card
          elevation="0"
        >
          <v-list-item-avatar
            v-if="profileUserInfo.user.profileImage"
            size="80"
          >
            <img
              src=""
            >
          </v-list-item-avatar>
          <v-icon
            v-else
            color="primary"
            size="85"
          >mdi-account-circle</v-icon>
          
          <!-- 계정설정 버튼 시작 -->
          <v-btn
            v-if="loginUserId === profileUserId"
            class="no-background-color top-right-position"
            x-small
            fab 
            elevation="0"
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
        </v-card>

        <v-list-item-content
          class="ml-2"
        >
          <v-list-item-title
            align="right"
            class="headline mb-2"
          >
            {{ profileUserInfo.user.nickname}}
          </v-list-item-title>
          <v-list-item-subtitle>
            {{ profileUserInfo.user.message}}
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
        {{ profileUserInfo.articleCount }}
        <br/>
        게시글
      </v-btn>
      <v-btn 
        text 
        class="col-3"
        @click="onFollowerListButton"  
      >
        {{ profileUserInfo.followerCount }}
        <br/>
        팔로워
      </v-btn>
      <v-btn 
        text 
        class="col-3"
        @click="onFollowingListButton"
      >
        {{ profileUserInfo.followingCount }}
        <br/>
        팔로잉
      </v-btn>
      <v-btn 
        text 
        class="col-3"
        @click="onMemoryListButton"
      >
        {{ profileUserInfo.memoryCount }}
        <br/>
        기억장소
      </v-btn>
    </v-row>
    <!-- 게시글, 팔로워 리스트, 팔로잉 리스트, 기억장소 리스트 끝 -->


    <!-- 프로필 편집/팔로잉하기/팔로우 취소하기 버튼 시작 -->
    <v-btn
      v-if="loginUserId !== profileUserId"
      class="my-4"
      :color="this.profileUserInfo.isFollowed ? 'error' : 'primary'"
      block
      @click="onFollowButton"
    >
      <span v-if="this.profileUserInfo.isFollowed">언팔로우</span>
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
        <v-card flat>
          <v-card-text>
            <!-- keep-alive 태그를 통해 탭 컴포넌트를 바꿀 때마다 재 생성하는 것이 아닌 데이터를 캐시해두고 다시 볼수있도록 하는 태그 -->
            <!-- TODO: 일단 없이 해보고 필요하다면 사용할 것임 -->
            <!-- <keep-alive>  -->
            <component v-bind:is="tabItem.content"></component>
            <!-- </keep-alive> -->
          </v-card-text>
        </v-card>
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

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'Profile',
  components: {
    UserArticleGrid,
    UserArticleMap,
  },
  props: {
    profileUserId: String,
  },
  data() {
    return {
      loginUserId : '',
      profileUserInfo : null,
      tab : null,
      tabItems: [
        { tabIcon: 'mdi-view-module', content: 'UserArticleGrid' },
        { tabIcon: 'mdi-map-marker', content: 'UserArticleMap' }
      ]
    }
  },
  created() {
    this.dataFetch()
  },
  methods: {
    dataFetch() {
      axios.get(`${SERVER_URL}/users/${this.profileUserId}/page`)
      .then(res => {
        // 현재 보고있는 프로필 페이지 유저의 정보 초기화
        this.profileUserInfo = res.data
        // 현재 로그인한 유저의 uid 초기화
        this.loginUserId = sessionStorage.getItem('uid')
      })
      .catch(err => {
        alert("오류"); // TODO: 오류페이지로 변경
        console.log('Error', err.message);
        // self.$router.push({ name: 'Error' })
      })
    },
    onFollowButton() {
      if (this.profileUserInfo.isFollowed) {
        axios.delete(`${SERVER_URL}/users/${this.loginUserId}/follows/${this.profileUserId}`)
        .then(() => {
          this.profileUserInfo.followingCount -= 1
          this.profileUserInfo.isFollowed = !this.profileUserInfo.isFollowed
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      } else {
        var params = {'targetUserId' : this.profileUserId }
        axios.post(`${SERVER_URL}/users/${this.loginUserId}/follows`, params)
        .then(() => {
          this.profileUserInfo.followingCount += 1
          this.profileUserInfo.isFollowed = !this.profileUserInfo.isFollowed
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
      this.$router.push({ 
        name: 'EditProfile', 
        params: {
          profileUserInfo : this.profileUserInfo,
        }
      })
    },
    onEditAccountButton() {
      this.$router.push({ 
        name: 'EditAccount', 
        params: {
          profileUserId : this.profileUserId,
        }
      })
    },
  }
}
</script>

<style scoped>
/* 마우스를 버튼에 올려도 마우스 커서가 활성화 되지 않는다 */
  .disable-events {
    pointer-events: none
  }

/* 배경을 사용하지 않는다 */
  .no-background-color {
    background-color: transparent !important;
  }

/* 계정설정 버튼의 위치를 설정한다 */
  .top-right-position {
    position: absolute; 
    bottom: 0px; 
    right: 0px;
  }
</style>