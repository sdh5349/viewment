<template>

<v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
      <Alert
        v-if="alert.alerted"
        :message="alert.message"
        :color="alert.color ? alert.color : 'error'"
      />

  <div v-if="pickArticle">
    <v-row class="m-auto text-center">
      <v-col
        cols='1'
        class="my-auto"
      >
        <v-icon
          
        >
          mdi-map-marker
        </v-icon>
      </v-col>

      <v-col 
        cols='7'
        class="my-auto"
      >
        {{pickArticle.pin.addressName}}
      </v-col>

      <v-col 
        cols='4'
        class=""
      >
        <v-dialog v-model="saveMemoryDialog" persistent >
          <template v-slot:activator="{ on, attrs }" >
            <v-btn color="primary" dark v-bind="attrs" v-on="on">
              기억하기
            </v-btn>
          </template>
  
  <!-- v-slot="{ invalid }" -->
          <v-card >
            <v-card-title>기억하기 저장</v-card-title>
            <v-divider></v-divider>

              <validation-observer
                ref="observer"
                v-slot="{ invalid }"
              >
              <v-card-text style="height: 300px;" class="">

              
                  <validation-provider
                    mode="aggressive"
                    rules="required|max:10"
                    v-slot="{ errors }"
                  >
                  <v-text-field 
                    block
                    v-model="memoryName" 
                    label='기억하기 이름' 
                    @click="resetMemoryName"
                    :error-messages="errors"
                    ></v-text-field>
                  </validation-provider>

                  <validation-provider
                    mode="aggressive"
                    rules="required|max_value:1500"
                    v-slot="{ errors }"
                  >
                  <v-text-field 
                    block
                    v-model="memoryRadius" 
                    label='반경(m)' 
                    @click="resetMemoryRadius"
                    :error-messages="errors"
                  ></v-text-field>
                  </validation-provider>
                  </v-card-text>
                  <v-divider></v-divider>
                  <v-card-actions > 
                    
                    <v-col cols="6" >
                    <v-btn  
                      @click="saveMemoryDialog = false" 
                      text 
                    >닫기</v-btn>
                    </v-col>

                    <v-col cols="6" class="text-right">
                    <v-btn 
                      @click='saveMemory' 
                      text 
                      color='primary'
                      :disabled="invalid"
                    >기억 저장</v-btn>
                    </v-col>
                  
                  </v-card-actions>
                  </validation-observer>
                
              
            
            
          </v-card>
        </v-dialog>
      </v-col>
    </v-row>



      
      <!-- 기억하기핀 버튼을 누르고 map을 누르면 기억하기를 저장할때 이름과 반경을 정할수 있는 카드 (끝) -->




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
    
    
    <BindArticleComponents 
      v-if="articles != ''"
      :article="pickArticle"
    />


  

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
import BindArticleComponents from '@/components/article/BindArticleContents'
import Alert from '@/components/common/Alert'
import { required, min, max, max_value } from 'vee-validate/dist/rules'
import { extend, ValidationObserver, ValidationProvider } from 'vee-validate'
const SERVER_URL = process.env.VUE_APP_SERVER_URL

extend('required', {
  ...required,
  message: '필수 입력 항목입니다.'
})
extend('max', {
  ...max,
  message: '기억하기 이름은 10글자 이하이어야 합니다.'
})
extend('max_value', {
  ...max_value,
  message: '기억하기 반경은 1500m 이하이어야 합니다.'
})


export default {
  filters: {
    dateFormat(date) {
      const dateArray = date.split(' ')[0].split('-')
      return dateArray[0]+'년 '+dateArray[1]+'월 '+ dateArray[2]+'일'
    },
  },
  components: {
    Bind,
    BindArticleComponents,
    ValidationProvider,
    ValidationObserver,
    Alert, // alert 컴포넌트
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
      startDate: '',
      dates: [this.startDate, this.endDate],
      saveMemoryDialog: false,
      memoryName: '기억장소', // 기억하기를 저장할때 장소 이름을 담을 변수
      memoryRadius: '500', // 기억하기를 저장할때 반경을 담을 변수
      alert: {
        alerted: false,
        message: '',
        color: '',
      },
      memoryInfo: { // 기억하기 정보들을 담을 변수
        name: '',
        radius: '',
        lat: '',
        lng: ''
      },
    }
  },
  methods: {
    resetMemoryName() {
      this.memoryName = ''
    },
    resetMemoryRadius() {
      this.memoryRadius = ''
    },
    openBindSetting() {
      this.dialog = true
      this.dates = [this.startDate, this.endDate]
    },
    bindSetting() {
      this.alert.alerted = false 
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
      this.alert.message = '날짜 설정 완료.'
      this.alert.color = 'primary'
      this.alert.alerted = true
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
      
      if (this.endDate == undefined){
        
        this.endDate = ''
        
      }

      
      axios.get(`${SERVER_URL}/articles/pins?end=${this.endDate}&start=${this.startDate}&pinId=${this.pinId}`, this.getToken)
      .then( (res)=> {
        
        if (res.data.length){
          this.articles = res.data
          this.articleId = this.articles[this.articles.length-1].articleId
          this.getBindArticle()
        }
        else {
          this.articles = ''
          
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
        
      })
    },
    saveMemory(res) {
      this.alert.alerted = false 
 
      this.saveMemoryDialog = false
      this.memoryInfo.name = this.memoryName
      this.memoryInfo.radius = this.memoryRadius
      this.memoryInfo.lat = this.position.lat
      this.memoryInfo.lng = this.position.lng
      const userId = sessionStorage.getItem('uid') // uid를 저장할 변수 axios요청에 uid가 사용되기 때문에
      // 기억 하기 요청 axios 
      // post(url, 기억하기 정보, uid) 가 들어감 
      
      axios.post(`${SERVER_URL}/users/${userId}/memories`, this.memoryInfo, this.getToken)
      .then(()=> {
        this.alert.message = '기억이 완료되었습니다.'
        this.alert.color = 'primary'
        this.alert.alerted = true
        
        // this.$router.go() // 기억 완료했으면 새로고침 해줌
      })
      .catch((err)=> { // 에러가 났을때
        console.log(err) // 에러 출력
      }) 

    },
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