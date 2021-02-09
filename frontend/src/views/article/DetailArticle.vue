<template>
  <!-- 데이터 요청이 완료되지 않았다면 로딩화면 시작 -->	
  
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
  <!-- 데이터 요청이 완료되지 않았다면 로딩화면 끝-->

  <v-row 
    v-else
    justify="center"
    class="scroll-container"
  >
    <v-col lg="4" md="4" sm="6">
      
      <!-- 게시물 헤더 부분 시작 -->
      <v-list-item
        class="pa-0"
      >
        <v-list-item-content class="pa-0">
          <v-list-item-action class="pa-0">
            <v-row
              class="ma-0"
              justify="space-between"
              align="center"
            >
              <!-- 사용자 아이콘, 닉네임 시작 -->
              <UserProfileImage 
                :profile-image="articleInfo.user.profileImage"
                :nickname="articleInfo.user.nickname"
              />
              <!-- 사용자 아이콘, 닉네임 끝 -->
              
              <!-- 게시글 수정, 삭제을 선택 할수있는 케밥 버튼 시작 -->
              <v-menu 
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
                    <v-list-item-title @click='updateArticle'>수정</v-list-item-title>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-title @click='deleteArticle'>삭제</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
              <!-- 게시글 수정, 삭제을 선택 할수있는 케밥 버튼 끝 -->
            </v-row>
          </v-list-item-action>
        </v-list-item-content>
      </v-list-item>
      <!-- 게시물 헤더 부분 끝 -->
      <v-divider class="pb-2"></v-divider>
      
      <!-- 게시물 작성 시간 시작 -->
      <p class="text-overline mb-0">{{articleInfo.wdate | dateFormat()}}</p>
      <!-- 게시물 작성 시간 끝 -->
      
      <!-- 디바이더 -->
  
      <!--  -->
      <v-card elevation="0">
        <!-- 주소정보 시작 -->
        <v-card-title>
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
        </v-card-title>
        <!-- 주소정보 끝 -->

        <!-- 캐러셀 + 좋아요 버튼 시작 -->
        <div class="relative-container">
       
          <!-- 사진을 조회하는 캐러셀 시작 -->
          <v-carousel 
            :show-arrows="false" 
            hide-delimiter-background 
            delimiter-icon="mdi-minus" 
            height="300"
            mouse-drag=true
          >
            <v-carousel-item 
              v-for="(articleImage, idx) in articleInfo.images" 
              :key="idx" 
              :src="imageServerPrefix +  articleImage.path">
            </v-carousel-item>
          </v-carousel>      
          <!-- 사진을 조회하는 캐러셀 끝 -->
          
          <!-- 좋아요 버튼 시작 -->
          <v-btn 
            icon
            class="bottom-left-position"
            @click="onLikeButton"
          >        
            <v-icon v-if="articleInfo.liked" x-large color="error">mdi-heart</v-icon>
            <v-icon v-else x-large color="white">mdi-heart-outline</v-icon>
          </v-btn>
          <!-- 좋아요 버튼 시작 -->

        </div>
        <!-- 캐러셀 + 좋아요 버튼 끝 -->

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

        <!-- 게시글 내용 시작 -->
        <v-card-text class="pa-2">
          {{articleInfo.contents}}
        </v-card-text>
        <!-- 게시글 내용 끝 -->

        <!-- 게시글 좋아요 수, 스크랩 수 -->
        <v-card-actions class="pa-1">

          <!-- 게시글 좋아요 유저 리스트를 띄우는 모달창 버튼 시작 -->
          <v-dialog
            v-model="dialog"
            scrollable
            max-width="40vh"
          >
            <template v-slot:activator="{ on, attrs }">
              <!-- 좋아요가 하나도 없으면 버튼이 활성화되지 않는다 -->
              <v-btn
                small
                :class="{ 'disable-events': !articleInfo.likes }"
                text
                v-bind="attrs"
                v-on="on"
              >
                좋아요 {{articleInfo.likes}} 개
              </v-btn>
            </template>
            <v-card>
              <v-card-text class="like-user-container">  
                <ArticleLikeUserList 
                  :article-id="articleId"
                />
              </v-card-text>
            </v-card>
          </v-dialog>
          <!-- 게시글 좋아요 유저 리스트를 띄우는 모달창 버튼 끝 -->
        </v-card-actions>
        <!-- 게시글 좋아요 수, 스크랩 수 -->
        <v-divider class="mb-2"></v-divider>
        <v-text-field
          v-model="commentInput"
          class="ma-0 pa-1"
          label="댓글 달기"
          append-icon="mdi-pencil"
          dense
          outlined
          hide-details
          @click:append="onCreateReply"
          @keypress.enter="onCreateReply"
        ></v-text-field>
        <ReplyList
          v-if="articleInfo.replies.length > 0"
          :replies="articleInfo.replies"
          :profileUserId="articleInfo.user.userId"
          :loginUserId="loginUserId"
          replyType="reply"
        />
        <p 
          v-else
          class="text-caption pa-2"
        >아직 댓글이 없어요... 첫 댓글을 달아주세요</p>
        
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
  import axios from 'axios'
  import UserProfileImage from '@/components/user/UserProfileImage'
  import ArticleLikeUserList from '@/components/user/ArticleLikeUserList'
  import ReplyList from '@/components/reply/ReplyList'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'DatailArticle',
  components: {
    ArticleLikeUserList,
    ReplyList,
    UserProfileImage,
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
  props: {
    articleId: [Number, String]
  },
  data() {
    return{
      dialog: false,
      commentInput: '',
      loading: true,
      imageServerPrefix: `${SERVER_URL}/images/`,
      articleInfo: '',
      loginUserId: '',
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
    window.history.forward()
    function noBack(){window.history.forward()}
    this.fetchData()
    this.loginUserId = sessionStorage.getItem('uid')
  },
  methods: {
    fetchData() {
      axios.get(`${SERVER_URL}/articles/`+ this.articleId, this.getToken)
      .then(res => {
        this.articleInfo = res.data
        console.log(this.articleInfo)
        // TODO: 지금 접속한 유저의 정보를 불러와 해당 유저가 이 게시물을 좋아요 하는지 알아내야함
      })
      .then(() => {
        this.loading = false
      })
      .catch(err => {
        alert(err)
      })
    },
    updateArticle(){
      this.$router.push({name: 'UpdateArticle', params: {
        articleId: this.articleId,
        hashtagArray: this.articleInfo.hashtags,
        contents: this.articleInfo.contents,
        lat: this.articleInfo.pin.lat,
        lng: this.articleInfo.pin.lng,
        preview: this.articleInfo.images
      }})
    },
    deleteArticle(){
      axios.delete(`${SERVER_URL}/articles/`+ this.articleId, this.getToken )
      .then((res) => {
        alert('게시물 삭제 완료')
        this.$router.push({name: 'Feed'})
      })
    },
    clickHashtag(res){
      this.$router.push({name: 'Search', params: {
        clickedHash: res
      }})
    },
    onCreateReply() {
      if (this.commentInput) {
        const params = {
          'articleId': this.articleInfo.articleId, 
          'contents': this.commentInput, 
          'userId': this.loginUserId
        }
  
        axios.post(`${SERVER_URL}/replies`, params, this.getToken)
        .then(()=> {
          this.fetchData()
        })
        .then(() => {
          this.commentInput = ''
        })
        .catch(() => {
        })
      }
    },
    onProfileImage() {
      this.$router.push({ 
        name: 'Profile', 
        params: { profileUserId : this.articleInfo.user.userId }
      })
      .catch (err=>{
        if(err.name === "NavigationDuplicated" ){
          location.reload();
        }
      })
    },
    onLikeButton() {
      if (this.articleInfo.liked) {
        axios.delete(`${SERVER_URL}/articles/${this.articleId}/unlike`, this.getToken)
        .then(res => {
          this.articleInfo.liked = !this.articleInfo.liked
          this.articleInfo.likes -= 1 
        })
        .catch(err => {
          console.log(err)
        })
      } else {
        axios.post(`${SERVER_URL}/articles/${this.articleId}/like`, {}, this.getToken)
        .then(res => {
          this.articleInfo.liked = !this.articleInfo.liked
          this.articleInfo.likes += 1 
        })
        .catch(err => {
          console.log(err)

        })
      }
    }
  },
}
</script>

<style scoped>
/* 가로로 컨텐츠가 넘칠 땐 다음 컨테이너로 가로 스크롤을 사용한다 */
.x-overflow-container {
  overflow-x: hidden;
  overflow-y: hidden;
  white-space: nowrap;
}

/* 스크롤 컨테이너 안의 아이템이 넘쳐도 스크롤 컨테이너의 크기는 고정 */
.scroll-container {
  height: 100%;
  overflow: hidden;
  margin-bottom: 50px;
}

/* 계정설정 버튼의 위치를 설정한다 */
.bottom-left-position {
  position: absolute; 
  bottom: 0.4rem; 
  left: 0.4rem;
  z-index: 1;
}

/* 컨테이너를 relative position으로 바꾼다. */
.relative-container {
  position: relative
}

.bottom-comment-input {
  position: fixed;
  bottom: 2px;
  left: 0;
  background: white;
  width: 100%;
  z-index: 5;
}

.mouse-hover:hover {
  cursor: pointer;
}

/* 마우스를 버튼에 올려도 마우스 커서가 활성화 되지 않는다 */
.disable-events {
  pointer-events: none
}

/* 게시글 좋아요 누른 사람들을 보여줄 모달 창의 컨테이너 */
.like-user-container {
  height: 70vh;
  padding: 12px 0 12px 0 !important;
}
</style>