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
        <!-- <v-list-item-avatar
          v-if="userinfo.user.profileImage"
          size="80"
        > -->
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
    

    <!-- 게시글, 팔로잉, 팔로워, 기억장소 시작 -->
    <v-row class="my-4 px-4">
      <v-btn 
        class="col-3"
        text
      >
        {{ profileUserInfo.articleCount }}
        <br/>
        게시글
      </v-btn>
      <v-btn text class="col-3">
        {{ profileUserInfo.followingCount }}
        <br/>
        팔로잉
      </v-btn>
      <v-btn text class="col-3">
        {{ profileUserInfo.followerCount }}
        <br/>
        팔로워
      </v-btn>
      <v-btn text class="col-3">
        {{ profileUserInfo.memoryCount }}
        <br/>
        기억장소
      </v-btn>
    </v-row>
    <!-- 게시글, 팔로잉, 팔로워, 기억장소 끝 -->


    <!-- 프로필 편집/팔로잉하기/팔로우 취소하기 버튼 시작 -->
    <v-btn
      v-if="loginUserId !== profileUserInfo.user.userId"
      class="my-4"
      :color="profileUserfollowInfo.isFollow ? 'error' : 'primary'"
      block
      @click="onFollowButton"
    >
      <span v-if="profileUserfollowInfo.isFollow">언팔로우</span>
      <span v-else>팔로우</span>
    </v-btn>
    <v-btn
      v-else
      class="my-4"
      color="success"
      block
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
    userid: Number
  },
  data() {
    return {
      loginUserId : '',
      profileUserInfo : {
        "user": {
          "userId": "string",
          "nickname": "string",
          "email": "string",
          "message": "string",
          "profileImage": null
        },
        "followingCount": 0,
        "followerCount": 0,
        "articleCount": 0,
        "memoryCount": 0,
        "articleImages": [
          {
            "imageId": "string", 
            "path": "string"
          }
        ]
      },
      profileUserfollowInfo : {
        "followers": [{
          "userId": "string",
          "nickname": "string",
          "email": "string",
          "message": "string",
          "isFollow": false,
          "profileImage": {
            "imageId": "string",
            "path": "string" 
          }
          }],
        "followings": [{
          "userId": "string",
          "nickname": "string",
          "email": "string",
          "message": "string",
          "isFollow": false,
          "profileImage": {
            "imageId": "string",
            "path": "string" 
          }
        }],
        "isFollow": false,
      },
      tab : null,
      tabItems: [
        { tabIcon: 'mdi-view-module', content: 'UserArticleGrid' },
        { tabIcon: 'mdi-map-marker', content: 'UserArticleMap' }
      ]
    }
  },
  created() {
    // 데이터 초기화
    // this.getUserProfile()
  },
  methods: {
    dataFetch() {
      // 필요한 데이터 가져오기
      axios.get(`${SERVER_URL}/users/${this.userid}/page`)
      .then(res => {
        // 현재 보고있는 프로필 페이지 유저의 정보 초기화
        this.profileUserInfo = res.data
        // 현재 로그인한 유저의 uid 초기화
        this.loginUserId = sessionStorage.getItem('uid')

        axios.get(`${SERVER_URL}/users/${this.loginUserId}/follows/${this.profileUserInfo.user.userId}`)
        .then(res => {
          // 현재 보고있는 프로필 페이지 유저의 팔로우 관련 정보 초기화
          this.profileUserfollowInfo = res.data
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      })
      .catch(err => {
        alert("오류"); // TODO: 오류페이지로 변경
        console.log('Error', err.message);
        // self.$router.push({ name: 'Error' })
      })
    },
    onFollowButton() {
      this.profileUserfollowInfo.isFollow = !this.profileUserfollowInfo.isFollow
      // if (this.profileUserfollowInfo.isFollow) {
      //   axios.delete(`${SERVER_URL}/users/:userId/follows/:followUid`)
      //   .then(() => {
      //     this.profileUserfollowInfo.isFollow = !this.profileUserfollowInfo.isFollow
      //   })
      //   .catch(err => {
      //     alert("오류"); // TODO: 오류페이지로 변경
      //     console.log('Error', err.message);
      //     // self.$router.push({ name: 'Error' })
      //   })
      // } else {
      //   var params = {'followUid' : this.profileUserInfo.user.userId }
      //   axios.post(`${SERVER_URL}/users/:userId/follows`, params)
      //   .then(() => {
      //     this.profileUserfollowInfo.isFollow = !this.profileUserfollowInfo.isFollow
      //   })
      //   .catch(err => {
      //     alert("오류"); // TODO: 오류페이지로 변경
      //     console.log('Error', err.message);
      //     // self.$router.push({ name: 'Error' })
      //   })
      // }
    }
  }
}
</script>

<style>

</style>