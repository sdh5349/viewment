<template>

<v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
  <div v-if="pickArticle">
    <v-row class="mt-5">
      <v-col
        cols='3'
      >
        <v-icon color="green darken-2">mdi-call-split</v-icon>
      </v-col>

      <v-col 
        cols='5'
      >
        {{pickArticle.pin.addressName}}
      </v-col>

      <v-col 
        cols='4'
      >
        <v-btn @click='markerCheck'>기억하기</v-btn> 
      </v-col>
    </v-row>
    <MemoryLocation
      v-if="is_Memoryshow"
      @close-Memorymodal="is_Memoryshow=false"
      @onMemory='saveMemory'
      :position='position'
      >
    </MemoryLocation>

    <v-row>
      <v-col 
      >
        {{pickArticle.wdate | dateFormat()}}
      </v-col>
      
    </v-row>


  <v-card
    elevation="24"
    max-width="444"
    class="mx-auto"
  >

    <v-carousel
      :continuous="false"
      :show-arrows="false"
      hide-delimiter-background
      delimiter-icon="mdi-minus"
      height="300"
    >
      <v-carousel-item
        v-for="(image, i) in pickArticle.images"
        :key="i"       
        >  
        <v-sheet
          height="100%"
          tile
        > 
          <v-img
            :src="SERVER + '/images/'+ image.path"
          >
          </v-img>
        </v-sheet>
      </v-carousel-item>
    </v-carousel>




    <v-list two-line>
      <v-list-item>


        <div>
          <v-list-item-avatar
            v-if="pickArticle.user.profileImage"
            size="36"
            class="my-0"
          >
            <img
              :src="SERVER + '/images/'+ pickArticle.user.profileImage.path"
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
        </div>
    
        <v-list-item-content>
          <!-- 이름 -->
          <v-list-item-title >{{pickArticle.user.nickname}}</v-list-item-title>
          <!-- 날짜 -->
          <v-list-item-subtitle>{{pickArticle.wdate | dateFormat()}}</v-list-item-subtitle>
          <!-- 해시태그 -->
          <v-list-item-subtitle
            v-for="(hashtag, i) in pickArticle.hashtags"
            :key="i"
          >
            <v-btn @click="goHash()">#{{ hashtag.contents }}</v-btn>
            
          </v-list-item-subtitle>
        </v-list-item-content>
        <v-menu transition="slide-y-transition">
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              color="primary"
              class="ma-2"
              v-bind="attrs"
              v-on="on"
            >
              <v-icon>
                수정/삭제
              </v-icon>
            </v-btn>
          </template>
          <v-list>
            <v-list-item>
              <v-list-item-title @click='updateArticle'>수정</v-list-item-title>
            </v-list-item>

            <v-list-item>
              <v-list-item-title @click='deleteArticle'>삭제</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>   
      </v-list-item>
    </v-list>


  </v-card>

  <v-system-bar lights-out></v-system-bar>
  
 
  
  <Bind
    :articles='articles'
    @onClick='getBindArticle'
    >
  </Bind>






  </div>
  </v-col>
</v-row>
</template>

<script>
import axios from 'axios'
import Bind from '@/components/article/Bind'
import MemoryLocation from '@/components/article/MemoryLocation.vue'
const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'BindArticle',
  filters: {
    dateFormat(date) {
      const dateArray = date.split(' ')[0].split('-')
      return dateArray[0]+'년 '+dateArray[1]+'월 '+ dateArray[2]+'일'
    },
  },
  components: {
    Bind,
    MemoryLocation
  },
  data() {
    return {
      pinId: this.$route.params.pinId,
      articles: '',
      SERVER: process.env.VUE_APP_SERVER_URL,
      pickArticle: '',
      drawer: false,
      group: null,
      articleId: '',
      is_Memoryshow: false,
    }
  },
  methods: {
    getArticles() {
      axios.get(`${SERVER_URL}/articles/pins/?pinId=${this.pinId}`, this.getToken)
      .then( (res)=> {
        this.articles = res.data
        this.articleId = this.articles[0].articleId
        this.getBindArticle()
      })
    },
    getBindArticle(res) {
      
      if (res === undefined){
        0
      }
      else{
        this.articleId = res.articleId
      }
      axios.get(`${SERVER_URL}/articles/${this.articleId}`, this.getToken)
      .then( (res)=> {
        this.pickArticle = res.data
        this.position = this.pickArticle.pin
        
      })
    },
    updateArticle(){
      
      this.$router.push({name: 'UpdateArticle', params: {
        articleId: this.articleId,
        hashtagArray: this.pickArticle.hashtags,
        contents: this.pickArticle.contents,
        lat: this.pickArticle.pin.lat,
        lng: this.pickArticle.pin.lng,
        preview: this.pickArticle.images
      }})
    },
    deleteArticle(){
      axios.delete(`${SERVER_URL}/articles/`+ this.articleId, this.getToken )
      .then((res) => {
        alert('게시물 삭제 완료')
        this.$router.push({name: 'Feed'})
      })
    },
    goHash(res) {
      this.$router.push({name:'Search', params: {
      hashtag: res}
      })
    },
    markerCheck(res) {
      this.is_Memoryshow = !this.is_Memoryshow
    },
    saveMemory(res) {
      console.log(res)
      const userId = sessionStorage.getItem('uid')
      axios.post(`${SERVER_URL}/users/${userId}/memories`, res, this.getToken)
      .then(()=> {
        alert('이 장소를 기억했습니다.')
        this.$router.go()

      })
      .catch((err)=> {
        console.log(err)
      })
    }
  },
  created() {
    this.getArticles()
  
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

}
</script>

<style>

</style>