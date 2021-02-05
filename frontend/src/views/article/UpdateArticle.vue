<template>
  <div>
    <v-container>
      <v-row justify="center">
        <v-col cols="12">
          <h4>게시글에서 사진과 위치는 수정할수 없습니다.</h4>
        </v-col>

        <!-- <v-col cols="12">
          <v-carousel
            :show-arrows="false"
            hide-delimiter-background
            delimiter-icon="mdi-minus"
            height="50px"
            mouse-drag = true
          >
            <v-carousel-item
              v-for="(image, i) in preview"
              :key="i"
              :src="SERVER+ '/images/'+ image.path"
            ></v-carousel-item>
          </v-carousel>
        </v-col>  -->

        <v-col cols="12">
          <validation-provider rules="required" v-slot="{ errors }" >
            <v-textarea
              placeholder="추억을 적어주세요!"
              type="text"
              label="게시글"
              v-model="contents"
              :error-messages="errors"
              outlined
              ></v-textarea>
          </validation-provider>
        </v-col>

        <v-col cols="12">
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
        </v-col>


        <v-col cols='12'>
          <v-menu
            ref="menu"
            v-model="menu"
            :close-on-content-click="false"
            transition="scale-transition"
            offset-y
            min-width="auto"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="date"
                label="날짜 입력"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
              ref="picker"
              v-model="date"
              :max="new Date().toISOString().substr(0, 10)"
              min="1950-01-01"
              @change="save"
            ></v-date-picker>
          </v-menu>
        </v-col>




        <v-btn
          class="mr-4"
          color="primary"
          block
          @click="onUpdate"
        >
          게시물 수정
        </v-btn>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios'
import { required } from 'vee-validate/dist/rules'
import { extend, ValidationProvider, setInteractionMode } from 'vee-validate'
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
  },
  data: () => {
    return {
      SERVER: process.env.VUE_APP_SERVER_URL,
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
      date: null,
      menu: false, 
    }
  },
  methods: {
    handleClickButton() {
      this.visible = !this.visible
    },
    writeHash(res) {
      this.hash = res
      },
    save(date) {
      this.$refs.menu.save(date)
    },
    onUpdate() {
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
      

      axios.put(`${SERVER_URL}/articles/` + this.articleId, this.articleInfo, {
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
    this.preview = this.$route.params.preview
    this.date = this.$route.params.date
    this.gethash()
  }
}
</script>

<style>

</style>