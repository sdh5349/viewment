<template>
  <v-row
    justify="center"
  >
    <v-col
        lg="4"
        md="4"
        sm="6"
    >
      <h1>게시글 작성</h1>

      <validation-observer ref="observer" v-slot="{ invalid }">
        <form @submit.prevent="submit">
          <validation-provider rules="" v-slot="{ errors }">
            <v-file-input
              accept="image/*"
              multiple
              placeholder="추억을 올려주세요!"   
              prepend-icon="mdi-camera"
              :error-messages="errors"
              @change="previewImg"
              >
            </v-file-input>

      <v-carousel 
        v-if="preview"
        :show-arrows="false">
        <v-carousel-item
          v-for="(file, index) in preview" 
          :key="index"
          :src="file.url"
          reverse-transition="fade-transition"
          transition="fade-transition"          
        ></v-carousel-item>
      </v-carousel>

          </validation-provider>

          <SetLocation
            @onClick="onInput"
          />
          

          <validation-provider rules="required" v-slot="{ errors }">
            <v-textarea
              placeholder="추억을 적어주세요!"
              type="text"
              label="게시글"
              v-model="content"
              :error-messages="errors"
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
      imgFiles: undefined,
      fileInfos: [
      ],
      preview: '',
      articleInfo: {
        content: '',
        img: '',
        hashtag: '',
      },
      hash: '',
      items: ['가나다', '가나', '가나마바사', '가아낭남'],
      content: '',
      position: {
        latitude: '',
        longitude: '',
      }
    }
  },
  methods: {
    previewImg(res) {
      this.imgFiles = res
      for (var i=0; i<this.imgFiles.length; i++) {
        this.fileInfos[i] = {
          name: this.imgFiles[i].name,
          url: URL.createObjectURL(this.imgFiles[i])
          }
        }  
      this.preview = this.fileInfos
    },
    writeHash(res) {
      // this.hash.append(res)
      this.hash = res
      },
    onInput(res) {
      this.position.latitude = res.Ma
      this.position.longitude = res.La
      },
    submit() {
      this.articleInfo.content = this.content
      this.articleInfo.img = this.fileInfos
      this.articleInfo.hashtag = this.hash

      // axios.post("주소", this.articleInfo)
      // .then(() => {
      //   console.log('성공')
      //   })
      },
    },
}
</script>

<style>

</style>