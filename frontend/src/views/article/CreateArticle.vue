<template>
  <v-row
    justify="center"
    position: relative
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >

  <v-stepper
    v-model="e6"
    vertical
  >
    <v-stepper-step
      :complete="e6 > 1"
      step="1"
      @click="e6 = 1"
    >
      사진 추가하기
      <small>설명 추가할께 있나?</small>
    </v-stepper-step>

    <v-stepper-content step="1" >

        <v-card
          class="img-card-container"
        >
        
        <v-file-input
          v-if="this.preview != []"
          accept="image/*"
          multiple 
          v-model="files"
          @change="previewImg"
          hide-input  
          prepend-icon = mdi-camera
          class="file-input"
          
          ></v-file-input> 


        <v-carousel
          class="carousel-container"
          :show-arrows="false"
          hide-delimiter-background
          delimiter-icon="mdi-checkbox-blank-circle-outline"
          height="100%"
        >
        
        <v-carousel-item
          v-for="(file, index) in preview" 
          :key="index"
          :src="file.url"             
          >   
          <v-row class="d-flex justify-space-between text-center">
            <v-col cols="6">
              <v-btn text @click='imageDelete(index)' color="black">
                <v-icon>
                  mdi-close
                </v-icon>
              </v-btn> 
            </v-col>

            <v-col cols="6">
              <v-btn icon color="black">
                <v-icon>mdi-plus
                <v-file-input
                  accept="image/*"
                  v-model="files"
                  multiple 
                  @change="previewImg"    
                  hide-input  
                  >
                </v-file-input> 
                </v-icon>
              </v-btn>
            </v-col>
          </v-row>
          </v-carousel-item>
        </v-carousel>
      </v-card>


      <v-btn
        color="primary"
        @click="e6 = 2"
      >
        다음
      </v-btn>
      <v-btn 
        @click="goHome"
      >
        취소
      </v-btn>
    </v-stepper-content>




    <v-stepper-step
      :complete="e6 > 2"
      step="2"
      @click="e6 = 2"
    >
      게시글 해시태그 날짜
      <small>설명 추가할께 있나?</small>
    </v-stepper-step>

    <v-stepper-content step="2">
      <v-card
        color="lighten-1"
        class="mb-12"
        height="350px"
      >
        <validation-provider rules="required" v-slot="{ errors }">
          <v-textarea placeholder="추억을 적어주세요!" type="text" label="게시 글 입력" v-model="contents" :error-messages="errors"
          outlined></v-textarea>
        </validation-provider>
      
        <v-combobox v-model="hashtags" :items="items" label="해시태그" multiple chips @keyup="hashKeyup" :search-input.sync="search">
          <template v-slot:selection="data">
            <v-chip :key="JSON.stringify(data.item)" v-bind="data.attrs" :input-value="data.selected"
              :disabled="data.disabled" @click:close="data.parent.selectItem(data.item)">
              <v-avatar class="accent white--text" left v-text="'#'" ></v-avatar>
              {{ data.item }}
            </v-chip>
          </template>
        </v-combobox> 
      
      <v-menu
        ref="menu"
        v-model="menu"
        :close-on-content-click="false"
        :return-value.sync="date"
        transition="scale-transition"
        offset-y
        min-width="auto"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
            v-model="date"
            label="날짜 선택"
            prepend-icon="mdi-calendar"
            readonly
            v-bind="attrs"
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker
          v-model="date"
          no-title
          scrollable
        >
          <v-spacer></v-spacer>
          <v-btn
            text
            color="primary"
            @click="menu = false"
          >
            Cancel
          </v-btn>
          <v-btn
            text
            color="primary"
            @click="$refs.menu.save(date)"
          >
            OK
          </v-btn>
        </v-date-picker>
      </v-menu>


      </v-card>
      <v-btn
        color="primary"
        @click="e6 = 3"
      >
        다음
      </v-btn>
      <v-btn 
        @click="goHome"
      >
        이전
      </v-btn>
    </v-stepper-content>

    <v-stepper-step
      :complete="e6 > 3"
      step="3"
      @click="e6 = 3"
    >
      위치 지정
      <small>설명 추가할께 있나?</small>
    </v-stepper-step>

    <v-stepper-content step="3">
      <v-card
        color="lighten-1"
        class="mb-12"
        height="600px"
      >
      
      <SetLocation @onClick="savePosition"></SetLocation>
      
      </v-card>
      <v-btn
        color="primary"
        @click="onSumbit"
      >
        완료
      </v-btn>
      <v-btn 
        @click="e6 = 2"
      >
        이전
      </v-btn>
    </v-stepper-content>




  </v-stepper>



     </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'
import SetLocation from "@/components/article/SetLocation.vue" 
import { required } from 'vee-validate/dist/rules'
import { extend, setInteractionMode, ValidationProvider } from 'vee-validate'
import { mdiCloseCircleOutline } from '@mdi/js'
const SERVER_URL = process.env.VUE_APP_SERVER_URL

const request = axios.CancelToken.source()



// https://logaretm.github.io/vee-validate/guide/interaction-and-ux.html#interaction-modes
setInteractionMode('eager') // 유효성 검사의 시기

extend('required', {
  ...required,
  message: '필수 입력 항목입니다.'
})

export default {
  components: {
    ValidationProvider,
    SetLocation,
  },
  data: () => {
    return {
      e6: 1,
      preview: [],
      articleImages: null,
      files: [],
      contents: '',
      items: [],
      hashtags: '',
      menu: false,
      articleInfo: {
        userId: '',
        contents: '',
        hashtags: '',
        lat: '',
        lng: '',
        addressName: '',
        date: '',
        pinId: '',
      }, 
      prac:'',
      search: '',
      date: new Date().toISOString().substr(0, 10),
    }
  },
  methods: {
    hashKeyup(){
      this.getHashtags()
    },
    getHashtags() {
      if (this.search){
      axios.get(`${SERVER_URL}/hashtags/${this.search}`, this.getToken)
        .then((res) => {
          
          for (var i = 0; i < res.data.length; i++){
            this.items.push(res.data[i].contents)
            }  
          
          })
        .catch((err)=> {
          alert('error'+err.message)
        })
      }
    },
    previewImg(res) {
      if (res){     
        const temp = res.map(imgObt => {
          return {name: imgObt.name, url: URL.createObjectURL(imgObt)}
        })
        this.preview.push(...temp)
      }
    },
    imageDelete(index) {
      this.preview.splice(index, 1)
    },
    save(date) {
      this.$refs.menu.save(date)
    },
    savePosition(res) {
      if (res.pinId) {
        this.articleInfo.pinId = res.pinId
        this.articleInfo.addressName = res.addressName
        this.articleInfo.lat = res.lat
        this.articleInfo.lng = res.lng
      }
      else{
        this.articleInfo.addressName = res.addressName
        this.articleInfo.lat = res.Ma
        this.articleInfo.lng = res.La
      }
    },
    onSumbit() {
      this.articleInfo.userId = sessionStorage.getItem('uid')
      this.articleInfo.contents = this.contents
      this.articleInfo.hashtags = this.hashtags
      this.articleInfo.date = this.date
      this.articleInfo.addressName = this.addressName
      console.log(this.articleInfo)

      this.articleImages = new FormData()
      for (var i = 0; i < this.files.length; i++) {
        this.articleImages.append('articleImages', this.files[i]);
      }
      

      var headers = {
        headers: {
          'Content-type': 'multipart/form-data',
          'X-Authorization-Firebase': sessionStorage.getItem('jwt')
        },
      }
      axios.post(`${SERVER_URL}/articles`, this.articleInfo, {
        headers: {
            'X-Authorization-Firebase': sessionStorage.getItem('jwt'),
          }
        })
        .then((res) => {
          this.articleId = res.data
          axios.post(`${SERVER_URL}/images/article/` + this.articleId, this.articleImages, headers)
          .then((res) => {
            
            this.$router.push({name: 'DetailArticle', params: {
              articleId: this.articleId,
            }})
          })
          .catch((err) => {
            alert(err)
          })
        })
        .catch((err) => {
          alert(err)
        })   
    },
    goHome() {
      this.$router.push({ name:'Feed' })
    },
    updateTags(res) {
      console.log(res)
      console.log(this.hashtags)
    },
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

<style scoped>
.img-card-container {
  position: relative;
  height: 300px; 
}

.file-input {
  position: absolute; 
  margin: 0;
  padding: 0;
  top: 50%; 
  left: 50%;
  width: 30px;
  height: 30px;
  margin-left: -15px;
  margin-top: -15px;
  z-index: 9999;
}


</style>