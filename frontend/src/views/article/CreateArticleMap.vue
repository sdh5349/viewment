<template>
<v-row justify="center">
  <v-col lg="4" md="4" sm="6">
    <v-row class="py-0">

      <v-col cols='12'>
        <SetLocation @onClick="savePosition"></SetLocation>

      </v-col>

      <v-col cols="12">
        <v-btn block @click="onSumbit" color="primary">추억 공유</v-btn>
      </v-col>

    </v-row>
  </v-col>
</v-row>
</template>

<script>
import SetLocation from "@/components/article/SetLocation.vue" 
import axios from "axios"
const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  data() {
    return {
      articleInfo: this.$route.params.articleInfo,
      articleImages: this.$route.params.articleImages,
    }
  },
  components: {
    SetLocation
  },
  methods: {
    savePosition(res) {
      this.articleInfo.addressName = res.addressName
      this.articleInfo.lat = res.Ma
      this.articleInfo.lng = res.La
      this.articleInfo.userId = sessionStorage.getItem('uid')
    },
    onSumbit() {
      var headers = {
        headers: {
          'Content-type': 'multipart/form-data',
          'X-Authorization-Firebase': sessionStorage.getItem('jwt')
        },
      }
      axios.post(`${SERVER_URL}/articles`, this.articleInfo, {
        headers: {
            'X-Authorization-Firebase': sessionStorage.getItem('jwt'),
          }
        })
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
            alert(err)
          })
        })
        .catch((err) => {
          alert(err)
        })

      
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