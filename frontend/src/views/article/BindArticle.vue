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
        cols='1'
      >
        <v-icon color="green darken-2">mdi-call-split</v-icon>
      </v-col>

      <v-col 
        cols='7'
      >
        {{pickArticle.pin.addressName}}
      </v-col>

      <v-col 
        cols='4'
        class=""
      >
        <v-btn @click='markerCheck' color="primary">기억하기</v-btn> 
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
        cols="3"
        class="text-center"
      ><v-icon @click="openBindSetting">mdi-calendar</v-icon>
      </v-col>

      <v-col
        cols="9" 
        class=""
      >
        <div @click.stop="dialog = true">
          {{ startDate }} ~ {{ endDate }}
        </div>
        
      </v-col>
      
    </v-row>


  <v-card
    v-if="articles"
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

        <v-menu v-if="is_Creater" transition="slide-y-transition">
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
  
 <v-dialog
      v-model="dialog"
      max-width="100%"
      
    >
      <v-card height="100%">
        <v-card-title class="headline">
          모아보기 기간 지정
        </v-card-title>

      <v-card-text>
        <v-row>
          <v-col
            cols="12"
            sm="6"
          >
            <v-date-picker
              v-model="dates"
              range
            ></v-date-picker>
          </v-col>
          <v-col
            cols="12"
            sm="6"
          >
            <v-text-field
              v-model="dateRangeText"
              label="Date range"
              prepend-icon="mdi-calendar"
              readonly
            ></v-text-field>
          </v-col>
        </v-row>
      </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
            color="green darken-1"
            text
            @click="dialog = false"
          >
            취소
          </v-btn>

          <v-btn
            color="green darken-1"
            text
            @click="bindSetting"
          >
            설정
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>




  
  <Bind
    v-if="articles"
    :articles='articles'
    @onClick='getBindArticle'
    >
  </Bind>

  <div v-if="articles == ''">
    게시물이 없습니다. 기간을 다시 설정해주세요.
    (게시글 없을때 먼가 출력해야할듯)
  </div>




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
      is_Creater: false,
      dialog: false,
      items: [{a:'1'},{a:'1'},{a:'1'},{a:'1'},],
      endDate: '',
      startDate: '2000-01-01',
      dates: ['2019-09-10', '2019-09-20'],

    }
  },
  methods: {
    openBindSetting() {
      this.dialog = true
      this.dates = [this.startDate, this.endDate]
    },
    bindSetting() {
      this.dialog = false
      if (this.dates[0] > this.dates[1]){
        this.startDate = this.dates[1]
        this.endDate = this.dates[0]
      }
      else{
        this.startDate = this.dates[0]
        this.endDate = this.dates[1]  
      }
      this.getArticles()
      alert('설정 완료')
    },
    getDate() {
      function getTimeStamp() {
          var d = new Date();
          var s =
              leadingZeros(d.getFullYear(), 4) + '-' +
              leadingZeros(d.getMonth() + 1, 2) + '-' +
              leadingZeros(d.getDate(), 2)
          return s
      }
      function leadingZeros(n, digits) {
          var zero = '';
          n = n.toString();
          if (n.length < digits) {
              for (var i = 0; i < digits - n.length; i++)
                  zero += '0';
          }
          return zero + n;
      }
      this.endDate = getTimeStamp()
       
    },
    getArticles() {

      axios.get(`${SERVER_URL}/articles/pins?end=${this.endDate}&start=${this.startDate}&pinId=${this.pinId}`, this.getToken)
      .then( (res)=> {
        console.log(res.data)
        if (res.data.length){
          this.articles = res.data
          this.articleId = this.articles[this.articles.length-1].articleId
          this.getBindArticle()
        }
        else {
          this.articles = ''
          console.log('해당 기간에 게시물이 없습니다. 기간을 다시 설정해 주세요.')
        }
        
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
        const userId = sessionStorage.getItem('uid')
        if ( userId == this.pickArticle.user.userId){
          this.is_Creater = true
        }
        else {
          this.is_Creater = false
        }
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
    this.getDate()
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
    },
    dateRangeText () {
      return this.dates.join(' ~ ')
      },
  },
}
</script>

<style scoped>

</style>