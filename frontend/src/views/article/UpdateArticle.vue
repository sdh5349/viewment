<template>
  <v-row
    justify="center"
  >
    <v-col
        lg="4"
        md="4"
        sm="6"
    >
      <h1>추억 기록하기(수정)</h1>

      <validation-observer ref="observer" v-slot="{ invalid }">
        <form @submit.prevent="submit">
  
      
     <v-carousel
      :show-arrows="false"
      hide-delimiter-background
      delimiter-icon="mdi-minus"
      height="300"
      mouse-drag = true
      
    >
      <v-carousel-item
        v-for="(image, i) in preview"
        :key="i"
        :src="'http://i4b105.p.ssafy.io:8080/api/v1/images/'+ image.path">    
      >
      </v-carousel-item>
    </v-carousel>
  




    <!-- <div>
      <v-btn @click="handleClickButton">지도 열기</v-btn>
      <hr>
      <div v-if="visible">
        <SetLocation
          @onClick="onMarker"
        />
      </div>
    </div> -->
    



        <validation-provider rules="required" v-slot="{ errors }" >
          <v-textarea
            placeholder="추억을 적어주세요!"
            type="text"
            label="게시글"
            v-model="contents"
            :error-messages="errors"
            outlined
            >  
          </v-textarea>
        </validation-provider>


        <v-combobox
          v-model="hashtags"
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
          게시물 수정
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
// import SetLocation from '@/components/article/SetLocation.vue'
const SERVER_URL = process.env.VUE_APP_SERVER_URL




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
    // SetLocation
  },
  data: () => {
    return {
      imgFiles: [],
      fileInfos: [
      ],
      articleInfo: {
        userId: '',
        lat: '',
        lng: '',
        // address: '',
        contents: '',
        hashtags: '',
        imgFormData: '',
      },
      articleImages: null,

      articleId: '',
      hashtags: [],
      contents: '',
      lat: '',
      lng: '',
      preview: '',
      items: ['자취방', '덕명동', '2021년', '집 앞'],
      content: '',
      position: {
        latitude: '',
        longitude: '',
      },
      files: [],
      visible: false,
      
    }
  },
  methods: {
    handleClickButton() {
      this.visible = !this.visible
    },
    writeHash(res) {
      
      this.hash = res
      },
    onMarker(res) {
      this.position.lat = res.Ma
      this.position.lng = res.La
      
      },
    submit() {

      this.articleInfo.userId = sessionStorage.getItem('uid')
      this.articleInfo.articleId = this.articleId
      this.articleInfo.lat = this.lat
      this.articleInfo.lng = this.lng
      this.articleInfo.contents = this.contents
      this.articleInfo.hashtags = this.hashtags

      var headers = {
        headers: {
          'Content-type': 'multipart/form-data',
          'X-Authorization-Firebase': sessionStorage.getItem('jwt')
        },
      }
      

      axios.put(`http://i4b105.p.ssafy.io:8080/api/v1/articles/` + this.articleId, this.articleInfo, {
        headers: {
            'X-Authorization-Firebase': sessionStorage.getItem('jwt')
          }
      } )
      .then((res) => {
        this.$router.push({name: 'DetailArticle', params: {
          articleId: this.articleId
        }})
      })
      .catch((err) => {
        console.log(err)
        alert(err)
      })
    },
    gethash(){
      const hashtagArray = this.$route.params.hashtagArray
      for (var i=0; i< hashtagArray.length; i++){
        this.hashtags.push(hashtagArray[i].contents)
      }
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
    this.articleId= this.$route.params.articleId,
    this.contents= this.$route.params.contents,
    this.lat= this.$route.params.lat,
    this.lng= this.$route.params.lng,
    this.preview= this.$route.params.preview
    this.gethash()
  }
}
</script>

<style>

</style>