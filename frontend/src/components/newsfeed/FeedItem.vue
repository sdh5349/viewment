<template>
  <v-card elevation="0" ref="infoCard">
    <v-card-title
      class="justify-space-between px-0 py-1"
    >
      
      <!-- 사용자 아이콘 / 이름 / 작성 시간이 들어갈 곳! -->
      <div
        class="mouse-hover"
        @click="onProfileImage"
      >
        <UserProfileImage
          :profileImage="articleInfo.user.profileImage"
          :nickname="articleInfo.user.nickname"
        />
      </div>
      <!-- 사용자 아이콘 / 이름 / 작성 시간이 들어갈 곳! -->

      <!-- 추천 글이면 추천 표시! -->
      <v-chip
        v-if="articleInfo.recommanded"
        label
        color="yellow lighten-2"
      >
          추천
      </v-chip>
      <!-- 추천 글이면 추천 표시! -->
  </v-card-title>

  <v-divider></v-divider>
  <p class="text-overline mb-0">{{articleInfo.wdate | dateFormat()}}</p>
  <!-- <v-row class="ma-0"> -->
  <!-- </v-row> -->

    <!-- 주소 들어갈 곳! -->
  <v-card-subtitle class="pa-0 pb-1">
    <v-icon
      left
    >
      mdi-map-marker
    </v-icon>
    <v-tooltip top>
      <template v-slot:activator="{ on, attrs }">
        <span 
          v-if="articleInfo.pin.addressName"
          v-bind="attrs"
          v-on="on" 
          class="text-body-1"
        >{{ articleInfo.pin.addressName | truncate(10, '...')}}</span>
      </template>
      <span>{{articleInfo.pin.addressName}}</span>
    </v-tooltip>
  </v-card-subtitle>

  <!-- 사진 들어갈 곳 -->
  <v-card-actions class="pa-0 mouse-hover">
    <v-img
      :src="imageServerPrefix + articleInfo.thumbnail.path"
      :lazy-src="imageServerPrefix + articleInfo.thumbnail.path"
      aspect-ratio="1"
      class="grey lighten-2 image-hover"
      @click="onArticleThumbnail(articleInfo.articleId)"
    >
      <!-- 이미지 요청이 길어지면 뜨는 로딩 창 시작 -->
      <template v-slot:placeholder>
        <v-row
          class="fill-height ma-0"
          align="center"
          justify="center"
        >
          <v-progress-circular
            indeterminate
            color="grey lighten-5"
          ></v-progress-circular>
        </v-row>
      </template>
      <!-- 이미지 요청이 길어지면 뜨는 로딩 창 끝 -->
    </v-img>
    <v-btn 
      icon
      class="bottom-right-position"
      @click="onLikeButton"
    >        
      <v-icon v-if="articleInfo.liked" x-large color="error">mdi-heart</v-icon>
      <v-icon v-else x-large color="white">mdi-heart-outline</v-icon>
    </v-btn>
  </v-card-actions>
  <!-- 사진 들어갈 곳 -->

    <!-- 해시태그 시작 -->
    <v-card-actions class="pa-0">
      <v-chip-group
        column
      >
        <v-chip
          v-for="(hashtag, i) in articleInfo.hashtags" :key="i"
          class="mx-1 px-2"
          label
          small
          color="primary"
          @click='clickHashtag(hashtag.contents)'
        >
          # {{ hashtag.contents }}
        </v-chip>
      </v-chip-group>
    </v-card-actions>
    <!-- 해시태그 끝 -->
  </v-card>
</template>

<script>
import axios from 'axios'
import UserProfileImage from '@/components/user/UserProfileImage'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'FeedItem',
  components: {
    UserProfileImage
  },
  props: {
    articleInfo: Object
  },
  filters: {
    truncate(text, length, suffix) {
      if (text.length > length) {
        return text.substring(0, length) + suffix;
      } else {
        return text;
      }
    },
    dateFormat(date) {
      const dateArray = date.split(' ')[0].split('-')
      return dateArray[0]+'년 '+dateArray[1]+'월 '+ dateArray[2]+'일'
    },
  },
  data() {
    return {
      imageServerPrefix: `${SERVER_URL}/images/`,
      loginUserId: sessionStorage.getItem('uid')
    }
  },
  computed: {
    getToken(){
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
    onArticleThumbnail(articleId) {
      this.$router.push({ 
        name: 'DetailArticle', 
        params: { articleId }
      })
    },
    onLikeButton() {
      if (this.articleInfo.liked) {
        axios.delete(`${SERVER_URL}/articles/${this.articleInfo.articleId}/unlike`, this.getToken)
        .then(res => {
          this.articleInfo.liked = !this.articleInfo.liked
          this.articleInfo.likes -= 1 
        })
        .catch(err => {
          console.log(err)
        })
      } else {
        axios.post(`${SERVER_URL}/articles/${this.articleInfo.articleId}/like`, {}, this.getToken)
        .then(res => {
          this.articleInfo.liked = !this.articleInfo.liked
          this.articleInfo.likes += 1 
        })
        .catch(err => {
          console.log(err)
        })
      }
    },
    onFollowButton (targetUser) {      
      if (targetUser.followed) {
        axios.delete(`${SERVER_URL}/users/${this.loginUserId}/followings/${targetUser.userId}`, this.getToken)
        .then(() => {
          this.articleInfo.user.followed = !this.articleInfo.user.followed
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
          this.articleInfo.user.followed = !this.articleInfo.user.followed
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      }
    },
    onProfileImage() {
      console.log(">>")
      this.$router.push({name: 'Profile', params: { profileUserId: this.articleInfo.user.userId }})
    }
  }
}
</script>

<style scoped>
/* 계정설정 버튼의 위치를 설정한다 */
.bottom-right-position {
  position: absolute; 
  bottom: 0.8rem; 
  right: 0.4rem;
  z-index: 1;
}

/* 마우스 호버 시 커서 포인터로 전환 */
.mouse-hover:hover {
  cursor: pointer;
}
</style>