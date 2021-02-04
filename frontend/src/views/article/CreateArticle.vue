<template>
  <div>
    <v-container>
      <v-row>
      
        <v-col cols="10" align="center" class="my-5">사진올리기</v-col>

        <v-col cols="2">
            <v-file-input
              v-if="preview.length === 0"
              accept="image/*"
              v-model="files"
              multiple 
              prepend-icon="mdi-camera"
              @change="previewImg"
              hide-input     
              >
            </v-file-input>
        </v-col>
    
        <v-carousel
          :show-arrows="false"
          hide-delimiter-background
          delimiter-icon="mdi-minus"
          height="300"
          mouse-drag = true
        >
        <v-carousel-item
          v-for="(file, index) in preview" 
          :key="index"
          :src="file.url"
          >   
            <v-btn @click='imageDelete(index)'>X</v-btn>
          </v-carousel-item>
        </v-carousel>

        <v-btn block @click="onNext" color="primary">다음</v-btn>
      </v-row>      
    </v-container>
  </div>
</template>

<script>
import axios from 'axios'
import { required } from 'vee-validate/dist/rules'
import { extend, setInteractionMode } from 'vee-validate'
const SERVER_URL = process.env.VUE_APP_SERVER_URL

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
  },
  data: () => {
    return {
      preview: [],
      articleImages: null,
      files: [],
    }
  },
  methods: {
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
    onNext() {
      this.articleImages = new FormData()
      for (var i = 0; i < this.files.length; i++) {
        this.articleImages.append('articleImages', this.files[i]);
      }
      this.$router.push({name:'CreateArticleEtc', params: {
        articleImages: this.articleImages
      }})
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
}
</script>

<style>

</style>