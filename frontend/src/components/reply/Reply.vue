<template>
  <div v-if="replyInfo">
    <div
      v-if="isUpdating"
      class="ma-0 row justify-space-between align-top"
    >
      <div style="width: 100%;" class="d-flex">
        <UserProfileImage
          class="py-1"
          :profile-image="replyInfo.user.profileImage"
          :size="2.3"
          :font-size="1"
        />
        <!-- 유저 프로필 이미지 or 아이콘 / 유저 닉네임 끝 -->

        <!-- 댓글 수정 시작 -->
        <v-text-field
          v-model="updateContent"
          autofocus
          class="pt-0 px-2"
          append-icon="mdi-pencil"
          dense
          @click:append="onUpdateReplyComplete"
          @keypress.enter="onUpdateReplyComplete"
        ></v-text-field>
        <!-- 댓글 수정 끝-->
      </div>
    </div>
    <div
      v-else
      class="ma-0 row justify-space-between align-top"
    >
      <div :style="loginUserId !== replyInfo.user.userId ? 'width: 100%;' : 'width: 88%;'" class="d-flex">
        <div 
          class="mouse-hover"
          @click="onProfileImage">
          <UserProfileImage 
            class="py-1"
            :profile-image="replyInfo.user.profileImage"
            :size="2.3"
            :font-size="1"
          />
        </div>
        <div style="width: 90%;" :class="replyType === 'rereply' ? 'align-self-center' : ''">
          <p class="mb-0">
            <span 
              style="font-weight: bold;" 
              class="mouse-hover"
              @click="onProfileImage"
            >
              {{replyInfo.user.nickname}}
            </span>
            {{replyInfo.contents }}
          </p>
          <span 
            v-if="replyType === 'reply'" 
            class="text-caption mr-2"
            style="cursor: pointer"
            @click="showInput = !showInput">댓글 달기</span>
            <span class="" style="color: grey;">{{ replyInfo.wdate | dateFormat()}}</span>
            <v-text-field
              v-if="showInput"
              autofocus
              v-model="commentInput"
              class="pt-0"
              append-icon="mdi-pencil"
              dense
              @click:append="onCreateRereply"
              @keypress.enter="onCreateRereply"
            ></v-text-field>
        </div>
      </div>
      <div>
        <v-menu 
          v-if="loginUserId === replyInfo.user.userId"
          class=""
          transition="scroll-y-transition"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              class="m-0"
              v-bind="attrs"
              v-on="on"
              icon
            >
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </template>
          <v-list class="py-0">
            <v-list-item>
              <v-list-item-title class="mouse-hover" @click='onUpdateReply'>수정</v-list-item-title>
            </v-list-item>
            <v-list-item>
              <v-list-item-title class="mouse-hover" @click='onDeleteReply'>삭제</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
    </div>
    <v-divider class="mb-1"></v-divider>

    <!-- child라는 프로퍼티가 있고 이 것이 빈값이 아니라면 -->
    <v-row v-if="replyType==='reply' && replyInfo.child.length > 0">
      <v-col
        cols="11"
        offset="1"
      >
      <ReplyList 
        :replies="replyInfo.child"
        :profileUserId="profileUserId"
        :loginUserId="loginUserId"
        replyType="rereply"
      />
      </v-col>
    </v-row>
  </div>
</template>

<script>
import axios from 'axios'
import UserProfileImage from '@/components/user/UserProfileImage'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'reply',
  components: {
    UserProfileImage,
    ReplyList: () => import('./ReplyList.vue')
  },
  filters: {
    dateFormat(date) {
      const dateArray = date.split(' ')[0].split('-')
      return dateArray[0]+'년 '+dateArray[1]+'월 '+ dateArray[2]+'일'
    },
  },
  props: {
    reply: Object,
    profileUserId: String,
    loginUserId: String,
    replyType: String,
  },
  data: () => ({  
    replyInfo: null,
    isUpdating: false,
    updateContent: '',
    showInput: false,
    commentInput: '',
  }),
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
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.replyInfo = this.reply
    },
    onCreateRereply() {
      if (this.commentInput) {
        // parentId, contents, userId
        const params = {
          'parentId': this.replyInfo.replyId, 
          'contents': this.commentInput, 
          'userId': this.loginUserId
        }

        axios.post(`${SERVER_URL}/replies/rereply`, params, this.getToken)
        .then(res => {
          this.replyInfo.child.push(res.data)
        })
        .then(() => {
          this.commentInput = ''
          this.showInput = false
        })
        .catch(() => {
        })
      }
    },
    onUpdateReply() {
      this.updateContent = this.replyInfo.contents
      this.isUpdating = true
    },
    onDeleteReply() {
      var postFix = ''

      if (this.replyType === "rereply") {
        postFix = `/rereply/${this.replyInfo.rereplyId}`
      } else {
        postFix = `/${this.replyInfo.replyId}`
      }

      axios.delete(`${SERVER_URL}/replies${postFix}`, this.getToken)
      .then(() => {
        this.replyInfo = null
      })
      .catch(err => {
        alert(err)
      })
    },
    onUpdateReplyComplete() {
      var postFix = ''

      var params = {
        'contents': this.updateContent,
        'userId': this.replyInfo.userId,
      }

      if (this.replyType === "rereply") {
        params.rereplyId = this.replyInfo.rereplyId
        postFix = "/rereply"
      } else {
        params.replyId = this.replyInfo.replyId
      }

      axios.put(`${SERVER_URL}/replies${postFix}`, params, this.getToken)
      .then(() => {
        var date = new Date();
        date = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
        this.replyInfo.wdate = date
        this.replyInfo.contents = this.updateContent
      })
      .then(()=> {
        this.isUpdating = false
      })
      .catch(err => {
        alert(err)
      })
    },
    onProfileImage() {
      this.$router.push({ 
        name: 'Profile', 
        params: { profileUserId : this.replyInfo.user.userId }
      })
    },
  },
}
</script>

<style scoped>
/* 마우스를 버튼에 올리면 포인터로 활성화 된다 */
.mouse-hover:hover {
  cursor: pointer;
}
</style>