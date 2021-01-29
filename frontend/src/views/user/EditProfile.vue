<template>
  <v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    > 
      <div class="d-flex justify-center">
        <div>
          <v-avatar
            v-if="true"
            size="150"
          >
            <img
              :src="previewUrl"
            >
          </v-avatar>
          <v-icon
            v-else
            color="primary"
            size="160"
          >mdi-account-circle</v-icon>

          <div class="d-flex justify-center my-4">
            <!-- <input type="file" ref="file" hidden>
            <v-btn @click="$refs.file.click().selectFile">사진 선택</v-btn> -->
            <v-btn @click="onPickFile">사진 선택</v-btn>
            <input
              type="file"
              hidden
              ref="fileInput"
              accept="image/*"
              @change="onFilePicked"/>
          </div>
        </div>
      </div>


      <!-- 닉네임, 자기소개 입력 창 + 유효성 검사 시작 -->
      <validation-observer
        ref="observer"
        v-slot="{ invalid }"
      >
        <form @submit.prevent="submit">
          <validation-provider
            v-slot="{ errors }"
            rules="required|max:8"
          >
            <v-text-field
              v-model="nickname"
              :error-messages="errors"
              label="닉네임"
            ></v-text-field>
          </validation-provider>
          <validation-provider
            v-slot="{ errors }"
            rules="max:50"
          >
            <v-text-field
              v-model="message"
              :error-messages="errors"
              label="소개"
              multi-line
            ></v-text-field>
          </validation-provider>
          <v-btn
            class="mr-4"
            type="submit"
            :disabled="invalid"
            block
          >
            submit
          </v-btn>
        </form>
      </validation-observer>
      <!-- 닉네임, 자기소개 입력 창 + 유효성 검사 끝 -->
    </v-col>
  </v-row>
</template>

<script>
import { required, max } from 'vee-validate/dist/rules'
import { extend, ValidationObserver, ValidationProvider, setInteractionMode } from 'vee-validate'

// https://logaretm.github.io/vee-validate/guide/interaction-and-ux.html#interaction-modes
setInteractionMode('eager') // 유효성 검사의 시기

extend('required', {
  ...required,
  message: '필수 입력 항목입니다.'
})

// extend('max', {
//   ...max,
//   params: ['length'],
//   message: (fieldName, placeholders) => {
//     if (placeholders.length === 8) {
//      return '닉네임은 8자이하 입니다.'
//     } else {
//       return '자기소개는 50자이하 입니다.'
//     }
//   }
// })

export default {
  name: 'EditProfile',
  components: {
    ValidationProvider,
    ValidationObserver,
  },
  props: {
    profileUserId: String,
  },
  data() {
    return {
      nickname: '',
      message: '',
      imgFile: null,
      previewUrl: null,
    }
  },
  computed: {
    getToken() {
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
    // this.dataFetch()
  },
  methods: {
    dataFetch() {
      
    },
    submit () {
      this.$refs.observer.validate()
    },
    onPickFile () {
      this.$refs.fileInput.click()
    },
    onFilePicked (event) {
      this.imgFile = event.target.files[0]

      if (this.imgFile) {
        this.previewUrl = URL.createObjectURL(this.imgFile)
      }
      
      // let filename = files[0].name
      // const fileReader = new FileReader()
      // fileReader.addEventListener('load', () => {
      //   this.imageUrl = fileReader.result
      // })
      // fileReader.readAsDataURL(files[0])
      // this.image = files[0]
    }
  }

}
</script>

<style>

</style>