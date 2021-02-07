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
    class="scroll-container">
    <v-col lg="4" md="4" sm="6">
      
      <!-- 게시물 헤더 부분 시작 -->
      <v-list-item
      >
        <v-list-item-content>
          <v-list-item-title>
            <div class="d-flex justify-space-between"
            >
              <!-- 사용자 아이콘, 닉네임 시작 -->
              <div class="text-h6 mouse-hover" @click="onProfileImage">
                <v-list-item-avatar
                  v-if="articleInfo.user.profileImage"
                  size="36"
                  class="my-0"
                >
                  <img
                    :src="imageServerPrefix + articleInfo.user.profileImage.path"
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
                  {{ articleInfo.user.nickname }}
              </div>
              <!-- 사용자 아이콘, 닉네임 끝 -->
              
              <!-- 게시글 수정, 삭제을 선택 할수있는 케밥 버튼 시작 -->
              <v-menu transition="scroll-y-transition">
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

            </div>
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <!-- 게시물 헤더 부분 끝 -->
      
      <!-- 게시물 작성 시간 시작 -->
      <p class="text-overline mb-0">{{articleInfo.wdate | dateFormat()}}</p>
      <!-- 게시물 작성 시간 끝 -->
      
      <!-- 디바이더 -->
      <v-divider class="pb-2"></v-divider>
  
      <!--  -->
      <v-card elevation="10">
        <!-- 위치정보 시작 -->
        <v-card-title>
          <v-icon
            large
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
                class="text-h6"
              >{{ articleInfo.pin.addressName | truncate(10, '...')}}</span>
            </template>
            <span>{{articleInfo.pin.addressName}}</span>
          </v-tooltip>
        </v-card-title>
        <!-- 위치정보 끝 -->

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
          >        
            <v-icon v-if="true" x-large>mdi-heart-outline</v-icon>
            <v-icon v-else x-large color="error">mdi-heart</v-icon>
          </v-btn>
          <!-- 좋아요 버튼 시작 -->
        </div>
        <!-- 캐러셀 + 좋아요 버튼 끝 -->
  
        <v-card-actions>
          <v-row>
            <v-col class="x-overflow-container" >
              <v-chip
                v-for="(hashtag, i) in articleInfo.hashtags" :key="i"
                class="mx-1"
                style="display: inline-block;"
                label
                color="primary"
                @click='clickHashtag(hashtag.contents)'
              >
              #{{ hashtag.contents }}
              </v-chip>
            </v-col>
          </v-row>
        </v-card-actions>

        <v-card-text class="pa-2">
          {{articleInfo.contents}}
        </v-card-text>

        <v-card-text class="pa-1 text-caption">
          좋아요 <span> {{0}}</span>개  스크랩 <span> {{0}}</span>개
        </v-card-text>

      <v-divider class="pb-2"></v-divider>
      </v-card>

      <v-text-field
        v-model="commentInput"
        class="bottom-comment-input ma-0 pa-0"
        label="댓글 달기"
        outlined
        hide-details
      ></v-text-field>
    </v-col>
  </v-row>
</template>

<script>
  import {
    mdiAccount,
    mdiHeart,
  } from '@mdi/js'
  import axios from 'axios'
  import UpdateArticleVue from './UpdateArticle.vue'

  const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'DatailArticle',
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
      loading: true,
      imageServerPrefix: `${SERVER_URL}/images/`,
      icons: {
        mdiAccount,
        mdiHeart,
      },
      drawer: false,
      group: null,
      articleInfo: '',
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
  watch: {
    group () {
      this.drawer = false
    },
  },
  created() {
    window.history.forward()
    function noBack(){window.history.forward()}
    this.fetchData()
    
  },
  methods: {
    fetchData() {
      axios.get(`${SERVER_URL}/articles/`+ this.articleId, this.getToken)
      .then(res => {
        this.articleInfo = res.data
        console.log(this.articleInfo)
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

.relative-container {
  position: relative
}

.bottom-comment-input {
  position: fixed;
  bottom: 2px;
  left: 0;
  background: white;
  width: 100%;
  z-index: 4;
}

.mouse-hover:hover {
  cursor: pointer;
}
</style>