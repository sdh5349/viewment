<template>
  <v-card-text>
    <Reply 
      v-for="reply in replies" 
      :key="reply.replyId" 
      :reply="reply" 
      :profileUserId="profileUserId"
      :loginUserId="loginUserId"
      :replyType="replyType" 
    />
  </v-card-text>
</template>

<script>
import Reply from '@/components/reply/Reply'

export default {
  name: 'ReplyList',
  components: {
    Reply
  },
  props: {
    replies: Array,
    profileUserId: String,
    loginUserId: String,
    replyType: String,
    // articleId: {
    //   type: Number
    // },
    // request_user: {
    //   type: String
    // }
  },
  methods: {
    // changeComments() {
    //   this.$emit('create-comment')
    // },
  }
}
</script>

<style>

</style>