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
  <v-row 
    v-else
    justify="center" 
    position: relative
  >
    <v-col lg="4" md="4" sm="6" class="pa-0">
      <Alert
        v-if="alert.alerted"
        :message="alert.message"
        :color="alert.color ? alert.color : 'error'"
      />
      <v-stepper class="stepper-container" v-model="e6" vertical>
        
        <v-stepper-step :complete="e6 > 1" step="1" class="pl-2 py-1">
          사진
        </v-stepper-step>
        
        <v-stepper-content step="1" class="ml-5">
          <v-card class="img-card-container" elevation='0'>
            <v-file-input 
              v-if="this.preview.length == 0" 
              accept="image/*"
              multiple v-model="files" 
              @change="previewImg"
              label="Avatar"
              hide-input 
              prepend-icon=mdi-camera 
              class="file-input"></v-file-input>
            
            <v-carousel  v-if="this.preview.length != 0" style=""  width="100%" height="100%" :show-arrows="false" hide-delimiter-background
              delimiter-icon="mdi-checkbox-blank-circle-outline">
              <v-carousel-item v-for="(file, index) in preview" :key="index" :src="file.url" style="max-width: 100%; height: 100%;">
                <v-row class="d-flex justify-space-between" >
                
                <v-col cols="3" >
                  <v-btn fab small class="close-button"  @click='imageDelete(index)' >
                    <v-icon dark>
                      mdi-close
                    </v-icon>
                  </v-btn>
                </v-col>
    
                <v-col cols="3" >
                  <v-btn fab small class="plus-button">
                    <v-file-input class="plus" color="black" accept="image/*" v-model="files" multiple @change="previewImg"
                      hide-input prepend-icon=mdi-camera-enhance >
                    </v-file-input>
                  </v-btn>
                </v-col>

              </v-row>
              </v-carousel-item>
            </v-carousel>
          </v-card>
          
          <v-row class="button-container">
           
            <v-col cols="6">
              <v-btn block @click="goHome">
                취소
              </v-btn>
            </v-col>
  
            <v-col cols="6">
              <v-btn :disabled="this.preview.length == 0" block color="primary" @click="e6 = 2">
                다음
              </v-btn>
            </v-col>
          
          </v-row>
  
        </v-stepper-content>
  
  
  
  
  
  
        <v-stepper-step :complete="e6 > 2" step="2" class="pl-2 py-1">
          정보
        </v-stepper-step>
  
        <v-stepper-content step="2" class="ml-5">
          <v-card color="lighten-1" class="" height="50vh" elevation='0'>
            <v-textarea  placeholder="추억을 적어주세요!" type="text" label="게시 글 입력" v-model="contents" outlined class="mt-0 pt-5" ></v-textarea>
  
            <v-combobox v-model="hashtags" :items="items" label="해시태그" multiple chips @keyup="hashKeyup"
              :search-input.sync="search" :delimiters="[' ']" hint="해시태그는 최대 5개 입니다. 특수문자는 불가능 합니다.">
  
              <template v-slot:selection="data">
                <v-chip :key="JSON.stringify(data.item)" v-bind="data.attrs" :input-value="data.selected"
                  :disabled="data.disabled" @click:close="data.parent.selectItem(data.item)">
                  <v-avatar class="accent white--text" left v-text="'#'"></v-avatar>
                  {{ data.item }}
                </v-chip>
              </template>
            </v-combobox>
  
            <v-menu ref="menu" v-model="menu" :close-on-content-click="false" :return-value.sync="date"
              transition="scale-transition" offset-y min-width="auto">
              <template v-slot:activator="{ on, attrs }">
                <v-text-field v-model="date" label="날짜 선택" prepend-icon="mdi-calendar" readonly v-bind="attrs" v-on="on">
                </v-text-field>
              </template>
              <v-date-picker v-model="date" no-title scrollable>
                <v-spacer></v-spacer>
                <v-btn text color="primary" @click="menu = false">
                  Cancel
                </v-btn>
                <v-btn text color="primary" @click="$refs.menu.save(date)">
                  OK
                </v-btn>
              </v-date-picker>
            </v-menu>
  
          </v-card>
  
          <v-row class="button-container">
            <v-col cols="6">
              <v-btn block  @click="e6 = 1">
                이전
              </v-btn>
            </v-col>
            <v-col cols="6">
              <v-btn :disabled="this.contents.length == 0" block color="primary" @click="e6 = 3">
                다음
              </v-btn>
            </v-col>
          </v-row>

        </v-stepper-content>
  
        <v-stepper-step :complete="e6 > 3" step="3" class="pl-2 py-1">
          위치
        </v-stepper-step>
  
        <v-stepper-content step="3" class="ma-0 pa-2">
          <v-card color="lighten-1" class="mb-0 " height="50vh" elevation='0'>
  
            <SetLocation @onClick="savePosition"></SetLocation>
  
          </v-card> 
          <v-row class="button-container">
            <v-col cols="6">
              <v-btn block @click="e6 = 2">
                이전
              </v-btn>
            </v-col>
            <v-col cols="6">
              <v-btn :disabled="this.articleInfo.lat == ''" block color="primary" @click="onSubmit">
                완료
              </v-btn>
            </v-col>
          </v-row>

        </v-stepper-content>
  
      </v-stepper>
  
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'
import SetLocation from "@/components/article/SetLocation.vue" 
import Alert from '@/components/common/Alert'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  components: {
    SetLocation,
    Alert
  },
  data() {
    return {
      loading: true,
      e6: 1,
      preview: [],
      articleImages: null,
      files: [],
      contents: '',
      items: [],
      hashtags: [],
      menu: false,
      articleInfo: {
        userId: '',
        contents: '',
        hashtags: [],
        lat: '',
        lng: '',
        addressName: '',
        date: '',
        pinId: '',
      }, 
      prac:'',
      search: '',
      date: new Date().toISOString().substr(0, 10),
      alert: {
        alerted: false,
        message: '',
        color: '',
      },
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
    },
  },
  watch: {
    hashtags (val) {
      var special_pattern = /[~!@#$%^&*()_+|<>?:{}]/;
      if(special_pattern.test(val[val.length-1]) == true) { 
        this.hashtags.pop()
      } 
      else { 
        0
      }

      var index = this.hashtags.indexOf('')
      if (index!=-1){
        this.hashtags.splice(index, 1)
      }
      if (val.length > 5) {
        this.$nextTick(() => this.hashtags.pop())
      }
    },
  },
  created() {
    this.loading = false
  },
  methods: {
    spaceEvent() {
      this.search = this.search+'enter'
    },
    hashKeyup(){
      this.getHashtags()
    },
    getHashtags() {
      var special_pattern = /[~!@#$%^&*()_+|<>?:{}]/
      if (this.search){
        this.search.replace(" ","")
      
      if(special_pattern.test(this.search) == true) { 
        this.search = ''
      } 
      else {
        console.log(this.search) 
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
      }
    },
    previewImg(res) {
      var tempSumImgSize = 0
      var tempPreview = []
      res.forEach(imageFile => {
        tempSumImgSize = tempSumImgSize + imageFile.size
        var temp = {
          name: imageFile.name, 
          url: URL.createObjectURL(imageFile)
        }
        tempPreview.push(temp)
      })
      
      if (tempSumImgSize > 20971520){
        this.alert.message = '파일 총 크기는 20MB를 넘길 수 없습니다.'
        this.alert.alerted = true
      }
      else{
        this.preview.push(...tempPreview)
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
      }
      else{
        this.articleInfo.pinId = ''
      }
      this.articleInfo.addressName = res.addressName
      this.articleInfo.lat = res.lat
      this.articleInfo.lng = res.lng
      
    },
    onSubmit() {
      this.loading = true

      var index = this.hashtags.indexOf('')

      if (index!=-1){
        this.hashtags.splice(index, 1)
      }
      this.articleInfo.userId = sessionStorage.getItem('uid')
      this.articleInfo.contents = this.contents
      this.articleInfo.hashtags = this.hashtags
      this.articleInfo.date = this.date


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
      let pin =''
      if (this.articleInfo.pinId){
        pin = '/pin'
      }
      else {
        pin = ''
      }
      axios.post(`${SERVER_URL}/articles${pin}`, this.articleInfo, {
        headers: {
            'X-Authorization-Firebase': sessionStorage.getItem('jwt'),
          }
        })
        .then((res) => {
          this.articleId = res.data
          axios.post(`${SERVER_URL}/images/article/` + this.articleId, this.articleImages, headers)
          .then(res => {
            
            this.$router.push({name: 'DetailArticle', params: {
              articleId: this.articleId,
            }})
          })
          .then(() => {
            this.loading = false
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
      this.$router.go(-1)
    },
  },
}
</script>

<style scoped>
.stepper-container{
  height: calc(100vh - 105px);
  
}
.img-card-container {
  position: relative;
  height: 30vh; 
}

.img-card-container-has {
  position: relative;
  height: 100%; 
}

.carousel-container {  
  width: 100%;
  height: auto;
  
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
  z-index: 0;
}
.button-container{
  justify-content: space-between;
  margin-bottom: 0px;
  padding-bottom: 0px;
}
.close-button {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 3;
}
.plus-button {
  position: absolute;
  z-index: 3;  
  top: 10px;
  right: 10px;
}
.plus {
  position: absolute;
  bottom: -15px;
  right: -1px;
}
</style>