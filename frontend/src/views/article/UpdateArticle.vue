<template>
  <v-row justify="center">
    <v-col lg="4" md="4" sm="6">
      <v-row justify="center">


        <v-col cols="12">
          <validation-provider rules="required" v-slot="{ errors }">
            <v-textarea placeholder="추억을 적어주세요!" type="text" label="게시글" v-model="contents" :error-messages="errors"
              outlined></v-textarea>
          </validation-provider>
        </v-col>

        <v-col cols="12">
          <v-combobox v-model="hashtags" :items="items" label="해시태그" multiple chips @keyup="hashKeyup" :search-input.sync="search"  :delimiters="[' ']">
            <template v-slot:selection="data">
              <v-chip :key="JSON.stringify(data.item)" v-bind="data.attrs" :input-value="data.selected"
                :disabled="data.disabled" @click:close="data.parent.selectItem(data.item)">
                <v-avatar class="accent white--text" left v-text="'#'" ></v-avatar>
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
        </v-col>




        <v-btn  color="primary" block @click="onUpdate">
          게시물 수정
        </v-btn>
      </v-row>

    </v-col>
  </v-row>
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
        addressName: '',
        contents: '',
        hashtags: '',
        imgFormData: '',
        date: '',
      },
      articleImages: null,

      articleId: '',
      hashtags: [],
      contents: '',
      lat: '',
      lng: '',
      preview: '',
      items: [],
      content: '',
      position: {
        latitude: '',
        longitude: '',
      },
      files: [],
      visible: false,
      date: new Date().toISOString().substr(0, 10),
      menu: false, 
      search: '',
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
      var index = this.hashtags.indexOf('')
      if (index!=-1){
        this.hashtags.splice(index, 1)
      }
      this.articleInfo.userId = sessionStorage.getItem('uid')
      this.articleInfo.articleId = this.articleId
      this.articleInfo.lat = this.lat
      this.articleInfo.lng = this.lng
      this.articleInfo.contents = this.contents
      this.articleInfo.hashtags = this.hashtags
      this.articleInfo.date = this.date
      this.articleInfo.addressName = this.addressName

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
    },
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
    this.addressName = this.$route.params.addressName
    this.gethash()
  }
}
</script>

<style>

</style>