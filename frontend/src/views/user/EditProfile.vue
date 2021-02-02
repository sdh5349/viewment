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
              :src="previewImage"
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
import axios from 'axios'
import { required, max } from 'vee-validate/dist/rules'
import { extend, ValidationObserver, ValidationProvider, setInteractionMode } from 'vee-validate'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

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
      loading: true,
      profileUserInfo: null,
      nickname: '',
      message: '',
      profileImage: null,
      previewImage: null,
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
      axios.get(`${SERVER_URL}/users/${this.profileUserId}/page`, this.getToken)
      .then(res => {
        // 현재 보고있는 프로필 페이지 유저의 정보 초기화
        this.profileUserInfo = res.data
        this.nickname = this.profileUserInfo.nickname
        this.message = this.profileUserInfo.message

        // 현재 로그인한 유저의 uid 초기화
        this.loginUserId = sessionStorage.getItem('uid')
        if (this.profileUserInfo.profileImage) {
          axios.get(`${SERVER_URL}/images/${this.profileUserInfo.profileImage.path}`, this.getToken)
          .then(res => {
            this.previewImage = res.data
            this.profileImage = res.data
          })
        }
      })
      .then(() => {
        this.loading = false
      })
      .catch(err => {
        alert("오류"); // TODO: 오류페이지로 변경
        console.log('Error', err.message);
        // self.$router.push({ name: 'Error' })
      })
    },
    submit () {
      this.$refs.observer.validate()

      const params = {
        'nickname': this.nickname,
        'message': this.message
      }

      const config = {
        headers: {
          'Content-type': 'multipart/form-data',
          'X-Authorization-Firebase': sessionStorage.getItem('jwt')
        }
      } 

      // 닉네임과 메세지 변경사항에 대한 부분먼저 보낸다
      axios.patch(`${SERVER_URL}/accounts/${this.profileUserId}`, params, this.getToken)
      .then(() => {
        // 프로필 이미지 관련 요청
        // 현재 페이지에 선택한 이미지가 있고
        if (this.profileImage) {
          // 기존에 프로필 이미지가 있었던 유저라면
          if (this.profileUserInfo.profileImage) {
            axios.put(`${SERVER_URL}/images/${this.profileUserId.profileImage.path}`, this.profileImage, config)
            .then(() => {
            })
            .catch(err => {
            })
          } else {
            axios.post(`${SERVER_URL}/images/${this.profileUserId.profileImage.path}`, this.profileImage, config)
            .then(() => {
              })
            .catch(err => {
              })
          }
        // 현재 페이지에 선택한 이미지가 없고
        } else {
          // 기존에 프로필 이미지가 있었던 유저라면
          if (this.profileUserInfo.profileImage) {
            axios.delete(`${SERVER_URL}/images/${this.profileUserId.profileImage.path}`, this.profileImage, config)
            .then(() => {
            })
            .catch(err => {
            })
          }
        }
      })
      .catch(err => {

      })
    },
    onPickFile () {
      this.$refs.fileInput.click()
    },
    onFilePicked (event) {
      const imageFile = event.target.files[0]

      if (imageFile) {
        this.previewImage = URL.createObjectURL(imageFile)

        this.profileImage = new FormData()
        this.profileImage.append('profileImage', imageFile)
      }
    }
  }
}
</script>

<style>

</style>