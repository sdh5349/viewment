<template>
  <v-row
    justify="center"
  >
    <v-col
        lg="4"
        md="4"
        sm="6"
        style="margin-bottom: 50px;"
    >
      <h1>추억 기록하기</h1>

      <validation-observer ref="observer" v-slot="{ invalid }">
        <form @submit.prevent="submit">
      
      
      <v-row justify="space-around">
        <v-col cols="10" class="text-center my-5">게시물에 사진올리기</v-col>

        <v-col cols="2">
          <validation-provider rules="" v-slot="{ errors }">
            <v-file-input
              v-if="preview.length === 0"
              accept="image/*"
              v-model="files"
              multiple
              placeholder="추억을 올려주세요!"   
              prepend-icon="mdi-camera"
              :error-messages="errors"
              @change="previewImg"
              hide-input     
              >
            </v-file-input>
            
            <v-file-input
              v-else
              accept="image/*"
              multiple
              placeholder="추억 추가하기" 
              prepend-icon="+"
              @change="previewImg"
              hide-input 
              >             
            </v-file-input>
          </validation-provider>
        </v-col>
      </v-row>
  

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



    <div>
      <v-btn @click="handleClickButton">지도 열기</v-btn>
      
      <div v-if="visible">
        <SetLocation
          @onClick="onMarker"
        />
      </div>
    </div>
    



        <validation-provider rules="required" v-slot="{ errors }" >
          <v-textarea
            placeholder="추억을 적어주세요!"
            type="text"
            label="게시글"
            v-model="content"
            :error-messages="errors"
            outlined
            >  
          </v-textarea>
        </validation-provider>


        <v-combobox
          v-model="hash"
          :items="items"
          label="해시태그"
          multiple
          chips
          @change="writeHash"
          >
        
          <template v-slot:selection="data">
            <v-chip
              :key="JSON.stringify(data.item)"
              v-bind="data.attrs"
              :input-value="data.selected"
              :disabled="data.disabled"
              @click:close="data.parent.selectItem(data.item)"
            >
              <v-avatar
                class="accent white--text"
                left
                v-text="'#'"
              ></v-avatar>
              {{ data.item }}
            </v-chip>
          </template>
        </v-combobox>


        <v-btn
          class="mr-4"
          type="submit"
          :disabled="invalid"
          block
        >
          게시물 작성
        </v-btn>


        </form>
      </validation-observer>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'
import firebase from 'firebase/app'
import { required } from 'vee-validate/dist/rules'
import { extend, ValidationObserver, ValidationProvider, setInteractionMode } from 'vee-validate'
import SetLocation from '@/components/article/SetLocation.vue'

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
    ValidationObserver,
    SetLocation
  },
  data: () => {
    return {
      imgFiles: [],
      fileInfos: [
      ],
      preview: [],
      articleInfo: {
        userId: '',
        lat: '',
        lng: '',
        addressName: '',
        contents: '',
        hashtags: '',
      },
      articleImages: null,
      hash: '',
      items: ['자취방', '덕명동', '2021년', '집 앞'],
      content: '',
      position: {
        latitude: '',
        longitude: '',
      },
      files: [],
      visible: false,
      articleId : ''
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
    handleClickButton() {
      this.visible = !this.visible
    },
    writeHash(res) {
      
      this.hash = res
      },
    onMarker(res) {
      this.position.lat = res.Ma
      this.position.lng = res.La
      this.addressName = res.addressName
      
      },
    submit() {

      this.articleInfo.userId = sessionStorage.getItem('uid')
      this.articleInfo.lat = this.position.lat
      this.articleInfo.lng = this.position.lng
      this.articleInfo.contents = this.content
      this.articleInfo.hashtags = this.hash
      this.articleInfo.addressName = this.addressName
      
      var headers = {
        headers: {
          'Content-type': 'multipart/form-data',
          'X-Authorization-Firebase': sessionStorage.getItem('jwt')
        },
      }
      
      this.articleImages = new FormData()
      for (var i = 0; i < this.files.length; i++) {
        this.articleImages.append('articleImages', this.files[i]);
      }
      
      axios.post(`${SERVER_URL}/articles`, this.articleInfo, {
        headers: {
            'X-Authorization-Firebase': sessionStorage.getItem('jwt'),
          }
      } )
      .then((res) => {
        
        this.articleId = res.data
        axios.post(`${SERVER_URL}/images/article/` + this.articleId, this.articleImages, headers)
        .then((res) => {
          console.log(res)
          this.$router.push({name: 'DetailArticle', params: {
            articleId: this.articleId,
          }})
        })
        .catch((err) => {
          console.log(err)
          alert(err)
        })
      })
      .catch((err) => {
        console.log(err)
        alert(err)
      })
    },
    imageDelete(index) {
      this.preview.splice(index, 1)
    }
  },
  computed: {
      getToken(){
        const token = sessionStorage.getItem('jwt')
        const config = {
          headers: {
            'X-Authorization-Firebase': token,
          }
        }
        return config
      }
    },
}
</script>

<style>

</style>