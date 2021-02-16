<template>
  <!-- 데이터 요청이 완료되지 않았다면 로딩화면 시작 -->	
  
  <v-row
    v-if="loading"
    style="height: 50vh;"
    class="fill-height ma-0"
    align="center"
    justify="center"
  >
    <v-progress-circular
      indeterminate
      color="primary"
    ></v-progress-circular>
  </v-row>
  <!-- 데이터 요청이 완료되지 않았다면 로딩화면 끝-->

  <v-row 
    v-else
    justify="center"
    class="scroll-container"
  >
    <v-col lg="4" md="4" sm="6">
      
      <ArticleComponents 
        :article="articleInfo"
      />
    
      <v-divider class="my-2"></v-divider>
      <v-card elevation="0">
        <v-text-field
          v-model="commentInput"
          class="ma-0 pa-1"
          label="댓글 달기"
          append-icon="mdi-pencil"
          dense
          outlined
          hide-details
          @click:append="onCreateReply"
          @keypress.enter="onCreateReply"
        ></v-text-field>

        <ReplyList
          v-if="articleInfo.replies.length > 0"
          :replies="articleInfo.replies"
          :profileUserId="articleInfo.user.userId"
          :loginUserId="loginUserId"
          replyType="reply"
        />
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'
import ArticleComponents from '@/components/article/ArticleContents'
import ReplyList from '@/components/reply/ReplyList'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'DatailArticle',
  components: {
    ArticleComponents,
    ReplyList,
  },
  props: {
    articleId: [Number, String]
  },
  data() {
    return{
      dialog: false,
      commentInput: '',
      loading: true,
      articleInfo: '',
      loginUserId: sessionStorage.getItem('uid'),
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
    window.history.forward()
    function noBack(){window.history.forward()}
    this.fetchData()
  },
  methods: {
    fetchData() {
      axios.get(`${SERVER_URL}/articles/`+ this.articleId, this.getToken)
      .then(res => {
        console.log(res.data)
        this.articleInfo = res.data
        // TODO: 지금 접속한 유저의 정보를 불러와 해당 유저가 이 게시물을 좋아요 하는지 알아내야함
      })
      .then(() => {
        this.loading = false
      })
      .catch(err => {
        alert(err)
      })
    },
    onCreateReply() {
      if (this.commentInput) {
        const params = {
          'articleId': this.articleInfo.articleId, 
          'contents': this.commentInput, 
          'userId': this.loginUserId
        }
  
        axios.post(`${SERVER_URL}/replies`, params, this.getToken)
        .then(res => {
          this.articleInfo.replies.push(res.data)
        })
        .then(() => {
          this.commentInput = ''
        })
        .catch(() => {
        })
      }
    },
  },
}
</script>

<style scoped>
/* 스크롤 컨테이너 안의 아이템이 넘쳐도 스크롤 컨테이너의 크기는 고정 */
.scroll-container {
  height: 100%;
  overflow: hidden;
  margin-bottom: 50px;
}
</style>