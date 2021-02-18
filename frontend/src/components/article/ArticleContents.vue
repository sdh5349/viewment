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
  <div
    v-else
  >
    <!-- 게시물 헤더 부분 시작 -->
    <v-list-item
      class="pa-0 mx-3"
    >
      <v-list-item-content class="pa-0">
        <v-list-item-action class="pa-0 ma-0">
          <v-row
            class="ma-0"
            justify="space-between"
            align="center"
          >
            <div
              class="mouse-hover"
              @click="onProfileImage"
            >
              <!-- 사용자 아이콘, 닉네임 시작 -->
              <UserProfileImage 
                :profile-image="articleInfo.user.profileImage"
                :nickname="articleInfo.user.nickname"
                :size="2.3"
                :write-date="articleInfo.wdate | dateFormat()"
              />
            <!-- 사용자 아이콘, 닉네임 끝 -->
            </div>
            
            <!-- 게시글 수정, 삭제을 선택 할수있는 케밥 버튼 시작 -->
            <v-menu
              v-if="loginUserId === articleInfo.user.userId && !isNotDetail"
              transition="scroll-y-transition"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  class="m-0"
                  v-bind="attrs"
                  v-on="on"
                  icon
                >
                  <v-icon>mdi-dots-vertical</v-icon>
                </v-btn>
              </template>
              <v-list class="py-0">
                <v-list-item>
                  <v-list-item-title class="mouse-hover" @click='updateArticle'>수정</v-list-item-title>
                </v-list-item>
                <v-list-item>
                  <v-list-item-title class="mouse-hover" @click='deleteArticle'>삭제</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>
            <div v-else>
              <v-img
                max-width="2rem"
                height="auto"
                src="@/assets/images/recommend.png"
              >
              </v-img>
            </div>
            <!-- 게시글 수정, 삭제을 선택 할수있는 케밥 버튼 끝 -->
          </v-row>
        </v-list-item-action>
      </v-list-item-content>
    </v-list-item>
    <!-- 게시물 헤더 부분 끝 -->
  
    <!-- 디바이더 -->
    <v-divider class="pb-2 "></v-divider>

    <v-card elevation="0">
      <!-- 주소정보 시작 -->
      <v-card-title class="pa-0 pb-1">
        <v-icon
          class="ml-3"
          left
        >
          mdi-map-marker
        </v-icon>
        <v-tooltip v-model="showTip" top>
          <template v-slot:activator="{ attrs }">
            <span 
              v-if="articleInfo.pin.addressName"
              v-bind="attrs"
              @click="onTooltip" 
              style="font-size: 1rem;"
            >{{ articleInfo.pin.addressName | truncate(15, '...')}}</span>
          </template>
          <span>{{articleInfo.pin.addressName}}</span>
        </v-tooltip>
      </v-card-title>
      <!-- 주소정보 끝 -->
    
      <!-- 사진을 조회하는 캐러셀 시작 -->
      <div style="position: relative;">
        <v-carousel
          style="width: 100%;"
          height="auto"
          :show-arrows="false"
          hide-delimiter-background  
          mouse-drag=true
        >
          <v-carousel-item 
            v-for="(articleImage, idx) in articleInfo.images"
            :key="idx" 
            style="max-width: 100%; height: auto;"
          >
          <v-img
            :aspect-ratio="1"
            style="max-width: 100%;"
            :src="imageServerPrefix +  articleImage.path"
          ></v-img>
          </v-carousel-item>
        </v-carousel>

        <!-- 필름 사진과 같은 날짜 효과 시작 -->
        <span class="digital-font">{{articleInfo.date}}</span>
        <!-- 필름 사진과 같은 날짜 효과 끝 -->

      </div>

      <!-- 디테일 페이지가 아닌 피드에서 넘어오는 경우 다음 버튼을 누르면 디테일 페이지로 이동한다 -->
      <div v-if="isNotDetail" class="d-flex justify-end">
        <v-btn class="px-0" height="20" dense text right @click="onMoreButton">...더 보기</v-btn>
      </div>
      <!-- 디테일 페이지가 아닌 피드에서 넘어오는 경우 다음 버튼을 누르면 디테일 페이지로 이동한다 -->

      <!-- 사진을 조회하는 캐러셀 끝 -->

      <!-- 해시태그 시작 -->
      <v-card-actions class="pa-0 mx-3">
        <v-chip-group
          column
        >
          <v-chip
            v-for="(hashtag, i) in articleInfo.hashtags" :key="i"
            class="mx-1 my-1 px-2  text-body-1"
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

      <!-- 게시글 내용 시작 -->
      <p v-if="articleInfo.contents" class="pa-2 mx-2 mb-0 text-body-1">
        {{articleInfo.contents}}
      </p>
      <!-- 게시글 내용 끝 -->

      <!-- 게시글 좋아요 수, 좋아요 버튼 시작 -->
      <div class="ml-3 d-flex justify-space-between align-center">

        <!-- 게시글 좋아요 유저 리스트를 띄우는 모달창 버튼 시작 -->
        <v-dialog
          v-model="dialog"
          scrollable
          max-width="40vh"
        >
          <template v-slot:activator="{ on, attrs }">
            <!-- 좋아요가 하나도 없으면 버튼이 활성화되지 않는다 -->
            <v-btn
              v-bind="attrs"
              small
              :class="!articleInfo.likes ? 'disable-events pl-1 text-body-1' : 'pl-1 text-body-1'"
              text
              v-on="on"
            >
              좋아요 {{articleInfo.likes}} 개
            </v-btn>
          </template>
          <v-card>
            <v-card-title>좋아요</v-card-title>
            <v-divider></v-divider>
            <v-card-text class="pa-1 like-user-container">  
              <ArticleLikeUserList 
                :article-id="articleInfo.articleId"
              />
            </v-card-text>
            <v-divider></v-divider>
            <v-card-actions>
              <v-btn text @click="dialog=false">
                닫기
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <!-- 게시글 좋아요 유저 리스트를 띄우는 모달창 버튼 끝 -->

        <!-- 좋아요 버튼 시작 -->
        <v-btn 
          icon
          class="mx-3"
          @click="onLikeButton"
        >        
          <v-icon v-if="articleInfo.liked" x-large color="error">mdi-heart</v-icon>
          <v-icon v-else x-large>mdi-heart-outline</v-icon>
        </v-btn>
        <!-- 좋아요 버튼 시작 -->

      </div>
      <!-- 게시글 좋아요 수, 좋아요 버튼 끝 -->
    </v-card>
  </div>
</template>

<script>
import axios from 'axios'
import UserProfileImage from '@/components/user/UserProfileImage'
import ArticleLikeUserList from '@/components/user/ArticleLikeUserList'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'ArticleContents',
  components: {
    ArticleLikeUserList,
    UserProfileImage,
  },
  props: {
    article: Object,
    isNotDetail: {
      type: Boolean,
      default: false,
    }
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
    return{
      loading: true,
      dialog: false,
      imageServerPrefix: `${SERVER_URL}/images/`,
      articleInfo: '',
      loginUserId: sessionStorage.getItem('uid'),
      showTip: false
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
  created() {
    this.articleInfo = this.article
    this.loading = false
  },
  methods: {
    updateArticle(){
      this.$router.replace({name: 'UpdateArticle', params: {
        articleId: this.articleInfo.articleId,
        hashtagArray: this.articleInfo.hashtags,
        contents: this.articleInfo.contents,
        lat: this.articleInfo.pin.lat,
        lng: this.articleInfo.pin.lng,
        preview: this.articleInfo.images,
        addressName: this.articleInfo.addressName,
        date: this.articleInfo.date
      }})
    },
    deleteArticle(){
      if (confirm('게시물을 삭제하시겠습니까?')) {
        axios.delete(`${SERVER_URL}/articles/`+ this.articleInfo.articleId, this.getToken )
        .then(res => { 
          alert('게시물 삭제 완료')
          this.$router.push({name: 'Profile', params: {profileUserId: sessionStorage.getItem('uid')}})
        })
      }
    },
    clickHashtag(res){
      this.$router.push({name: 'SearchHashtagGrid', params: {
        clickedHash: res
      }})
    },
    onProfileImage() {
      this.$router.push({ 
        name: 'Profile', 
        params: { profileUserId : this.articleInfo.user.userId }
      })
    },
    onLikeButton() {
      if (this.articleInfo.liked) {
        this.articleInfo.liked = !this.articleInfo.liked
        this.articleInfo.likes -= 1 

        axios.delete(`${SERVER_URL}/articles/${this.articleInfo.articleId}/unlike`, this.getToken)
        .then(res => {
        })
        .catch(err => {
          console.log(err)
        })
      } else {
        this.articleInfo.liked = !this.articleInfo.liked
        this.articleInfo.likes += 1 
        
        axios.post(`${SERVER_URL}/articles/${this.articleInfo.articleId}/like`, {}, this.getToken)
        .then(res => {
        })
        .catch(err => {
          console.log(err)

        })
      }
    },
    onMoreButton() {
      this.$router.push({ 
        name: 'DetailArticle', 
        params: {
          articleId: this.articleInfo.articleId,
        }
      })
    },
    onTooltip() {
      this.showTip = true;
     setTimeout(() => {
        this.showTip = false
      }, 1000)
    }
  },
}
</script>

<style scoped>
@font-face {
  font-family: "Digital";
  src: local("Digital"),
   url(../../assets/fonts/digital.ttf) format("truetype");
}

.digital-font { 
  font-family: "Digital", Helvetica, Arial !important;
  font-size: 15px;
  font-weight: bold;
  position: absolute;
  text-shadow:1px 1px 10px #d4bd55;
  bottom: 0.2rem;
  right: 0.5rem;
  color: #d4bd55;
}

/* 마우스를 버튼에 올리면 포인터로 활성화 된다 */
.mouse-hover:hover {
  cursor: pointer;
}

/* 마우스를 버튼에 올려도 마우스 커서가 활성화 되지 않는다 */
.disable-events {
  pointer-events: none
}

/* 게시글 좋아요 누른 사람들을 보여줄 모달 창의 컨테이너 */
.like-user-container {
  /* height: 70vh;
  padding: 12px 0 12px 0 !important; */
  height: 300px;
}
</style>