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
          <validation-provider rules="required" v-slot="{ errors }">
            <v-file-input
              accept="image/*"
              multiple
              placeholder="추억을 올려주세요!"   
              prepend-icon="mdi-camera"
              :error-messages="errors"
              @change="previewImg"
              >
            </v-file-input>

      <v-card>
        <v-img 
          v-for="(file, index) in preview" :key="index"
          :src="file.url">
        </v-img>
      </v-card>

          </validation-provider>

          <p>위치설정</p>
          <div id="map" style="width:500px;height:400px;"></div>


          <validation-provider rules="ruquired" v-slot="{ errors }">
            <v-textarea
              placeholder="추억을 적어주세요!"
              type="text"
              label="게시글"
              :error-messages="errors"
              >
              
            </v-textarea>
          </validation-provider>
          <p>해시태그 자리</p>

        
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
    ValidationObserver
  },
  data: () => {
    return {
      imgFiles: undefined,
      fileInfos: [
      ],
      preview: '',
      articleInfo: {
      },

      
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
    submit() {
      
      },
    },
}
</script>

<style>

</style>